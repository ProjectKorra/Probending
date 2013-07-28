package com.etriacraft.probending;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import tools.BendingType;
import tools.Tools;

public class Methods {

	Probending plugin;

	public Methods(Probending plugin) {
		this.plugin = plugin;
	}

	public static Set<String> teams = new HashSet<String>();
	public static HashMap<String, String> players = new HashMap<String, String>();
	public static String storage = Probending.plugin.getConfig().getString("General.Storage");

	// Gives the player Leather Armor (With Color)
	public static ItemStack createColorArmor(ItemStack i, Color c)
	{
		LeatherArmorMeta meta = (LeatherArmorMeta)i.getItemMeta();
		meta.setColor(c);
		i.setItemMeta(meta);
		return i;
	}

	// Populates list of colors.
	public static void populateColors() {
		SignListener.colors.add("Cyan");
		SignListener.colors.add("Black");
		SignListener.colors.add("Blue");
		SignListener.colors.add("Magenta");
		SignListener.colors.add("Gray");
		SignListener.colors.add("Green");
		SignListener.colors.add("LightGreen");
		SignListener.colors.add("DarkRed");
		SignListener.colors.add("DarkBlue");
		SignListener.colors.add("Olive");
		SignListener.colors.add("Orange");
		SignListener.colors.add("Purple");
		SignListener.colors.add("Red");
		SignListener.colors.add("Gray");
		SignListener.colors.add("Teal"); 
		SignListener.colors.add("White");
		SignListener.colors.add("Yellow");

	}

	// Gets a color from a string.
	public static Color getColorFromString (String pretendColor) {
		if (pretendColor.equalsIgnoreCase("Cyan")) {
			return Color.AQUA;
		}
		if (pretendColor.equalsIgnoreCase("Black")) {
			return Color.BLACK;
		}
		if (pretendColor.equalsIgnoreCase("Blue")) {
			return Color.BLUE;
		}
		if (pretendColor.equalsIgnoreCase("Magenta")) {
			return Color.FUCHSIA;
		}
		if (pretendColor.equalsIgnoreCase("Gray")) {
			return Color.GRAY;
		}
		if (pretendColor.equalsIgnoreCase("Green")) {
			return Color.GREEN;
		}
		if (pretendColor.equalsIgnoreCase("LightGreen")) {
			return Color.LIME;
		}
		if (pretendColor.equalsIgnoreCase("DarkRed")) {
			return Color.MAROON;
		}
		if (pretendColor.equalsIgnoreCase("Navy")) {
			return Color.NAVY;
		}
		if (pretendColor.equalsIgnoreCase("Olive")) {
			return Color.OLIVE;
		}
		if (pretendColor.equalsIgnoreCase("Orange")) {
			return Color.ORANGE;
		}
		if (pretendColor.equalsIgnoreCase("Purple")) {
			return Color.PURPLE;
		}
		if (pretendColor.equalsIgnoreCase("Red")) {
			return Color.RED;
		}
		if (pretendColor.equalsIgnoreCase("Silver")) {
			return Color.SILVER;
		}
		if (pretendColor.equalsIgnoreCase("Teal")) {
			return Color.TEAL;
		}
		if (pretendColor.equalsIgnoreCase("White")) {
			return Color.WHITE;
		}
		if (pretendColor.equalsIgnoreCase("Yellow")) {
			return Color.YELLOW;
		}
		return null;
	}

	// Checks if the team exists, returns true if the team does exist, returns false if not.
	public static boolean teamExists(String teamName) {
		for (String team: teams) {
			if (team.equalsIgnoreCase(teamName)) {
				return true;
			}
		}
		return false;
	}

