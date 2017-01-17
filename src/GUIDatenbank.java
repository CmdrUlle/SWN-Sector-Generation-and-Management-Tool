import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUIDatenbank extends JPanel implements ActionListener {
	private JButton sector = new JButton("Sectorspace");
	private JButton generate = new JButton("Generate a new database");
	private JButton corporations = new JButton("Corporations");
	private JButton nPCs = new JButton("NPCs");
	private JButton exit = new JButton("Exit");
	private JButton alien = new JButton("Alien");
	private JButton roll = new JButton("Roll"); 
	private JTextField tdice = new JTextField("6");
	private JTextField tdiceAmount = new JTextField("1");
	private JLabel ldiceAmount = new JLabel("Amount"); 
	private JLabel ldice = new JLabel("Dice"); 
	private JLabel rolled = new JLabel("");
	private JLabel rolledL = new JLabel("Outcome"); 
	private Random rGen = new Random();

	public GUIDatenbank() {
		super();
		this.init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(sector, c);

		c.gridx = 1;
		c.gridy = 0;
		this.add(generate, c);

		c.gridx = 0;
		c.gridy = 1;
		this.add(corporations, c);

		c.gridx = 0;
		c.gridy = 2;
		this.add(nPCs, c);

		c.gridx = 1;
		c.gridy = 3;
		this.add(exit);

		c.gridx = 0;
		c.gridy = 3;
		this.add(alien, c);
		
		c.gridx = 0;
		c.gridy = 6;
		this.add(roll, c);
		c.gridx = 1;
		c.gridy = 5;
		this.add(ldiceAmount, c);
		c.gridx = 2;
		c.gridy = 5;
		this.add(ldice, c);
		c.gridx = 1;
		c.gridy = 6;
		this.add(tdiceAmount, c);
		c.gridx = 2;
		c.gridy = 6;
		this.add(tdice, c);
		c.gridx = 3;
		c.gridy = 6;
		this.add(rolled, c);
		c.gridx = 3; 
		c.gridy = 5;
		this.add(rolledL, c);
		

		sector.addActionListener(this); // Verknüpfe Button mit ActionListener
		generate.addActionListener(this);
		corporations.addActionListener(this);
		nPCs.addActionListener(this);
		exit.addActionListener(this);
		alien.addActionListener(this);
		roll.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) { // Methode die aufgerufen wird, wenn ein Button geklickt wird
		if (evt.getSource().equals(sector)) { // evt ist das ActionEvent. evt.getSource() gibt Namen des Elements, von dem aus die Action ausgeht
			sector();
		} else if (evt.getSource().equals(generate)) {
			generate();
		} else if (evt.getSource().equals(corporations)) {
			corporations();
		} else if (evt.getSource().equals(nPCs)) {
			nPCs();
		} else if (evt.getSource().equals(alien)) {
			alien();
		} else if (evt.getSource().equals(exit)) {
			System.exit(0);
		} else if(evt.getSource().equals(roll))	{
			rollTheDice();
		}
	}
	
	private void rollTheDice()	{ 
		try{
			int amount = Integer.parseInt(this.tdiceAmount.getText());
			int die = Integer.parseInt(this.tdice.getText());
			int outcome = 0; 
			for(int i = 0; i < amount; i++)	{
				outcome += rGen.nextInt(die)+1; 
			}
			this.rolled.setText("   "+amount+"d"+die+":   "+outcome);
			
		} catch (NumberFormatException e) {
			this.rolled.setText("Only numbers allowed (no \"d\")");
		}
		
		
	}

	private void sector() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector();
		frame.getContentPane().add(guiPanel);
	}

	private void generate() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiGenerate guiPanel = new GuiGenerate();
		frame.getContentPane().add(guiPanel);
	}

	private void corporations() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiCorporations guiPanel = new GuiCorporations();
		frame.getContentPane().add(guiPanel);
	}

	private void nPCs() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiNPCs guiPanel = new GuiNPCs();
		frame.getContentPane().add(guiPanel);
	}
	
	private void alien() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiAlien guiPanel = new GuiAlien();
		frame.getContentPane().add(guiPanel);
	}
}
