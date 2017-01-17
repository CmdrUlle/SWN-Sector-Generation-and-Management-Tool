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

public class GuiAlien extends JPanel implements ActionListener, MouseListener {
	private JButton zurueck = new JButton("Back");
	private JButton addAlien = new JButton("Add alien");
	private JButton deleteAlien = new JButton("Delete alien");
	private JButton addRandomAlien = new JButton("Add random alien");
	private JButton saveChangesToAlien = new JButton("Save changes to alien");
	private JTable jTabAliens;
	private JLabel[] JLabelAlienDescrp = new JLabel[13];
	private JTextArea[] JTextAlienInfo = new JTextArea[13];

	private final String[] descriptions = new String[] { "Racename", "Homeworld", "More worlds", "Bodytype", "Body detail", "Lenses", "Lenses detail", "Social", "Social detail", "TechLevel", "Status in sector", "Motivation", "Notes" };
	private DatenbankMain db;

	public GuiAlien() {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init("NoAlien");

	}

	public GuiAlien(String racename) {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init(racename);
	}

	private void init(String alienName) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 16;
		zurueck.addActionListener(this);
		this.add(zurueck, c);

		c.gridx = 5;
		c.gridy = 16;
		addAlien.addActionListener(this);
		this.add(addAlien, c);
		c.gridx = 5;
		c.gridy = 17;
		addRandomAlien.addActionListener(this);
		this.add(addRandomAlien, c);

		c.gridx = 4;
		c.gridy = 16;
		deleteAlien.addActionListener(this);
		this.add(deleteAlien, c);

		c.gridx = 4;
		c.gridy = 17;
		saveChangesToAlien.addActionListener(this);
		this.add(saveChangesToAlien, c);

		DefaultTableModel tabSector = new DefaultTableModel(this.getTableDataAliens(), new String[] { "Racename", "Homeworld", "Bodytype", "Lenses", "Social", "TL" }) {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		jTabAliens = new JTable(tabSector);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		// c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 15;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		jTabAliens.addMouseListener(this);
		JScrollPane jspSector = new JScrollPane(jTabAliens);
		jspSector.setPreferredSize(new Dimension(800, 400));
		this.add(jspSector, c);

		String[] results = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "" };
		if (!(alienName.equals("NoAlien") || alienName.equals(""))) {
			results = db.searchAliens(alienName);
		}

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < 13; i++) {
			this.JLabelAlienDescrp[i] = new JLabel(this.descriptions[i]);
			// c.weightx = 0.1;
			c.gridx = 4;
			c.gridy = i;
			c.gridwidth = 1;
			this.add(JLabelAlienDescrp[i], c);
			if (i == 4 || i == 6 || i == 8 || i == 10 || i == 12) {
				this.JTextAlienInfo[i] = new JTextArea(results[i], 5, 1);
				JTextAlienInfo[i].setPreferredSize(new Dimension(425, 110));
			} else {
				this.JTextAlienInfo[i] = new JTextArea(results[i], 1, 1);
				JTextAlienInfo[i].setPreferredSize(new Dimension(425, 25));
			}
			c.gridx = 5;
			c.gridy = i;
			c.gridwidth = 2;
			// c.weightx = 0.4;

			this.add(JTextAlienInfo[i], c);

		}
	}

	private String[][] getTableDataAliens() {
		String[][] result = db.searchAliens();
		ArrayList<String[]> tableView = new ArrayList<String[]>();
		for (String[] results : result) {
			// "Racename", "Homeworld", "Bodytype", "Lenses", "Social", "TL"
			tableView.add(new String[] { results[0], results[1], results[3], results[5], results[7], results[9] });
		}
		return tableView.toArray(new String[][] {});
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(zurueck)) {
			this.zurueck();
		} else if (evt.getSource().equals(deleteAlien)) {
			this.deleteAlien();
		} else if (evt.getSource().equals(addAlien)) {
			this.addAlien();
		} else if (evt.getSource().equals(addRandomAlien)) {
			this.addRandomAlien();
		} else if (evt.getSource().equals(saveChangesToAlien)) {
			this.saveChangesToAlien();
		}
	}

	private void deleteAlien() {
		db.deleteAlien(this.JTextAlienInfo[0].getText());
		refresh("NoAlien");
	}

	private void addAlien() {
		db.addAlien(JTextAlienInfo[0].getText().trim(), JTextAlienInfo[1].getText().trim(), JTextAlienInfo[2].getText().trim(), JTextAlienInfo[3].getText().trim(), JTextAlienInfo[4].getText().trim(), JTextAlienInfo[5].getText().trim(), JTextAlienInfo[6].getText().trim(), JTextAlienInfo[7].getText()
				.trim(), JTextAlienInfo[8].getText().trim(), JTextAlienInfo[9].getText().trim(), JTextAlienInfo[10].getText().trim(), JTextAlienInfo[11].getText().trim(), JTextAlienInfo[12].getText().trim());
		refresh(this.JTextAlienInfo[0].getText().trim());
	}

	private void addRandomAlien() {
		GuiGenerate temporary = new GuiGenerate();
		String[] aStrings = temporary.getAlienAttributesString();

		db.addAlien(aStrings[0].trim(), aStrings[1].trim(), aStrings[2].trim(), aStrings[3].trim(), aStrings[4].trim(), aStrings[5].trim(), aStrings[6].trim(), aStrings[7].trim(), aStrings[8].trim(), aStrings[9].trim(), aStrings[10].trim(), aStrings[11].trim(), aStrings[12].trim());
		refresh(aStrings[0]);
	}

	private void zurueck() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GUIDatenbank guiPanel = new GUIDatenbank();
		frame.getContentPane().add(guiPanel);
	}

	private void refresh(String raceName) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiAlien guiPanel = new GuiAlien(raceName);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToPlanet(String planetName, String systemName) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector(planetName, systemName);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToAlien(String raceName) {
		String[] results = db.searchAliens(raceName);
		for (int i = 0; i < results.length; i++) {
//			if (i == 4 || i == 6 || i == 8 || i == 10 || i == 12) {
//				JTextAlienInfo[i].setPreferredSize(new Dimension(425, 97));
//				JTextAlienInfo[i].setText(results[i]);
//			}
			JTextAlienInfo[i].setText(results[i]);
		}
	}

	private void saveChangesToAlien() {
		String alienName = this.JTextAlienInfo[0].getText();
		// String homeWorld = this.JTextPlanetInfo[1].getText();
		db.deleteAlien(alienName);
		this.addAlien();
		refresh(JTextAlienInfo[0].getText().trim());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(jTabAliens)) {
			if (jTabAliens.columnAtPoint(arg0.getPoint()) == 1) {
				String planetName = (String) jTabAliens.getValueAt(jTabAliens.rowAtPoint(arg0.getPoint()), 1);
				System.out.println(planetName);
				String[] results = db.searchHabitatByPlanetnameUNSAFE(planetName);
				System.out.println(results[1]);
				if (results.length != 0 && results[1] != null && !results[1].isEmpty()) {
					this.changeToPlanet(planetName, results[1]);
				}
			}
			String raceName = (String) jTabAliens.getValueAt(jTabAliens.rowAtPoint(arg0.getPoint()), 0);
			changeToAlien(raceName);

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
