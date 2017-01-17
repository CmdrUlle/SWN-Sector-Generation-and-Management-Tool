import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.NClob;
import java.util.ArrayList;
import java.util.Random;

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

public class GuiNPCs extends JPanel implements ActionListener, MouseListener {
	private JButton zurueck = new JButton("Back");
	private JButton addNPC = new JButton("Add NPC");
	private JButton deleteNPC = new JButton("Delete NPC");
	private JButton addRandomNPC = new JButton("Add random NPC");
	private JButton saveChangesToNPC = new JButton("Save changes to NPC");
	private JTable jTabNPCs, jTabLevel;
	private JLabel[] JLabelNPCDescrp = new JLabel[15];
	private JTextArea[] JTextNPCInfo = new JTextArea[15];
	private Random rGen = new Random();

	private DatenbankMain db;

	private final String[] descriptions = new String[] { "Name", "Homeworld", "Status", "Relationship", "Race", "Gender", "Age", "Height", "Problems", "Job Motivation", "Noticeable Quirks", "Class", "Level", "ID", "OtherNotes" };
	private final String[] Ngender = new String[]{"Male", "Female"};
	private final String[] Nage = new String[]{"Young", "Middle-aged", "Old"};
	private final String[] Nheight = new String[]{"Very Short", "Short", "Short", "Average", "Average", "Tall", "Tall", "Very Tall"};
	private final String[] Nproblems = new String[]{"Grudge against local authorities", "Has a secret kept from their family.", "Chronic illness", "Enmity of a local psychic", "Has enemies at work", "Owes loan sharks", "Th reatened with loss of spouse, sibling, or child", "Close relative in trouble with the law", "Drug or behavioral addict", "Blackmailed by enemy"};
	private final String[] NjobMot = new String[]{"Greed, because nothing else they can do pays better","Idealistic about the job","Sense of social duty","Force of habit takes them through the day","Seeks to please another","Feels inadequate as anything else","Family tradition","Religious obligation or vow","Nothing better to do, and they need the money","They’re quitting at the first good opportunity","It’s a stepping stone to better things","Spite against an enemy discomfited by the work"};
	private final String[] Nquirks = new String[]{"Bald","Terrible taste in clothing","Very thin","Powerful build","Bad eyesight, wears spectacles","Carries work tools constantly","Long hair","Bearded, if male. Ankle-length hair if female.","Scars all over hands","Missing digits or an car","Smells like their work","Repeats himself constantly","Talks about tabloid articles","Booming voice","Vocal dislike of off worlders","Always snuffling","Missing teeth","Fastidiously neat","Wears religious emblems","Speaks as little as possible"};
	private final String[] Nclasse = new String[]{"Expert", "Expert", "Expert", "Psych", "Warrior", "Warrior"};
//	private final String[][] levelDesc = new String[][] { { "4", "7", "+0", "Semi-auto (d6+1)/Undersuit" }, { "8", "7", "+0", "Semi-auto (d6+1)/Undersuit" }, { "12", "5", "+1", "Laser Pistol (d6)/Woven" }, { "16", "5", "+1", "Laser Pistol (d6)/Woven" },
//			{ "20", "4", "+3", "Laser Pistol (d6+1)/CFU" }, { "24", "4", "+3", "Laser Pistol (d6+1)/CFU" }, { "28", "4", "+4", "Thermal Pistol (2d6)/CFU" }, { "32", "4", "+4", "Thermal Pistol (2d6)/CFU" }, { "36", "2", "+6", "Thermal Pistol (2d6)/Deflector" },
//			{ "40", "2", "+6", "Shear Rifle (2d8)/Deflector"} };
//	private final String[] levelSaves = new String[] {"PE 16/ME 15/Ev 12/Tech 11/Luck 14","PE 16/ME 15/Ev 12/Tech 11/Luck 14","PE 16/ME 15/Ev 12/Tech 11/Luck 14","PE 16/ME 15/Ev 12/Tech 11/Luck 14","PE 14/ME 13/Ev 10/Tech 9/Luck 12","PE 14/ME 13/Ev 10/Tech 9/Luck 12","PE 14/ME 13/Ev 10/Tech 9/Luck 12","PE 12/ME 11/Ev 8/Tech 7/Luck 10","PE 12/ME 11/Ev 8/Tech 7/Luck 10"};

