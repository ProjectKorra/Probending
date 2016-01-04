package com.projectkorra.probending;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.projectkorra.probending.objects.Team;
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
	public static boolean buildDisabled = Probending.plugin.getConfig().getBoolean("WorldGuard.DisableBuildOnField");
	public static String ProbendingField = Probending.plugin.getConfig().getString("WorldGuard.ProbendingField");
	public static boolean AutomateMatches = Probending.plugin.getConfig().getBoolean("WorldGuard.AutomateMatches");
	public static String t1z1 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneOne").toLowerCase();
	public static String t1z2 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneTwo").toLowerCase();
	public static String t1z3 = Probending.plugin.getConfig().getString("WorldGuard.TeamOneZoneThree").toLowerCase();
	public static String t2z1 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneOne").toLowerCase();
	public static String t2z2 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneTwo").toLowerCase();
	public static String t2z3 = Probending.plugin.getConfig().getString("WorldGuard.TeamTwoZoneThree").toLowerCase();

	/**
	 * Checks if the config has WorldGuard Support Enabled.
	 * @return true if the config says to support WorldGuard.
	 */
	public static boolean isWorldGuardSupportEnabled() {
		return Probending.plugin.getConfig().getBoolean("WorldGuard.EnableSupport");
	}
	/**
	 * Checks if the server has WorldGuard installed.
	 * @return true if the server finds WorldGuard.
	 */
	public static boolean hasWorldGuard() {
		if (Probending.plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null) return true;
		return false;
	}
	/**
	 * Sets the Spectator Spawn to the specified location.
	 * @param loc The location to set the spawn to.
	 */
	public static void setSpectatorSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.SpectatorSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}
	
	/**
	 * Sets the spawn location for Team One.
	 * @param loc The location to set the spawn to.
	 */
	public static void setTeamOneSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.TeamOneSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}
	
	/**
	 * Sets the spawn location for Team Two.
	 * @param loc The Location to set the spawn to.
	 */
	public static void setTeamTwoSpawn(Location loc) {
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.World", loc.getWorld().getName());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.x", loc.getX());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.y", loc.getY());
		Probending.plugin.getConfig().set("TeamSettings.TeamTwoSpawn.z", loc.getZ());
		Probending.plugin.saveConfig();
	}

	/**
	 * Returns the team one spawn location.
	 * @return
	 */
	public static Location getTeamOneSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.TeamOneSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.TeamOneSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}

	/**
	 * Returns the Team Two Spawn location.
	 * @return
	 */
	public static Location getTeamTwoSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.TeamTwoSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.TeamTwoSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}

	/**
	 * Returns the set Spectator spawn.
	 * @return
	 */
	public static Location getSpectatorSpawn() {
		String worldS = Probending.plugin.getConfig().getString("TeamSettings.SpectatorSpawn.World");
		World world = Bukkit.getWorld(worldS);
		Double x = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("TeamSettings.SpectatorSpawn.z");
		Location loc = new Location(world,x,y,z);
		return loc;
	}


	/**
	 * Restores the armor for any player that has had their armor changed by this plugin.
	 */
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
	
	/**
	 * Checks if the zone is empty of all players from a specific team.
	 * @param teamName The team you want to check.
	 * @param zone The zone you want to check.
	 * @return true if the selected zone contains no players of the specified team.
	 */

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
	
	/**
	 * Moves all players still in a Probending match on a specific team up one zone.
	 * @param team = Name of the team to move.
	 * @param side = What side they are on. (One or Two)
	 */
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
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z1)) {
								allowedZone.put(player.getName(), t2z2);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z2)) {
								allowedZone.put(player.getName(), t1z1);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z3)) {
								allowedZone.put(player.getName(), t1z2);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
						}
						if (Side.equalsIgnoreCase("Two")) {
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z1)) {
								allowedZone.put(player.getName(), t1z1);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z2)) {
								allowedZone.put(player.getName(), t2z1);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t2z3)) {
								allowedZone.put(player.getName(), t2z2);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
							if (allowedZone.get(player.getName()).equalsIgnoreCase(t1z1)) {
								allowedZone.put(player.getName(), t1z2);
								player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", allowedZone.get(player.getName())));
								continue;
							}
						}

					}
				}
			}
		}
	}
	
	
	/**
	 * Returns all players allowed to be in a zone.
	 * @param zone The name of the zone you want to check.
	 * @return Set<String> of player names allowed in a zone.
	 */
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
	public static HashMap<String, String> players = new HashMap<String, String>();
	public static String storage = Probending.plugin.getConfig().getString("General.Storage");

	/**
	 * Colors a piece of armor.
	 * @param i The ItemStack to color. Must be leather armor.
	 * @param c The color to make the armor.
	 * @return The colored ItemStack. Should be a single piece of armor.
	 */
	public static ItemStack createColorArmor(ItemStack i, Color c)
	{
		LeatherArmorMeta meta = (LeatherArmorMeta)i.getItemMeta();
		meta.setColor(c);
		i.setItemMeta(meta);
		return i;
	}

	/**
	 * Populates a list of colors for use.
	 */
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

	/**
	 * Checks if WorldGuard is installed.
	 * @return The WorldGuard plugin.
	 */
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Probending.plugin.getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}
	/**
	 * Checks to see which regions are at a location.
	 * @param loc The location to check.
	 * @return Set<String> of regions at the given location. The String is the region name.
	 */
	public static Set<String> RegionsAtLocation(Location loc) {

		ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
		Set<String> regions = new HashSet<String>();
		for (ProtectedRegion region: set) {
			regions.add(region.getId());
		}

		return regions;

	}

	/**
	 * Sends a message in the Probending Chat channel.
	 * @param message The message to send.
	 */
	public static void sendPBChat(String message) {
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (Commands.pbChat.contains(player)) {
				player.sendMessage(PBMethods.Prefix + message);
			}
		}
	}
	
	/**
	 * Get a color from a user friendly string.
	 * @param pretendColor The string of the color you want.
	 * @return Color of specified String as a color, null if String is invalid.
	 */
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

	/**
	 * Checks if a team exists. Case insensitive.
	 * @param teamName The name of the team as a string.
	 * @return true if the team exists. False if it does not.
	 */
	public static boolean teamExists(String teamName) {
		for (String team: Team.teams.keySet()) {
			if (team.equalsIgnoreCase(teamName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads all teams. Should only be done on startup.
	 */
	public static void loadTeams() {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT team FROM probending_teams");
		try {
			while (rs2.next()) {
				String name = rs2.getString("team");
				UUID owner = UUID.fromString(rs2.getString("owner"));
				UUID airbender = UUID.fromString(rs2.getString("Air"));
				UUID waterbender = UUID.fromString(rs2.getString("Water"));
				UUID earthbender = UUID.fromString(rs2.getString("Earth"));
				UUID firebender = UUID.fromString(rs2.getString("Fire"));
				UUID chiblocker = UUID.fromString(rs2.getString("Chi"));
				int wins = rs2.getInt("wins");
				int losses = rs2.getInt("losses");
				new Team(name, owner, airbender, waterbender, earthbender, firebender, chiblocker, wins, losses);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Creates data for a player. 
	 * @param uuid The uuid of the player to create data for.
	 */
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
	
	/**
	 * Loads all player data. Should only be done during startup by this plugin.
	 */
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
	
	/**
	 * Creates a new Probending team.
	 * @param teamName The name of the new team.
	 * @param owner The UUID of the owner of the team.
	 */
	public static void createTeam(String teamName, UUID owner) {
		DBConnection.sql.modifyQuery("INSERT INTO probending_teams (team, owner) VALUES ('" + teamName + "', '" + owner.toString() + "')");
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = '" + teamName + "' WHERE uuid = '" + owner.toString() + "'");
		new Team(teamName, owner, null, null, null, null, null, 0, 0);
	}
	
	/**
	 * Deletes an existing Probending team. Check to make sure it exists first.
	 * See {@link #teamExists(String)}
	 * @param teamName The name of the team to delete.
	 */
	public static void deleteTeam(String teamName) {
		DBConnection.sql.modifyQuery("DELETE FROM probending_teams WHERE team = '" + teamName + "'");
		Team.teams.remove(teamName);
	}
	
	/**
	 * Adds a player to an existing Probending team. Check to make sure the team exists and doesn't have the element already.
	 * See {@link #teamExists(String)}
	 * @param teamName The name of the team to add a player to.
	 * @param player The UUID of the player to add to the team.
	 * @param element The player's element.
	 */
	public static void addPlayerToTeam(String teamName, UUID player, String element) {
		Team team = getTeam(teamName);
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = '" + teamName + "' WHERE uuid = '" + player.toString() + "'");
		if (element.equalsIgnoreCase("air")) team.setAirbender(player);
		if (element.equalsIgnoreCase("water")) team.setWaterbender(player);
		if (element.equalsIgnoreCase("earth")) team.setEarthbender(player);
		if (element.equalsIgnoreCase("fire")) team.setFirebender(player);
		if (element.equalsIgnoreCase("chi")) team.setChiblocker(player);
		players.put(player.toString(), teamName);
	}

	/**
	 * Removes a player from a Probending team. Check to make sure the team exists and the player is on the team.
	 * See {@link #teamExists(String)}
	 * See {@link #playerInTeam(UUID)}
	 * See {@link #getPlayerTeam(UUID)}
	 * @param teamName The name of the team to remove the player from.
	 * @param player The UUID of the player to remove.
	 * @param element The element of the player on the team. See {@link #getPlayerElementInTeam(UUID, String)}
	 */
	public static void removePlayerFromTeam(Team team, UUID player, String element) {
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = NULL WHERE uuid = '" + player.toString() + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = NULL WHERE team = '" + team.getName() + "'");
		if (element.equalsIgnoreCase("air")) team.setAirbender(null);
		if (element.equalsIgnoreCase("water")) team.setWaterbender(null);
		if (element.equalsIgnoreCase("earth")) team.setEarthbender(null);
		if (element.equalsIgnoreCase("fire")) team.setFirebender(null);
		if (element.equalsIgnoreCase("chi")) team.setChiblocker(null);
		players.put(player.toString(), null);
	}

	/**
	 * Checks if a player is in any Probending team.
	 * @param player The uuid of the player to check.
	 * @return true if the player is in a team. False if the player is not in a team.
	 */
	public static boolean playerInTeam(UUID player) {
		if (players.get(player.toString()) == null) return false;
		return true;
	}

	/**
	 * Checks the player's element in a Probending team.
	 * @param player The uuid of the player to check.
	 * @param teamName The name of the team to check.
	 * @return The element as a String.
	 */
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

	/**
	 * Gets the name of a player team as a String.
	 * @param player The UUID of the player to check.
	 * @return Name of player String. Can return null if the player does not exist. See {@link #playerInTeam(UUID)}
	 */
	
	public static String getPlayerTeam(UUID player) {
		return players.get(player.toString());
	}

	/**
	 * Gets the amount of players on a team.
	 * @param teamName The name of the team to check.
	 * @return Number of players in a team. 0 if the team does not exist.
	 */
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

	/**
	 * Returns the owner of a team. 
	 * @param teamName The name of the team to check.
	 * @return Owner's Player Name.
	 */
	@Deprecated
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
	
	/**
	 * Returns the owner of the team.
	 * @param team The team object.
	 * @return The owner of the team.
	 */
	public static OfflinePlayer getOwner(Team team) {
		return Bukkit.getOfflinePlayer(team.getOwner());
	}
	
	/**
	 * Changes the owner of a team. Make sure the player is on the team first.
	 * @param player The UUID of the player to make owner of the team.
	 * @param teamName The name of the team you're changing.
	 */
	public static void setOwner(UUID player, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET owner = '" + player.toString() + "' WHERE team = '" + teamName + "'");
	}

	/**
	 * Checks if the player is the owner of the team.
	 * @param player The UUID of the player you're checking.
	 * @param teamName The name of the team you're checking.
	 * @return true if the player is the owner of the team, false if not.
	 */
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

	/**
	 * Returns all of the elements on a team.
	 * @param teamName The name of the team you are checking.
	 * @return A Set<String> of the elements on a team. Strings are Air, Water, Earth, Fire, Chi
	 */
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

	/**
	 * Returns the team's Airbender if they exist. 
	 * @param teamName The name of the team to check.
	 * @return OfflinePlayer of the team's Airbender. null if the team does not have an Airbender.
	 */
	@Deprecated
	public static OfflinePlayer getTeamAirbender(String teamName) {
		OfflinePlayer airbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Air FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Air");
				if (uuid == null) return null;
				airbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return airbender;
	}
	
	/**
	 * Returns the team's Airbender if they exist.
	 * @param team The team object to check.
	 * @return OfflinePlayer of the team's Airbender. null if the team doesn't have an Airbender.
	 */
	public static OfflinePlayer getAirbender(Team team) {
		return Bukkit.getOfflinePlayer(team.getAirbender());
	}
	
	/**
	 * Returns the team's Waterbender if they exist.
	 * @param team The team object to check.
	 * @return OfflinePlayer of the team's Waterbender. null if the team doesn't have an Waterbender.
	 */
	public static OfflinePlayer getWaterbender(Team team) {
		return Bukkit.getOfflinePlayer(team.getWaterbender());
	}
	
	/**
	 * Returns the team's Earthbender if they exist.
	 * @param team The team object to check.
	 * @return OfflinePlayer of the team's Earthbender. null if the team doesn't have an Earthbender.
	 */
	public static OfflinePlayer getEarthbender(Team team) {
		return Bukkit.getOfflinePlayer(team.getEarthbender());
	}
	
	/**
	 * Returns the team's Firebender if they exist.
	 * @param team The team object to check.
	 * @return OfflinePlayer of the team's Firebender. null if the team doesn't have an Firebender.
	 */
	public static OfflinePlayer getFirebender(Team team) {
		return Bukkit.getOfflinePlayer(team.getFirebender());
	}
	
	/**
	 * Returns the team's Chiblocker if they exist.
	 * @param team The team object to check.
	 * @return OfflinePlayer of the team's Chiblocker. null if the team doesn't have an Chiblocker.
	 */
	public static OfflinePlayer getChiblocker(Team team) {
		return Bukkit.getOfflinePlayer(team.getChiblocker());
	}

	/**
	 * Returns the team's Waterbender if they exist.
	 * @param teamName The name of the team to check.
	 * @return OfflinePlayer of the team's Waterbender. null if the team does not have a Waterbender.
	 */
	@Deprecated
	public static OfflinePlayer getTeamWaterbender(String teamName) {
		OfflinePlayer waterbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Water FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Water");
				if (uuid == null) return null;
				waterbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return waterbender;
	}

	/**
	 * Returns the team's Earthbender if they exist.
	 * @param teamName The name of the team to check.
	 * @return OfflinePlayer of the team's Earthbender. null if the team does not have an Earthbender.
	 */
	@Deprecated
	public static OfflinePlayer getTeamEarthbender(String teamName) {
		OfflinePlayer earthbender = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Earth FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Earth");
				if (uuid == null) return null;
				earthbender = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return earthbender;
	}

	/**
	 * Returns the team's Firebender if they exist.
	 * @param teamName The name of the team to check.
	 * @return OfflinePlayer of the team's Firebender. null if the team does not have a Firebender.
	 */
	@Deprecated
	public static OfflinePlayer getTeamFirebender(String teamName) {
		OfflinePlayer fire = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Fire FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Fire");
				if (uuid == null) return null;
				fire = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return fire;
	}

	/**
	 * Returns the team's Chiblocker if they exist.
	 * @param teamName The name of the team to check.
	 * @return OfflinePlayer of the team's Chiblocker. null if the team does not exist.
	 */
	@Deprecated
	public static OfflinePlayer getTeamChiblocker(String teamName) {
		OfflinePlayer chi = null;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT Chi FROM probending_teams WHERE team = '"+ teamName + "'");
		try {
			if (rs2.next()) {
				String uuid = rs2.getString("Chi");
				if (uuid == null) return null;
				chi = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return chi;
	}

	/**
	 * Checks if Airbenders are allowed to Probend.
	 * @return true if Airbenders can Probend, false if not.
	 */
	public static boolean getAirAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowAir");
	}

	/**
	 * Checks if Waterbenders are allowed to Probend.
	 * @return true if Waterbenders can Probend, false if not.
	 */
	public static boolean getWaterAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowWater");
	}
	
	/**
	 * Checks if Earthbenders are allowed o Probend.
	 * @return true if Earthbenders can Probend, false if not.
	 */

	public static boolean getEarthAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowEarth");
	}

	/**
	 * Checks if Firebenders are allowed to Probend.
	 * @return true if Firebenders can Probend, false if not.
	 */
	public static boolean getFireAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowFire");
	}

	/**
	 * Checks if Chiblockers are allowed to Probend.
	 * @return true of Chiblockers can Probend, false if not.
	 */
	public static boolean getChiAllowed() {
		return Probending.plugin.getConfig().getBoolean("TeamSettings.AllowChi");
	}

	/**
	 * Returns the player's element as a String.
	 * @param uuid The uuid of the player to check.
	 * @return The name of the element the player possesses. null if none. Strings are: Air, Water, Earth, Fire, Chi
	 */
	public static String getPlayerElementAsString(UUID uuid) {
		String player = Bukkit.getOfflinePlayer(uuid).getName();
		if (GeneralMethods.isBender(player, Element.Air)) return "Air";
		if (GeneralMethods.isBender(player, Element.Water)) return "Water";
		if (GeneralMethods.isBender(player, Element.Earth)) return "Earth";
		if (GeneralMethods.isBender(player, Element.Fire)) return "Fire";
		if (GeneralMethods.isBender(player, Element.Chi)) return "Chi";
		return null;
	}

	/**
	 * Adds color to a message. Replacing the & symbol with the appropriate color.
	 * @param message The message to add color to.
	 * @return The message with color.
	 */
	public static String colorize(String message) {
		return message.replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1");
	}


	/**
	 * Returns a ConcurrentHashMap of Probending teams.
	 * The Key is the String name of the team.
	 * The Value is the Team Object.
	 * @return ConcurrentHashMap<String, Team> of Probending teams.
	 */
	public static ConcurrentHashMap<String, Team> getTeams() {
		return Team.teams;
	}

	/**
	 * Sets up the economy feature.
	 * @return true if the economy is enabled (vault is installed), false if Vault is not found.
	 */
	public static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Probending.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		Probending.econ = economyProvider.getProvider();
		return (Probending.econ != null);
	}
	
	/**
	 * Set the number of wins a team has.
	 * @param wins The number of wins you want to set the team to.
	 * @param teamName The name of the team you want to change.
	 */
	@Deprecated
	public static void setWins(int wins, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + wins + " WHERE team = '" + teamName + "'");
	}
	
	/**
	 * Set the number of losses a team has.
	 * @param losses The number of losses you want to set the team to.
	 * @param teamName The name of the team you want to change.
	 */
	@Deprecated
	public static void setLosses(int losses, String teamName) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + losses + " WHERE team = '" + teamName + "'");

	}
	
	/**
	 * Get the amount of wins a team has.
	 * @param teamName The name of the team to check.
	 * @return The number of wins a team has.
	 */
	@Deprecated
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
	
	/**
	 * Get the amount of losses a team has.
	 * @param teamName The name of the team to check.
	 * @return The number of losses a team has.
	 */
	@Deprecated
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

	/**
	 * Add a win to a team.
	 * @param teamName The name of the team to add a win to.
	 */
	@Deprecated
	public static void addWin(String teamName) {
		int currentWins = getWins(teamName);
		int newWins = currentWins + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + newWins + " WHERE team = '" + teamName + "'");
	}

	/**
	 * Add a loss to a team.
	 * @param teamName The name of the team to add a loss to.
	 */
	@Deprecated
	public static void addLoss(String teamName) {
		int currentLosses = getLosses(teamName);
		int newLosses = currentLosses + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + newLosses + " WHERE team = '" + teamName + "'");
	}

	/**
	 * Get the number of players a team has online. For total number of players (including offline) see {@link #getTeamSize(String)}
	 * @param teamName The name of the team to check.
	 * @return The number of online players a team has. Returns 0 if the team does not exist or no players are online.
	 */
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
	
	public static Team getTeam(String teamName) {
		for (String s: Team.teams.keySet()) {
			if (s.equalsIgnoreCase(teamName)) {
				return Team.teams.get(s);
			}
		}
		return null;
	}
	
	public static String Prefix;
	public static String noPermission;
	public static String configReloaded;
	public static String NoTeamPermissions;
	
	/*
	 * Player
	 */
	public static String noBendingType;
	public static String PlayerAlreadyInTeam;
	public static String ElementNotAllowed;
	public static String PlayerNotInTeam;
	public static String PlayerNotOnline;
	public static String PlayerInviteSent;
	public static String PlayerInviteReceived;
	public static String InviteInstructions;
	public static String NoInviteFromTeam;
	public static String YouHaveBeenBooted;
	public static String YouHaveQuit;
	public static String RemovedFromTeamBecauseDifferentElement;
	
	/*
	 * Team
	 */
	public static String teamAlreadyExists;
	public static String TeamCreated;
	public static String NotOwnerOfTeam;
	public static String MaxSizeReached;
	public static String TeamAlreadyHasElement;
	public static String TeamDoesNotExist;
	public static String PlayerJoinedTeam;
	public static String CantBootFromOwnTeam;
	public static String PlayerNotOnThisTeam;
	public static String PlayerHasBeenBooted;
	public static String PlayerHasQuit;
	public static String TeamDisbanded;
	public static String NameTooLong;
	public static String TeamRenamed;
	public static String TeamAlreadyNamedThat;
	public static String OwnerNotOnline;
	
	/*
	 * Economy
	 */
	
	public static String NotEnoughMoney;
	public static String MoneyWithdrawn;
	
	/*
	 * Round
	 */
	
	public static String OneMinuteRemaining;
	public static String RoundComplete;
	public static String RoundAlreadyGoing;
	public static String InvalidTeamSize;
	public static String RoundStarted;
	public static String RoundStopped;
	public static String RoundPaused;
	public static String RoundResumed;
	public static String NoOngoingRound;
	public static String PlayerEliminated;
	public static String PlayerFouled;
	public static String RoundEnded;
	public static String TeamWon;
	public static String MoveUpOneZone;
	public static String CantEnterField;
	public static String CantTeleportDuringMatch;
	public static String MoveNotAllowed;

	/*
	 * Misc
	 */

	public static String ChatEnabled;
	public static String ChatDisabled;
	public static String WinAddedToTeam;
	public static String LossAddedToTeam;
	public static String TeamSpawnSet;

}