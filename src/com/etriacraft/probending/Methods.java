package com.etriacraft.probending;

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
		SignListener.colors.add("Cyan"); // Tested
		SignListener.colors.add("Black"); // Tested
		SignListener.colors.add("Blue"); // Tested
		SignListener.colors.add("Magenta"); // Tested
		SignListener.colors.add("Gray"); // Dark Gray?? TODO
		SignListener.colors.add("Green"); // Tested
		SignListener.colors.add("LightGreen"); // Tested
		SignListener.colors.add("DarkRed"); // Tested
		SignListener.colors.add("DarkBlue"); // Tested
		SignListener.colors.add("Olive"); // Tested
		SignListener.colors.add("Orange"); // Tested
		SignListener.colors.add("Purple"); // Tested
		SignListener.colors.add("Red"); // Tested
		SignListener.colors.add("Gray"); // Tested
		SignListener.colors.add("Teal"); // Tested
		SignListener.colors.add("White"); // Tested
		SignListener.colors.add("Yellow"); //Tested

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
		if (Probending.plugin.getConfig().getConfigurationSection("TeamInfo") == null) {
			return false;
		}
		Set<String> teams = getTeams();
		for (String team: teams) {
			if (team.equalsIgnoreCase(teamName)) {
				return true;
			}
		}
		return false;
	}

	// Creates a team.
	public static void createTeam(String teamName, String owner) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Owner", owner);
		Probending.plugin.saveConfig();
	}

	// Adds a player to a team.
	public static void addPlayerToTeam(String teamName, String player, String element) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, player);
		Probending.plugin.getConfig().set("players." + player, teamName);
		Probending.plugin.saveConfig();
	}

	// Removes a player from a team.
	public static void removePlayerFromTeam(String teamName, String player, String element) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, null);
		Probending.plugin.getConfig().set("players." + player, null);
		Probending.plugin.saveConfig();
	}

	// Checks if the player is in a team. Returns true if the player is in a team.
	public static boolean playerInTeam(String playerName) {
		if (Probending.plugin.getConfig().get("players." + playerName) == (null)) {
			return false;
		} else {
			return true;
		}
	}

	// Checks the player's element in the team. (Regardless of if they've changed)
	public static String getPlayerElementInTeam(String playerName, String teamName) {
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
		return null;
	}

	// Returns the name of the player's team.
	public static String getPlayerTeam(String player) {
		return Probending.plugin.getConfig().getString("players." + player);
	}

	// Returns the amount of players in a team.
	public static int getTeamSize(String teamName) {
		int size = 0;
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
		return size;
	}

	// Returns true if the player is the owner of the team.
	public static boolean isPlayerOwner(String player, String teamName) {
		if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Owner").equals(player)) {
			return true;
		}
		return false;
	}

	// Returns a set (List) of all of the teams elements.
	public static Set<String> getTeamElements(String teamName) {
		Set<String> teamelements = Probending.plugin.getConfig().getConfigurationSection("TeamInfo." + teamName).getKeys(true);
		return teamelements;
	}

	// Returns the name of the team's airbender.
	public static String getTeamAirbender(String teamName) {
		String airbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Air");
		return airbender;
	}

	// Returns the name of the team's waterbender.
	public static String getTeamWaterbender(String teamName) {
		String waterbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Water");
		return waterbender;
	}

	// Returns the name of the team's earthbender.
	public static String getTeamEarthbender(String teamName) {
		String earthbender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Earth");
		return earthbender;
	}

	// Returns the name of the team's firebender.
	public static String getTeamFirebender(String teamName) {
		String firebender = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Fire");
		return firebender;
	}

	// Returns the name of the team's chiblocker.
	public static String getTeamChiblocker(String teamName) {
		String chiblocker = Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Chi");
		return chiblocker;
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
		Set<String> teams = Probending.plugin.getConfig().getConfigurationSection("TeamInfo").getKeys(false);
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

}