	public GuiNPCs() {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init("NoNPC");

	}

	public GuiNPCs(String npcName) {
		db = new DatenbankMain();
		this.setLayout(null);
		this.init(npcName);
	}

	private void init(String id) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 18;
		zurueck.addActionListener(this);
		this.add(zurueck, c);

		c.gridx = 5;
		c.gridy = 18;
		addNPC.addActionListener(this);
		this.add(addNPC, c);
		c.gridx = 5;
		c.gridy = 19;
		addRandomNPC.addActionListener(this);
		this.add(addRandomNPC, c);

		c.gridx = 4;
		c.gridy = 18;
		deleteNPC.addActionListener(this);
		this.add(deleteNPC, c);

		c.gridx = 4;
		c.gridy = 19;
		saveChangesToNPC.addActionListener(this);
		this.add(saveChangesToNPC, c);

		DefaultTableModel tabSector = new DefaultTableModel(this.getTableDataNPCs(), new String[] { "Name", "Homeworld", "Status", "Relationship", "ID" }) {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		jTabNPCs = new JTable(tabSector);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		// c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 17;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		jTabNPCs.addMouseListener(this);
		JScrollPane jspSector = new JScrollPane(jTabNPCs);
		jspSector.setPreferredSize(new Dimension(800, 400));
		this.add(jspSector, c);

//		DefaultTableModel tabLevel = new DefaultTableModel(this.getTableDataNPCsLevel(), new String[] { "LVL", "HP", "AC", "AB", "WP/ARM" }) {
//			@Override
//			public boolean isCellEditable(int arg0, int arg1) {
//				return false;
//			}
//		};
//		jTabLevel = new JTable(tabLevel);
//
//		c = new GridBagConstraints();
//		c.fill = GridBagConstraints.BOTH;
//		c.gridx = 4;
//		c.gridy = 15;
//		c.gridheight = 2;
//		c.gridwidth = 3;
//
//		jTabLevel.addMouseListener(this);
//		JScrollPane jspLevel = new JScrollPane(jTabLevel);
//		// jspLevel.setPreferredSize(new Dimension(800, 400));
//		this.add(jspLevel, c);

		String[] results = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
		if (!(id.equals("NoNPC") || id.equals(""))) {
			results = db.searchNPC(id);
		}

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < 15; i++) {
			this.JLabelNPCDescrp[i] = new JLabel(this.descriptions[i]);
			// c.weightx = 0.1;
			c.gridx = 4;
			c.gridy = i;
			c.gridwidth = 1;
			this.add(JLabelNPCDescrp[i], c);
			if (i == 3 || i == 8 || i == 9 || i == 10 || i == 14) {
				this.JTextNPCInfo[i] = new JTextArea(results[i], 1, 1);
				JTextNPCInfo[i].setPreferredSize(new Dimension(425, 100));
			} else {
				this.JTextNPCInfo[i] = new JTextArea(results[i], 1, 1);
				JTextNPCInfo[i].setPreferredSize(new Dimension(425, 20));
			}
			c.gridx = 5;
			c.gridy = i;
			c.gridwidth = 2;
			// c.weightx = 0.4;

			this.add(JTextNPCInfo[i], c);

		}
		this.JTextNPCInfo[13].setEditable(false);
	}

	private String[][] getTableDataNPCs() {
		String[][] result = db.searchNPC();
		ArrayList<String[]> tableView = new ArrayList<String[]>();
		for (String[] results : result) {
			// "Name", "Homeworld", "Status", "Relationship", "ID"
			tableView.add(new String[] { results[0], results[1], results[2], results[3], results[13] });
		}
		return tableView.toArray(new String[][] {});
	}

	// private String[][] getTableDataNPCsLevel() {
	// String[][] result = db.searchNPC();
	// ArrayList<String[]> tableView = new ArrayList<String[]>();
	// for (String[] results : result) {
	// //"Name", "Homeworld", "Status", "Relationship", "ID"
	// tableView.add(new String[] { results[0], results[1], results[2], results[3], results[13]});
	// }
	// return tableView.toArray(new String[][] {});
	// }

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(zurueck)) {
			this.zurueck();
		} else if (evt.getSource().equals(deleteNPC)) {
			this.deleteNPC();
		} else if (evt.getSource().equals(addNPC)) {
			this.addNPC();
		} else if (evt.getSource().equals(addRandomNPC)) {
			this.addRandomNPC();
		} else if (evt.getSource().equals(saveChangesToNPC)) {
			this.saveChangesToNPC();
		}
	}

	private void deleteNPC() {
		db.deleteNPC(this.JTextNPCInfo[13].getText()); // id
		refresh("NoNPC");
	}

	private void addNPC() {
		db.addNPC(JTextNPCInfo[0].getText().trim(), JTextNPCInfo[1].getText().trim(), JTextNPCInfo[2].getText().trim(), JTextNPCInfo[3].getText().trim(), JTextNPCInfo[4].getText().trim(), JTextNPCInfo[5].getText().trim(), JTextNPCInfo[6].getText().trim(), JTextNPCInfo[7].getText().trim(),
				JTextNPCInfo[8].getText().trim(), JTextNPCInfo[9].getText().trim(), JTextNPCInfo[10].getText().trim(), JTextNPCInfo[11].getText().trim(), JTextNPCInfo[12].getText().trim(), this.getID(), JTextNPCInfo[14].getText().trim());
		refresh(this.JTextNPCInfo[13].getText().trim());
	}

	private void addRandomNPC() {
		String[] aStrings = this.getNPCAttributesString();

		db.addNPC(aStrings[0].trim(), aStrings[1].trim(), aStrings[2].trim(), aStrings[3].trim(), aStrings[4].trim(), aStrings[5].trim(), aStrings[6].trim(), aStrings[7].trim(), aStrings[8].trim(), aStrings[9].trim(), aStrings[10].trim(), aStrings[11].trim(), aStrings[12].trim(), aStrings[13].trim(), aStrings[14].trim());
		// aStrings[13].trim(), aStrings[14].trim());
		refresh(aStrings[13]);
	}
	
	private String getID()	{
//		int entries = db.getRowsize("NPC");
//		System.out.println(entries);
		int id = 0; 
		String[] result = db.searchNPC(String.valueOf(id));
		while(result[0]!=null)	{
			id++; 
			result = db.searchNPC(String.valueOf(id));
		}
		return String.valueOf(id); 
	}

	private String[] getNPCAttributesString() {
		String gender = Ngender[rGen.nextInt(Ngender.length)]; 
		String age = Nage[rGen.nextInt(Nage.length)];
		String height = Nheight[rGen.nextInt(Nheight.length)];
		String problems = Nproblems[rGen.nextInt(Nproblems.length)] ;
//		String problems = Ngender[rGen.nextInt(Ngender.length)];
		String jobMot = NjobMot[rGen.nextInt(NjobMot.length)];
		String quirks = Nquirks[rGen.nextInt(Nquirks.length)];
		String classe = Nclasse[rGen.nextInt(Nclasse.length)]; 
		String[] aStrings = new String[] {"", "", "", "neutral", "", gender, age, height, problems, jobMot, quirks, classe, "", getID(), ""};
		return aStrings;
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
		GuiNPCs guiPanel = new GuiNPCs(raceName);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToPlanet(String planetName, String systemName) {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector(planetName, systemName);
		frame.getContentPane().add(guiPanel);
	}

	private void changeToNPC(String id) {
		String[] results = db.searchNPC(id);
		for (int i = 0; i < results.length; i++) {
			JTextNPCInfo[i].setText(results[i]);
		}
	}

	private void saveChangesToNPC() {
		String id = this.JTextNPCInfo[13].getText();
		// String homeWorld = this.JTextPlanetInfo[1].getText();
		db.deleteNPC(id);
		this.addNPC();
		refresh(JTextNPCInfo[13].getText().trim());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(jTabNPCs)) {
			if (jTabNPCs.columnAtPoint(arg0.getPoint()) == 1) {
				String planetName = (String) jTabNPCs.getValueAt(jTabNPCs.rowAtPoint(arg0.getPoint()), 1);
				System.out.println(planetName);
				String[] results = db.searchHabitatByPlanetnameUNSAFE(planetName);
				System.out.println(results[1]);
				if (results.length != 0 && results[1] != null && !results[1].isEmpty()) {
					this.changeToPlanet(planetName, results[1]);
				}
			}
			String id = (String) jTabNPCs.getValueAt(jTabNPCs.rowAtPoint(arg0.getPoint()), 4);
			changeToNPC(id);

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
