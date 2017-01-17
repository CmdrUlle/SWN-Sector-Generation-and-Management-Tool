import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GuiCorporations extends JPanel implements ActionListener, MouseListener {
	private JButton zurueck = new JButton("Back");
	private JButton addFaction = new JButton("Add Faction");
	private JButton deleteFaction = new JButton("Delete Faction");
	private JButton addRandomFaction = new JButton("Add random faction");
	private JButton saveChangesToFaction = new JButton("Save changes to faction"); 
	private JTable jTabFactions;;
	private JLabel[] JLabelFactionDescrp = new JLabel[17];
	private JTextArea[] JTextFactionInfo = new JTextArea[17];

	private final String[] descriptions = new String[] { "Name", "Homeworld", "Influenced worlds", "Business", "Reputation", "Tags", "MaxHitpoints", "Hitpoints", "Force rating", "Cunning rating", "Wealth rating", "Faction credits", "Experience points", "Assets", "Goals", "Relationship", "Notes" };
	private DatenbankMain db;

	public GuiCorporations() {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init("NoFaction");

	}

	public GuiCorporations(String factionName, String homeworld) {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init(factionName);

	}

	private void init(String FactionName) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 17;
		zurueck.addActionListener(this);
		this.add(zurueck, c);

		c.gridx = 5;
		c.gridy = 17;
		addFaction.addActionListener(this);
		this.add(addFaction, c);
		c.gridx = 5;
		c.gridy = 18;
		addRandomFaction.addActionListener(this);
		this.add(addRandomFaction, c);

		c.gridx = 4;
		c.gridy = 17;
		deleteFaction.addActionListener(this);
		this.add(deleteFaction, c);
		
		
		c.gridx = 4;
		c.gridy = 18;
		saveChangesToFaction.addActionListener(this);
		this.add(saveChangesToFaction, c);

		DefaultTableModel tabSector = new DefaultTableModel(this.getTableDataFaction(), new String[] { "Faction Name", "Homeworld", "Business", "MaxHP", "HP", "FacCreds", "Goals", "Relationship" }) {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		jTabFactions = new JTable(tabSector);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		// c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 18;
		c.gridwidth = 3;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		jTabFactions.addMouseListener(this);
		JScrollPane jspSector = new JScrollPane(jTabFactions);
		jspSector.setPreferredSize(new Dimension(800, 400));
		this.add(jspSector, c);

		String[] results = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
		if (!(FactionName.equals("NoFaction") || FactionName.equals(""))) {
			String[][] resultsUnchecked = db.searchFactions(FactionName);
			if (resultsUnchecked.length > 1 || resultsUnchecked.length == 0) {
				System.out.println("More/No factions found by this name. Will only display the first/none...");
			} else {
				for (int i = 0; i < 16; i++) {
					results[i] = resultsUnchecked[0][i];
				}
				// results = resultsUnchecked[0];
			}
		}
//		System.out.println("----------------------------------------" + results[0]);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < 17; i++) {
			this.JLabelFactionDescrp[i] = new JLabel(this.descriptions[i]);
			// c.weightx = 0.1;
			c.gridx = 4;
			c.gridy = i;
			c.gridwidth = 1;
			this.add(JLabelFactionDescrp[i], c);
			if (i == 13 || i==16) {
				this.JTextFactionInfo[i] = new JTextArea("" + results[i], 12, 1);
				JTextFactionInfo[i].setPreferredSize(new Dimension(400, 25));
			} else {
				this.JTextFactionInfo[i] = new JTextArea(results[i], 1, 1);
				JTextFactionInfo[i].setPreferredSize(new Dimension(400, 25));
			}
			c.gridx = 5;
			c.gridy = i;
			c.gridwidth = 2;
			// c.weightx = 0.4;

			this.add(JTextFactionInfo[i], c);

		}
	}

	private String[][] getTableDataFaction() {
		String[][] result = db.searchFactions("");
		ArrayList<String[]> tableView = new ArrayList<String[]>();
		for (String[] results : result) {
			// name, homeworld, business, maxhp, hp, faccreds, goals, relationshop
			tableView.add(new String[] { results[0], results[1], results[3], results[6], results[7], results[11], results[14], results[15] });
		}
		System.out.println(result.length);
		return tableView.toArray(new String[][] {});
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(zurueck)) {
			this.zurueck();
		} else if (evt.getSource().equals(deleteFaction)) {
			this.deleteFaction();
		} else if (evt.getSource().equals(addFaction)) {
			this.addFaction();
		} else if (evt.getSource().equals(addRandomFaction)) {
			this.addRandomFaction();
		} else if (evt.getSource().equals(saveChangesToFaction))	{
			this.saveChangesToFaction();
		}
	}

	private void deleteFaction() {
		db.deleteFaction(this.JTextFactionInfo[0].getText(), this.JTextFactionInfo[1].getText());
		refresh("NoFaction", "");
	}

	private void addFaction()	{
//		db.addFaction(factionName, Maxhitpoints, hitpoints, forceRating, cunningRating, wealthRating, facCreds, expoints, homeworld, influencedWorlds, tags, assets, goals, relationship, business, reputation);
		for(int i = 6; i <= 12; i++)	{
			if(JTextFactionInfo[i].getText().equals(""))	{
				JTextFactionInfo[i].setText("0");
			}
		}
		try{
		db.addFaction(JTextFactionInfo[0].getText().trim(), Integer.parseInt(JTextFactionInfo[6].getText()),Integer.parseInt(JTextFactionInfo[7].getText()),Integer.parseInt(JTextFactionInfo[8].getText()),Integer.parseInt(JTextFactionInfo[9].getText()),Integer.parseInt(JTextFactionInfo[10].getText()),Integer.parseInt(JTextFactionInfo[11].getText()),Integer.parseInt(JTextFactionInfo[12].getText()),JTextFactionInfo[1].getText().trim(),JTextFactionInfo[2].getText().trim(),JTextFactionInfo[5].getText().trim(),JTextFactionInfo[13].getText().trim(),JTextFactionInfo[14].getText().trim(),JTextFactionInfo[15].getText().trim(),JTextFactionInfo[3].getText().trim(),JTextFactionInfo[4].getText().trim(), JTextFactionInfo[16].getText().trim());
		refresh(this.JTextFactionInfo[0].getText(), this.JTextFactionInfo[1].getText());
		} catch(NumberFormatException e)	{
			System.out.println(e+ "\n in adding faction");
		}		
	}
	
	private void addRandomFaction()	{
		GuiGenerate temporary = new GuiGenerate();
		String[] fStrings = temporary.getFactionAttributesString();
		int[] fInts = temporary.getFactionAttributesInt(); 
		
		db.addFaction(fStrings[8], fInts[0], fInts[1], fInts[3], fInts[4], fInts[5], fInts[2], fInts[6], fStrings[0], fStrings[4], fStrings[1], fStrings[5], fStrings[2], fStrings[3], fStrings[6], fStrings[7], "");
		refresh(fStrings[8], fStrings[0]);
	}

	private void zurueck() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GUIDatenbank guiPanel = new GUIDatenbank();
		frame.getContentPane().add(guiPanel);
	}

	private void refresh(String factionName, String homeworld) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiCorporations guiPanel = new GuiCorporations(factionName, homeworld);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToFaction(String factionName, String homeworld) {
		String[] results = db.searchUniqueFaction(factionName, homeworld);
		for (int i = 0; i < 16; i++) {
			JTextFactionInfo[i].setText(results[i]);
		}
		int income = 0; 
		try{
			income = (int) Integer.parseInt(results[10])/2+ (int) (Integer.parseInt(results[8])+Integer.parseInt(results[9]))/4;
		} catch (NumberFormatException e)	{
			System.out.println(e);
		}
		 
		JLabelFactionDescrp[11].setToolTipText(new String("Income: "+income));
	}
	
	private void saveChangesToFaction() {
		String factionName = this.JTextFactionInfo[0].getText();
//		String homeWorld = this.JTextPlanetInfo[1].getText();
		db.deleteFaction(factionName);
		this.addFaction(); 
		refresh(JTextFactionInfo[0].getText().trim(), JTextFactionInfo[1].getText().trim());;
	}
	
	private void changeToPlanet(String planetName, String systemName) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector(planetName, systemName);
		frame.getContentPane().add(guiPanel);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(jTabFactions)) {
			
			String factionName = (String) jTabFactions.getValueAt(jTabFactions.rowAtPoint(arg0.getPoint()), 0);
			String Homeworld = (String) jTabFactions.getValueAt(jTabFactions.rowAtPoint(arg0.getPoint()), 1);
			
			if (jTabFactions.columnAtPoint(arg0.getPoint()) == 1) {
				System.out.println(Homeworld);
				String[] results = db.searchHabitatByPlanetnameUNSAFE(Homeworld);
				if (results.length != 0 && results[1] != null && !results[1].isEmpty()) {
					this.changeToPlanet(Homeworld, results[1]);
				}
			}

			changeToFaction(factionName, Homeworld);
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
