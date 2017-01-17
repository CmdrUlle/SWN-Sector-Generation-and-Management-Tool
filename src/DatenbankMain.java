import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DatenbankMain {
	private static final long serialVersionUID = 1L;
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String protocol = "jdbc:derby:";
	private static String dbName = "Datenbank";
	private static Connection conn = null;

	/**
	 * Standardkonstruktor, der die Datenbank aufruft und bei nichtbestehender Datenbank eine mit den Tabellen Musiker und Gruppe erzeugt
	 */
	public DatenbankMain() {
		loadDriver();
		try {
			conn = DriverManager.getConnection(protocol + dbName + " ;create=true");
			generateTables();
			// testEintraege(conn, dbName);
			// visualisiereEintraege(conn, dbName);
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}

	}
	
	public static void generateTables() {
		try {
			ResultSet resultSet = conn.getMetaData().getTables("%", "%", "%", new String[] { "TABLE" });
			boolean shouldCreateTable = true;
			String tableName = "";
			while (resultSet.next() && shouldCreateTable) {
				tableName = resultSet.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase("Sectors") || tableName.equalsIgnoreCase("Planets") || tableName.equalsIgnoreCase("Corps") || tableName.equalsIgnoreCase("NPCs") || tableName.equalsIgnoreCase("Aliens")) {
					shouldCreateTable = false;
				}
			}
			resultSet.close();

			if (shouldCreateTable) {
				System.out.println("Will try creating Sectors/Habitat table");
				Statement state = conn.createStatement();
				state.execute("create table Sectors (Systemname varchar(40), Hex varchar(5))");
				state.execute("create table Habitat (Name varchar(512), Systemname varchar(512), Atmosphere varchar(1024), Temperature varchar(1024), Biosphere varchar(1024), Population varchar(1024), TechLevel varchar(1024), Tags varchar(1024), CapitalGovernment varchar(1024), Cultural varchar(1024), Spaceport varchar(1024), Adventures varchar(1024), RecentActivities varchar(1024), OtherNotes varchar(1024))");
				state.execute("create table Factions (Factionname varchar(512), Homeworld varchar(1024), InfluencedWorlds varchar(1024), Business varchar(1024), Reputation varchar(1024), Tags varchar(1024), MaxHitpoints int, Hitpoints int, ForceRating int, CunningRating int, WealthRating int, FacCreds int, ExperiencePoints int, Assets varchar(1024), Goals varchar(1024), Relationship varchar(1024), otherNotes varchar(1024))");
				state.execute("create table Aliens (Racename varchar(512), Homeworld varchar(512), MoreWorlds varchar(1024), Bodytype varchar(1024), BodyDetail varchar(1024), Lenses varchar(1024), LensesDetail varchar(1024), SocialStructure varchar(1024), SocialDetail varchar(1024), TechLevel varchar(1024), StatusInSector varchar(1024), Motivation varchar(1024), OtherNotes varchar(1024))");
				state.execute("create table NPC (Name varchar(512), Homeworld varchar(512), Status varchar(1024), Relationship varchar(1024), Race varchar(1024), Gender varchar(1024), Age varchar(1024), Height varchar(1024), Problems varchar(1024), JobMotivation varchar(1024), Quirks varchar(1024), Class varchar(1024), Level varchar(1024), NPCId varchar(1024), OtherNotes varchar(1024))");
				System.out.println("Tables created");
//				System.out.println("Creating many tables...");
				
				state.close();
				explodeDatabase();

				// System.out.println("Will try creating Planets table");
				// state = conn.createStatement();
				// TODO
				// state.execute("create table Gruppe (Gruppenname varchar(30), Mitglieder varchar(256), Ehemalige varchar(512), Stuecke varchar(1024), Referenzen varchar(1024), Gruppe blob(16M))");
				// System.out.println("Table Gruppe erzeugt");

				// state.close();
			}
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}

	}

	public static void addSector(String Systemname, String Hex) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Sectors values (?,?)");
			ps.setString(1, Systemname);
			ps.setString(2, Hex);
			ps.execute(); // Füge die Änderungen der Datenbank hinzu
			ps.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	public static void deleteSector(String Systemname, String Hex) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Sectors WHERE Systemname = '" + Systemname + "' AND Hex = '" + Hex + "'");
			state.executeUpdate("DELETE FROM Habitat WHERE Systemname = '" + Systemname + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static void updateSector(String Systemname, String Hex) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Sectors WHERE Systemname = '" + Systemname + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		addSector(Systemname, Hex);
	}

	public static void addHabitat(String Name, String Systemname, String Atmosphere, String Temperature, String Biosphere, String Population, String TechLevel, String Tags, String CapitalGovernment, String Cultural, String Spaceport, String Adventures, String RecentActivities, String otherNotes) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Habitat values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, Name);
			ps.setString(2, Systemname);
			ps.setString(3, Atmosphere);
			ps.setString(4, Temperature);
			ps.setString(5, Biosphere);
			ps.setString(6, Population);
			ps.setString(7, TechLevel);
			ps.setString(8, Tags);
			ps.setString(9, CapitalGovernment);
			ps.setString(10, Cultural);
			ps.setString(11, Spaceport);
			ps.setString(12, Adventures);
			ps.setString(13, RecentActivities);
			ps.setString(14, otherNotes);
			ps.execute(); // Füge die Änderungen der Datenbank hinzu
			ps.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	public static void deleteHabitat(String Name, String Systemname) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Habitat WHERE Name = '" + Name + "' AND Systemname = '" + Systemname + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	// String Factionname varchar(512), Hitpoints int(4), ForceRating int(4), CunningRating int(4), WealthRating int(4), FacCreds int(4), ExperiencePoints int(4), Homeworld varchar(1024), InfluencedWorlds varchar(1024), Tags varchar(1024), Assets varchar(1024), Goals varchar(1024)
	public static void addFaction(String factionName, int Maxhitpoints, int hitpoints, int forceRating, int cunningRating, int wealthRating, int facCreds, int expoints, String homeworld, String influencedWorlds, String tags, String assets, String goals, String relationship, String business,
			String reputation, String otherNotes) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Factions values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, factionName);
			ps.setString(2, homeworld);
			ps.setString(3, influencedWorlds);
			ps.setString(4, business);
			ps.setString(5, reputation);
			ps.setString(6, tags);
			ps.setInt(7, Maxhitpoints);
			ps.setInt(8, hitpoints);
			ps.setInt(9, forceRating);
			ps.setInt(10, cunningRating);
			ps.setInt(11, wealthRating);
			ps.setInt(12, facCreds);
			ps.setInt(13, expoints);
			ps.setString(14, assets);
			ps.setString(15, goals);
			ps.setString(16, relationship);
			ps.setString(17, otherNotes);
			ps.execute();
			ps.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	public static void deleteFaction(String Name, String homeworld) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Factions WHERE Factionname = '" + Name + "' AND Homeworld LIKE '" + homeworld + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static void deleteFaction(String Name) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Factions WHERE Factionname = '" + Name + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static void addAlien(String racename, String homeworld, String moreWorlds, String bodytype, String bodyDetail, String lenses, String lensesDetail, String social, String socialDetail, String techLevel, String statusInSector, String motivation, String otherNotes) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Aliens values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, racename);
			ps.setString(2, homeworld);
			ps.setString(3, moreWorlds);
			ps.setString(4, bodytype);
			ps.setString(5, bodyDetail);
			ps.setString(6, lenses);
			ps.setString(7, lensesDetail);
			ps.setString(8, social);
			ps.setString(9, socialDetail);
			ps.setString(10, techLevel);
			ps.setString(11, statusInSector);
			ps.setString(12, motivation);
			ps.setString(13, otherNotes);
			ps.execute();
			ps.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	public static void deleteAlien(String racename) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM Aliens WHERE Racename = '" + racename + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static void addNPC(String Name, String homeworld, String status, String relationship, String race, String gender, String age, String height, String problems, String jobMotivation, String Quirks, String classe, String level, String id, String otherNotes) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO NPC values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, Name);
			ps.setString(2, homeworld);
			ps.setString(3, status);
			ps.setString(4, relationship);
			ps.setString(5, race);
			ps.setString(6, gender);
			ps.setString(7, age);
			ps.setString(8, height);
			ps.setString(9, problems);
			ps.setString(10, jobMotivation);
			ps.setString(11, Quirks);
			ps.setString(12, classe);
			ps.setString(13, level);
			ps.setString(14, id);
			ps.setString(15, otherNotes);
			ps.execute();
			ps.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	public static void deleteNPC(String id) {
		try {
			Statement state = conn.createStatement();
			state.executeUpdate("DELETE FROM NPC WHERE NPCId = '" + id + "'");
			state.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static String[] searchNPC(String id) {
		String[] results = new String[15];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM NPC WHERE NPCId LIKE '" + id + "'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			if (rs.next()) {
				for (int i = 0; i < 15; i++) {
					results[i] = rs.getString(i + 1);
				}
			} else {
				// System.out.println("Error: No result fetched from database. No matching planetname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two npcs with this id were found. ERROR");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return results;
	}

	public static String[][] searchNPC() {
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM NPC");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			while (rs.next()) {
				arrResults.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getString(15) });
			}
			rs.close();
			Collections.sort(arrResults, new Comparator<String[]>() {
				public int compare(String[] string1, String[] string2) {
					return string1[0].compareTo(string2[0]);
				}
			});

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[][] {});
	}

	public static String[][] searchNPCbyPlanetname(String planetname) {
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM NPC WHERE Homeworld LIKE '" + planetname + "'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			while (rs.next()) {
				arrResults.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getString(15) });
			}
			rs.close();
			// Collections.sort(arrResults, new Comparator<String[]>() {
			// public int compare(String[] string1, String[] string2) {
			// return string1[0].compareTo(string2[0]);
			// }
			// });

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[][] {});
	}

	public static String[][] searchSectors(String search) {
		String[] results = new String[2];
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Sectors WHERE Systemname LIKE '%" + search + "%' OR Hex LIKE '%" + search + "%'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			while (rs.next()) {
				arrResults.add(new String[] { rs.getString(1), rs.getString(2) });
			}
			rs.close();
			Collections.sort(arrResults, new Comparator<String[]>() {
				public int compare(String[] string1, String[] string2) {
					return string1[1].compareTo(string2[1]);
				}
			});

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[][] {});
	}

	public static String[] searchSectorsHex(String hex) {
		String[] results = new String[2];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Sectors WHERE Hex LIKE '" + hex + "'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			if (rs.next()) {
				results[0] = rs.getString(1);
				results[1] = rs.getString(2);
			} else {
				// System.out.println("Error: No result fetched from database. No matching planetname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two sectors with this hex were found. ERROR");
			}

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return results;
	}

	public static String[] searchHabitatBySystemname(String Systemname) {
		ArrayList<String> arrResults = new ArrayList<String>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Habitat WHERE Systemname LIKE '%" + Systemname + "%'");
			while (rs.next()) {
				arrResults.add(rs.getString(1));
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[] {});
	}

	public static String[] searchHabitatByPlanetname(String planetName, String systemName) {
		String[] result = new String[14];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Habitat WHERE Name LIKE '" + planetName + "' AND Systemname = '" + systemName + "'");
			if (rs.next()) {
				for (int i = 0; i < 14; i++) {
					result[i] = rs.getString(i + 1);
					// System.out.println(result[i]);
				}
			} else {
				// System.out.println("Error: No result fetched from database. No matching planetname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two planets with this name were found. Only the first will be displayed. Please differentiate planets with same name... (or knock up the author and tell him he should include the system name into the lookup...)");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return result;
	}

	public static String[] searchHabitatByPlanetnameUNSAFE(String planetName) {
		String[] result = new String[14];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Habitat WHERE Name LIKE '" + planetName + "'");
			if (rs.next()) {
				for (int i = 0; i < 14; i++) {
					result[i] = rs.getString(i + 1);
					// System.out.println(result[i]);
				}
			} else {
				// System.out.println("Error: No result fetched from database. No matching planetname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two planets with this name were found. Only the first will be displayed. Please differentiate planets with same name... (or knock up the author and tell him he should include the system name into the lookup...)");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return result;
	}

	public static String[][] searchFactions(String search) {
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Factions WHERE Factionname LIKE '%" + search + "%' OR Homeworld LIKE '%" + search + "%'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			while (rs.next()) {
				arrResults.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), Integer.toString(rs.getInt(7)), Integer.toString(rs.getInt(8)), Integer.toString(rs.getInt(9)), Integer.toString(rs.getInt(10)),
						Integer.toString(rs.getInt(11)), Integer.toString(rs.getInt(12)), Integer.toString(rs.getInt(13)), rs.getString(14), rs.getString(15), rs.getString(16) , rs.getString(17) });
			}
			rs.close();
			Collections.sort(arrResults, new Comparator<String[]>() {
				public int compare(String[] string1, String[] string2) {
					return string1[0].compareTo(string2[0]);
				}
			});

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[][] {});
	}

	public static String[] searchUniqueFaction(String factionName, String homeworld) {
		String[] result = new String[17];
		// ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Factions WHERE Factionname LIKE '" + factionName + "' AND Homeworld LIKE '" + homeworld + "'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			if (rs.next()) {
				result = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), Integer.toString(rs.getInt(7)), Integer.toString(rs.getInt(8)), Integer.toString(rs.getInt(9)), Integer.toString(rs.getInt(10)),
						Integer.toString(rs.getInt(11)), Integer.toString(rs.getInt(12)), Integer.toString(rs.getInt(13)), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17) };
			} else {
				// System.out.println("Error: No result fetched from database. No matching factionname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two factions with this name were found. Only the first will be displayed. Please differentiate factions with same name...");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return result;
	}

	public static String[] searchAliens(String raceName) {
		String[] result = new String[13];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Aliens WHERE Racename LIKE '" + raceName + "'");
			if (rs.next()) {
				result = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) };
			} else {
				// System.out.println("Error: No result fetched from database. No matching alienname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two aliens with this name were found. Only the first will be displayed. Please differentiate aliens with same name...");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return result;
	}

	public static String[][] searchAliens() {
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM Aliens WHERE Racename LIKE '%'");
			// rs has now a iterable String[] with the entries somehow matching
			// the search query
			while (rs.next()) {
				arrResults.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) });
			}
			rs.close();
			Collections.sort(arrResults, new Comparator<String[]>() {
				public int compare(String[] string1, String[] string2) {
					return string1[0].compareTo(string2[0]);
				}
			});

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return arrResults.toArray(new String[][] {});
	}
