import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import java.util.Random;

public class GuiGenerate extends JPanel implements ActionListener {
	private JButton back = new JButton("Back");
	private JButton generate = new JButton("Generate new database");
	private Random randomGenerator = new Random();
	private JTextField JTextSeed = new JTextField("" + randomGenerator.nextInt(100000));
	private JTextField JTextSectorNumber = new JTextField("" + (randomGenerator.nextInt(10) + 20));
	private JTextField JTextFactioNumber = new JTextField("" + (randomGenerator.nextInt(5) + 5));
	private JTextField JTextAlienNumber = new JTextField("" + (randomGenerator.nextInt(3) + 2));
	private JLabel JLabelSeed = new JLabel("Seed");
	private JLabel JLabelSectorNumber = new JLabel("Number of Systems");
	private JLabel JLabelFaction = new JLabel("Number of Factions");
	private JLabel JLabelAlien = new JLabel("Number of Alien races");
	private DatenbankMain db;
	private ArrayList<String[]> arrSector = new ArrayList<String[]>();
	private ArrayList<String> arrSectorsUsed = new ArrayList<String>();
	private ArrayList<String> arrPlanet = new ArrayList<String>();
	private ArrayList<String> arrFactionNamesUsed = new ArrayList<String>();

	private final String[] sectornames = new String[] { "Tartatrus", "Fakir", "Jamilah", "Freydis", "Da", "Phegeus", "Alcimede", "Thora", "Erdoza", "Hyperion", "Atlas", "Herkules", "New Eden", "Anoplur", "Antantotas", "Zyx", "Yuna", "Xall", "Valgersphere", "Varsok", "Umbra", "Ursuf", "Thirdal",
			"Skags", "Shabaa", "Shade", "Satyat", "Platon", "Leitos", "Andari", "Andvari", "Gozo", "Gateway", "Ismail", "Anbar", "Madhur", "Gunnhild", "Armola", "Arnora", "Danae", "Achit", "Fonseca", "Vinita", "Maera", "Arrako", "Sagittarius", "Tadena", "Telamon", "Dejamara", "Poyo", "Panpoxa",
			"Chromos" };
	private final String[] planetnames = new String[] { "Gateway", "Terra Nova", "Terra II", "Aethra", "Afifah", "Agamemnon", "Ahmed-mohammad", "Amagoia", "Asleif", "Autolye", "Avarigan", "Chhavvi", "Halldis", "Holguin", "New Terra", "Ingunn", "Jorunn", "Jurada", "Kokkinos", "Lazardis",
			"Lycomedes", "Mara", "Melite", "Naca", "Nafisah", "Naroa", "Nineus", "Numa", "Panthoos", "Phaistos", "Prote", "Qashqai", "Roshni", "Sajjad", "Sarmistha", "Siyavushi", "Stavro", "Thurayya", "Zafirah", "Zervas", "Aida", "Akhlaqi", "Atiya", "Cabrero", "Deicoon", "Gavrielides", "Gizikis",
			"Hallgerd", "Hicteon", "Hrefna", "Koru", "Koryzis", "Loinaz", "Mehani", "Nisha", "Papademetriou", "Papadopolos", "Parrado", "Phereclos", "Razmara", "Sartzetakis", "Sharifah", "Siantos", "Sosa", "Sotoudeh", "Taliadoros", "Tarrega", "Thaleia", "Thorbjorg", "Thorgerd", "Uttara", "Vahdat",
			"Xanthippe", "Yannopoulos", "Ziortza", "Adonie", "Al-habash", "Ameena", "Antonious", "Argi", "Arrutti", "Callianeira", "Doltza", "Freydis", "Gousetti", "Hallfrid", "Hild", "Idomeneus", "Iniguez", "Katerin", "Mansur", "Muhjah", "Nidra", "Panagoulias", "Pittheus", "Pizzaro", "Pylaimenos",
			"Roohizadegan", "Sneh", "Telamon", "Touliatos", "Unn", "Urreturre", "Xemein", "Xa", "Zarala", "Zolotas", "Aegialeus", "Aglaia", "Ametza", "Amphinome", "Antiphos", "Arne", "Carbonell", "Chromios", "Daphne", "Eidyia", "Fonseca", "Garazi", "Husain", "Katla", "Kenteris", "Kolokotronis",
			"Larraneta", "Lolaksi", "Narmada", "Nassiri", "Nira", "Pavi", "Phegeus", "Priam", "Rasheeda", "Reema", "Sala", "Saleh", "Sanyogita", "Spano", "Talebi", "Thorgunna", "Vasdeki", "Voulgaropoulos", "Zubizarreta" };
	private final String[] defAtmosphere = new String[] { "Corrosive", "Inert gas", "Airless or thin atmosphere", "Breathable Mix", "Breathable Mix", "Breathable Mix", "Breathable Mix", "Breathable Mix", "Thick atmosphere, breathable with a pressure mask", "Invasive, toxic athmoosphere",
			"Corrosive and invasive atmosphere" };
	private final String[] defTemperature = new String[] { "Frozen", "Variable cold-to-temperate", "Cold", "Cold", "Temperate", "Temperate", "Temperate", "Warm (Abundance of surface water)", "Warm (Desert-World)", "Variable temperate-to-warm", "Burning" };
	private final String[] defBiosphere = new String[] { "Biosphere remnants", "Microbial life", "No native biosphere", "No native biosphere", "Human-miscible biosphere", "Human-miscible biosphere", "Human-miscible biosphere", "Immiscible biosphere", "Immiscible biosphere", "Hybrid biosphere",
			"Engineered biosphere" };
	private final String[] defPopulation = new String[] { "Failed Colony", "Outpost", "10K+", "10K+", "100K+", "100K+", "100K+", "1M+", "1M+", "1B+", "Alien" };
	private final String[] defTechLevel = new String[] { "TL 0: Stone-age technology", "TL 1: Medieval technology", "TL 2: 19th-century technology", "TL 3: 20th-century technology", "TL 3: 20th-century technology", "TL 4: Baseline postech", "TL 4: Baseline postech", "TL 4: Baseline postech",
			"TL 4: Baseline postech", "TL 4+: Postech with specialties or some surviving pretech", "TL 5: Pretech, pre-Silence technology" };
	private final String[] defWorldTags = new String[] { "Abandoned Colony", "Out of Contact", "Ancient Ruins", "Altered Humanity", "Area 51", "Badlands World", "Bubble Cities", "Civil War", "Cold War", "Colonized Population", "Desert World", "Eugenic Cult", "Psionics Academy", "Exchange Consulate",
			"Feral World", "Flying Cities", "Forbidden Tech", "Freak Geology", "Freak Weather", "Friendly Foe", "Gold Rush", "Hatred", "Heavy Industry", "Secret Masters", "Heavy Mining", "Hostile Biosphere", "Hostile Space", "Local Specialty", "Local Tech", "Major Spaceyard", "Minimal Contact",
			"Misandry/Misogyny", "Oceanic World", "Outpost World", "Perimeter Agency", "Pilgrimage Site", "Police State", "Preceptor Archive", "Pretech Cultists", "Primitive Aliens", "Psionics Fear", "Psionics Worship", "Quarantined World", "Radioactive World", "Regional Hegemon",
			"Restrictive Laws", "Rigid Culture", "Seagoing Cities", "Sealed Menace", "Sectarians", "Seismic Instability ", "Theocracy", "Tomb World", "Trade Hub", "Tyranny", "Unbraked AI", "Warlords", "Xenophiles", "Xenophobes", "Zombies" };
	private final String[] factionnames1 = new String[] { "Ad Astra", "Colonial", "Compass", "Daybreak", "Frontier", "Guo Yin", "Highbeam", "Imani", "Magnus", "Meteor", "Neogen", "New Dawn", "Omnitech", "Outertech", "Overwatch", "Panstellar", "Shogun", "Silverlight", "Spiker", "Stella", "Striker",
			"Sunbeam", "Terra Prime", "Wayfarer", "West Wind" };
	private final String[] factionnames2 = new String[] { "Alliance", "Association", "Band", "Circle", "Clan", "Combine", "Company", "Cooperative", "Corporation", "Enterprises", "Faction", "Group", "Megacorp", "Multistellar", "Organization", "Outfit", "Pact", "Partnership", "Ring", "Society",
			"Sodality", "Syndicate", "Union", "Unity", "Zaibatsu" };
	private final String[] factionBusiness = new String[] { "Aeronautics", "Agriculture", "Art", "Assassination", "Asteroid Mining", "Astrotech", "Biotech", "Bootlegging", "Computer Hardware", "Construction", "Cybernetics", "Electronics", "Energy Weapons", "Entertainment", "Espionage",
			"Exploration", "Fishing", "Fuel Refining", "Gambling", "Gemstones", "Gengineering", "Grav Vehicles", "Heavy Weapons", "Ideology", "Illicit Drugs", "Journalism", "Law Enforcement", "Liquor", "Livestock", "Maltech", "Mercenary Work", "Metallurgy", "Pharmaceuticals", "Piracy",
			"Planetary Mining", "Plastics", "Pretech", "Prisons", "Programming", "Projectile Guns ", "Prostitution", "Psionics", "Psitech", "Robotics", "Security", "Shipyards", "Snacks", "Telcoms", "Transport", "Xenotech" };
	private final String[] factionReputation = new String[] { "Reckless with the lives of their employees", "Have a dark secret about their board of directors", "Notoriously xenophobic towards aliens", "Lost much money to an embezzler who evaded arrest", "Reliable and trustworthy goods",
			"Stole a lot of R&D from a rival corporation", "They have high-level political connections", "Rumored cover-up of a massive industrial accident", "Stodgy and very conservative in their business plans", "The company’s owner is dangerously insane", "Rumored ties to a eugenics cult",
			"Said to have a cache of pretech equipment", "Possibly teetering on the edge of bankruptcy", "Front for a planetary government’s espionage arm", "Secretly run by a psychic cabal", "Secretly run by hostile aliens", "Secretly run by an unbraked AI",
			"They’ve turned over a new leaf with the new CEO", " Deeply entangled with the planetary underworld" };
	private final String[] factionTags = new String[] { "Colonists", "Deep Rooted", "Eugenics Cult", "Exchange Consultant", "Fanatical", "Imperialists", "Machiavellian", "Mercenary Group", "Perimeter Agency", "Pirates", "Planetary Government", "Plutocratic", "Preceptor Archive", "Psychic Academy",
			"Savage", "Scavengers", "Secretive", "Technical Expertise", "Theocratic", "Warlike" };
	private final String[] factionGoals = new String[] { "Military Conquest", "Commercial Expansion", "Intelligence Coup", "Planetary Seizure", "Expand Influence", "Blood the Enemy", "Peaceable Kingdom", "Destroy the Foe", "Inside Enemy Territory", "Invincible Valor", "Wealth of Worlds" };

