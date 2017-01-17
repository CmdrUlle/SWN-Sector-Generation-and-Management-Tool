import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.Border;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class GuiSector extends JPanel implements ActionListener, MouseListener {
	private JButton zurueck = new JButton("back");
	private JButton saveChanges = new JButton("Save changes to planet");
	private JButton addPlanet = new JButton("Add Planet");
	private JButton deletePlanet = new JButton("Delete current planet");
	private JButton randomize = new JButton("Randomize");
	private JButton deleteSector = new JButton("Delete current sector");
	private JButton ChangeHexOfSector = new JButton("Change HEX of Sector");
	private JButton addSector = new JButton("Add this sector");
	private JButton GoToCorp = new JButton("Corporations");
	private JButton GoToNPCs = new JButton("NPCs");
	private JTable jTabSector, jTabPlanet;
	private JLabel[] JLabelPlanetInfo = new JLabel[15];
	private JTextArea[] JTextPlanetInfo = new JTextArea[15];
	private JTextField[] JTextSectorInfo = new JTextField[2];
	private DatenbankMain db;
	private final String[] descriptions = new String[] { "Planet", "In Sector", "Atmosphere", "Temperature", "Biosphere", "Population", "TechLevel", "Tags", "Capital & Governement", "Cultural Notes", "Spaceport", "Adventures Prepared", "Party Activities", "Notes", "NPCs or Factions" };

	private GuiSectorMap sectorMap;

	// private Random rGen = new Random();

	public GuiSector(String planetName, String systemName) {
		db = new DatenbankMain();
		this.init(planetName, systemName);
		String[][] result = db.searchSectors(systemName);
		String hex = "0000";
		for (String[] results : result) {
			if (results[0] != null) {
				hex = results[1];
			}
		}
		this.JTextSectorInfo[0].setText(hex);
		this.JTextSectorInfo[1].setText(systemName);
		this.deleteSector.setEnabled(true);
		this.addSector.setEnabled(true);
		this.sectorMap.highlightSector(hex);
	}

	public GuiSector() {
		db = new DatenbankMain();
		this.init("NoPlanet", "");
	}

	private void init(String planetName, String systemName) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 14;
		c.gridwidth = 4;
		c.weighty = 1.0;

		sectorMap = new GuiSectorMap();
		TitledBorder border = BorderFactory.createTitledBorder("Sector Map");
		sectorMap.setBorder(border);

		this.add(sectorMap, c);
		sectorMap.setVisible(true);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 15;

		zurueck.addActionListener(this);
		this.add(zurueck, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 14;

		GoToCorp.addActionListener(this);
		this.add(GoToCorp, c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 14;

		GoToNPCs.addActionListener(this);
		this.add(GoToNPCs, c);

		c.gridx = 9;
		c.gridy = 15;

		saveChanges.addActionListener(this);
		this.add(saveChanges, c);
		c = new GridBagConstraints();
		c.gridx = 10;
		c.gridy = 15;

		addPlanet.addActionListener(this);
		this.add(addPlanet, c);

		c.gridx = 11;
		c.gridy = 15;

		randomize.addActionListener(this);
		this.add(randomize, c);

		c.gridx = 8;
		c.gridy = 15;

		deletePlanet.addActionListener(this);
		this.add(deletePlanet, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 15;

		deleteSector.addActionListener(this);
		this.add(deleteSector, c);
		deleteSector.setEnabled(false);

		c.gridx = 6;
		c.gridy = 15;

		addSector.addActionListener(this);
		this.add(addSector, c);

		c.gridx = 7;
		c.gridy = 15;

		ChangeHexOfSector.addActionListener(this);
		this.add(ChangeHexOfSector, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 14;

		JTextSectorInfo[0] = new JTextField();
		JTextSectorInfo[0].addActionListener(this);
		this.add(JTextSectorInfo[0], c);

		c.gridx = 6;
		c.gridy = 14;

		JTextSectorInfo[1] = new JTextField();
		JTextSectorInfo[1].addActionListener(this);
		this.add(JTextSectorInfo[1], c);

		DefaultTableModel tabSector = new DefaultTableModel(this.getTableDataSector(), new String[] { "Hex", "Sectorname", "Planets" }) {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		jTabSector = new JTable(tabSector);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 5;
		c.gridy = 0;
		c.gridheight = 14;
		c.gridwidth = 3;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		jTabSector.addMouseListener(this);
		JScrollPane jspSector = new JScrollPane(jTabSector);
		this.add(jspSector, c);

		String[] results = db.searchHabitatByPlanetname(planetName, systemName);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < JLabelPlanetInfo.length - 1; i++) {
			this.JLabelPlanetInfo[i] = new JLabel(this.descriptions[i]);
			c.gridx = 8;
			c.gridy = i;
			c.gridwidth = 1;
			this.add(JLabelPlanetInfo[i], c);
			if (i < 8) {
				this.JTextPlanetInfo[i] = new JTextArea(results[i], 1, 1);
				JTextPlanetInfo[i].setPreferredSize(new Dimension(425, 25));
			} else {
				this.JTextPlanetInfo[i] = new JTextArea(results[i], 5, 1);
				JTextPlanetInfo[i].setPreferredSize(new Dimension(425, 95));
			}
			c.gridx = 9;
			c.gridy = i;
			c.gridwidth = 3;

			this.add(JTextPlanetInfo[i], c);
		}

		String factionNpc = "";
		if (!(planetName.equals("") || planetName.equals("NoPlanet"))) {
			String[][] resultsFactions = db.searchFactions(planetName);
			// System.out.println("Hello: "+resultsFactions.length);
			for (String[] resultFaction : resultsFactions) {
				factionNpc += resultFaction[0] + ", ";
			}
			String[][] resultsNPCs = db.searchNPCbyPlanetname(planetName);
			// System.out.println("Hello: "+resultsFactions.length);
			for (String[] resultNPC : resultsFactions) {
				factionNpc += resultNPC[0] + ", ";
			}
		}
		this.JLabelPlanetInfo[14] = new JLabel(this.descriptions[14]);
		c.gridx = 8;
		c.gridy = 14;
		c.gridwidth = 1;
		this.add(JLabelPlanetInfo[14], c);
		this.JTextPlanetInfo[14] = new JTextArea(factionNpc, 1, 1);
		// JTextPlanetInfo[13].setPreferredSize(new Dimension(400, 25));

		c.gridx = 9;
		c.gridy = 14;
		c.gridwidth = 3;

		this.add(JTextPlanetInfo[14], c);

	}

	private String[][] getTableDataSector() {
		String[][] results = db.searchSectors("");
		ArrayList<String[]> arrResult = new ArrayList<String[]>();
		String[] arrPlanetNames;
		int numberOfPlanets;

		for (int i = 0; i < results.length; i++) {
			arrPlanetNames = db.searchHabitatBySystemname(results[i][0]);
			numberOfPlanets = arrPlanetNames.length;

			if (numberOfPlanets == 0) {
				arrResult.add(new String[] { results[i][1], results[i][0], new String() });
			} else {
				for (int j = 0; j < numberOfPlanets; j++) {
					arrResult.add(new String[] { results[i][1], results[i][0], arrPlanetNames[j] });
				}
			}
		}
		return arrResult.toArray(results);
	}

	// private String[][] getTableDataPlanet(String planet) {
	// if (planet.equals("NoPlanet")) return new String[][]{};
	// String[] results = db.searchHabitatByPlanetname(planet);
	// String[][] data = new String[11][2];
	// for (int i = 0; i < results.length; i++) {
	// data[i][0] = descriptions[i];
	// data[i][1] = results[i];
	// // System.out.println(results[i]);
	// // temp = db.searchHabitatBySystemname(results[i][0]);
	// // for (String helper : temp) {
	// // arrResult.add(new String[] { results[i][1], results[i][0],
	// // new String(helper) });
	// // }
	// }
	// return data;
	// }

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(zurueck)) {
			this.zurueck();
		} else if (evt.getSource().equals(saveChanges)) {
			saveChangesToPlanet();
		} else if (evt.getSource().equals(deletePlanet)) {
			deletePlanet();
		} else if (evt.getSource().equals(deleteSector)) {
			deleteSector();
		} else if (evt.getSource().equals(addSector)) {
			addSector();
		} else if (evt.getSource().equals(addPlanet)) {
			addPlanet();
		} else if (evt.getSource().equals(GoToCorp)) {
			changeToCorp();
		} else if (evt.getSource().equals(GoToNPCs)) {
			changeToNPCs();
		} else if (evt.getSource().equals(ChangeHexOfSector)) {
			changeHexOfSector();
		} else if (evt.getSource().equals(randomize)) {
			randomize();
		}
	}

	private void randomize() {
		GuiGenerate gen = new GuiGenerate();
		// defAtmosphere[atmosphere], defTemperature[temperature], defBiosphere[biosphere], defPopulation[population], defTechLevel[techlevel], worldtags
		String[] names = gen.getRandomPlanetSectorName();
		String[] results = gen.getAttributesRAW();
		for (int i = 0; i < 2; i++) {
			if (JTextPlanetInfo[i].getText().equals("")) {
				this.JTextPlanetInfo[i].setText(names[i]);
			}
		}
		for (int i = 0; i < 6; i++) {
			if (JTextPlanetInfo[i+2].getText().equals("")) {
				this.JTextPlanetInfo[i+2].setText(results[i]);
			}
		}
	}

	private void changeHexOfSector() {
		this.ChangeHexOfSector.setForeground(Color.BLACK);
		this.ChangeHexOfSector.setBackground(new JButton().getBackground());
		String hex = JTextSectorInfo[0].getText();
		String name = JTextSectorInfo[1].getText();
		String[] result = db.searchSectorsHex(hex); // Hex occupied => Something found in database for Hex ..
		if (result[0] == null || result[0].isEmpty()) {
			db.updateSector(name, hex);
			refresh("NoPlanet", name);
		} else {
			this.ChangeHexOfSector.setText("Hex occupied");
			this.ChangeHexOfSector.setToolTipText("The hex is occupied. Pease move system to another hex");
			this.ChangeHexOfSector.setForeground(Color.WHITE);
			this.ChangeHexOfSector.setBackground(Color.RED);
			this.sectorMap.highlightSector(hex);
		}
	}

	private void addSector() {
		db.addSector(JTextSectorInfo[1].getText(), JTextSectorInfo[0].getText());
		refresh("NoPlanet", "");
	}

	private void deleteSector() {
		String systemname = JTextSectorInfo[1].getText();
		String hex = JTextSectorInfo[0].getText();
		db.deleteSector(systemname, hex);
		this.deleteSector.setEnabled(true);
		refresh("NoPlanet", "");
	}

	private void saveChangesToPlanet() {
		String planetname = this.JTextPlanetInfo[0].getText();
		String systemname = this.JTextPlanetInfo[1].getText();
		db.deleteHabitat(planetname, systemname);
		db.addHabitat(planetname, systemname, JTextPlanetInfo[2].getText().trim(), JTextPlanetInfo[3].getText().trim(), JTextPlanetInfo[4].getText().trim(), JTextPlanetInfo[5].getText().trim(), JTextPlanetInfo[6].getText().trim(), JTextPlanetInfo[7].getText().trim(), JTextPlanetInfo[8].getText()
				.trim(), JTextPlanetInfo[9].getText().trim(), JTextPlanetInfo[10].getText().trim(), JTextPlanetInfo[11].getText().trim(), JTextPlanetInfo[12].getText().trim(), JTextPlanetInfo[13].getText().trim());
		refresh(planetname, systemname);
	}

	private void addPlanet() {
		String planetName = this.JTextPlanetInfo[0].getText();
		String systemName = this.JTextPlanetInfo[1].getText();
		String[] uniqueCheck = db.searchHabitatByPlanetname(planetName, systemName);
		if (uniqueCheck[0] == null || uniqueCheck[0].isEmpty() || !uniqueCheck[0].equals(planetName) || !uniqueCheck[1].trim().equals(systemName)) {
			db.addHabitat(planetName.trim(), systemName.trim(), JTextPlanetInfo[2].getText().trim(), JTextPlanetInfo[3].getText().trim(), JTextPlanetInfo[4].getText().trim(), JTextPlanetInfo[5].getText().trim(), JTextPlanetInfo[6].getText().trim(), JTextPlanetInfo[7].getText().trim(),
					JTextPlanetInfo[8].getText().trim(), JTextPlanetInfo[9].getText().trim(), JTextPlanetInfo[10].getText().trim(), JTextPlanetInfo[11].getText().trim(), JTextPlanetInfo[12].getText().trim(), JTextPlanetInfo[13].getText().trim());
			refresh(planetName.trim(), systemName.trim());
		} else {
			System.out.println("Planet with same name in same System detected. Omitting creation.");
			this.addPlanet.setText("Planet existing...");
		}

	}

	private void deletePlanet() {
		db.deleteHabitat(this.JTextPlanetInfo[0].getText(), this.JTextPlanetInfo[1].getText());
		refresh("NoPlanet", "");
	}

	private void zurueck() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GUIDatenbank guiPanel = new GUIDatenbank();
		frame.getContentPane().add(guiPanel);
	}

	private void refresh(String planetName, String systemName) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector(planetName, systemName);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToCorp() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiCorporations guiPanel = new GuiCorporations();
		frame.getContentPane().add(guiPanel);
	}

	private void changeToNPCs() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiNPCs guiPanel = new GuiNPCs();
		frame.getContentPane().add(guiPanel);
	}

	private void changeToPlanet(String planet, String systemname) {
		saveChanges.setText("Save changes to planet");
		deletePlanet.setEnabled(true);
		String[] results = db.searchHabitatByPlanetname(planet, systemname);
		for (int i = 0; i < 14; i++) {
			this.JTextPlanetInfo[i].setText(results[i]);
		}
		if (!JTextPlanetInfo[7].getText().equals("")) {
			String tooltip = "<html>";
			String[][] tagDescr = db.getWorldTag(JTextPlanetInfo[7].getText());
			for (String[] singleTag : tagDescr) {
				for (String singleEntry : singleTag) {
					tooltip += singleEntry + "<br>";
				}
			}
			tooltip += "</html>";
			if (tagDescr[0] != null && tagDescr[0][1] != null) {
				// System.out.println(tagDescr[0][1]);
			} else {
				System.out.println("null-fail");
			}
			JTextPlanetInfo[7].setToolTipText(tooltip);
		}
		String factionNpc = "";
		if (!(planet.equals("") || planet.equals("NoPlanet"))) {
			String[][] resultsFactions = db.searchFactions(planet);
			for (String[] resultFaction : resultsFactions) {
				factionNpc += resultFaction[0] + ", ";
			}
		}
		JTextPlanetInfo[14].setText(factionNpc);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(jTabSector)) {
			String planetName = (String) jTabSector.getValueAt(jTabSector.rowAtPoint(arg0.getPoint()), 2);
			String hex = (String) jTabSector.getValueAt(jTabSector.rowAtPoint(arg0.getPoint()), 0);
			String systemName = (String) jTabSector.getValueAt(jTabSector.rowAtPoint(arg0.getPoint()), 1);
			this.JTextSectorInfo[0].setText(hex);
			this.JTextSectorInfo[1].setText(systemName);
			this.deleteSector.setEnabled(true);
			this.addSector.setEnabled(false);
			this.sectorMap.highlightSector(hex);
			if (planetName != null && !planetName.isEmpty()) {
				this.changeToPlanet(planetName, systemName);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