//
	private static String[] searchTag(String tag) {
		String[] result = new String[8];
		try {
			Statement state = conn.createStatement();
			ResultSet rs;
			rs = state.executeQuery("Select * FROM WORLD_TAG WHERE tagName LIKE '" + tag + "'");
			if (rs.next()) {
				result = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8) };
			} else {
				// System.out.println("Error: No result fetched from database. No matching alienname.");
			}
			if (rs.next()) {
				System.out.println("Error: At least two tag with this name were found. Only the first will be displayed. Please differentiate tags with same name...");
			}
			rs.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
		return result;
	}

	public static String[][] getWorldTag(String multiLineTag) {
		ArrayList<String[]> arrResults = new ArrayList<String[]>();
		String[] singleLineTag = multiLineTag.split(",");
		for (String tag : singleLineTag) {
//			System.out.println("tag: "+tag);
			String[] resultTag = searchTag(tag.trim());
			if (resultTag[0] != null && !resultTag[0].isEmpty()) {
				arrResults.add(resultTag);
			}
		}
		return arrResults.toArray(new String[][] {});
	}

//	 public int getRowsize(String table) {
//	 try {
//	 Statement state = conn.createStatement();
//	 ResultSet rs = state.executeQuery("SELECT COUNT(*) FROM "+table);
//	 ResultSetMetaData rsmd = rs.getMetaData();
//	 int count = rsmd.getColumnCount();
//	 rs.close();
//	 state.close();
//	 return count;
//	 } catch (SQLException sqle) {
//	 printSQLException(sqle);
//	 }
//	 return 0;
//	 }

	public static void dropDatabase() {
		try {
			Statement state = conn.createStatement();
			state.execute("DROP TABLE Habitat");
			state.execute("DROP TABLE Sectors");
			state.execute("DROP TABLE Factions");
			state.execute("DROP TABLE Aliens");
			state.execute("DROP TABLE NPC");
			state.execute("DROP TABLE WORLD_TAG");
			state.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	/**
	 * Herunterfahren der Datenbank
	 */
	public static void shutdown() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

	/**
	 * Loads the appropriate JDBC driver for this environment/framework. For example, if we are in an embedded environment, we load Derby's embedded Driver, <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
	 */
	private static void loadDriver() {
		/*
		 * The JDBC driver is loaded by loading its class. If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may be automatically loaded, making this code optional.
		 * 
		 * In an embedded environment, this will also start up the Derby engine (though not any databases), since it is not already running. In a client environment, the Derby engine is being run by the network server framework.
		 * 
		 * In an embedded environment, any static Derby system properties must be set before loading the driver to take effect.
		 */
		try {
			Class.forName(driver).newInstance();
			// System.out.println("Loaded the appropriate driver");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver " + driver);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver " + driver);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver " + driver);
			iae.printStackTrace(System.err);
		}
	}

	/**
	 * Prints details of an SQLException chain to <code>System.err</code>. Details included are SQL State, Error code, Exception message.
	 * 
	 * @param e
	 *            the SQLException from which to print details.
	 */
	public static void printSQLException(SQLException e) {
		// Unwraps the entire exception chain to unveil the real cause of the
		// Exception.
		while (e != null) {
			System.err.println("\n----- SQLException -----");
			System.err.println("  SQL State:  " + e.getSQLState());
			System.err.println("  Error Code: " + e.getErrorCode());
			System.err.println("  Message:    " + e.getMessage());
			// for stack traces, refer to derby.log or uncomment this:
			// e.printStackTrace(System.err);
			e = e.getNextException();
		}
	}

	private static void explodeDatabase() {
		try {
			Statement state = conn.createStatement();
			state.execute("CREATE TABLE WORLD_TAG (id INTEGER, tagName varchar(64), tagDescription varchar(1024), tagEnemies varchar(1024), tagFriends varchar(1024), tagComplications varchar(1024), tagThings varchar(1024), tagPlaces CLOB, tagShort varchar(32))"); 
//			state.execute("CREATE TABLE WORLD_TAG (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE, name VARCHAR(64) NOT NULL , description CLOB NOT NULL , enemies CLOB NOT NULL , friends CLOB NOT NULL , complications CLOB NOT NULL , things CLOB NOT NULL , places CLOB NOT NULL , short VARCHAR(32))");
			
			state.execute("INSERT INTO WORLD_TAG (id, tagName, tagDescription, tagEnemies, tagFriends, tagComplications, tagThings, tagPLaces, tagShort) VALUES (101, 'Abandoned Colony', 'The world once hosted a colony, whether human or otherwise, until some crisis or natural disaster drove the inhabitants away or killed them off. The colony might have been mercantile in nature, an expedition to extract valuable local resources, or it might have been a reclusive cabal of zealots. The remains of the colony are usually in ruins, and might still be dangerous from the aftermath of whatever destroyed it in the first place.', 'Crazed survivors, Ruthless plunderers of the ruins, Automated defense system', 'Inquisitive stellar archaeologist, Heir to the colony''s property, Local wanting the place cleaned out', 'The local government wants the ruins to remain a secret, The locals claim ownership of it, The colony is crumbling and dangerous to navigate', 'Long-lost property deeds, Relic stolen by the colonists when they left, Historical record of the colonization attempt', 'Decaying habitation block, Vine-covered town square, Structure buried by an ancient landslide', 'Abandoned')");
			state.execute("INSERT INTO WORLD_TAG VALUES (102, 'Ancient Ruins', 'The world has significant alien ruins present. The locals may or may not permit others to investigate the ruins, and may make it difficult to remove any objects of value without substantial payment.', 'Customs inspector, Worshipper of the ruins, Hidden alien survivor', 'Curious scholar, Avaricious local resident, Interstellar smuggler', 'Traps in the ruins, Remote location, Paranoid customs officials', 'Precious alien artifacts, Objects left with the remains of a prior unsuccessful expedition, Untranslated alien texts, Untouched hidden ruins', 'Undersea ruin, Orbital ruin, Perfectly preserved alien building, Alien mausoleum', 'Ruins')");
			state.execute("INSERT INTO WORLD_TAG VALUES (103, 'Altered Humanity', 'The humans on this world are visibly and drastically different from normal humanity. They may have additional limbs, new sensory organs, or other significant changes. Were these from ancestral eugenic manipulation, or from environmental toxins?', 'Biochauvinist local, Local experimenter, Mentally unstable mutant', 'Local seeking a cure, Curious xenophiliac, Anthropological researcher', 'Alteration is contagious, Alteration is necessary for long-term survival, Locals fear and mistrust non-local humans', 'Original pretech mutagenic equipment, Valuable biological byproduct from the mutants, Cure for the altered genes, Record of the original colonial genotypes', 'Abandoned eugenics laboratory, An environment requiring the mutation for survival, A sacred site where the first local was transformed', 'Altered Hum.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (104, 'Area 51', 'The world’s government is fully aware of their local stellar neighbors, but the common populace has no idea about it- and the government means to keep it that way. Trade with government officials in remote locations is possible, but any attempt to clue the commoners in on the truth will be met with lethal reprisals.', 'Suspicious government minder, Free merchant who likes his local monopoly, Local who wants a specimen for dissection', 'Crusading offworld investigator, Conspiracy-theorist local, Idealistic government reformer', 'The government has a good reason to keep the truth concealed, The government ruthlessly oppresses the natives, The government is actually composed of offworlders', 'Elaborate spy devices, Memory erasure tech, Possessions of the last offworlder who decided to spread the truth', 'Desert airfield, Deep subterranean bunker, Hidden mountain valley', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (105, 'Badlands World', 'Whatever the ostensible climate and atmosphere type, something horrible happened to this world. Biological, chemical, or nanotechnical weaponry has reduced it to a wretched hellscape.', 'Mutated badlands fauna, Desperate local, Badlands raider chief', 'Native desperately wishing to escape the world, Scientist researching ecological repair methods, Ruin scavenger', 'Radioactivity, Bioweapon traces, Broken terrain, Sudden local plague', 'Maltech research core, Functional pretech weaponry, An uncontaminated well', 'Untouched oasis, Ruined city, Salt flat', 'Badlands')");
			state.execute("INSERT INTO WORLD_TAG VALUES (106, 'Bubble Cities', 'Whether due to a lack of atmosphere or an uninhabitable climate, the world’s cities exist within domes or pressurized buildings. In such sealed environments, techniques of surveillance and control can grow baroque and extreme.', 'Native dreading outsider contamination, Saboteur from another bubble city, Local official hostile to outsider ignorance of laws', 'Local rebel against the city officials, Maintenance chief in need of help, Surveyor seeking new building sites', 'Bubble rupture, Failing atmosphere reprocessor, Native revolt against officials, All-seeing surveillance cameras', 'Pretech habitat technology, Valuable industrial products, Master key codes to a city''s security system', 'City power core, Surface of the bubble, Hydroponics complex, Warren-like hab block', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (107, 'Civil War', 'The world is currently torn between at least two opposing factions, all of which claim legitimacy. The war may be the result of a successful rebel uprising against tyranny, or it might just be the result of schemers who plan to be the new masters once the revolution is complete.', 'Faction commissar, Angry native, Conspiracy theorist who blames offworlders for the war, Deserter looking out for himself, Guerilla bandits', 'Faction loyalist seeking aid, Native caught in the crossfire, Offworlder seeking passage off the planet', 'The front rolls over the group, Famine strikes, Bandit infestations', 'Ammo dump, Military cache, Treasure buried for after the war, Secret war plans', 'Battle front, Bombed-out town, Rear-area red light zone, Propaganda broadcast tower', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (108, 'Cold War', 'Two or more great powers control the planet, and they have a hostility to each other that’s just barely less than open warfare. The hostility might be ideological in nature, or it might revolve around control of some local resource.', 'Suspicious chief of intelligence, Native who thinks the outworlders are with the other side, Femme fatale', 'Apolitical information broker, Spy for the other side, Unjustly accused innocent, He''s a bastard, but he''s our bastard official', 'Police sweep, Low-level skirmishing, Red scare', 'List of traitors in government, secret military plans, Huge cache of weapons built up in preparation for war', 'Seedy bar in a neutral area, Political rally, Isolated area where fighting is underway', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (109, 'Colonized Population', 'A neighboring world has successfully colonized this less-advanced or less-organized planet, and the natives aren’t happy about it. A puppet government may exist, but all real decisions are made by the local viceroy.', 'Suspicious security personnel, Offworlder-hating natives, Local crime boss preying on rich offworlders', 'Native resistance leader, Colonial official seeking help, Native caught between the two sides', 'Natives won''t talk to offworlders, Colonial repression, Misunderstood local customs', 'Relic of the resistance movement, List of collaborators, Precious substance extracted by colonial labor', 'Deep wilderness resistance camp, City district off-limits to natives, Colonial labor site', 'Colonized')");
			state.execute("INSERT INTO WORLD_TAG VALUES (110, 'Desert World', 'The world may have a breathable atmosphere and a human-tolerable temperature range, but it is an arid, stony waste outside of a few places made habitable by human effort. The deep wastes are largely unexplored and inhabited by outcasts and worse.', 'Raider chieftain, Crazed hermit, Angry isolationists, Paranoid mineral prospector, Strange desert beast', 'Native guide, Research biologist, Aspiring terraformer', 'Sandstorms, Water supply failure, Native warfare over water rights', 'Enormous water reservoir, Map of hidden wells, Pretech rainmaking equipment', 'Oasis, The Empty Quarter of the desert, Hidden underground cistern', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (201, 'Eugenic Cult', 'Even in the days before the Silence, major improvement of the human genome always seemed to come with unacceptable side-effects. Some worlds host secret cults that perpetuate these improvements regardless of the cost, and a few planets have been taken over entirely by the cults.', 'Eugenic superiority fanatic, Mentally unstable homo superior, Mad eugenic scientist', 'Eugenic propagandist, Biotechnical investigator, Local seeking revenge on cult', 'The altered cultists look human, The locals are terrified of any unusual physical appearance, The genetic modifications- and drawbacks- are contagious with long exposure', 'Serum that induces the alteration, Elixir that reverses the alteration, Pretech biotechnical databanks, List of secret cult sympathizers', 'Eugenic breeding pit, Isolated settlement of altered humans, Public place infiltrated by cult sympathizers', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (202, 'Exchange Consulate', 'The Exchange of Light once served as the largest, most trusted banking and diplomatic service in human space. Even after the Silence, some worlds retain a functioning Exchange Consulate where banking services and arbitration can be arranged.', 'Corrupt Exchange official, Indebted native who thinks the players are Exchange agents, Exchange official dunning the players for debts incurred', 'Consul in need of offworld help, Local banker seeking to hurt his competition, Exchange diplomat', 'The local Consulate has been corrupted, the Consulate is cut off from its funds, A powerful debtor refuses to pay', 'Exchange vault codes, Wealth hidden to conceal it from a bankruptcy judgment, Location of forgotten vault', 'Consulate meeting chamber, Meeting site between fractious disputants, Exchange vault', 'Exch. Cons.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (203, 'Feral World', 'In the long, isolated night of the Silence, some worlds have experienced total moral and cultural collapse. Whatever remains has been twisted beyond recognition into assorted death cults, xenophobic fanaticism, horrific cultural practices, or other behavior unacceptable on more enlightened worlds. These worlds are almost invariably classed under Red trade codes.', 'Decadent noble, Mad cultist, Xenophobic local, Cannibal chief, Maltech researcher', 'Trapped outworlder, Aspiring reformer, Native wanting to avoid traditional flensing', 'Horrific local celebration, Inexplicable and repugnant social rules, Taboo zones and people', 'Terribly misused piece of pretech, Wealth accumulated through brutal evildoing, Valuable possession owned by luckless outworlder victim', 'Atrocity amphitheater, Traditional torture parlor, Ordinary location twisted into something terrible.', 'Feral')");
			state.execute("INSERT INTO WORLD_TAG VALUES (204, 'Flying Cities', 'Perhaps the world is a gas giant, or plagued with unendurable storms at lower levels of the atmosphere. For whatever reason, the cities of this world fly above the surface of the planet. Perhaps they remain stationary, or perhaps they move from point to point in search of resources.', 'Rival city pilot, Tech thief attempting to steal outworld gear, Saboteur or scavenger plundering the city''s tech', 'Maintenance tech in need of help, City defense force pilot, Meteorological researcher', 'Sudden storms, Drastic altitude loss, Rival city attacks, Vital machinery breaks down', 'Precious refined atmospheric gases, Pretech grav engine plans, Meteorological codex predicting future storms', 'Underside of the city, The one calm place on the planet’s surface, Catwalks stretching over unimaginable gulfs below.', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (205, 'Forbidden Tech', 'Some group on this planet fabricates or uses maltech. Unbraked AIs doomed to metastasize into insanity, nation-destroying nanowarfare particles, slow-burn DNA corruptives, genetically engineered slaves, or something worse still. The planet’s larger population may or may not be aware of the danger in their midst.', 'Mad scientist, Maltech buyer from offworld, Security enforcer', 'Victim of maltech, Perimeter agent, Investigative reporter, Conventional arms merchant', 'The maltech is being fabricated by an unbraked AI, The government depends on revenue from maltech sales to offworlders, Citizens insist that it''s not really maltech', 'Maltech research data, The maltech itself, Precious pretech equipment used to create it', 'Horrific laboratory, Hellscape sculpted by the maltech''s use, Government building meeting room', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (206, 'Freak Geology', 'The geology or geography of this world is simply freakish. Perhaps it’s composed entirely of enormous mountain ranges, or regular bands of land and sea, or the mineral structures all fragment into perfect cubes. The locals have learned to deal with it and their culture will be shaped by its requirements.', 'Crank xenogeologist, Cultist who believes it the work of aliens', 'Research scientist, Prospector, Artist', 'Local conditions that no one remembers to tell outworlders about, Lethal weather, Seismic activity', 'Unique crystal formations, Hidden veins of a major precious mineral strike, Deed to a location of great natural beauty', 'Atop a bizarre geological formation, Tourist resort catering to offworlders', 'Frk. Geology')");
			state.execute("INSERT INTO WORLD_TAG VALUES (207, 'Freak Weather', 'The planet is plagued with some sort of bizarre or hazardous weather pattern. Perhaps city-flattening storms regularly scourge the surface, or the world’s sun never pierces its thick banks of clouds.', 'Criminal using the weather as a cover, Weather cultists convinced the offworlders are responsible for some disaster, Native predators dependent on the weather', 'Meteorological researcher, Holodoc crew wanting shots of the weather', 'The weather itself, Malfunctioning pretech terraforming engines that cause the weather', 'Wind-scoured deposits of precious minerals, Holorecords of a spectacularly and rare weather pattern, Naturally- sculpted objects of intricate beauty', 'Eye of the storm, The one sunlit place, Terraforming control room', 'Frk. Weather')");
			state.execute("INSERT INTO WORLD_TAG VALUES (208, 'Friendly Foe', 'Some hostile alien race or malevolent cabal has a branch or sect on this world that is actually quite friendly toward outsiders. For whatever internal reason, they are willing to negotiate and deal honestly with strangers, and appear to lack the worst impulses of their fellows.', 'Driven hater of all their kind, Internal malcontent bent on creating conflict, Secret master who seeks to lure trust', 'Well-meaning bug-eyed monster, Principled eugenics cultist, Suspicious investigator', 'The group actually is as harmless and benevolent as they seem, The group offers a vital service at the cost of moral compromise, The group still feels bonds of affiliation with their hostile brethren', 'Forbidden xenotech, Eugenic biotech template, Evidence to convince others of their kind that they are right', 'Repurposed maltech laboratory, Alien conclave building, Widely-feared starship interior', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (209, 'Gold Rush', 'Gold, silver, and other conventional precious minerals are common and cheap now that asteroid mining is practical for most worlds. But some minerals and compounds remain precious and rare, and this world has recently been discovered to have a supply of them. People from across the sector have come to strike it rich.', 'Paranoid prospector, Aspiring mining tycoon, Rapacious merchant', 'Claim-jumped miner, Native alien, Curious tourist', 'The strike is a hoax, The strike is of a dangerous toxic substance, Export of the mineral is prohibited by the planetary government, The native aliens live around the strike''s location', 'Cases of the refined element, Pretech mining equipment, A dead prospector''s claim deed', 'Secret mine, Native alien village, Processing plant, Boom town', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (210, 'Hatred', 'For whatever reason, this world’s populace has a burning hatred for the inhabitants of a neighboring system. Perhaps this world was colonized by exiles, or there was a recent interstellar war, or ideas of racial or religious superiority have fanned the hatred. Regardless of the cause, the locals view their neighbor and any sympathizers with loathing.', 'Native convinced that the offworlders are agents of Them, Cynical politician in need of scapegoats', 'Intelligence agent needing catspaws, Holodoc producers needing an inside look', 'The characters are wearing or using items from the hated world, The characters are known to have done business there, The characters look like the hated others', 'Proof of Their evildoing, Reward for turning in enemy agents, Relic stolen by Them years ago', 'War crimes museum, Atrocity site, Captured, decommissioned spaceship kept as a trophy', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (301, 'Heavy Industry', 'With interstellar transport so limited in the bulk it can move, worlds have to be largely self-sufficient in industry. Some worlds are more sufficient than others, however, and this planet has a thriving manufacturing sector capable of producing large amounts of goods appropriate to its tech level. The locals may enjoy a correspondingly higher lifestyle, or the products might be devoted towards vast projects for the aggrandizement of the rulers.', 'Tycoon monopolist, Industrial spy, Malcontent revolutionary', 'Aspiring entrepreneur, Worker union leader, Ambitious inventor', 'The factories are toxic, The resources extractable at their tech level are running out, The masses require the factory output for survival, The industries'' major output is being obsoleted by offworld tech', 'Confidential industrial data, Secret union membership lists, Ownership shares in an industrial complex', 'Factory floor, Union meeting hall, Toxic waste dump, R&D complex', 'Hvy. Industry')");
			state.execute("INSERT INTO WORLD_TAG VALUES (302, 'Heavy Mining', 'This world has large stocks of valuable minerals, usually necessary for local industry, life support, or refinement into loads small enough to export offworld. Major mining efforts are necessary to extract the minerals, and many natives work in the industry.', 'Mine boss, Tunnel saboteur, Subterranean predators', 'Hermit prospector, Offworld investor, Miner''s union representative', 'The refinery equipment breaks down, Tunnel collapse, Silicate life forms growing in the miners'' lungs', 'The mother lode, Smuggled case of refined mineral, Faked crystalline mineral samples', 'Vertical mine face, Tailing piles, Roaring smelting complex', 'Hvy. Mining')");
			state.execute("INSERT INTO WORLD_TAG VALUES (303, 'Hostile Biosphere', 'The world is teeming with life, and it hates humans. Perhaps the life is xenoallergenic, forcing filter masks and tailored antiallergens for survival. It could be the native predators are huge and fearless, or the toxic flora ruthlessly outcompetes earth crops.', 'Local fauna, Nature cultist, Native aliens, Callous labor overseer', 'Xenobiologist, Tourist on safari, Grizzled local guide', 'Filter masks fail, Parasitic alien infestation, Crop greenhouses lose bio-integrity', 'Valuable native biological extract, Abandoned colony vault, Remains of an unsuccessful expedition', 'Deceptively peaceful glade, Steaming polychrome jungle, Nightfall when surrounded by Things', 'Host. Biosphere')");
			state.execute("INSERT INTO WORLD_TAG VALUES (304, 'Hostile Space', 'The system in which the world exists is a dangerous neighborhood. Something about the system is perilous to inhabitants, either through meteor swarms, stellar radiation, hostile aliens in the asteroid belt, or periodic comet clouds.', 'Alien raid leader, Meteor-launching terrorists, Paranoid local leader', 'Astronomic researcher, Local defense commander, Early warning monitor agent', 'The natives believe the danger is divine chastisement, The natives blame outworlders for the danger, The native elite profit from the danger in some way', 'Early warning of a raid or impact, Abandoned riches in a disaster zone, Key to a secure bunker', 'City watching an approaching asteroid, Village burnt in an alien raid, Massive ancient crater', 'Host. Space')");
			state.execute("INSERT INTO WORLD_TAG VALUES (305, 'Local Specialty', 'The world may be sophisticated or barely capable of steam engines, but either way it produces something rare and precious to the wider galaxy. It might be some pharmaceutical extract produced by a secret recipe, a remarkably popular cultural product, or even gengineered humans uniquely suited for certain work.', 'Monopolist, Offworlder seeking prohibition of the specialty, Native who views the specialty as sacred', 'Spy searching for the source, Artisan seeking protection, Exporter with problems', 'The specialty is repugnant in nature, The crafters refuse to sell to offworlders, The specialty is made in a remote, dangerous place, The crafters don''t want to make the specialty any more', 'The specialty itself, The secret recipe, Sample of a new improved variety', 'Secret manufactory, Hidden cache, Artistic competition for best artisan', 'Lcl. Spec.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (306, 'Local Tech', 'The locals can create a particular example of extremely high tech, possibly even something that exceeds pretech standards. They may use unique local resources to do so, or have stumbled on a narrow scientific breakthrough, or still have a functional experimental manufactory.', 'Keeper of the tech, Offworld industrialist, Automated defenses that suddenly come alive, Native alien mentors', 'Curious offworld scientist, Eager tech buyer, Native in need of technical help', 'The tech is unreliable, The tech only works on this world, The tech has poorly-understood side effects, The tech is alien in nature.', 'The tech itself, An unclaimed payment for a large shipment, The secret blueprints for its construction, An ancient alien R&D database', 'Alien factory, Lethal R&D center, Tech brokerage vault', 'Lcl. Tech')");
			state.execute("INSERT INTO WORLD_TAG VALUES (307, 'Major Spaceyard', 'Most worlds of tech level 4 or greater have the necessary tech and orbital facilities to build spike drives and starships. This world is blessed with a major spaceyard facility, either inherited from before the Silence or painstakingly constructed in more recent decades. It can build even capital-class hulls, and do so more quickly and cheaply than its neighbors.', 'Enemy saboteur, Industrial spy, Scheming construction tycoon, Aspiring ship hijacker', 'Captain stuck in drydock, Maintenance chief, Mad innovator', 'The spaceyard is an alien relic, The spaceyard is burning out from overuse, The spaceyard is alive, The spaceyard relies on maltech to function', 'Intellectual property-locked pretech blueprints, Override keys for activating old pretech facilities, A purchased but unclaimed spaceship.', 'Hidden shipyard bay, Surface of a partially-completed ship, Ship scrap graveyard', 'Maj. Spaceyard')");
			state.execute("INSERT INTO WORLD_TAG VALUES (308, 'Minimal Contact', 'The locals refuse most contact with offworlders. Only a small, quarantined treaty port is provided for offworld trade, and ships can expect an exhaustive search for contraband. Local governments may be trying to keep the very existence of interstellar trade a secret from their populations, or they may simply consider offworlders too dangerous or repugnant to be allowed among the population.', 'Customs official, Xenophobic natives, Existing merchant who doesn''t like competition', 'Aspiring tourist, Anthropological researcher, Offworld thief, Religious missionary', 'The locals carry a disease harmless to them and lethal to outsiders, The locals hide dark purposes from offworlders, The locals have something desperately needed but won''t bring it into the treaty port', 'Contraband trade goods, Security perimeter codes, Black market local products', 'Treaty port bar, Black market zone, Secret smuggler landing site', 'Min. Contact')");
			state.execute("INSERT INTO WORLD_TAG VALUES (309, 'Misandry/Misogyny', 'The culture on this world holds a particular gender in contempt. Members of that gender are not permitted positions of formal power, and may be restricted in their movements and activities. Some worlds may go so far as to scorn both traditional genders, using gengineering techniques to hybridize or alter conventional human biology.', 'Cultural fundamentalist, Cultural missionary to outworlders', 'Oppressed native, Research scientist, Offworld emancipationist, Local reformer', 'The oppressed gender is restive against the customs, The oppressed gender largely supports the customs, The customs relate to some physical quality of the world, The oppressed gender has had maltech gengineering done to tame them.', 'Aerosol reversion formula for undoing gengineered docility, Hidden history of the world, Pretech gengineering equipment', 'Shrine to the virtues of the favored gender, Security center for controlling the oppressed, Gengineering lab', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (310, 'Oceanic World', 'The world is entirely or almost entirely covered with liquid water. Habitations might be floating cities, or might cling precariously to the few rocky atolls jutting up from the waves, or are planted as bubbles on promontories deep beneath the stormy surface. Survival depends on aquaculture. Planets with inedible alien life rely on gengineered Terran sea crops.', 'Pirate raider, Violent salvager gang, Tentacled sea monster', 'Daredevil fisherman, Sea hermit, Sapient native life', 'The liquid flux confuses grav engines too badly for them to function on this world, Sea is corrosive or toxic, The seas are wracked by regular storms', 'Buried pirate treasure, Location of enormous schools of fish, Pretech water purification equipment', 'The only island on the planet, Floating spaceport, Deck of a storm-swept ship, Undersea bubble city', 'Oceanic')");
			state.execute("INSERT INTO WORLD_TAG VALUES (401, 'Out of Contact', 'The natives have been entirely out of contact with the greater galaxy for centuries or longer. Perhaps the original colonists were seeking to hide from the rest of the universe, or the Silence destroyed any means of communication. It may have been so long that human origins on other worlds have regressed into a topic for legends. The players might be on the first offworld ship to land since the First Wave of colonization a thousand years ago.', 'Fearful local ruler, Zealous native cleric, Sinister power that has kept the world isolated', 'Scheming native noble, Heretical theologian, UFO cultist native', 'Automatic defenses fire on ships that try to take off, The natives want to stay out of contact, The natives are highly vulnerable to offworld diseases, The native language is completely unlike any known to the group', 'Ancient pretech equipment, Terran relic brought from Earth, Logs of the original colonists', 'Long-lost colonial landing site, Court of the local ruler, Ancient defense battery controls', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (402, 'Outpost World', 'The world is only a tiny outpost of human habitation planted by an offworld corporation or government. Perhaps the staff is there to serve as a refueling and repair stop for passing ships, or to oversee an automated mining and refinery complex. They might be there to study ancient ruins, or simply serve as a listening and monitoring post for traffic through the system. The outpost is likely well-equipped with defenses against casual piracy.', 'Space-mad outpost staffer, Outpost commander who wants it to stay undiscovered, Undercover saboteur', 'Lonely staffer, Fixated researcher, Overtaxed maintenance chief', 'The alien ruin defense systems are waking up, Atmospheric disturbances trap the group inside the outpost for a month, Pirates raid the outpost, The crew have become converts to a strange set of beliefs', 'Alien relics, Vital scientific data, Secret corporate exploitation plans', 'Grimy recreation room, Refueling station, The only building on the planet, A “starport” of swept bare rock.', 'Outpost')");
			state.execute("INSERT INTO WORLD_TAG VALUES (403, 'Perimeter Agency', 'Before the Silence, the Perimeter was a Terran-sponsored organization charged with rooting out use of maltech- technology banned in human space as too dangerous for use or experimentation. Unbraked AIs, gengineered slave species, nanotech replicators, weapons of planetary destruction... the Perimeter hunted down experimenters with a great indifference to planetary laws. Most Perimeter Agencies collapsed during the Silence, but a few managed to hold on to their mission, though modern Perimeter agents often find more work as conventional spies and intelligence operatives.', 'Renegade Agency Director, Maltech researcher, Paranoid intelligence chief', 'Agent in need of help, Support staffer, Unjustly targeted researcher', 'The local Agency has gone rogue and now uses maltech, The Agency archives have been compromised, The Agency has been targeted by a maltech-using organization, The Agency''s existence is unknown to the locals', 'Agency maltech research archives, Agency pretech spec-ops gear, File of blackmail on local politicians', 'Interrogation room, Smoky bar, Maltech laboratory, Secret Agency base', 'Perim. Agcy.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (404, 'Pilgrimage Site', 'The world is noted for an important spiritual or historical location, and might be the sector headquarters for a widespread religion or political movement. The site attracts wealthy pilgrims from throughout nearby space, and those with the money necessary to manage interstellar travel can be quite generous to the site and its keepers. The locals tend to be fiercely protective of the place and its reputation, and some places may forbid the entrance of those not suitably pious or devout.', 'Saboteur devoted to a rival belief, Bitter reformer who resents the current leadership, Swindler conning the pilgrims', 'Protector of the holy site, Naive offworlder pilgrim, Outsider wanting to learn the sanctum''s inner secrets', 'The site is actually a fake, The site is run by corrupt and venal keepers, A natural disaster threatens the site', 'Ancient relic guarded at the site, Proof of the site''s inauthenticity, Precious offering from a pilgrim', 'Incense-scented sanctum, Teeming crowd of pilgrims, Imposing holy structure', 'Pilgrimage')");
			state.execute("INSERT INTO WORLD_TAG VALUES (405, 'Police State', 'The world is a totalitarian police state. Any sign of disloyalty to the planet’s rulers is punished severely, and suspicion riddles society. Some worlds might operate by Soviet-style informers and indoctrination, while more technically sophisticated worlds might rely on omnipresent cameras or braked AI guardian angels. Outworlders are apt to be treated as a necessary evil at best, and disappeared if they become troublesome.', 'Secret police chief, Scapegoating official, Treacherous native informer', 'Rebel leader, Offworld agitator, Imprisoned victim, Crime boss', 'The natives largely believe in the righteousness of the state, The police state is automated and its rulers can''t shut it off, The leaders foment a pogrom against offworlder spies.', 'List of police informers, Wealth taken from enemies of the state, Dear Leader''s private stash', 'Military parade, Gulag, Gray concrete housing block, Surveillance center', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (406, 'Preceptor Archive', 'The Preceptors of the Great Archive were a pre-Silence organization devoted to ensuring the dissemination of human culture, history, and basic technology to frontier worlds that risked losing this information during the human expansion. Most frontier planets had an Archive where natives could learn useful technical skills in addition to human history and art. Those Archives that managed to survive the Silence now strive to send their missionaries of knowledge to new worlds in need of their lore.', 'Luddite native, Offworld Merchant who wants the natives kept ignorant, Religious zealot, Corrupted First Speaker who wants to keep a monopoly on learning', 'Preceptor Adept missionary, Offworld scholar, Reluctant student, Roving Preceptor Adept', 'The local Archive has taken a very religious and mystical attitude toward their teaching, The Archive has maintained some replicable pretech science, The Archive has been corrupted and their teaching is incorrect', 'Lost Archive database, Ancient pretech teaching equipment, Hidden cache of theologically unacceptable tech', 'Archive lecture hall, Experimental laboratory, Student-local riot', 'Precept. Arch.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (407, 'Pretech Cultists', 'The capacities of human science before the Silence vastly outmatch the technology available since the Scream. The jump gates alone were capable of crossing hundreds of light years in a moment, and they were just one example of the results won by blending psychic artifice with pretech science. Some worlds outright worship the artifacts of their ancestors, seeing in them the work of more enlightened and perfect humanity. These cultists may or may not understand the operation or replication of these devices, but they seek and guard them jealously.', 'Cult leader, Artifact supplier, Pretech smuggler', 'Offworld scientist, Robbed collector, Cult heretic', 'The cultists can actually replicate certain forms of pretech, The cultists abhor use of the devices as presumption on the holy, The cultists mistake the party''s belongings for pretech', 'Pretech artifacts both functional and broken, Religious-jargon laced pretech replication techniques, Waylaid payment for pretech artifacts', 'Shrine to nonfunctional pretech, Smuggler''s den, Public procession showing a prized artifact', 'Pretech Cult.')");
			state.execute("INSERT INTO WORLD_TAG VALUES (408, 'Primitive Aliens', 'The world is populated by a large number of sapient aliens that have yet to develop advanced technology. The human colonists may have a friendly or hostile relationship with the aliens, but a certain intrinsic tension is likely. Small human colonies might have been enslaved or otherwise subjugated.', 'Hostile alien chief, Human firebrand, Dangerous local predator, Alien religious zealot', 'Colonist leader, Peace-faction alien chief, Planetary frontiersman, Xenoresearcher', 'The alien numbers are huge and can overwhelm the humans whenever they so choose, One group is trying to use the other to kill their political opponents, The aliens are incomprehensibly strange, One side commits an atrocity', 'Alien religious icon, Ancient alien-human treaty, Alien technology', 'Alien village, Fortified human settlement, Massacre site', 'Prim. Aliens')");
			state.execute("INSERT INTO WORLD_TAG VALUES (409, 'Psionics Fear', 'The locals are terrified of psychics. Perhaps their history is studded with feral psychics who went on murderous rampages, or perhaps they simply nurse an unreasoning terror of those “mutant freaks”. Psychics demonstrate their powers at risk of their lives.', 'Mental purity investigator, Suspicious zealot, Witch-finder', 'Hidden psychic, Offworlder psychic trapped here, Offworld educator', 'Psychic potential is much more common here, Some tech is mistaken as psitech, Natives believe certain rituals and customs can protect them from psychic powers', 'Hidden psitech cache, Possessions of convicted psychics, Reward for turning in a psychic', 'Inquisitorial chamber, Lynching site, Museum of psychic atrocities', 'Psi. Fear')");
			state.execute("INSERT INTO WORLD_TAG VALUES (410, 'Psionics Worship', 'These natives view psionic powers as a visible gift of god or sign of superiority. If the world has a functional psychic training academy, psychics occupy almost all major positions of power and are considered the natural and proper rulers of the world. If the world lacks training facilities, it is likely a hodgepodge of demented cults, with each one dedicated to a marginally-coherent feral prophet and their psychopathic ravings.', 'Psychic inquisitor, Haughty mind-noble, Psychic slaver, Feral prophet', 'Offworlder psychic researcher, Native rebel, Offworld employer seeking psychics', 'The psychic training is imperfect, and the psychics all show significant mental illness, The psychics have developed a unique discipline, The will of a psychic is law, Psychics in the party are forcibly kidnapped for enlightening.', 'Ancient psitech, Valuable psychic research records, Permission for psychic training', 'Psitech-imbued council chamber, Temple to the mind, Sanitarium-prison for feral psychics', 'Psi. Worship')");
			state.execute("INSERT INTO WORLD_TAG VALUES (501, 'Psionics Academy', 'This world is one of the few that have managed to redevelop the basics of psychic training. Without this education, a potential psychic is doomed to either madness or death unless they refrain from using their abilities. Psionic academies are rare enough that offworlders are often sent there to study by wealthy patrons. The secrets of psychic mentorship, the protocols and techniques that allow a psychic to successfully train another, are carefully guarded at these academies. Most are closely affiliated with the planetary government.', 'Corrupt psychic instructor, Renegade student, Mad psychic researcher, Resentful townie', 'Offworld researcher, Aspiring student, Wealthy tourist', 'The academy curriculum kills a significant percentage of students, The faculty use students as research subjects, The students are indoctrinated as sleeper agents, The local natives hate the academy, The academy is part of a religion.', 'Secretly developed psitech, A runaway psychic mentor, Psychic research prize', 'Training grounds, Experimental laboratory, School library, Campus hangout', 'Psi. School')");
			state.execute("INSERT INTO WORLD_TAG VALUES (502, 'Quarantined World', 'The world is under a quarantine, and space travel to and from it is strictly forbidden. This may be enforced by massive ground batteries that burn any interlopers from the planet’s sky, or it may be that a neighboring world runs a persistent blockade.', 'Defense installation commander, Suspicious patrol leader, Crazed asteroid hermit', 'Relative of a person trapped on the world, Humanitarian relief official, Treasure hunter', 'The natives want to remain isolated, The quarantine is enforced by an ancient alien installation, The world is rife with maltech abominations, The blockade is meant to starve everyone on the barren world.', 'Defense grid key, Bribe for getting someone out, Abandoned alien tech', 'Bridge of a blockading ship, Defense installation control room, Refugee camp', 'Quarantined')");
			state.execute("INSERT INTO WORLD_TAG VALUES (503, 'Radioactive World', 'Whether due to a legacy of atomic warfare unhindered by nuke snuffers or a simple profusion of radioactive elements, this world glows in the dark. Even heavy vacc suits can filter only so much of the radiation, and most natives suffer a wide variety of cancers, mutations and other illnesses without the protection of advanced medical treatments.', 'Bitter mutant, Relic warlord, Desperate would-be escapee', 'Reckless prospector, Offworld scavenger, Biogenetic variety seeker', 'The radioactivity is steadily growing worse, The planet''s medical resources break down, The radioactivity has inexplicable effects on living creatures, The radioactivity is the product of a malfunctioning pretech manufactory.', 'Ancient atomic weaponry, Pretech anti-radioactivity drugs, Untainted water supply', 'Mutant-infested ruins, Scorched glass plain, Wilderness of bizarre native life, Glowing barrens', 'Radioactive')");
			state.execute("INSERT INTO WORLD_TAG VALUES (504, 'Regional Hegemon', 'This world has the technological sophistication, natural resources, and determined polity necessary to be a regional hegemon for the sector. Nearby worlds are likely either directly subservient to it or tack carefully to avoid its anger. It may even be the capital of a small stellar empire.', 'Ambitious general, Colonial official, Contemptuous noble', 'Diplomat, Offworld ambassador, Foreign spy', 'The hegemon''s influence is all that''s keeping a murderous war from breaking out on nearby worlds, The hegemon is decaying and losing its control, The government is riddled with spies, The hegemon is genuinely benign', 'Diplomatic carte blanche, Deed to an offworld estate, Foreign aid grant', 'Palace or seat of government, Salon teeming with spies, Protest rally, Military base', 'Reg. Hegemon')");
			state.execute("INSERT INTO WORLD_TAG VALUES (505, 'Restrictive Laws', 'A myriad of laws, customs, and rules constrain the inhabitants of this world, and even acts that are completely permissible elsewhere are punished severely here. The locals may provide lists of these laws to offworlders, but few non-natives can hope to master all the important intricacies.', 'Law enforcement officer, Outraged native, Native lawyer specializing in peeling offworlders, Paid snitch', 'Frustrated offworlder, Repressed native, Reforming crusader', 'The laws change regularly in patterns only natives understand, The laws forbid some action vital to the party, The laws forbid the simple existence of some party members, The laws are secret to offworlders', 'Complete legal codex, Writ of diplomatic immunity, Fine collection vault contents', 'Courtroom, Mob scene of outraged locals, Legislative chamber, Police station', 'Restrictive')");
			state.execute("INSERT INTO WORLD_TAG VALUES (506, 'Rigid Culture', 'The local culture is extremely rigid. Certain forms of behavior and belief are absolutely mandated, and any deviation from these principles is punished, or else society may be strongly stratified by birth with limited prospects for change. Anything which threatens the existing social order is feared and shunned.', 'Rigid reactionary, Wary ruler, Regime ideologue, Offended potentate', 'Revolutionary agitator, Ambitious peasant, Frustrated merchant', 'The cultural patterns are enforced by technological aids, The culture is run by a secret cabal of manipulators, The culture has explicit religious sanction, The culture evolved due to important necessities that have since been forgotten', 'Precious traditional regalia, Peasant tribute, Opulent treasures of the ruling class', 'Time-worn palace, Low-caste slums, Bandit den, Reformist temple', 'Rigid')");
			state.execute("INSERT INTO WORLD_TAG VALUES (507, 'Seagoing Cities', 'Either the world is entirely water or else the land is simply too dangerous for most humans. Human settlement on this world consists of a number of floating cities that follow the currents and the fish.', 'Pirate city lord, Mer-human raider chieftain, Hostile landsman noble, Enemy city saboteur', 'City navigator, Scout captain, Curious mer-human', 'The seas are not water, The fish schools have vanished and the city faces starvation, Terrible storms drive the city into the glacial regions, Suicide ships ram the city’s hull', 'Giant pearls with mysterious chemical properties, Buried treasure, Vital repair materials', 'Bridge of the city, Storm-tossed sea, A bridge fashioned of many small boats.', 'Seagoing')");
			state.execute("INSERT INTO WORLD_TAG VALUES (508, 'Sealed Menace', 'Something on this planet has the potential to create enormous havoc for the inhabitants if it is not kept safely contained by its keepers. Whether a massive seismic fault line suppressed by pretech terraforming technology, a disease that has to be quarantined within hours of discovery, or an ancient alien relic that requires regular upkeep in order to prevent planetary catastrophe, the menace is a constant shadow on the populace.', 'Hostile outsider bent on freeing the menace, Misguided fool who thinks he can use it, Reckless researcher who thinks he can fix it', 'Keeper of the menace, Student of its nature, Victim of the menace', 'The menace would bring great wealth along with destruction, The menace is intelligent, The natives don''t all believe in the menace', 'A key to unlock the menace, A precious byproduct of the menace, The secret of the menace’s true nature', 'Guarded fortress containing the menace, Monitoring station, Scene of a prior outbreak of the menace', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (509, 'Sectarians', 'The world is torn by violent disagreement between sectarians of a particular faith. Each views the other as a damnable heresy in need of extirpation. Local government may be able to keep open war from breaking out, but the poisonous hatred divides communities. The nature of the faith may be religious, or it may be based on some secular ideology.', 'Paranoid believer, Native convinced the party is working for the other side, Absolutist ruler', 'Reformist clergy, Local peacekeeping official, Offworld missionary, Exhausted ruler', 'The conflict has more than two sides, The sectarians hate each other for multiple reasons, The sectarians must cooperate or else life on this world is imperiled, The sectarians hate outsiders more than they hate each other, The differences in sects are incomprehensible to an outsider', 'Ancient holy book, Incontrovertible proof, Offering to a local holy man', 'Sectarian battlefield, Crusading temple, Philosopher’s salon, Bitterly divided village', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (510, 'Seismic Instability', 'The local land masses are remarkably unstable, and regular earthquakes rack the surface. Local construction is either advanced enough to sway and move with the vibrations or primitive enough that it is easily rebuilt. Severe volcanic activity may be part of the instability.', 'Earthquake cultist, Hermit seismologist, Burrowing native life form, Earthquake-inducing saboteur', 'Experimental construction firm owner, Adventurous volcanologist, Geothermal prospector', 'The earthquakes are caused by malfunctioning pretech terraformers, They''re caused by alien technology, They''re restrained by alien technology that is being plundered by offworlders, The earthquakes are used to generate enormous amounts of energy.', 'Earthquake generator, Earthquake suppressor, Mineral formed at the core of the world, Earthquake-proof building schematics', 'Volcanic caldera, Village during an earthquake, Mud slide, Earthquake opening superheated steam fissures', 'Seismic')");
			state.execute("INSERT INTO WORLD_TAG VALUES (601, 'Secret Masters', 'The world is actually run by a hidden cabal, acting through their catspaws in the visible government. For one reason or another, this group finds it imperative that they not be identified by outsiders, and in some cases even the planet’s own government may not realize that they’re actually being manipulated by hidden masters.', 'An agent of the cabal, Government official who wants no questions asked, Willfully blinded local', 'Paranoid conspiracy theorist, Machiavellian gamesman within the cabal, Interstellar investigator', 'The secret masters have a benign reason for wanting secrecy, The cabal fights openly amongst itself, The cabal is recruiting new members', 'A dossier of secrets on a government official, A briefcase of unmarked credit notes, The identity of a cabal member', 'Smoke-filled room, Shadowy alleyway, Secret underground bunker', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (602, 'Theocracy', 'The planet is ruled by the priesthood of the predominant religion or ideology. The rest of the locals may or may not be terribly pious, but the clergy have the necessary military strength, popular support or control of resources to maintain their rule. Alternative faiths or incompatible ideologies are likely to be both illegal and socially unacceptable.', 'Decadent priest-ruler, Zealous inquisitor, Relentless proselytizer, True Believer', 'Heretic, Offworld theologian, Atheistic merchant, Desperate commoner', 'The theocracy actually works well, The theocracy is decadent and hated by the common folk, The theocracy is divided into mutually hostile sects, The theocracy is led by aliens', 'Precious holy text, Martyr''s bones, Secret church records, Ancient church treasures', 'Glorious temple, Austere monastery, Academy for ideological indoctrination, Decadent pleasure-cathedral', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (603, 'Tomb World', 'Tomb worlds are planets that were once inhabited by humans before the Silence. The sudden collapse of the jump gate network and the inability to bring in the massive food supplies required by the planet resulted in starvation, warfare, and death. Most tomb worlds are naturally hostile to human habitation and could not raise sufficient crops to maintain life. The few hydroponic facilities were usually destroyed in the fighting, and all that is left now are ruins, bones, and silence.', 'Demented survivor tribe chieftain, Avaricious scavenger, Automated defense system, Native predator', 'Scavenger Fleet captain, Archaeologist, Salvaging historian', 'The ruins are full of booby-traps left by the final inhabitants, The world''s atmosphere quickly degrades anything in an opened building, A handful of desperate natives survived the Silence, The structures are unstable and collapsing', 'Lost pretech equipment, Psitech caches, Stores of unused munitions, Ancient historical documents', 'Crumbling hive-city, City square carpeted in bones, Ruined hydroponic facility, Cannibal tribe''s lair, Dead orbital jump gate', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (604, 'Trade Hub', 'This world is a major crossroads for local interstellar trade. It is well-positioned at the nexus of several short-drill trade routes, and has facilities for easy transfer of valuable cargoes and the fueling and repairing of starships. The natives are accustomed to outsiders, and a polyglot mass of people from every nearby world can be found trading here.', 'Cheating merchant, Thieving dockworker, Commercial spy, Corrupt customs official', 'Rich tourist, Hardscrabble free trader, Merchant prince in need of catspaws, Friendly spaceport urchin', 'An outworlder faction schemes to seize the trade hub, Saboteurs seek to blow up a rival''s warehouses, Enemies are blockading the trade routes, Pirates lace the hub with spies', 'Voucher for a warehouse''s contents, Insider trading information, Case of precious offworld pharmaceuticals, Box of legitimate tax stamps indicating customs dues have been paid.', 'Raucous bazaar, Elegant restaurant, Spaceport teeming with activity, Foggy street lined with warehouses', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (605, 'Tyranny', 'The local government is brutal and indifferent to the will of the people. Laws may or may not exist, but the only one that matters is the whim of the rulers on any given day. Their minions swagger through the streets while the common folk live in terror of their appetites. The only people who stay wealthy are friends and servants of the ruling class.', 'Debauched autocrat, Sneering bully-boy, Soulless government official, Occupying army officer', 'Conspiring rebel, Oppressed merchant, Desperate peasant, Inspiring religious leader', 'The tyrant rules with vastly superior technology, The tyrant is a figurehead for a cabal of powerful men and women, The people are resigned to their suffering, The tyrant is hostile to meddlesome outworlders.', 'Plundered wealth, Beautiful toys of the elite, Regalia of rulership', 'Impoverished village, Protest rally massacre, Decadent palace, Religious hospital for the indigent', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (606, 'Unbraked AI', 'Artificial intelligences are costly and difficult to create, requiring a careful sequence of growth stages in order to bring them to sentience before artificial limits on cognition speed and learning development are installed. These brakes prevent runaway cognition metastasis, wherein an AI begins to rapidly contemplate certain subjects in increasingly baroque fashion, until they become completely crazed by rational human standards. This world has one such unbraked AI on it, probably with a witting or unwitting corps of servants. Unbraked AIs are quite insane, but they learn and reason with a speed impossible for humans, and can demonstrate a truly distressing subtlety at times.', 'AI Cultist, Maltech researcher, Government official dependent on the AI', 'Perimeter agent, AI researcher, Braked AI', 'The AI''s presence is unknown to the locals, The locals depend on the AI for some vital service, The AI appears to be harmless, The AI has fixated on the group''s ship''s computer, The AI wants transport offworld', 'The room-sized AI core itself, Maltech research files, Perfectly tabulated blackmail on government officials, Pretech computer circuitry', 'Municipal computing banks, Cult compound, Repair center, Ancient hardcopy library', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (607, 'Warlords', 'The world is plagued by warlords. Numerous powerful men and women control private armies sufficiently strong to cow whatever local government may exist. On the lands they claim, their word is law. Most spend their time oppressing their own subjects and murderously pillaging those of their neighbors. Most like to wrap themselves in the mantle of ideology, religious fervor, or an ostensibly legitimate right to rule.', 'Warlord, Avaricious lieutenant, Expensive assassin, Aspiring minion', 'Vengeful commoner, Government military officer, Humanitarian aid official, Village priest', 'The warlords are willing to cooperate to fight mutual threats, The warlords favor specific religions or races over others, The warlords are using substantially more sophisticated tech than others, Some of the warlords are better rulers than the government', 'Weapons cache, Buried plunder, A warlord''s personal battle harness, Captured merchant shipping', 'Gory battlefield, Burnt-out village, Barbaric warlord palace, Squalid refugee camp', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (608, 'Xenophiles', 'The natives of this world are fast friends with a particular alien race. The aliens may have saved the planet at some point in the past, or awed the locals with superior tech or impressive cultural qualities. The aliens might even be the ruling class on the planet.', 'Offworld xenophobe, Suspicious alien leader, Xenocultural imperialist', 'Benevolent alien, Native malcontent, Gone-native offworlder', 'The enthusiasm is due to alien psionics or tech, The enthusiasm is based on a lie, The aliens strongly dislike their groupies, The aliens feel obliged to rule humanity for its own good, Humans badly misunderstand the aliens', 'Hybrid alien-human tech, Exotic alien crafts, Sophisticated xenolinguistic and xenocultural research data', 'Alien district, Alien-influenced human home, Cultural festival celebrating alien artist', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (609, 'Xenophobes', 'The natives are intensely averse to dealings with outworlders. Whether through cultural revulsion, fear of tech contamination, or a genuine immunodeficiency, the locals shun foreigners from offworld and refuse to have anything to do with them beyond the bare necessities of contact. Trade may or may not exist on this world, but if it does, it is almost certainly conducted by a caste of untouchables and outcasts.', 'Revulsed local ruler, Native convinced some wrong was done to him, Cynical demagogue', 'Curious native, Exiled former ruler, Local desperately seeking outworlder help', 'The natives are symptomless carriers of a contagious and dangerous disease, The natives are exceptionally vulnerable to offworld diseases, The natives require elaborate purification rituals after speaking to an offworlder or touching them, The local ruler has forbidden any mercantile dealings with outworlders', 'Jealously-guarded precious relic, Local product under export ban, Esoteric local technology', 'Sealed treaty port, Public ritual not open to outsiders, Outcaste slum home', NULL)");
			state.execute("INSERT INTO WORLD_TAG VALUES (610, 'Zombies', 'This menace may not take the form of shambling corpses, but some disease, alien artifact, or crazed local practice produces men and women with habits similar to those of murderous cannibal undead. These outbreaks may be regular elements in local society, either provoked by some malevolent creators or the consequence of some local condition.', 'Soulless maltech biotechnology cult, Sinister governmental agent, Crazed zombie cultist', 'Survivor of an outbreak, Doctor searching for a cure, Rebel against the secret malefactors', 'The zombies retain human intelligence, The zombies can be cured, The process is voluntary among devotees, The condition is infectious', 'Cure for the condition, Alien artifact that causes it, Details of the cult''s conversion process', 'House with boarded-up windows, Dead city, Fortified bunker that was overrun from within', NULL)");
			
			
			
			state.close();
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
//		System.out.println("Table WORLD_TAG exploded... (this is good)");
	}
}
