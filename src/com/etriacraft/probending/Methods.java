package com.etriacraft.probending;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Methods {

	Probending plugin;

	public Methods(Probending plugin) {
		this.plugin = plugin;
	}
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

	public static void createTeam(String teamName, String owner) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + ".Owner", owner);
		Probending.plugin.saveConfig();
	}

	public static void addPlayerToTeam(String teamName, String player, String element) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, player);
		Probending.plugin.getConfig().set("players." + player, teamName);
		Probending.plugin.saveConfig();
	}

	public static void removePlayerFromTeam(String teamName, String player, String element) {
		Probending.plugin.getConfig().set("TeamInfo." + teamName + "." + element, null);
		Probending.plugin.getConfig().set("players." + player, null);
		Probending.plugin.saveConfig();
	}

	public static boolean playerInTeam(String playerName) {
		if (Probending.plugin.getConfig().get("players." + playerName) == (null)) {
			return false;
		} else {
			return true;
		}
	}

	public static String getPlayerTeam(String player) {
		return Probending.plugin.getConfig().getString("players." + player);
	}

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

	public static boolean isPlayerOwner(String player, String teamName) {
		if (Probending.plugin.getConfig().getString("TeamInfo." + teamName + ".Owner").equals(player)) {
			return true;
		}
		return false;
	}
	public static Set<String> teamHasElement(String teamName) {
		Set<String> teamelements = Probending.plugin.getConfig().getConfigurationSection("TeamInfo." + teamName).getKeys(true);
		return teamelements;
		//		if (Probending.plugin.getConfig().get("TeamInfo." + teamName + "." + element) == null) {
//			return true;
//		}
//		return false;
	}
	public static String colorize(String message) {
		return message.replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1");
	}

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
	
	public static void createArena(String arenaName) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".created", true);
		Probending.plugin.saveConfig();
	}
	
	public static void deleteArena(String arenaName) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName, null);
		Probending.plugin.saveConfig();
	}
	
	public static void setSpectatorSpawn(String arenaName, Double x, Double y, Double z, String world) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.world", world);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.x", x);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.y", y);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".SpectatorSpawn.z", z);
		Probending.plugin.saveConfig();
	}
	
	public static void setFieldSpawn(String arenaName, Double x, Double y, Double z, String world) {
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.world", world);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.x", x);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.y", y);
		Probending.plugin.getConfig().set("ArenaInfo." + arenaName + ".FieldSpawn.z", z);
		Probending.plugin.saveConfig();
	}
	
	public static Location getSpectatorSpawn(String arenaName) {
		Double x = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".SpectatorSpawn.z");
		World world = Bukkit.getWorld(Probending.plugin.getConfig().getString("ArenaInfo." + arenaName + ".SpectatorSpawn.world"));
		Location location = new Location(world, x, y, z);
		return location;
	}
	
	public static Location getFieldSpawn(String arenaName) {
		Double x = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.x");
		Double y = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.y");
		Double z = Probending.plugin.getConfig().getDouble("ArenaInfo." + arenaName + ".FieldSpawn.z");
		World world = Bukkit.getWorld(Probending.plugin.getConfig().getString("ArenaInfo." + arenaName + ".FieldSpawn.world"));
		Location location = new Location(world, x, y, z);
		return location;
	}
	
	public static Set<String> getTeams() {
		Set<String> teams = Probending.plugin.getConfig().getConfigurationSection("TeamInfo").getKeys(false);
		return teams;
	}
	
	public static Set<String> getArenas() {
		Set<String> arenas = Probending.plugin.getConfig().getConfigurationSection("ArenaInfo").getKeys(false);
		return arenas;
	}

}
