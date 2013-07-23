package com.etriacraft.probending;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import tools.BendingType;
import tools.Tools;

public class Commands {

	// Booleans
	public static Boolean arenainuse;
	// Strings
	public static String Prefix;
	public static String teamAlreadyExists;
	public static String noBendingType;
	public static String PlayerAlreadyInTeam;
	public static String ElementNotAllowed;
	public static String TeamCreated;
	public static String noPermission;

	public static String PlayerNotInTeam;
	public static String MaxSizeReached;
	public static String TeamAlreadyHasElement;
	public static String PlayerInviteSent;
	public static String PlayerInviteReceived;
	public static String InviteInstructions;
	public static String NotOwnerOfTeam;
	public static String PlayerNotOnline;
	
	public static String TeamDoesNotExist;
	
	public static String NoInviteFromTeam;
	public static String PlayerJoinedTeam;
	
	public static String CantBootFromOwnTeam;
	public static String PlayerNotOnThisTeam;
	public static String YouHaveBeenBooted;
	public static String PlayerHasBeenBooted;
	
	public static String YouHaveQuit;
	public static String PlayerHasQuit;
	
	public static String TeamDisbanded;
	
	public static String ArenaAlreadyExists;
	public static String ArenaCreated;
	public static String ArenaDoesNotExist;
	public static String ArenaDeleted;
	
	public static String SpectatorSpawnSet;
	public static String fieldSpawnSet;
	
	public static String SentToArena;
	
	public static String configReloaded;
	
	Probending plugin;

	public Commands(Probending plugin) {
		this.plugin = plugin;
		init();
	}
	//HashMaps
	public static HashMap<String, LinkedList<String>> teamInvites = new HashMap<String, LinkedList<String>>();
	public static HashMap<String, LinkedList<String>> teamChallenges = new HashMap<String, LinkedList<String>>();
	