	private ArrayList<String> arrAlienNames = new ArrayList<String>();
	private final String[] alienNames = new String[]{"Alionda","Cyak","Gireh","Hiya","Ith","Mrikolo","Nuchugh","Ommoggo","Shteric","Yig","Arikau","Calamor","Gennie ","Jotlh ","Nololur","Qayu ","QelIj ","Sen ","Sorda ","Zhara ","Iw","Ay-mak ","Dren ","Hajun ","Jor ","Kte ","Maghama","Nach ","Polan ","Tald ","Ber","Carnim","Ervorod","Fukna","Hitloo","Hydra","Peronan","Shamble","Tar","Tinksta","Dalin","Dantos","Kar","Koch","Los","Mende","Para","Sha","Tob","Yughan"};
	private final String[] alienBody = new String[]{"Humanlike", "Avian", "Reptilian", "Insectile", "Exotic", "Hybrid"};
	private final String[] alienLenses = new String[]{"Collectivity","Curiosity","Despair","Domination","Faith","Fear","Gluttony","Greed","Hate","Honor","Journeying","Joy","Pacifism","Pride","Sagacity","Subtlety","Tradition","Treachery","Tribalism","Wrath"};
	private final String[] alienSocial = new String[]{"Democratic", "Monarchic", "Tribal", "Olgarchic", "Multipolar", "Multipolar"};
	private final String[] alienStatusLiving = new String[]{"Own homeworld", "Enclave on homeworld", "Living under humans", "Orbital Habitats", "Convoy of massive spike-drive ships"};
	private final String[] alienStatusHumas = new String[]{"Aggressive", "Peaceful", "At war", "Persued", "Passive", "Seperate"};
	
