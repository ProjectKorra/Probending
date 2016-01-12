package com.projectkorra.probending;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.objects.Arena;
import com.projectkorra.probending.objects.Round;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.probending.storage.DBConnection;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.milkbowl.vault.economy.Economy;


public class PBMethods {

	Probending plugin;

	public PBMethods(Probending plugin) {
		this.plugin = plugin;
	}

	public static boolean isAutomateMatches() {
		return Probending.plugin.getConfig().getBoolean("WorldGuard.AutomateMatches");
	}
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
	 * Checks to see if there is an ongoing round at the specified arena.
	 * @return true if there is a round, false if not.
	 */
	
	public static boolean isRoundAtArena(Arena arena) {
		for (UUID round: Round.rounds.keySet()) {
			if (Round.rounds.get(round).getArena() == arena) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Restores the armor for a player that has had their armor changed by this plugin.
	 * @param player The player to restore armor to.
	 */
	public static void restoreArmor(Player player) {
		if (Commands.tmpArmor.containsKey(player)) {
			if (player.getInventory().getArmorContents() != null) {
				player.getInventory().setArmorContents(null);
			}
			player.getInventory().setArmorContents(Commands.tmpArmor.get(player));
			Commands.tmpArmor.remove(player);
		}
	}
	
	/**
	 * Gets the ongoing round at the specified arena.
	 * @param arena The arena to check.
	 * @return The round object for the round at the arena. Null if no round at the arena.
	 */
	public static Round getRoundAtArena(Arena arena) {
		for (UUID uuids: Round.rounds.keySet()) {
			if (Round.rounds.get(uuids).getArena() == arena) {
				return Round.rounds.get(uuids);
			}
		}
		return null;
	}
	
	public static void loadArenas() {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_arenas");
		try {
			if (!rs2.next()) return;
			do {
				String name = rs2.getString("name");
				World world = null;
				if (rs2.getString("world") != null) world = Bukkit.getWorld(rs2.getString("world"));
				Integer spectatorX = rs2.getInt("spectatorX");
				Integer spectatorY = rs2.getInt("spectatorY");
				Integer spectatorZ = rs2.getInt("spectatorZ");
				Integer teamOneX = rs2.getInt("teamOneX");
				Integer teamOneY = rs2.getInt("teamOneY");
				Integer teamOneZ = rs2.getInt("teamOneZ");
				Integer teamTwoX = rs2.getInt("teamTwoX");
				Integer teamTwoY = rs2.getInt("teamTwoY");
				Integer teamTwoZ = rs2.getInt("teamTwoZ");
				String field = rs2.getString("field");
				String divider = rs2.getString("divider");
				String teamOneZoneOne = rs2.getString("teamOneZoneOne");
				String teamOneZoneTwo = rs2.getString("teamOneZoneTwo");
				String teamOneZoneThree = rs2.getString("teamOneZoneThree");
				String teamTwoZoneOne = rs2.getString("teamTwoZoneOne");
				String teamTwoZoneTwo = rs2.getString("teamTwoZoneTwo");
				String teamTwoZoneThree = rs2.getString("teamTwoZoneThree");
				Color teamOneColor = null;
				Color teamTwoColor = null;
				if (rs2.getString("teamOneColor") != null) teamOneColor = PBMethods.getColorFromString(rs2.getString("teamOneColor"));
				if (rs2.getString("teamTwoColor") != null) teamTwoColor = PBMethods.getColorFromString(rs2.getString("teamTwoColor"));
				Location spectatorSpawn = null;
				Location teamOneSpawn = null;
				Location teamTwoSpawn = null;
				if (world != null && spectatorX != null && spectatorY != null && spectatorZ != null) {
					spectatorSpawn = new Location(world, spectatorX, spectatorY, spectatorZ);
				}
				if (world != null && teamOneX != null && teamOneY != null && teamOneZ != null) {
					teamOneSpawn = new Location(world, teamOneX, teamOneY, teamOneZ);
				}
				
				if (world != null && teamTwoX != null && teamTwoZ != null && teamTwoZ != null) {
					teamTwoSpawn = new Location(world, teamTwoX, teamTwoY, teamTwoZ);
				}
				
				new Arena(name, world, spectatorSpawn, teamOneSpawn, teamTwoSpawn, field, divider, teamOneZoneOne, teamOneZoneTwo, teamOneZoneThree, teamTwoZoneOne, teamTwoZoneTwo, teamTwoZoneThree, teamOneColor, teamTwoColor);
			} while (rs2.next());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static Set<String> colors = new HashSet<String>();
	
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
	public static void sendPBChat(String message, Round round) {
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (Commands.pbChat.contains(player)) {
				if (round != null && !round.getRoundPlayers().contains(player)) continue;
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
	 * Checks if an arena exists. Case insensitive.
	 * @param name The name of the arena as a string.
	 * @return true if the team exists. False if it does not.
	 */
	
	public static boolean arenaExists(String name) {
		for (String arena: Arena.arenas.keySet()) {
			if (arena.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Loads all teams. Should only be done on startup.
	 */
	public static void loadTeams() {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM probending_teams");
		UUID airbender = null;
		UUID waterbender = null;
		UUID earthbender = null;
		UUID firebender = null;
		UUID chiblocker = null;
		try {
			while (rs2.next()) {
				airbender = null;
				waterbender = null;
				earthbender = null;
				firebender = null;
				chiblocker = null;
				String name = rs2.getString("team");
				UUID owner = UUID.fromString(rs2.getString("owner"));
				if (rs2.getString("Air") != null) {
					airbender = UUID.fromString(rs2.getString("Air"));
				}
				
				if (rs2.getString("Water") != null) {
					waterbender = UUID.fromString(rs2.getString("Water"));
				}
				
				if (rs2.getString("Earth") != null) {
					earthbender = UUID.fromString(rs2.getString("Earth"));
				}
				
				if (rs2.getString("Fire") != null) {
					firebender = UUID.fromString(rs2.getString("Fire"));
				}
				if (rs2.getString("Chi") != null) {
					chiblocker = UUID.fromString(rs2.getString("Chi"));
				}
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
	
	public static void createArena(String arena, String teamOne, String teamTwo) {
		if (getArena(arena) != null) return; // We don't want to create an arena that already exists.
		DBConnection.sql.modifyQuery("INSERT INTO probending_arenas (name, teamOneColor, teamTwoColor) VALUES ('" + arena + "', '" + teamOne + "', '" + teamTwo + "')");
		new Arena(arena, null, null, null, null, null, null, null, null, null, null, null, null, getColorFromString(teamOne), getColorFromString(teamTwo));
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
	 * @return The team the player is on. Returns null if the team does not exist.
	 */
	
	public static Team getPlayerTeam(UUID player) {
		return getTeam(players.get(player.toString()));
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
	
	public static List<String> getPlayerElementsAsString(UUID uuid) {
		String player = Bukkit.getOfflinePlayer(uuid).getName();
		List<String> elements = new ArrayList<>();
		if (GeneralMethods.isBender(player, Element.Air)) elements.add("Air");
		if (GeneralMethods.isBender(player, Element.Water)) elements.add("Water");
		if (GeneralMethods.isBender(player, Element.Earth)) elements.add("Earth");
		if (GeneralMethods.isBender(player, Element.Fire)) elements.add("Fire");
		if (GeneralMethods.isBender(player, Element.Chi)) elements.add("Chi");
		
		return elements;
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
	 * Returns a ConcurrentHashMap of Probending arenas.
	 * The key is the String name of the arena.
	 * The value is the arena object.
	 * @return ConcurrentHashMap<String, Arena> of Probending Arenas.
	 */
	
	public static ConcurrentHashMap<String, Arena> getArenas() {
		return Arena.arenas;
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
	 * Get the number of players a team has online. For total number of players (including offline) see {@link #getTeamSize(String)}
	 * @param teamName The name of the team to check.
	 * @return The number of online players a team has. Returns 0 if the team does not exist or no players are online.
	 */
	public static int getOnlineTeamSize(Team team) {
		return team.getOnlinePlayers().size();
	}
	
	/**
	 * Checks if the player is currently in a Probending round.
	 * @param player The player to check.
	 * @return true if the player is in a round. False if not.
	 */
	
	public static boolean isPlayerInRound(Player player) {
		for (UUID uuid: Round.rounds.keySet()) {
			if (Round.rounds.get(uuid).getRoundPlayers().contains(player)) return true;
		}
		return false;
	}
	
	public static Round getPlayerRound(Player player) {
		for (UUID uuid: Round.rounds.keySet()) {
			if (Round.rounds.get(uuid).getRoundPlayers().contains(player)) return Round.rounds.get(uuid);
		}
		return null;
	}
	
	/**
	 * Gets the team with the given name. Case insensitive.
	 * @param teamName
	 * @return The team object for the team with the given name.
	 */
	public static Team getTeam(String teamName) {
		for (String s: Team.teams.keySet()) {
			if (s.equalsIgnoreCase(teamName)) {
				return Team.teams.get(s);
			}
		}
		return null;
	}
	
	/**
	 * Gets the arena with the given name. Case insensitive.
	 * @param arenaName
	 * @return The arena object for the arena with the given name.
	 */
	public static Arena getArena(String arenaName) {
		for (String a: Arena.arenas.keySet()) {
			if (a.equalsIgnoreCase(arenaName)) {
				return Arena.arenas.get(a);
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
	public static String multiBendingTypes;
	public static String PlayerNotElement;
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