	private void init() {
		PluginCommand probending = plugin.getCommand("probending");
		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				if (args.length == 0) {
					s.sendMessage("-----§6Probending Commands§f----");
					s.sendMessage("§3/probending team§f - View team commands.");
					s.sendMessage("§3/probending arena§f - View arena commands.");
					s.sendMessage("§3/probending reload§f - Reload Configuration.");
					return true;
				}

					/*
					 * if (!teamInvites.containsKey(player.getName())) {
							teamInvites.put(player.getName(), new LinkedList<String>());
						}
					 */
				if (args[0].equalsIgnoreCase("reload")) {
					if (!s.hasPermission("probending.reload")) {
						s.sendMessage(Prefix + noPermission);
						return true;
					}
					plugin.reloadConfig();
					s.sendMessage(Prefix + configReloaded);
				}
				if (args[0].equalsIgnoreCase("arena")) {
					if (args.length == 1) {
						s.sendMessage("-----§6Probending Arena Commands§f-----");
						s.sendMessage("§3/pb arena create [Name]§f - Create an arena."); // Done
						s.sendMessage("§3/pb arena delete [Name]§f - Delete an arena."); // Done
						s.sendMessage("§3/pb arena setspawn [Name] spectator§f - Set spectator spawn."); // Done
						s.sendMessage("§3/pb arena setspawn [Name] field §f- Set field spawn point."); // Done
						s.sendMessage("§3/pb arena spawn [Name] spectator|field§f - Warp to an arena."); // Done
						return true;
					}
					if (args[1].equalsIgnoreCase("spawn")) {
						if (!s.hasPermission("probending.arena.spawn")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						String spawn = null;
						String arenaName = "Main";
						if (args.length == 4) {
							arenaName = args[2];
							spawn = args[3];
						}
						if (!Methods.arenaExists(arenaName)) {
							s.sendMessage(Prefix + ArenaDoesNotExist);
							return true;
						}
						if (args.length > 4) {
							s.sendMessage(Prefix + "§cProper Usage: §3/pb arena spawn [Name] spectator|field");
							return true;
						}
						if (args.length < 4) {
							spawn = args[2];
						}
						if (!spawn.equalsIgnoreCase("spectator") && !spawn.equalsIgnoreCase("field")) {
							s.sendMessage(Prefix + "§cProper Usage: §3/pb arena spawn [Name] spectator|field");
							return true;
						}
						Player p = (Player) s;
						Location loc = null;
						if (spawn.equalsIgnoreCase("spectator")) {
							loc = Methods.getSpectatorSpawn(arenaName);
						}
						if (spawn.equalsIgnoreCase("field")) {
							loc = Methods.getFieldSpawn(arenaName);
						}
						p.teleport(loc);
						s.sendMessage(Prefix + SentToArena.replace("%arena", arenaName));
					}
					if (args[1].equalsIgnoreCase("setspawn")) {
						if (args.length < 4) {
							s.sendMessage("§3/pb arena setspawn [Name] spectator§f - Set spectator spawn.");
							s.sendMessage("§3/pb arena setspawn [Name] field§f - Set field spawn.");
							return true;
						}
						String arenaName = args[2];
						if (!Methods.arenaExists(arenaName)) {
							s.sendMessage(Prefix + ArenaDoesNotExist);
							return true;
						}
						if (args[3].equalsIgnoreCase("spectator")) {
							if (!s.hasPermission("probending.arena.setspawn.spectator")) {
								s.sendMessage(Prefix + noPermission);
								return true;
							}
							Location specSpawn = ((Player) s).getLocation();
							Double x = specSpawn.getX();
							Double y = specSpawn.getY();
							Double z = specSpawn.getZ();
							String world = specSpawn.getWorld().getName();
							Methods.setSpectatorSpawn(arenaName, x,y,z,world);
							s.sendMessage(Prefix + SpectatorSpawnSet.replace("%arena", arenaName));
							return true;
						}
						if (args[3].equalsIgnoreCase("field")) {
							if (!s.hasPermission("probending.arena.setspawn.field")) {
								s.sendMessage(Prefix + noPermission);
								return true;
							}
							Location fieldSpawn = ((Player) s).getLocation();
							Double x = fieldSpawn.getX();
							Double y = fieldSpawn.getY();
							Double z = fieldSpawn.getZ();
							String world = fieldSpawn.getWorld().getName();
							Methods.setFieldSpawn(arenaName, x,y,z,world);
							s.sendMessage(Prefix + fieldSpawnSet.replace("%arena", arenaName));
							return true;
						}
					}
					if (args[1].equalsIgnoreCase("create")) {
						if (!s.hasPermission("probending.arena.create")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						String arenaName = "Main";
						if (args.length == 3) {
							arenaName = args[2];
						}
						
						if (Methods.arenaExists(arenaName)) {
							s.sendMessage(Prefix + ArenaAlreadyExists.replace("%arena", arenaName));
							return true;
						}
						Methods.createArena(arenaName);
						s.sendMessage(Prefix + ArenaCreated.replace("%arena", arenaName));
						return true;
					}
					
					if (args[1].equalsIgnoreCase("delete")) {
						if (!s.hasPermission("probending.arena.delete")) {
							s.sendMessage(Prefix + noPermission);
						}
						if (args.length != 3) {
							s.sendMessage("§cProper Usage: §3/pb arena delete [Name]");
							return true;
						}
						
						String arenaName = args[2];
						if (!Methods.arenaExists(arenaName)) {
							s.sendMessage(Prefix + ArenaDoesNotExist.replace("%arena", arenaName));
							return true;
						}
						
						Methods.deleteArena(arenaName);
						s.sendMessage(Prefix + ArenaDeleted.replace("%arena", arenaName));
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("team")) {
					if (args.length == 1) {
						s.sendMessage("-----§6Probending Team Commands§f-----");
						s.sendMessage("§3/pb team create [Name]§f - Create a team."); // Done
						s.sendMessage("§3/pb team invite [Player]§f - Invite a player to a team."); // Done
						s.sendMessage("§3/pb team info <Name>§f - View info on a team."); // Done
						s.sendMessage("§3/pb team join <Name>§f - Join a team."); // Done
						s.sendMessage("§3/pb team kick <Name>§f - Kick a player from your team."); // Done
						s.sendMessage("§3/pb team quit §f- Quit your current team."); // Done
						s.sendMessage("§3/pb team disband §f- Disband your team."); // Done
						s.sendMessage("§3/pb team list§f - List all teams.");
						return true;
					}
					if (args[1].equalsIgnoreCase("list")) {
						if (!s.hasPermission("probending.team.list")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						Set<String> teams = plugin.getConfig().getConfigurationSection("TeamInfo").getKeys(false);
						s.sendMessage("§cTeams: §a" + teams.toString());
						return true;
					}
					if (args[1].equalsIgnoreCase("disband")) {
						if (!s.hasPermission("probending.team.disband")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						if (args.length != 2) {
							s.sendMessage(Prefix + "&cProper Usage: &3/pb team disband");
							return true;
						}
						String teamName = Methods.getPlayerTeam(s.getName());
						if (!Methods.isPlayerOwner(s.getName(), teamName)) {
							s.sendMessage(Prefix + NotOwnerOfTeam);
							return true;
						}
						for (Player player: Bukkit.getOnlinePlayers()) {
							if (Methods.getPlayerTeam(player.getName()) == null) continue;
							if (Methods.getPlayerTeam(player.getName()).equals(teamName)) {
								s.sendMessage(Prefix + TeamDisbanded.replace("%team", teamName));
							}
						}
						String playerElement = null;
						if (Tools.isBender(s.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(s.getName(), BendingType.Water)){
							playerElement = "Water";
						}
						if (Tools.isBender(s.getName(), BendingType.Earth)){
							playerElement = "Earth";
						}
						if (Tools.isBender(s.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(s.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}
						Methods.removePlayerFromTeam(teamName, s.getName(), playerElement);
						Set<String> teamelements = Methods.teamHasElement(teamName);
						if (teamelements.contains("Air")) {
							Methods.removePlayerFromTeam(teamName, plugin.getConfig().getString("TeamInfo." + teamName + ".Air"), "Air");
						}
						if (teamelements.contains("Water")) {
							Methods.removePlayerFromTeam(teamName, plugin.getConfig().getString("TeamInfo." + teamName + ".Water"), "Water");
						}
						if (teamelements.contains("Earth")) {
							Methods.removePlayerFromTeam(teamName, plugin.getConfig().getString("TeamInfo." + teamName + ".Earth"), "Earth");
						}
						if (teamelements.contains("Fire")) {
							Methods.removePlayerFromTeam(teamName, plugin.getConfig().getString("TeamInfo." + teamName + ".Fire"), "Fire");
						}
						if (teamelements.contains("Chi")) {
							Methods.removePlayerFromTeam(teamName, plugin.getConfig().getString("TeamInfo." + teamName + ".Chi"), "Chi");
						}
						plugin.getConfig().set("TeamInfo." + teamName, null);
						plugin.saveConfig();
						return true;
					}
					if (args[1].equalsIgnoreCase("quit")) {
						if (!s.hasPermission("probending.team.quit")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						if (args.length != 2) {
							s.sendMessage(Prefix + "&cProper Usage: &3/pb team quit");
							return true;
						}
						
						String teamName = Methods.getPlayerTeam(s.getName());
						if (teamName == null) {
							s.sendMessage(Prefix + PlayerNotInTeam);
							return true;
						}
						if (Methods.isPlayerOwner(s.getName(), teamName)) {
							s.sendMessage(Prefix + CantBootFromOwnTeam);
							return true;
						}
						String playerElement = null;
						if (Tools.isBender(s.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(s.getName(), BendingType.Water)){
							playerElement = "Water";
						}
						if (Tools.isBender(s.getName(), BendingType.Earth)){
							playerElement = "Earth";
						}
						if (Tools.isBender(s.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(s.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}
						Methods.removePlayerFromTeam(teamName, s.getName(), playerElement);
						s.sendMessage(Prefix + YouHaveQuit.replace("%team", teamName));
						for (Player player: Bukkit.getOnlinePlayers()) {
							if (Methods.getPlayerTeam(player.getName()) == null) continue;
							if (Methods.getPlayerTeam(player.getName()).equals(teamName)) {
								s.sendMessage(Prefix + PlayerHasQuit.replace("%team", teamName).replace("%player", s.getName()));
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("kick")) {
						if (!s.hasPermission("probending.team.kick")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						if (args.length != 3) {
							s.sendMessage(Prefix + "&cProper Usage: &3/pb team kick <Name>");
							return true;
						}
						String teamName = Methods.getPlayerTeam(s.getName());
						if (teamName == null) {
							s.sendMessage(Prefix + PlayerNotInTeam);
							return true;
						}
						if (!Methods.isPlayerOwner(s.getName(), teamName)) {
							s.sendMessage(Prefix + NotOwnerOfTeam);
							return true;
						}
						String playerName = args[2];
						if (playerName.equals(s.getName())) {
							s.sendMessage(Prefix + CantBootFromOwnTeam);
							return true;
						}
						if (!Methods.getPlayerTeam(playerName).equals(teamName)) {
							s.sendMessage(Prefix + PlayerNotOnThisTeam);
							return true;
						}
						Player p3 = Bukkit.getPlayer(args[2]);
						String playerElement = null;
						if (Tools.isBender(p3.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(p3.getName(), BendingType.Water)){
							playerElement = "Water";
						}
						if (Tools.isBender(p3.getName(), BendingType.Earth)){
							playerElement = "Earth";
						}
						if (Tools.isBender(p3.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(p3.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}
						Methods.removePlayerFromTeam(teamName, playerName, playerElement);
						Player player = Bukkit.getPlayer(playerName);
						if (player != null) {
							player.sendMessage(Prefix + YouHaveBeenBooted.replace("%team", teamName));
						}
						for (Player player2: Bukkit.getOnlinePlayers()) {
							if (Methods.getPlayerTeam(player2.getName()) == null) continue;
							if (Methods.getPlayerTeam(player2.getName()).equals(teamName)) {
								player2.sendMessage(Prefix + PlayerHasBeenBooted.replace("%player", playerName).replace("%team", teamName));
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("join")) {
						if (!s.hasPermission("probending.team.join")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						
						if (args.length != 3) {
							s.sendMessage("§cProper Usage: §3/pb team join [TeamName]");
							return true;
						}
						if (Methods.playerInTeam(s.getName())) {
							s.sendMessage(Prefix + PlayerAlreadyInTeam);
							return true;
						}
						String teamName = args[2];
						if (teamInvites.get(s.getName()) == null) {
							s.sendMessage(Prefix + NoInviteFromTeam);
							return true;
						}
						if (!teamInvites.get(s.getName()).contains(teamName)) {
							s.sendMessage(Prefix + NoInviteFromTeam);
							return true;
						}
						String playerElement = null;
						if (Tools.isBender(s.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(s.getName(), BendingType.Water)){
							playerElement = "Water";
						}
						if (Tools.isBender(s.getName(), BendingType.Earth)){
							playerElement = "Earth";
						}
						if (Tools.isBender(s.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(s.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}
						Set<String> teamelements = Methods.teamHasElement(teamName);
						if (teamelements.contains(playerElement)) {
							s.sendMessage(Prefix + TeamAlreadyHasElement);
							return true;
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.Allow" + playerElement)) {
							s.sendMessage(Prefix + ElementNotAllowed.replace("%element", playerElement));
							return true;
						}
						Methods.addPlayerToTeam(teamName, s.getName(), playerElement);
						for (Player player: Bukkit.getOnlinePlayers()) {
							if (Methods.getPlayerTeam(player.getName()).equals(teamName)) {
								player.sendMessage(Prefix + PlayerJoinedTeam.replace("%player", player.getName()).replace("%team", teamName));
							}
						}
						teamInvites.remove(s.getName());
						return true;
					}
					if (args[1].equalsIgnoreCase("info")) {
						if (!s.hasPermission("probending.team.info")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						String teamName = null;
						if (args.length == 2) {
							teamName = Methods.getPlayerTeam(s.getName());
						}
						if (args.length == 3) {
							teamName = args[2];
						}
						
						if (!Methods.teamExists(teamName)) {
							s.sendMessage(Prefix + TeamDoesNotExist);
							return true;
						}
						String teamOwner = plugin.getConfig().getString("TeamInfo." + teamName + ".Owner");
						s.sendMessage("§3Team Name:§e " + teamName);
						s.sendMessage("§3Team Owner:§5 " + teamOwner);
						
						String air = plugin.getConfig().getString("TeamInfo." + teamName + ".Air");
						String water = plugin.getConfig().getString("TeamInfo." + teamName + ".Water");
						String earth = plugin.getConfig().getString("TeamInfo." + teamName + ".Earth");
						String fire = plugin.getConfig().getString("TeamInfo." + teamName + ".Fire");
						String chi = plugin.getConfig().getString("TeamInfo." + teamName + ".Chi");
						if (plugin.getConfig().getBoolean("TeamSettings.AllowAir")) {
							if (air != null) {
								s.sendMessage("§3Airbender: §7" + air);
							}
						}
						if (plugin.getConfig().getBoolean("TeamSettings.AllowWater")) {
							if (water != null) {
								s.sendMessage("§3Waterbender: §b" + water);
							}
						}
						if (plugin.getConfig().getBoolean("TeamSettings.AllowEarth")) {
							if (earth != null) {
								s.sendMessage("§3Earthbender: §a" + earth);
							}
						}
						if (plugin.getConfig().getBoolean("TeamSettings.AllowFire")) {
							if (fire != null) {
								s.sendMessage("§3Firebender: §c" + fire);
							}
						}
						if (plugin.getConfig().getBoolean("TeamSettings.AllowChi")) {
							if (chi != null) {
								s.sendMessage("§3Chiblocker: &6" + chi);
							}
						}
						
					}
					if (args[1].equalsIgnoreCase("invite")) {
						if (args.length != 3) {
							s.sendMessage(Prefix + "§cProper Usage: §3/pb team invite [Name]");
							return true;
						}
						if (!s.hasPermission("probending.team.invite")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						
						if (!Methods.playerInTeam(s.getName())) {
							s.sendMessage(Prefix + PlayerNotInTeam);
							return true;
						}
						
						String playerTeam = Methods.getPlayerTeam(s.getName());
						if (!Methods.isPlayerOwner(s.getName(), playerTeam)) {
							s.sendMessage(Prefix + NotOwnerOfTeam);
							return true;
						}
						
						Player player = Bukkit.getPlayer(args[2]);
						
						if (player == null) {
							s.sendMessage(Prefix + PlayerNotOnline);
							return true;
						}
						
						if (Methods.playerInTeam(player.getName())) {
							s.sendMessage(Prefix + PlayerAlreadyInTeam);
							return true;
						}
						
						if (!teamInvites.containsKey(player.getName())) {
							teamInvites.put(player.getName(), new LinkedList<String>());
						}
						
						int maxSize = plugin.getConfig().getInt("TeamSettings.MaxTeamSize");
						if (Methods.getTeamSize(playerTeam) >= maxSize) {
							s.sendMessage(Prefix + MaxSizeReached);
						}
						String playerElement = null;
						if (Tools.isBender(player.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(player.getName(), BendingType.Water)) {
							playerElement = "Water";
						}
						if (Tools.isBender(player.getName(), BendingType.Earth)) {
							playerElement = "Earth";
						}
						if (Tools.isBender(player.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(player.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowAir")) {
							if (playerElement.equals("Air")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Airbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowWater")) {
							if (playerElement.equals("Water")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Waterbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowEarth")) {
							if (playerElement.equals("Earth")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Earthbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowFire")) {
							if (playerElement.equals("Fire")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Firebenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowChi")) {
							if (playerElement.equals("Chi")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Chiblockers"));
								return true;
							}
						}
						Set<String> teamelements = Methods.teamHasElement(playerTeam);
						if (teamelements.contains(playerElement)) {
							s.sendMessage(Prefix + TeamAlreadyHasElement);
							return true;
						}
						
						teamInvites.get(player.getName()).add(playerTeam);
						s.sendMessage(Prefix + PlayerInviteSent.replace("%team", playerTeam).replace("%player", player.getName()));
						player.sendMessage(Prefix + PlayerInviteReceived.replace("%team", playerTeam).replace("%player", player.getName()));
						player.sendMessage(Prefix + InviteInstructions.replace("%team", playerTeam).replace("%player", player.getName()));
						return true;
					}
					if (args[1].equalsIgnoreCase("create")) {
						if (args.length != 3) {
							s.sendMessage(Prefix + "§cProper Usage: §3/pb team create [Name]");
							return true;
						}
						if (!s.hasPermission("probending.team.create")) {
							s.sendMessage(Prefix + noPermission);
							return true;
						}
						String teamName = args[2];
						if (Methods.teamExists(teamName)) {
							s.sendMessage(Prefix + teamAlreadyExists);
							return true;
						}

						if (!Tools.isBender(s.getName())) {
							s.sendMessage(Prefix + noBendingType);
							return true;
						}

						if (Methods.playerInTeam(s.getName())) {
							s.sendMessage(Prefix + PlayerAlreadyInTeam);
							return true;
						}

						String playerElement = null;
						if (Tools.isBender(s.getName(), BendingType.Air)) {
							playerElement = "Air";
						}
						if (Tools.isBender(s.getName(), BendingType.Water)) {
							playerElement = "Water";
						}
						if (Tools.isBender(s.getName(), BendingType.Earth)) {
							playerElement = "Earth";
						}
						if (Tools.isBender(s.getName(), BendingType.Fire)){
							playerElement = "Fire";
						}
						if (Tools.isBender(s.getName(), BendingType.ChiBlocker)) {
							playerElement = "Chi";
						}

						if (!plugin.getConfig().getBoolean("TeamSettings.AllowAir")) {
							if (playerElement.equals("Air")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Airbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowWater")) {
							if (playerElement.equals("Water")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Waterbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowEarth")) {
							if (playerElement.equals("Earth")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Earthbenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowFire")) {
							if (playerElement.equals("Fire")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Firebenders"));
								return true;
							}
						}
						if (!plugin.getConfig().getBoolean("TeamSettings.AllowChi")) {
							if (playerElement.equals("Chi")) {
								s.sendMessage(Prefix + ElementNotAllowed.replace("%element", "Chiblockers"));
								return true;
							}
						}
						Methods.createTeam(teamName, s.getName());
						Methods.addPlayerToTeam(teamName, s.getName(), playerElement);
						s.sendMessage(Prefix + TeamCreated.replace("%team", teamName));
						return true;
					}
				}
				return true;
			}
		}; probending.setExecutor(exe);
	}
}