	public GuiGenerate() {
//		System.out.println("sectornames: " + sectornames.length);
//		System.out.println("planetnames: " + planetnames.length);
//		System.out.println("defAtmo: " + defAtmosphere.length);
//		System.out.println("defTemperature: " + defTemperature.length);
//		System.out.println("defBiosphere: " + defBiosphere.length);
//		System.out.println("defPopulation: " + defPopulation.length);
//		System.out.println("defTechLevel: " + defTechLevel.length);
//		System.out.println("defWorldTags: " + defWorldTags.length);
		// for(int i = 2; i <= 12; i++) {
		// System.out.println(i+": "+defBiosphere[i-2]);
		// }
	
		for(String name : alienNames)	{
			arrAlienNames.add(name);
		}
		
		db = new DatenbankMain();
		this.setLayout(null);
		this.init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 2;
		c.gridy = 3;

		back.addActionListener(this);
		this.add(back, c);

		c.gridx = 2;
		c.gridy = 0;

		generate.addActionListener(this);
		this.add(generate, c);

		c.gridx = 1;
		c.gridy = 0;
		JTextSeed.setPreferredSize(new Dimension(200, 25));
		this.add(JTextSeed, c);

		c.gridx = 1;
		c.gridy = 1;
		JTextSectorNumber.setPreferredSize(new Dimension(200, 25));
		this.add(JTextSectorNumber, c);

		c.gridx = 1;
		c.gridy = 2;
		JTextFactioNumber.setPreferredSize(new Dimension(200, 25));
		this.add(JTextFactioNumber, c);
		
		c.gridx = 1;
		c.gridy = 3;
		JTextAlienNumber.setPreferredSize(new Dimension(200, 25));
		this.add(JTextAlienNumber, c);

		c.gridx = 0;
		c.gridy = 0;

		this.add(JLabelSeed, c);

		c.gridx = 0;
		c.gridy = 1;
		this.add(JLabelSectorNumber, c);
		
		c.gridx = 0;
		c.gridy = 2;
		this.add(JLabelFaction, c);
		
		c.gridx = 0;
		c.gridy = 3;
		this.add(JLabelAlien, c);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(back)) {
			this.back();
		} else if (evt.getSource().equals(generate)) {
			this.generate();
		}
	}

	private void generate() {
		db.dropDatabase();
		db.generateTables();

		int seed = 0;
		int systemNumber = 0;
		int factionNumber = 0;
		int alienNumber = 0; 

		try {
			seed = Integer.parseInt(this.JTextSeed.getText());
			systemNumber = Integer.parseInt(this.JTextSectorNumber.getText());
			factionNumber = Integer.parseInt(this.JTextFactioNumber.getText());
			alienNumber = Integer.parseInt(this.JTextAlienNumber.getText());
		} catch (NumberFormatException e) {
			System.out.println("Seed, System, Faction or Aliennumber was no valid number: " + e);
			seed = randomGenerator.nextInt();
			factionNumber = randomGenerator.nextInt(5) + 5;
			systemNumber = randomGenerator.nextInt(15) + 15;
			alienNumber = randomGenerator.nextInt(5) + 2;
			System.out.println("Setting seed to: " + seed);
		}
		generateSector(seed, systemNumber);
		generatePlanet(seed);
		generateFactions(seed, factionNumber);
		generateAliens(alienNumber);

		changeSector();
	}

	private void generateSector(int seed, int systemNumber) {
		int gridXMax = 9;
		int gridYMax = 11;
		String curHex = "";
		String randomSectorName = "";

		ArrayList<String> arrHex = new ArrayList<String>();
		for (int ix = 0; ix < gridXMax; ix++) {
			for (int iy = 0; iy < gridYMax; iy++) {
				if (iy < 10) {
					curHex = "0" + ix + "0" + iy;
				} else {
					curHex = "0" + ix + iy;
				}
				arrHex.add(curHex);
			}
		}
		ArrayList<String> arrName = new ArrayList<String>();
		for (String name : this.sectornames) {
			arrName.add(name);
		}
//		System.out.println("Creating #systems: " + systemNumber + " with " + arrName.size() + " names in the database...");
		for (int i = 0; i < systemNumber && i < gridXMax * gridYMax; i++) {
			// arrHex.get(randomGenerator.nextInt(arrHex.size()));
			randomSectorName = arrName.remove(randomGenerator.nextInt(arrName.size()));
			arrSectorsUsed.add(randomSectorName);
			db.addSector(randomSectorName, arrHex.remove(randomGenerator.nextInt(arrHex.size())));
		}
	}

	private void generatePlanet(long seed) {
		ArrayList<String> arrName = new ArrayList<String>();
		for (String name : this.planetnames) {
			arrName.add(name);
		}

		for (String system : arrSectorsUsed) { // 1 habitable Planet for each System once
			// db.addHabitat(Name, Systemname, Atmosphere, Temperature, Biosphere, TechLevel, Tags, CapitalGovernment, Cultural, Adventures, RecentActivities);
			String[] attributes = getAttributesHabitable();
			String planetName = arrName.remove(randomGenerator.nextInt(arrName.size()));
			this.arrPlanet.add(planetName);
			db.addHabitat(planetName, system, attributes[0], attributes[1], attributes[2], attributes[3], attributes[4], attributes[5], "", "", "", "", "", "");
		}
		int morePlanets;
		for (String system : arrSectorsUsed) {
			morePlanets = randomGenerator.nextInt(4);
			if (morePlanets > 2)
				morePlanets = randomGenerator.nextInt(2) + 1;
			for (int i = 0; i < morePlanets; i++) {
				if (randomGenerator.nextInt(10) > 4) {
					String[] attributes = getAttributesInhabitable();
					String planetName = arrName.remove(randomGenerator.nextInt(arrName.size()));
					this.arrPlanet.add(planetName);
					db.addHabitat(planetName, system, attributes[0], attributes[1], attributes[2], attributes[3], attributes[4], attributes[5], "", "", "", "", "", "");
				} else {
					String[] attributes = getAttributesCalculated();
					String planetName = arrName.remove(randomGenerator.nextInt(arrName.size()));
					this.arrPlanet.add(planetName);
					db.addHabitat(planetName, system, attributes[0], attributes[1], attributes[2], attributes[3], attributes[4], attributes[5], "", "", "", "", "", "");
				}
			}
		}
	}

	private void generateFactions(int seed, int factionNumber) {
		ArrayList<String> arrName = new ArrayList<String>();
		ArrayList<String> arrNamePre = new ArrayList<String>();
		ArrayList<String> arrNameLast = new ArrayList<String>();
		int min = Math.min(this.factionnames1.length, Math.min(this.factionnames2.length, factionNumber));
		for (String namePrefix : this.factionnames1) {
			arrNamePre.add(new String(namePrefix));
		}
		for (String namePostfix : this.factionnames2) {
			arrNameLast.add(new String(namePostfix));
		}
		for (int i = 0; i < min; i++) {
			arrName.add("" + new String(arrNamePre.remove(randomGenerator.nextInt(arrNamePre.size())) + " " + new String(arrNameLast.remove(randomGenerator.nextInt(arrNameLast.size())))));
			// System.out.println(arrName.size()+ " "+arrName.get(i));
		}
		for (int i = 0; i < factionNumber; i++) {
			String[] fStrings = this.getFactionAttributesString();// homeworld, tags, goals, relationship, influencedWorlds, assets
			int[] fInts = this.getFactionAttributesInt();// maxHitpoints, hitpoints, facCreds, forceRating, cunningRating, wealthRating, expoints
			// db.addFaction(factionName, Maxhitpoints, hitpoints, forceRating, cunningRating, wealthRating, facCreds, expoints, homeworld, influencedWorlds, tags, assets, goals, relationship);
			String factionName = arrName.remove(randomGenerator.nextInt(arrName.size()));
			this.arrFactionNamesUsed.add(factionName);
			db.addFaction(factionName, fInts[0], fInts[1], fInts[3], fInts[4], fInts[5], fInts[2], fInts[6], fStrings[0], fStrings[4], fStrings[1], fStrings[5], fStrings[2], fStrings[3], fStrings[6], fStrings[7],"");
		}
	}

	public String[] getFactionAttributesString() {
		String[] results = new String[] {};
		// db.addFaction(factionName, hitpoints, forceRating, cunningRating, wealthRating, facCreds, expoints, homeworld, influencedWorlds, tags, assets, goals, relationship);
		String homeworld = "";
		if (arrPlanet.size() != 0) {
			homeworld = this.arrPlanet.get(randomGenerator.nextInt(arrPlanet.size()));
		}
		String tags = this.factionTags[randomGenerator.nextInt(factionTags.length)];
		if (randomGenerator.nextInt(10) > 4) {
			String tempTag = this.factionTags[randomGenerator.nextInt(factionTags.length)];
			if (!tempTag.equals(tags)) {
				tags += ", " + this.factionTags[randomGenerator.nextInt(factionTags.length)];
			}
		}
		String goals = this.factionGoals[randomGenerator.nextInt(factionGoals.length)];
		String relationship = "neutral";
		String influencedWorlds = new String();
		String assets = new String();
		String business = this.factionBusiness[randomGenerator.nextInt(factionBusiness.length)];
		String reputation = this.factionReputation[randomGenerator.nextInt(factionReputation.length)];
		return new String[] { homeworld, tags, goals, relationship, influencedWorlds, assets, business, reputation, new String(new String(factionnames1[randomGenerator.nextInt(factionnames1.length)]) + " " + new String(factionnames2[randomGenerator.nextInt(factionnames2.length)])) };
	}

	public int[] getFactionAttributesInt() {
		int forceRating = randomGenerator.nextInt(5) + 1;
		int cunningRating = randomGenerator.nextInt(5) + 1;
		int wealthRating = randomGenerator.nextInt(5) + 1;
		int expoints = 0;

		int[] hpCalc = new int[] { 0, 1, 2, 4, 6, 9, 12, 16, 20 }; // Caution: Starting from 0 ...
		int maxHitpoints = 4 + hpCalc[forceRating] + hpCalc[cunningRating] + hpCalc[wealthRating];
		int hitpoints = maxHitpoints;
		int facCreds = randomGenerator.nextInt(8) + 1;
		return new int[] { maxHitpoints, hitpoints, facCreds, forceRating, cunningRating, wealthRating, expoints };
	}

	private String[] getAttributesCalculated() {
		/* Create worlds and calculate possibility for a high population/techlevel */
		int atmosphere = randomGenerator.nextInt(10) + 2; // 2-12
		int temperature = randomGenerator.nextInt(10) + 2;

		/* Look at atmo and temp for possible human colony */
		int penaltyPoints = 0;
		if (atmosphere == 0 || atmosphere == 12) {
			penaltyPoints -= 2;
		} else if (atmosphere == 1 || atmosphere == 11) {
			penaltyPoints--;
		} else if (atmosphere > 4 && atmosphere < 10) {
			penaltyPoints++;
		}
		if (temperature == 0 || temperature == 12) {
			penaltyPoints -= 2;
		} else if (temperature == 1 || temperature == 11) {
			penaltyPoints--;
		} else if (temperature > 4 && temperature < 10) {
			penaltyPoints++;
		}

		int biosphere = randomGenerator.nextInt(10) + 2 + penaltyPoints;
		if (biosphere > 12) {
			biosphere = randomGenerator.nextInt(7) + 6;
		} else if (biosphere < 2) {
			biosphere = randomGenerator.nextInt(2) + 2;
		}

		int population = randomGenerator.nextInt(10) + 2 + penaltyPoints;
		if (population > 12) {
			population = randomGenerator.nextInt(4) + 9;
		} else if (population < 2) {
			population = randomGenerator.nextInt(2) + 2;
		}

		int techlevel = randomGenerator.nextInt(10) + 2 + penaltyPoints;
		if (techlevel > 12) {
			techlevel = randomGenerator.nextInt(3) + 10;
		} else if (techlevel < 2) {
			techlevel = randomGenerator.nextInt(6) + 5;
		}

		int worldtag1 = randomGenerator.nextInt(60);
		int worldtag2 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag2)
			worldtag2 = randomGenerator.nextInt(60);
		int worldtag3 = -1;
		if (randomGenerator.nextInt(10) < 3)
			worldtag3 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag3 || worldtag2 == worldtag3) {
			worldtag3 = randomGenerator.nextInt(60);
		}
		String worldtags = defWorldTags[worldtag1] + ", " + defWorldTags[worldtag2];
		if (worldtag3 != -1)
			worldtags += ", " + defWorldTags[worldtag3];
		return new String[] { defAtmosphere[atmosphere - 2], defTemperature[temperature - 2], defBiosphere[biosphere - 2], defPopulation[population - 2], defTechLevel[techlevel - 2], worldtags };
	}

	private String[] getAttributesInhabitable() {
		/* More inhabitable worlds. Reroll good populated and tech level rolls */
		int atmosphere = randomGenerator.nextInt(10) + 2; // 2-12
		int temperature = randomGenerator.nextInt(10) + 2;
		int biosphere = randomGenerator.nextInt(10) + 2;
		int population = randomGenerator.nextInt(10) + 2;
		if (population > 6)
			population = randomGenerator.nextInt(10) + 2; // reroll
		int techlevel = randomGenerator.nextInt(10) + 2;
		if (techlevel < 4)
			techlevel = randomGenerator.nextInt(10) + 2; // reroll
		int worldtag1 = randomGenerator.nextInt(60);
		int worldtag2 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag2)
			worldtag2 = randomGenerator.nextInt(60);
		int worldtag3 = -1;
		if (randomGenerator.nextInt(10) < 3)
			worldtag3 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag3 || worldtag2 == worldtag3) {
			worldtag3 = randomGenerator.nextInt(60);
		}
		String worldtags = defWorldTags[worldtag1] + ", " + defWorldTags[worldtag2];
		if (worldtag3 != -1)
			worldtags += ", " + defWorldTags[worldtag3];
		return new String[] { defAtmosphere[atmosphere - 2], defTemperature[temperature - 2], defBiosphere[biosphere - 2], defPopulation[population - 2], defTechLevel[techlevel - 2], worldtags };
	}

	private String[] getAttributesHabitable() {
		/* More habitable world. Reroll a bad attribute and use this instead */
		int atmosphere = randomGenerator.nextInt(10) + 2;
		if (atmosphere < 4 && atmosphere > 6)
			atmosphere = randomGenerator.nextInt(10) + 2; // reroll
		int temperature = randomGenerator.nextInt(10) + 2;
		if (temperature == 2 && temperature == 12)
			temperature = randomGenerator.nextInt(10) + 2; // reroll
		int biosphere = randomGenerator.nextInt(10) + 2;
		if (biosphere == 2)
			biosphere = randomGenerator.nextInt(10) + 2; // reroll
		int population = randomGenerator.nextInt(10) + 2;
		if (population < 4)
			population = randomGenerator.nextInt(10) + 2; // reroll
		int techlevel = randomGenerator.nextInt(10 + 2);
		if (techlevel < 4)
			techlevel = randomGenerator.nextInt(10) + 2; // reroll
		int worldtag1 = randomGenerator.nextInt(60);
		int worldtag2 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag2)
			worldtag2 = randomGenerator.nextInt(60);
		int worldtag3 = -1;
		if (randomGenerator.nextInt(10) < 3)
			worldtag3 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag3 || worldtag2 == worldtag3) {
			worldtag3 = randomGenerator.nextInt(60);
		}
		String worldtags = defWorldTags[worldtag1] + ", " + defWorldTags[worldtag2];
		if (worldtag3 != -1)
			worldtags += ", " + defWorldTags[worldtag3];
		return new String[] { defAtmosphere[atmosphere - 2], defTemperature[temperature - 2], defBiosphere[biosphere - 2], defPopulation[population - 2], defTechLevel[techlevel - 2], worldtags };
	}

	public String[] getAttributesRAW() {
		/* Start with atmosphere */
		int atmosphere = randomGenerator.nextInt(10); // 2-12
		int temperature = randomGenerator.nextInt(10);
		int biosphere = randomGenerator.nextInt(10);
		int population = randomGenerator.nextInt(10);
		int techlevel = randomGenerator.nextInt(10);
		int worldtag1 = randomGenerator.nextInt(60);
		int worldtag2 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag2)
			worldtag2 = randomGenerator.nextInt(60);
		int worldtag3 = -1;
		if (randomGenerator.nextInt(10) < 3)
			worldtag3 = randomGenerator.nextInt(60);
		while (worldtag1 == worldtag3 || worldtag2 == worldtag3) {
			worldtag3 = randomGenerator.nextInt(60);
		}
		String worldtags = defWorldTags[worldtag1] + ", " + defWorldTags[worldtag2];
		if (worldtag3 != -1)
			worldtags += ", " + defWorldTags[worldtag3];
		return new String[] { defAtmosphere[atmosphere], defTemperature[temperature], defBiosphere[biosphere], defPopulation[population], defTechLevel[techlevel], worldtags };
	}
	
	public String[] getRandomPlanetSectorName()	{
		return new String[]{this.planetnames[randomGenerator.nextInt(planetnames.length)],this.sectornames[randomGenerator.nextInt(sectornames.length)]};
	}

	private void generateAliens(int alienNumber)	{
		for(int i = 0; i< Math.min(alienNumber,alienNames.length); i++)	{
			String[] aStrings = getAlienAttributesString(); 
			db.addAlien(aStrings[0].trim(), aStrings[1].trim(), aStrings[2].trim(), aStrings[3].trim(), aStrings[4].trim(), aStrings[5].trim(), aStrings[6].trim(), aStrings[7].trim(), aStrings[8].trim(), aStrings[9].trim(), aStrings[10].trim(), aStrings[11].trim(), aStrings[12].trim());
		}
	}
	
	public String[] getAlienAttributesString()	{
		String[] attributes = new String[]{};
		String name = arrAlienNames.remove(randomGenerator.nextInt(arrAlienNames.size()));
//		System.out.println("AlienNames: "+arrAlienNames.size());
		int failsafe = 0; //if every alien name is chosen prevent infinite loop
		while(db.searchAliens(name).length != 0 && failsafe++ < 25)	{
			name = arrAlienNames.get(randomGenerator.nextInt(arrAlienNames.size()));
		}
//		if(failsafe>25) {
//			name = "ListOfAlienNamesEMPTY-CANNOT CREATE RANDOM"; 
//		}
		String bodytype = alienBody[randomGenerator.nextInt(alienBody.length)];
		String lenses = alienLenses[randomGenerator.nextInt(alienLenses.length)];
		if(randomGenerator.nextInt() > 4)	{
			lenses += ", "+alienLenses[randomGenerator.nextInt(alienLenses.length)];
		}
		String social = alienSocial[randomGenerator.nextInt(alienSocial.length)];
		String techLevel = this.defTechLevel[randomGenerator.nextInt(defTechLevel.length)];
		String sectorStatus = alienStatusLiving[randomGenerator.nextInt(alienStatusLiving.length)]+", "+alienStatusHumas[randomGenerator.nextInt(alienStatusHumas.length)];

		return new String[]{name, "", "", bodytype, "", lenses, "", social, "", techLevel, sectorStatus, "", ""}; 
		
	}
	
	private void back() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GUIDatenbank guiPanel = new GUIDatenbank();
		frame.getContentPane().add(guiPanel);
	}

	private void changeSector() {
		this.setVisible(false);
		JFrame frame = (JFrame) SwingUtilities.getRoot(this);
		GuiSector guiPanel = new GuiSector("NoPlanet", "");
		frame.getContentPane().add(guiPanel);
	}

}