	// Loads teams on startup
	public static void loadTeams() {
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT team FROM probending_teams");
			try {
				while (rs2.next()) {
					teams.add(rs2.getString("team"));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			teams = Probending.plugin.getConfig().getConfigurationSection("TeamInfo").getKeys(false);
		}
	}
	
	// Load Players on Starup
	public static void loadPlayers() {
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_players");
			try {
				while (rs2.next()) {
					players.put(rs2.getString("player"), rs2.getString("team"));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Set<String> tmpPlayers = new HashSet<String>();
			tmpPlayers.addAll(Probending.plugin.getConfig().getConfigurationSection("players").getKeys(false));
			for (String player: tmpPlayers) {
				if (Probending.plugin.getConfig().getString("players." + player) != null) {
					String teamName = Probending.plugin.getConfig().getString("players." + player);
					players.put(player, teamName);
				}
			}
		}
	}
	// Creates a team.
	public static void createTeam(String teamName, String owner) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("INSERT INTO probending_teams (team, owner) VALUES ('" + teamName + "', '" + owner + "')");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Owner", owner);
			Probending.plugin.saveConfig();
		}
		teams.add(teamName);
	}
	// Deletes a team.
	public static void deleteTeam(String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("DELETE FROM probending_teams WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName, null);
			Probending.plugin.saveConfig();
		}
		teams.remove(teamName);
	}
	// Adds a player to a team.
	public static void addPlayerToTeam(String teamName, String player, String element) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = '" + player + "' WHERE team = '" + teamName + "'");
			DBConnection.sql.modifyQuery("INSERT INTO probending_players (player, team) VALUES ('" + player + "', '" + teamName + "')");
			players.put(player, teamName);
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, player);
			Probending.plugin.getConfig().set("players." + player, teamName);
			Probending.plugin.saveConfig();
			players.put(player, teamName);
		}
	}

	// Removes a player from a team.
	public static void removePlayerFromTeam(String teamName, String player, String element) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("DELETE FROM probending_players WHERE player = '" + player + "'");
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = NULL WHERE team = '" + teamName + "'");
			players.remove(player);
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, null);
			Probending.plugin.getConfig().set("players." + player, null);
			Probending.plugin.saveConfig();
			players.remove(player);
		}
	}

	// Checks if the player is in a team. Returns true if the player is in a team.
	public static boolean playerInTeam(String playerName) {
		if (players.containsKey(playerName)) {
			return true;
		}
		return false;
//		if (storage.equalsIgnoreCase("mysql")) {
//			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_players WHERE player = '" + playerName + "'");
//			try {
//				if (rs2.next()) {
//					return true;
//				} else {
//					return false;
//				}
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//			}
//		}
//		if (storage.equalsIgnoreCase("flatfile")) {
//			if (Probending.plugin.getConfig().get("players." + playerName) == (null)) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		return false;
	}

	// Checks the player's element in the team. (Regardless of if they've changed)
	public static String getPlayerElementInTeam(String playerName, String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				rs2.next();
				if (rs2.getString("Air") != null && rs2.getString("Air").equals(playerName)) {
					return "Air";
				}
				if (rs2.getString("Water") != null && rs2.getString("Water").equals(playerName)) {
					return "Water";
				}
				if (rs2.getString("Earth") != null && rs2.getString("Earth").equals(playerName)) {
					return "Earth";
				}
				if (rs2.getString("Fire") != null && rs2.getString("Fire").equals(playerName)) {
					return "Fire";
				}
				if (rs2.getString("Chi") != null && rs2.getString("Chi").equals(playerName)) {
					return "Chi";
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Air") != null && Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Air").equals(playerName)) {
				return "Air";
			}
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Water") != null && Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Water").equals(playerName)) {
				return "Water";
			}
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Earth") != null && Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Earth").equals(playerName)) {
				return "Earth";
			}
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Fire") != null && Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Fire").equals(playerName)) {
				return "Fire";
			}
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Chi") != null && Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Chi").equals(playerName)) {
				return "Chi";
			}
		}
		return null;
	}

	// Returns the name of the player's team.
	public static String getPlayerTeam(String player) {
		return players.get(player);
//		if (storage.equalsIgnoreCase("mysql")) {
//			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_players WHERE player = '" + player + "'");
//			try {
//				if (rs2.next()) {
//					return rs2.getString("team");
//				}
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//			}
//		}
//		if (storage.equalsIgnoreCase("flatfile")) {
//			return Probending.plugin.getConfig().getString("players." + player);
//		}
//		return null;
	}

	// Returns the amount of players in a team.
	public static int getTeamSize(String teamName) {
		int size = 0;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				rs2.next();
				if (rs2.getString("Air") != null) {
					size++;
				}
				if (rs2.getString("Water") != null) {
					size++;
				}
				if (rs2.getString("Earth") != null) {
					size++;
				}
				if (rs2.getString("Fire") != null) {
					size++;
				}
				if (rs2.getString("Chi") != null) {
					size++;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			if (Probending.plugin.getConfig().get("TeamInfo." + teamName + ".Air") != null) {
				size++;
			}
			if (Probending.plugin.getConfig().get("TeamInfo." + teamName + ".Water") != null) {
				size++;
			}
			if (Probending.plugin.getConfig().get("TeamInfo." + teamName + ".Earth") != null) {
				size++;
			}
			if (Probending.plugin.getConfig().get("TeamInfo." + teamName + ".Fire") != null) {
				size++;
			}
			if (Probending.plugin.getConfig().get("TeamInfo." + teamName + ".Chi") != null) {
				size++;
			}
		}
		return size;
	}

	// Gets the owner of a team.
	public static String getOwner(String teamName) {
		String owner = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT owner FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				rs2.next();
				owner = rs2.getString("owner");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			owner = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Owner");
		}
		return owner;
	}
	// Sets the owner of a team.
	public static void setOwner(String player, String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET owner = '" + player + "' WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Owner", player);
			Probending.plugin.saveConfig();
		}
	}
	// Returns true if the player is the owner of the team.
	public static boolean isPlayerOwner(String player, String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT owner FROM probending_teams WHERE team = '" + teamName + "' AND owner = '" + player + "'");
			try {
				if (rs2.next()) {
					return true;
				} else {
					return false;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Owner").equals(player)) {
				return true;
			}
		}
		return false;
	}

	// Returns a set (List) of all of the teams elements.
	public static Set<String> getTeamElements(String teamName) {
		Set<String> teamelements = new HashSet<String>();

		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				rs2.next();
				if (rs2.getString("Air") != null) {
					teamelements.add("Air");
				}
				if (rs2.getString("Water") != null) {
					teamelements.add("Water");
				}
				if (rs2.getString("Earth") != null) {
					teamelements.add("Earth");
				}
				if (rs2.getString("Fire") != null) {
					teamelements.add("Fire");
				}
				if (rs2.getString("Chi") != null) {
					teamelements.add("Chi");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			teamelements.addAll(Probending.plugin.getConfig().getConfigurationSection("TeamInfo." + teamName).getKeys(true));
			return teamelements;
		}
		return teamelements;
	}

	// Returns the name of the team's airbender.
	public static String getTeamAirbender(String teamName) {
		String airbender = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT Air FROM probending_teams WHERE team = '"+ teamName + "'");
			try {
				if (rs2.next()) {
					airbender = rs2.getString("Air");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			airbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Air");
		}
		return airbender;
	}

	// Returns the name of the team's waterbender.
	public static String getTeamWaterbender(String teamName) {
		String waterbender = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT Water FROM probending_teams WHERE team = '"+ teamName + "'");
			try {
				if (rs2.next()) {
					waterbender = rs2.getString("Water");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			waterbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Water");
		}
		return waterbender;
	}

	// Returns the name of the team's earthbender.
	public static String getTeamEarthbender(String teamName) {
		String earthbender = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT Earth FROM probending_teams WHERE team = '"+ teamName + "'");
			try {
				if (rs2.next()) {
					earthbender = rs2.getString("Earth");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			earthbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Earth");
		}
		return earthbender;
	}

	// Returns the name of the team's firebender.
	public static String getTeamFirebender(String teamName) {
		String fire = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT Fire FROM probending_teams WHERE team = '"+ teamName + "'");
			try {
				if (rs2.next()) {
					fire = rs2.getString("Fire");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			fire = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Fire");
		}
		return fire;
	}

	// Returns the name of the team's chiblocker.
	public static String getTeamChiblocker(String teamName) {
		String chi = null;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT Chi FROM probending_teams WHERE team = '"+ teamName + "'");
			try {
				if (rs2.next()) {
					chi = rs2.getString("Chi");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			chi = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Chi");
		}
		return chi;
	}

	// Checks if Airbenders are allowed to be in Probending teams.
	public static boolean getAirAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowAir");
	}

	// Checks if Waterbenders are allowed to be in Probending teams.
	public static boolean getWaterAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowWater");
	}

	// Checks if Earthbenders are allowed to be in Probending teams.
	public static boolean getEarthAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowEarth");
	}

	// Checks if Firebenders are allowed to be in Probending teams.
	public static boolean getFireAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowFire");
	}

	// Checks if Chiblockers are allowed to be in Probending teams.
	public static boolean getChiAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowChi");
	}

	//Returns the player's element as a string.
	public static String getPlayerElementAsString(String player) {
		if (!Tools.isBender(player)) {
			return null;
		}
		if (Tools.isBender(player, BendingType.Air)) {
			return "Air";
		}
		if (Tools.isBender(player, BendingType.Water)) {
			return "Water";
		}
		if (Tools.isBender(player, BendingType.Earth)) {
			return "Earth";
		}
		if (Tools.isBender(player, BendingType.Fire)){
			return "Fire";
		}
		if (Tools.isBender(player, BendingType.ChiBlocker)) {
			return "Chi";
		}
		return null;
	}

	// Adds color to messages.
	public static String colorize(String message) {
		return message.replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1");
	}

	// Returns true if an arena exists.
	public static boolean arenaExists(String arenaName) {
		if (Probending.plugin.getConfig().getConfigurationSection("ArenaInfo") == null) {
			return false;
		}
		Set<String> arenas = Probending.plugin.getConfig().getConfigurationSection("ArenaInfo").getKeys(false);
		if (arenas.contains(arenaName)) {
			return true;
		} else {
			return false;
		}
	}

	// Creates an arena.
	public static void createArena(String arenaName) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".created", true);
		Probending.plugin.saveConfig();
	}

	// Delete an arena.
	public static void deleteArena(String arenaName) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName, null);
		Probending.plugin.saveConfig();
	}

	// Set's the spectator spawn of an arena.
	public static void setSpectatorSpawn(String arenaName, Double x, Double y, Double z, String world) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.world", world);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.x", x);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.y", y);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.z", z);
		Probending.plugin.saveConfig();
	}

	// Sets the field spawn of an arena.
	public static void setFieldSpawn(String arenaName, Double x, Double y, Double z, String world) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.world", world);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.x", x);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.y", y);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.z", z);
		Probending.plugin.saveConfig();
	}

	// Returns the Spectator Spawn of an arena.
	public static Location getSpectatorSpawn(String arenaName) {
		Double x = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.z");
		World world = Bukkit.getWorld(Probending.plugin.getConfig().getString("ArenaInfo." + arenaName + ".SpectatorSpawn.world"));
		Location location = new Location(world, x, y, z);
		return location;
	}

	// Returns the field spawn of an arena.
	public static Location getFieldSpawn(String arenaName) {
		Double x = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.z");
		World world = Bukkit.getWorld(Probending.plugin.getConfig().getString("ArenaInfo." + arenaName + ".FieldSpawn.world"));
		Location location = new Location(world, x, y, z);
		return location;
	}

	// Returns a Set (List) of Strings.
	public static Set<String> getTeams() {
		return teams;
	}

	// Returns a Set (List) of Arenas.
	public static Set<String> getArenas() {
		Set<String> arenas = Probending.plugin.getConfig().getConfigurationSection("ArenaInfo").getKeys(false);
		return arenas;
	}

	public static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Probending.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		Probending.econ = economyProvider.getProvider();
		return (Probending.econ != null);
	}
	
	public static void setWins(int wins, String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + wins + " WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Wins", wins);
			Probending.plugin.saveConfig();
		}
	}
	
	public static void setLosses(int losses, String teamName) {
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + losses + " WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Losses", losses);
			Probending.plugin.saveConfig();
		}
	}
	
	public static int getWins(String teamName) {
		int wins = 0;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT wins FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				if (rs2.next()) {
					wins = rs2.getInt("wins");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			wins = Probending.plugin.getConfig().getInt("TeamInfo." + teamName + ".Wins");
		}
		return wins;
	}
	
	public static int getLosses(String teamName) {
		int losses = 0;
		if (storage.equalsIgnoreCase("mysql")) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT losses FROM probending_teams WHERE team = '" + teamName + "'");
			try {
				if (rs2.next()) {
					losses = rs2.getInt("losses");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			losses = Probending.plugin.getConfig().getInt("TeamInfo." + teamName + ".Losses");
		}
		return losses;
	}
	
	public static void addWin(String teamName) {
		int currentWins = getWins(teamName);
		int newWins = currentWins + 1;
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + newWins + " WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Wins", newWins);
			Probending.plugin.saveConfig();
		}
	}
	
	public static void addLoss(String teamName) {
		int currentLosses = getLosses(teamName);
		int newLosses = currentLosses + 1;
		if (storage.equalsIgnoreCase("mysql")) {
			DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + newLosses + " WHERE team = '" + teamName + "'");
		}
		if (storage.equalsIgnoreCase("flatfile")) {
			Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Losses", newLosses);
			Probending.plugin.saveConfig();
		}
	}
	public static void importTeams() {
		Set<String> yamlTeams = Probending.plugin.getConfig().getConfigurationSection("TeamInfo").getKeys(false);
		for (String team: yamlTeams) {
			String owner = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Owner");
			String airbender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Air");
			String waterbender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Water");
			String earthbender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Earth");
			String firebender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Fire");
			String chiblocker = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Chi");
			Integer wins = Probending.plugin.getConfig().getInt("TeamInfo." + team + ".Wins");
			Integer losses = Probending.plugin.getConfig().getInt("TeamInfo." + team + ".Losses");
			
			Methods.createTeam(team, owner);
			if (airbender != null) {
				Methods.addPlayerToTeam(team, airbender, "Air");
			}
			if (waterbender != null) {
				Methods.addPlayerToTeam(team, waterbender, "Water");
			}
			if (earthbender != null) {
				Methods.addPlayerToTeam(team, earthbender, "Earth");
			}
			if (firebender != null) {
				Methods.addPlayerToTeam(team, firebender, "Fire");
			}
			if (chiblocker != null) {
				Methods.addPlayerToTeam(team, chiblocker, "Chi");
			}
			
			if (wins == null) {
				Methods.setWins(0, team);
			} else {
				Methods.setWins(wins, team);
			}
			
			if (losses == null) {
				Methods.setLosses(0, team);
			} else {
				Methods.setLosses(losses, team);
			}
			
		}
	}

}