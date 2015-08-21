package com.projectkorra.probending;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.projectkorra.probending.storage.DBConnection;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class PBMethods {

	Probending plugin;

	public PBMethods(Probending plugin) {
		this.plugin = plugin;
	}

	// Probending Match Stuff
	public static boolean matchStarted = false;
	public static boolean matchPaused = false;
	public static Set<String> playingTeams = new HashSet<String>();
	public static String TeamOne = null;
	public static String TeamTwo = null;
	public static HashMap<String, String> allowedZone = new HashMap<String, String>();

	public static Set<String> teamOnePlayers = new HashSet<String>();
	public static Set<String> teamTwoPlayers = new HashSet<String>();

	// WorldGuard Stuffs
	public static boolean WGSupportEnabled = Probending.plugin.getConfig().getBoolean("WorldGuard.EnableSupport");
	public static boolean buildDisabled = Probending.plugin.getConfig().getBoolean("WorldGuard.DisableBuildOnField");
	public static String ProbendingField = Probending.plugin.getConfig().getString("WorldGuard.ProbendingField");
	public static boolean AutomateMatches = Probending.plugin.getConfig().getBoolean("WorldGuard.AutomateMatches");
	public static String t1z1 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneOne").toLowerCase();
	public static String t1z2 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneTwo").toLowerCase();
	public static String t1z3 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneThree").toLowerCase();
	public static String t2z1 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneOne").toLowerCase();
	public static String t2z2 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneTwo").toLowerCase();
	public static String t2z3 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneThree").toLowerCase();

	// Sets Spectator Spawn
	public static void setSpectatorSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}
	// Sets Spawn for TeamOne
	public static void setTeamOneSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}
	// Sets Spawn for TeamOne
	public static void setTeamTwoSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}

	// Returns Team One Spawn
	public static Location getTeamOneSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.TeamOneSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}

	// Returns Team Two Spawn
	public static Location getTeamTwoSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.TeamTwoSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}

	// Returns Spectator Spawn
	public static Location getSpectatorSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.SpectatorSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}


	// Ends a Match
	public static void restoreArmor() {
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (Commands.tmpArmor.containsKey(player)) {
				if (player.getInventory().getArmorContents() != null) {
					player.getInventory().setArmorContents(null);
				}
				player.getInventory().setArmorContents(Commands.tmpArmor.get(player));
				Commands.tmpArmor.remove(player);
			}
		}

	}

	public static boolean isZoneEmpty(String teamName, String zone) {
		if (teamName.equalsIgnoreCase(PBMethods.TeamOne)) { 
			for (String player: teamOnePlayers) {
				if (allowedZone.get(player).contains(zone)) {
					return false;
				}
			}
		}
		if (teamName.equalsIgnoreCase(PBMethods.TeamTwo)) {
			for (String player: teamTwoPlayers) {
				if (allowedZone.get(player).contains(zone)) {
					return false;
				}
			}
		}
		return true;
	}
	public static Set<String> colors = new HashSet<String>();
	// Moves players up
	public static void MovePlayersUp(String team, String Side) {
		for (Player player: Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			if (getPlayerTeam(uuid) != null) {
				if (getPlayerTeam(uuid).equalsIgnoreCase(team)) {
					String playerZone = allowedZone.get(player.getName());
					if (playerZone != null) {
						if (Side.equalsIgnoreCase("One")) {
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z1)) {
								allowedZone.put(player.getName(), t2z1); // Moves them up to Team Two Zone One
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z1)) {
								allowedZone.put(player.getName(), t2z2);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z2)) {
								allowedZone.put(player.getName(), t1z1);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z3)) {
								allowedZone.put(player.getName(), t1z2);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
						}
						if (Side.equalsIgnoreCase("Two")) {
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z1)) {
								allowedZone.put(player.getName(), t1z1);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z2)) {
								allowedZone.put(player.getName(), t2z1);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z3)) {
								allowedZone.put(player.getName(), t2z2);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z1)) {
								allowedZone.put(player.getName(), t1z2);
								player.sendMessage(Strings.Prefix + Strings.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
						}

					}
				}
			}
		}
	}
	//Returns a set of players allowed in a zone.
	public static Set<String> playersInZone(String zone) {
		Set<String> playersInZone = new HashSet<String>();
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (allowedZone.containsKey(p.getName())) {
				if (allowedZone.get(p.getName()).equalsIgnoreCase(zone)) {
					playersInZone.add(p.getName());
				}
			}
		}
		return playersInZone;
	}
	// Storage Data
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
		colors.add("Cyan");
		colors.add("Black");
		colors.add("Blue");
		colors.add("Magenta");
		colors.add("Gray");
		colors.add("Green");
		colors.add("LightGreen");
		colors.add("DarkRed");
		colors.add("DarkBlue");
		colors.add("Olive");
		colors.add("Orange");
		colors.add("Purple");
		colors.add("Red");
		colors.add("Gray");
		colors.add("Teal"); 
		colors.add("White");
		colors.add("Yellow");

	}

	// Checks if WorldGuard is enabled.
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Probending.plugin.getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}
	// Gets region player is in
	public static Set<String> RegionsAtLocation(Location loc) {

		ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
		Set<String> regions = new HashSet<String>();
		for (ProtectedRegion region: set) {
			regions.add(region.getId());
		}

		return regions;

	}

	// Sends a message in Probending Chat
	public static void sendPBChat(String message) {
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (Commands.pbChat.contains(player)) {
				player.sendMessage(Strings.Prefix + message);
			}
		}
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
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT team FROM probending_teams");
		try {
			while (rs2.next()) {
				teams.add(rs2.getString("team"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static void createPlayer(UUID uuid) {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_players WHERE uuid = '" + uuid.toString() + "'");
		try {
			if (rs2.next()) {
				/*
				 * Data already exists and should be loaded.
				 */
			} else {
				DBConnection.sql.modifyQuery("INSERT INTO probending_players (uuid) VALUES ('" + uuid.toString() + "')");
				players.put(uuid.toString(), null);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	// Load Players on Startup
	public static void loadPlayers() {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_players");
		try {
			while (rs2.next()) {
				players.put(rs2.getString("uuid"), rs2.getString("team"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	// Creates a team.
	public static void createTeam(String teamName, UUID owner) {
		DBConnection.sql.modifyQuery("INSERT INTO probending_teams (team, owner) VALUES ('" + teamName + "', '" + owner.toString() + "')");
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = '" + teamName + "' WHERE uuid = '" + owner.toString() + "'");
		teams.add(teamName);
	}
	// Deletes a team.
	public static void deleteTeam(String teamName) {
		DBConnection.sql.modifyQuery("DELETE FROM probending_teams WHERE team = '" + teamName + "'");
		teams.remove(teamName);
	}
	// Adds a player to a team.
	public static void addPlayerToTeam(String teamName, UUID player, String element) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = '" + player.toString() + "' WHERE team = '" + teamName + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = '" + teamName + "' WHERE uuid = '" + player.toString() + "'");
		players.put(player.toString(), teamName);
	}

	// Removes a player from a team.
	public static void removePlayerFromTeam(String teamName, UUID player, String element) {
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = NULL WHERE uuid = '" + player.toString() + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = NULL WHERE team = '" + teamName + "'");
		Bukkit.getServer().broadcastMessage(element);
		players.put(player.toString(), null);
	}

	// Checks if the player is in a team. Returns true if the player is in a team.
	public static boolean playerInTeam(UUID player) {
		if (players.get(player.toString()) == null) return false;
		return true;
	}

	// Checks the player's element in the team. (Regardless of if they've changed)
	public static String getPlayerElementInTeam(UUID player, String teamName) {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_teams WHERE team = '" + teamName + "'");
		try {
			rs2.next();
			if (rs2.getString("Air") != null && rs2.getString("Air").equals(player.toString())) {
				return "Air";
			}
			if (rs2.getString("Water") != null && rs2.getString("Water").equals(player.toString())) {
				return "Water";
			}
			if (rs2.getString("Earth") != null && rs2.getString("Earth").equals(player.toString())) {
				return "Earth";
			}
			if (rs2.getString("Fire") != null && rs2.getString("Fire").equals(player.toString())) {
				return "Fire";
			}
			if (rs2.getString("Chi") != null && rs2.getString("Chi").equals(player.toString())) {
				return "Chi";
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Returns the name of the player's team.
	public static String getPlayerTeam(UUID player) {
		return players.get(player.toString());
	}

	// Returns the amount of players in a team.
	public static int getTeamSize(String teamName) {
		int size = 0;
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

		return size;
	}

	// Gets the owner of a team.
	public static String getOwner(String teamName) {
		String owner = null;
		String playername = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT owner FROM probending_teams WHERE team = '" + teamName + "'");
		try {
			rs2.next();
			owner = rs2.getString("owner");
			playername = Bukkit.getOfflinePlayer(UUID.fromString(owner)).getName();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return playername;
	}
	// Sets the owner of a team.
	public static void setOwner(UUID player, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET owner = '" + player.toString() + "' WHERE team = '" + teamName + "'");
	}

	// Returns true if the player is the owner of the team.
	public static boolean isPlayerOwner(UUID player, String teamName) {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT owner FROM probending_teams WHERE team = '" + teamName + "' AND owner = '" + player.toString() + "'");
		try {
			if (rs2.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	// Returns a set (List) of all of the teams elements.
	public static Set<String> getTeamElements(String teamName) {
		Set<String> teamelements = new HashSet<String>();

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
		return teamelements;
	}

	// Returns the name of the team's airbender.
	public static String getTeamAirbender(String teamName) {
		String airbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Air FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Air");
				if (uuid == null) return null;
				airbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return airbender;
	}

	// Returns the name of the team's waterbender.
	public static String getTeamWaterbender(String teamName) {
		String waterbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Water FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Water");
				if (uuid == null) return null;
				waterbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return waterbender;
	}

	// Returns the name of the team's earthbender.
	public static String getTeamEarthbender(String teamName) {
		String earthbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Earth FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Earth");
				if (uuid == null) return null;
				earthbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return earthbender;
	}

	// Returns the name of the team's firebender.
	public static String getTeamFirebender(String teamName) {
		String fire = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Fire FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Fire");
				if (uuid == null) return null;
				fire = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return fire;
	}

	// Returns the name of the team's chiblocker.
	public static String getTeamChiblocker(String teamName) {
		String chi = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Chi FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Chi");
				if (uuid == null) return null;
				chi = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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
	public static String getPlayerElementAsString(UUID uuid) {
		String player = Bukkit.getOfflinePlayer(uuid).getName();
		if (GeneralMethods.isBender(player, Element.Air)) return "Air";
		if (GeneralMethods.isBender(player, Element.Water)) return "Water";
		if (GeneralMethods.isBender(player, Element.Earth)) return "Earth";
		if (GeneralMethods.isBender(player, Element.Fire)) return "Fire";
		if (GeneralMethods.isBender(player, Element.Chi)) return "Chi";
		return null;
	}

	// Adds color to messages.
	public static String colorize(String message) {
		return message.replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1");
	}


	// Returns a Set (List) of Strings.
	public static Set<String> getTeams() {
		return teams;
	}

	public static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Probending.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		Probending.econ = economyProvider.getProvider();
		return (Probending.econ != null);
	}

	public static void setWins(int wins, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + wins + " WHERE team = '" + teamName + "'");
	}

	public static void setLosses(int losses, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + losses + " WHERE team = '" + teamName + "'");

	}

	public static int getWins(String teamName) {
		int wins = 0;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT wins FROM probending_teams WHERE team = '" + teamName + "'");
		try {
			if (rs2.next()) {
				wins = rs2.getInt("wins");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wins;
	}

	public static int getLosses(String teamName) {
		int losses = 0;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT losses FROM probending_teams WHERE team = '" + teamName + "'");
		try {
			if (rs2.next()) {
				losses = rs2.getInt("losses");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return losses;
	}

	public static void addWin(String teamName) {
		int currentWins = getWins(teamName);
		int newWins = currentWins + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + newWins + " WHERE team = '" + teamName + "'");
	}

	public static void addLoss(String teamName) {
		int currentLosses = getLosses(teamName);
		int newLosses = currentLosses + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + newLosses + " WHERE team = '" + teamName + "'");
	}

	public static int getOnlineTeamSize(String teamName) {
		int o = 0;
		for (Player player: Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			if (player != null) {
				if (getPlayerTeam(uuid) != null) {
					if (getPlayerTeam(uuid).equalsIgnoreCase(teamName)) {
						o++;
					}
				}
			}
		}
		return o;

	}

}