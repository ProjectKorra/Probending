package com.projectkorra.probending;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.probending.storage.DBConnection;
import com.projectkorra.projectkorra.GeneralMethods;


public class Commands {

	// Integers
	public static int startingNumber;
	public static int currentNumber;
	public static int clockTask;
	// Booleans
	public static Boolean arenainuse;
	// Strings


	Probending plugin;

	public Commands(Probending plugin) {
		this.plugin = plugin;
		init();
	}
	//HashMaps
	public static Set<Player> pbChat = new HashSet<Player>();
	public static HashMap<String, LinkedList<String>> teamInvites = new HashMap<String, LinkedList<String>>();
	public static HashMap<String, LinkedList<String>> teamChallenges = new HashMap<String, LinkedList<String>>();
	public static HashMap<Player, ItemStack[]> tmpArmor = new HashMap<Player, ItemStack[]>();

	private void init() {
		PluginCommand probending = plugin.getCommand("probending");
		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				if (args.length == 0) {
					s.sendMessage("-----§6Probending Commands§f-----");
					s.sendMessage("§3/probending team§f - View team commands.");
					s.sendMessage("§3/probending round§f - View round Commands");
					if (s.hasPermission("probending.chat")) {
						s.sendMessage("§3/probending chat§f - Turn on Probending Chat.");
					}
					if (s.hasPermission("probending.reload")) {
						s.sendMessage("§3/probending reload§f - Reload Configuration.");
					}
					if (s.hasPermission("probending.setspawn")) {
						s.sendMessage("§3/probending setspawn [TeamOne|TeamTwo|Spectator]");
					}
					if (s.hasPermission("probending.challenge")) {
						s.sendMessage("§3/pb challenge <accept> [Team]§f - Challenge / Accept a challenge.");
					}
					return true;
				}

				if (args[0].equalsIgnoreCase("challenge")) {
					if (!s.hasPermission("probending.challenge")) {
						s.sendMessage(Strings.Prefix + Strings.noPermission);
						return true;
					}
					
					if (args.length == 1) {
						s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb challenge <accept> [Team]");
						return true;
					}

					if (args[1].equalsIgnoreCase("accept")) {
						
						UUID uuid = ((Player) s).getUniqueId();
						if (!s.hasPermission("probending.challenge.accept")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb challenge accept [Team]");
							return true;
						}
						if (PBMethods.getPlayerTeam(uuid) == null) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
							return true;
						}



						Set<String> teams = PBMethods.getTeams();
						String teamName = null;
						for (String team: teams) {
							if (team.equalsIgnoreCase(args[2])) {
								teamName = team;
							}
						}

						if (teamName == null) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}

						if (!teamChallenges.containsKey(PBMethods.getPlayerTeam(uuid))) {
							s.sendMessage(Strings.Prefix + Strings.NoChallengeFromTeam);
							return true;
						}

						if (teamChallenges.get(PBMethods.getPlayerTeam(uuid)) != null) {
							if (!teamChallenges.get(PBMethods.getPlayerTeam(uuid)).contains(teamName)) {
								s.sendMessage(Strings.Prefix + Strings.NoChallengeFromTeam);
								return true;
							}
						}

						int yourTeamSize = PBMethods.getOnlineTeamSize(PBMethods.getPlayerTeam(uuid));
						int opponentTeamSize = PBMethods.getOnlineTeamSize(teamName);
						int minTeamSize = plugin.getConfig().getInt("TeamSettings.MinTeamSize");
						int maxTeamSize = plugin.getConfig().getInt("TeamSettings.MaxTeamSize");

						if (yourTeamSize < minTeamSize || opponentTeamSize < minTeamSize || yourTeamSize > maxTeamSize || opponentTeamSize > maxTeamSize || opponentTeamSize != yourTeamSize) {
							s.sendMessage(Strings.Prefix + Strings.InvalidTeamSize);
							return true;
						}

						Player otherOwner = Bukkit.getPlayer(PBMethods.getOwner(teamName));
						if (otherOwner == null) {
							s.sendMessage(Strings.Prefix + Strings.OwnerNotOnline);
							return true;
						}

						if (PBMethods.matchStarted) {
							s.sendMessage(Strings.Prefix + Strings.RoundAlreadyGoing);
							return true;
						}

						// We've checked everything, now to actually start the match if everything is in place :)

						String team1 = PBMethods.getPlayerTeam(uuid); // Team 1
						String team2 = teamName; // Team 2


						// Add players to list of playing teams and send a message confirming it.
						PBMethods.playingTeams.add(team1.toLowerCase());
						PBMethods.playingTeams.add(team2.toLowerCase());
						PBMethods.TeamOne = team1.toLowerCase();
						PBMethods.TeamTwo = team2.toLowerCase();

						for (Player player: Bukkit.getOnlinePlayers()) {
							String playerTeam = PBMethods.getPlayerTeam(uuid);
							Color teamColor = null;
							if (playerTeam != null) {
								if (playerTeam.equalsIgnoreCase(team1)) teamColor = PBMethods.getColorFromString(plugin.getConfig().getString("TeamSettings.TeamOneColor"));
								if (playerTeam.equalsIgnoreCase(team2)) teamColor = PBMethods.getColorFromString(plugin.getConfig().getString("TeamSettings.TeamTwoColor"));
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne)) {
									PBMethods.teamOnePlayers.add(player.getName());
									player.teleport(PBMethods.getTeamOneSpawn());
								}
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
									PBMethods.teamTwoPlayers.add(player.getName());
									player.teleport(PBMethods.getTeamTwoSpawn());
								}
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
									tmpArmor.put(player, player.getInventory().getArmorContents()); // Backs up their armor.
									ItemStack armor1 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_HELMET), teamColor);
									ItemStack armor2 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), teamColor);
									ItemStack armor3 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), teamColor);
									ItemStack armor4 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), teamColor);
									player.getInventory().setHelmet(armor1);
									player.getInventory().setChestplate(armor2);
									player.getInventory().setLeggings(armor3);
									player.getInventory().setBoots(armor4);
								} else {
									if (PBMethods.RegionsAtLocation(player.getLocation()) != null && PBMethods.RegionsAtLocation(player.getLocation()).contains(PBMethods.ProbendingField)) {
										player.teleport(PBMethods.getSpectatorSpawn());
									}
								}
							}
						}


						int roundTime = plugin.getConfig().getInt("RoundSettings.Time");
						currentNumber = roundTime * 20;
						startingNumber = roundTime * 20;

						clockTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
							public void run() {
								PBMethods.matchStarted = true;
								currentNumber--;

								if (currentNumber == startingNumber - 1) {
									PBMethods.sendPBChat(Strings.RoundStarted.replace("%seconds", String.valueOf(startingNumber / 20)).replace("%team1", PBMethods.TeamOne).replace("%team2", PBMethods.TeamTwo));
								}
								if (currentNumber == 1200) {
									PBMethods.sendPBChat(Strings.Prefix + Strings.OneMinuteRemaining);
								}
								if (currentNumber == 0) {
									PBMethods.sendPBChat(Strings.RoundComplete);
									PBMethods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(clockTask);
									PBMethods.restoreArmor();
								}

							}
						}, 0L, 1L);

						if (PBMethods.WGSupportEnabled) {
							if (PBMethods.getWorldGuard() != null) {
								
								for (Player player: Bukkit.getOnlinePlayers()) {
									String playerTeam = PBMethods.getPlayerTeam(uuid);

									if (playerTeam != null) {
										if (playerTeam.equalsIgnoreCase(team1)) {
											PBMethods.allowedZone.put(player.getName(), PBMethods.t1z1);
										}
										if (playerTeam.equalsIgnoreCase(team2)) {
											PBMethods.allowedZone.put(player.getName(), PBMethods.t2z1);
										}
									}
								}
							}
						}
						teamChallenges.get(PBMethods.getPlayerTeam(uuid)).remove(teamName);
						return true;
					}
					Set<String> teams = PBMethods.getTeams();
					String teamName = null;
					for (String team: teams) {
						if (team.equalsIgnoreCase(args[1])) {
							teamName = team;
						}
					}
					
					if (teamName == null) {
						s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
						return true;
					}

					UUID uuid = ((Player) s).getUniqueId();
					if (PBMethods.getPlayerTeam(uuid) == null) {
						s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
						return true;
					}

					Player otherOwner = Bukkit.getPlayer(PBMethods.getOwner(teamName));

					if (otherOwner == null) {
						s.sendMessage(Strings.Prefix + Strings.OwnerNotOnline);
						return true;
					}

					int yourTeamSize = PBMethods.getOnlineTeamSize(PBMethods.getPlayerTeam(uuid));
					int opponentTeamSize = PBMethods.getOnlineTeamSize(teamName);
					int minTeamSize = plugin.getConfig().getInt("TeamSettings.MinTeamSize");
					int maxTeamSize = plugin.getConfig().getInt("TeamSettings.MaxTeamSize");

					if (yourTeamSize < minTeamSize || opponentTeamSize < minTeamSize || yourTeamSize > maxTeamSize || opponentTeamSize > maxTeamSize || opponentTeamSize != yourTeamSize) {
						s.sendMessage(Strings.Prefix + Strings.InvalidTeamSize);
						return true;
					}

					if (!teamChallenges.containsKey(teamName)) {
						teamChallenges.put(teamName, new LinkedList<String>());
					}

					teamChallenges.get(teamName).add(PBMethods.getPlayerTeam(uuid));
					s.sendMessage(Strings.Prefix + Strings.ChallengeSent.replace("%team", teamName));
					for (Player player: Bukkit.getOnlinePlayers()) {
						if (PBMethods.getPlayerTeam(player.getUniqueId()) != null) {
							if (PBMethods.getPlayerTeam(player.getUniqueId()).equalsIgnoreCase(teamName)) {
								if (PBMethods.isPlayerOwner(player.getUniqueId(), teamName)) {
									player.sendMessage(Strings.Prefix + Strings.ChallengeReceived.replace("%team", PBMethods.getPlayerTeam(uuid)));
								}
							}
						}
					}

					return true;

				}
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (!s.hasPermission("probending.setspawn")) {
						s.sendMessage(Strings.Prefix + Strings.noPermission);
						return true;
					}

					if (args.length != 2) {
						s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb setspawn [TeamOne|TeamTwo|Spectator]");
						return true;
					}

					if (!args[1].equalsIgnoreCase("teamone") && !args[1].equalsIgnoreCase("teamtwo") && !args[1].equalsIgnoreCase("spectator")) {
						s.sendMessage(Strings.Prefix + "§cProper Usage:  §3/pb setspawn [TeamOne|TeamTwo|Spectator]");
						return true;
					}
					if (args[1].equalsIgnoreCase("spectator")) {
						PBMethods.setSpectatorSpawn(((Player) s).getLocation());
						return true;
					}
					if (args[1].equalsIgnoreCase("teamone")) {
						PBMethods.setTeamOneSpawn(((Player) s).getLocation());
						s.sendMessage(Strings.Prefix + Strings.TeamSpawnSet.replace("%team", "TeamOne"));
						return true;
					}
					if (args[1].equalsIgnoreCase("teamtwo")) {
						PBMethods.setTeamTwoSpawn(((Player) s).getLocation());
						s.sendMessage(Strings.Prefix + Strings.TeamSpawnSet.replace("%team", "TeamTwo"));
						return true;
					}

				}
				if (args[0].equalsIgnoreCase("round")) {
					if (args.length == 1) {
						s.sendMessage("-----§6Probending Round Commands§f-----");
						if (s.hasPermission("probending.round.start")) {
							s.sendMessage("§3/pb round start [Team1] [Team2]§f - Starts Round.");
						}
						if (s.hasPermission("probending.round.stop")) {
							s.sendMessage("§3/pb round stop§f - Stops Round.");
						}
						if (s.hasPermission("probending.round.pause")) {
							s.sendMessage("§3/pb round pause§f - Pauses Round.");
						}
						if (s.hasPermission("probending.round.resume")) {
							s.sendMessage("§3/pb round resume§f - Round Resumed.");
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("resume")) {
						if (args[1].equalsIgnoreCase("resume")) {
							if (!s.hasPermission("probending.round.resume")) {
								s.sendMessage(Strings.Prefix + Strings.noPermission);
								return true;
							}

							if (args.length != 2) {
								s.sendMessage("§cProper Usage: §3/pb round resume");
								return true;
							}
							if (!PBMethods.matchStarted) {
								s.sendMessage(Strings.Prefix + Strings.NoOngoingRound);
								return true;
							}
							if (!PBMethods.matchPaused) {
								s.sendMessage(Strings.Prefix + Strings.RoundNotPaused);
								return true;
							}
							PBMethods.matchPaused = false;
							PBMethods.sendPBChat(Strings.RoundResumed.replace("%seconds", String.valueOf(currentNumber / 20)));

							clockTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
								public void run() {
									currentNumber--;
									if (currentNumber == 1200) {
										PBMethods.sendPBChat(Strings.OneMinuteRemaining);
									}
									if (currentNumber == 0) {
										PBMethods.sendPBChat(Strings.RoundComplete);
										PBMethods.matchStarted = false;
										Bukkit.getServer().getScheduler().cancelTask(clockTask);
										PBMethods.restoreArmor();
									}
								}
							}, 0L, 1L);
						}
					}
					if (args[1].equalsIgnoreCase("pause")) {
						if (!s.hasPermission("probending.round.pause")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 2) {
							s.sendMessage("§cProper Usage: §3/pb round pause");
							return true;
						}
						if (!PBMethods.matchStarted) {
							s.sendMessage(Strings.Prefix + Strings.NoOngoingRound);
							return true;
						}
						Bukkit.getServer().getScheduler().cancelTask(clockTask);
						PBMethods.matchPaused = true;
						PBMethods.sendPBChat(Strings.RoundPaused.replace("%seconds", String.valueOf(currentNumber / 20)));
					}
					if (args[1].equalsIgnoreCase("stop")) {
						// Permissions
						if (!s.hasPermission("probending.round.stop")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}

						if (args.length != 2) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb round stop");
							return true;
						}

						if (!PBMethods.matchStarted) {
							s.sendMessage(Strings.Prefix + Strings.NoOngoingRound);
							return true;
						}
						PBMethods.restoreArmor();

						Bukkit.getServer().getScheduler().cancelTask(clockTask);

						PBMethods.matchPaused = false;
						PBMethods.playingTeams.clear();
						PBMethods.matchStarted = false;
						PBMethods.sendPBChat(Strings.RoundStopped);
						return true;
					}
					if (args[1].equalsIgnoreCase("start")) {
						// Permissions check.
						if (!s.hasPermission("probending.round.start")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}

						// Makes sure the command has enough arguments.
						if (args.length != 4) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb round start [Team1] [Team2]");
							return true;
						}

						// Just so we dont start another match if one is already going.
						if (PBMethods.matchStarted) {
							s.sendMessage(Strings.Prefix + Strings.RoundAlreadyGoing);
							return true;
						}

						String team1 = args[2]; // Team 1
						String team2 = args[3]; // Team 2

						// Checks to make sure both teams exist.
						if (!PBMethods.teamExists(team1) || !PBMethods.teamExists(team2)) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}

						int minSize = plugin.getConfig().getInt("TeamSettings.MinTeamSize");

						// Checks to make sure the team has enough players.
						if (PBMethods.getOnlineTeamSize(team1) < minSize || PBMethods.getOnlineTeamSize(team2) < minSize) {
							s.sendMessage(Strings.Prefix + Strings.InvalidTeamSize);
							return true;
						}
						// Add players to list of playing teams and send a message confirming it.
						PBMethods.playingTeams.add(team1.toLowerCase());
						PBMethods.playingTeams.add(team2.toLowerCase());
						PBMethods.TeamOne = team1.toLowerCase();
						PBMethods.TeamTwo = team2.toLowerCase();

						for (Player player: Bukkit.getOnlinePlayers()) {
							String playerTeam = PBMethods.getPlayerTeam(player.getUniqueId());
							Color teamColor = null;
							if (playerTeam != null) {
								if (playerTeam.equalsIgnoreCase(team1)) teamColor = PBMethods.getColorFromString(plugin.getConfig().getString("TeamSettings.TeamOneColor"));
								if (playerTeam.equalsIgnoreCase(team2)) teamColor = PBMethods.getColorFromString(plugin.getConfig().getString("TeamSettings.TeamTwoColor"));
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne)) {
									PBMethods.teamOnePlayers.add(player.getName());
									player.teleport(PBMethods.getTeamOneSpawn());
								}
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
									player.teleport(PBMethods.getTeamTwoSpawn());
									PBMethods.teamTwoPlayers.add(player.getName());
								}
								if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
									tmpArmor.put(player, player.getInventory().getArmorContents()); // Backs up their armor.
									ItemStack armor1 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_HELMET), teamColor);
									ItemStack armor2 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), teamColor);
									ItemStack armor3 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), teamColor);
									ItemStack armor4 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), teamColor);
									player.getInventory().setHelmet(armor1);
									player.getInventory().setChestplate(armor2);
									player.getInventory().setLeggings(armor3);
									player.getInventory().setBoots(armor4);
								} else {
									if (PBMethods.RegionsAtLocation(player.getLocation()) != null && PBMethods.RegionsAtLocation(player.getLocation()).contains(PBMethods.ProbendingField)) {
										player.teleport(PBMethods.getSpectatorSpawn());
									}
								}
							}
						}


						int roundTime = plugin.getConfig().getInt("RoundSettings.Time");
						currentNumber = roundTime * 20;
						startingNumber = roundTime * 20;

						clockTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
							public void run() {
								PBMethods.matchStarted = true;
								currentNumber--;

								if (currentNumber == startingNumber - 1) {
									PBMethods.sendPBChat(Strings.RoundStarted.replace("%seconds", String.valueOf(startingNumber / 20)).replace("%team1", PBMethods.TeamOne).replace("%team2", PBMethods.TeamTwo));
								}
								if (currentNumber == 1200) {
									PBMethods.sendPBChat(Strings.Prefix + Strings.OneMinuteRemaining);
								}
								if (currentNumber == 0) {
									PBMethods.sendPBChat(Strings.RoundComplete);
									PBMethods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(clockTask);
									PBMethods.restoreArmor();
								}

							}
						}, 0L, 1L);

						if (PBMethods.WGSupportEnabled) {
							if (PBMethods.getWorldGuard() != null) {
								for (Player player: Bukkit.getOnlinePlayers()) {
									String teamName = PBMethods.getPlayerTeam(player.getUniqueId());
									if (teamName != null) {
										if (teamName.equalsIgnoreCase(team1)) {
											PBMethods.allowedZone.put(player.getName(), PBMethods.t1z1);
										}
										if (teamName.equalsIgnoreCase(team2)) {
											PBMethods.allowedZone.put(player.getName(), PBMethods.t2z1);
										}
									}
								}
							}
						}
					}
				}

				if (args[0].equalsIgnoreCase("chat")) {
					if (!s.hasPermission("probending.chat")) {
						s.sendMessage(Strings.Prefix + Strings.noPermission);
						return true;
					}
					if (args.length > 1) {
						s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb chat");
						return true;
					}
					Player p = (Player) s;
					if (!pbChat.contains(p)) {
						pbChat.add(p);
						s.sendMessage(Strings.Prefix + Strings.ChatEnabled);
						return true;
					}
					if (pbChat.contains(p)) {
						pbChat.remove(p);
						s.sendMessage(Strings.Prefix + Strings.ChatDisabled);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (!s.hasPermission("probending.reload")) {
						s.sendMessage(Strings.Prefix + Strings.noPermission);
						return true;
					}
					plugin.reloadConfig();
					s.sendMessage(Strings.Prefix + "§cVersion: " + plugin.getDescription().getVersion());
					s.sendMessage(Strings.Prefix + Strings.configReloaded);
				}
				if (args[0].equalsIgnoreCase("team")) {
					if (args.length == 1) {
						s.sendMessage("-----§6Probending Team Commands§f-----");
						if (s.hasPermission("probending.team.create")) {
							s.sendMessage("§3/pb team create [Name]§f - Create a team."); // Done
						}
						if (s.hasPermission("probending.team.rename")) {
							s.sendMessage("§3/pb team rename [Name]§f - Rename a team.");
						}
						if (s.hasPermission("probending.team.invite")) {
							s.sendMessage("§3/pb team invite [Player]§f - Invite a player to a team."); // Done
						}
						if (s.hasPermission("probending.team.info")) {
							s.sendMessage("§3/pb team info <Name>§f - View info on a team."); // Done
						}
						if (s.hasPermission("probending.team.join")) {
							s.sendMessage("§3/pb team join <Name>§f - Join a team."); // Done
						}
						if (s.hasPermission("probending.team.kick")) {
							s.sendMessage("§3/pb team kick <Name>§f - Kick a player from your team."); // Done
						}
						if (s.hasPermission("probending.team.quit")) {
							s.sendMessage("§3/pb team quit §f- Quit your current team."); // Done
						}
						if (s.hasPermission("probending.team.disband")) {
							s.sendMessage("§3/pb team disband §f- Disband your team."); // Done
						}
						if (s.hasPermission("probending.team.list")) {
							s.sendMessage("§3/pb team list§f - List all teams.");
						} if (s.hasPermission("probending.team.addwin")) {
							s.sendMessage("§3/pb team addwin [Team]§f - Adds a win to a team.");
						} if (s.hasPermission("probending.team.addloss")) {
							s.sendMessage("§3/pb team addloss [Team]§f - Adds a loss to a team.");
						} else {
							s.sendMessage(Strings.Prefix + Strings.NoTeamPermissions);
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("addwin")) {
						if (!s.hasPermission("probending.team.addwin")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team addwin [Team]");
							return true;
						}
						String teamName = args[2];
						Set<String> teams = PBMethods.getTeams();
						if (!PBMethods.teamExists(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}
						if (teams != null) {
							for (String team: teams) {
								if (team.equalsIgnoreCase(teamName)) {
									PBMethods.addWin(team);
									s.sendMessage(Strings.Prefix + Strings.WinAddedToTeam.replace("%team", team));
								}
							}
						}
						return true;	
					}
					if (args[1].equalsIgnoreCase("addloss")) {
						if (!s.hasPermission("probending.team.addloss")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 3) {
							s.sendMessage("§cProper Usage: §3/pb team addloss [Team]");
							return true;
						}
						String teamName = args[2];
						Set<String> teams = PBMethods.getTeams();
						if (!PBMethods.teamExists(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}
						if (teams != null) {
							for (String team: teams) {
								if (team.equalsIgnoreCase(teamName)) {
									PBMethods.addLoss(team);
									s.sendMessage(Strings.Prefix + Strings.LossAddedToTeam.replace("%team", team));
								}
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("rename")) {
						if (!s.hasPermission("probending.team.rename")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						UUID uuid = ((Player) s).getUniqueId();

						String teamName = PBMethods.getPlayerTeam(uuid);
						if (!PBMethods.playerInTeam(uuid)) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
							return true;
						}
						if (!PBMethods.isPlayerOwner(uuid, teamName)) {
							s.sendMessage(Strings.Prefix + Strings.NotOwnerOfTeam);
							return true;
						}
						if (args.length < 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage §3/pb team rename [Name]");
							return true;
						}
						boolean econEnabled = plugin.getConfig().getBoolean("Economy.Enabled");

						String newName = args[2];
						if (newName.length() > 15) {
							s.sendMessage(Strings.Prefix + Strings.NameTooLong);
							return true;
						}
						if (newName.equalsIgnoreCase(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.TeamAlreadyNamedThat.replace("%newname", teamName));
							return true;
						}
						if (econEnabled) {
							Double playerBalance = Probending.econ.getBalance(s.getName());
							Double renameFee = plugin.getConfig().getDouble("Economy.TeamRenameFee");
							String serverAccount = plugin.getConfig().getString("Economy.ServerAccount");
							String currency = Probending.econ.currencyNamePlural();
							if (playerBalance < renameFee) {
								s.sendMessage(Strings.Prefix + Strings.NotEnoughMoney.replace("%amount", renameFee.toString()).replace("%currency", currency));
								return true;
							}
							Probending.econ.withdrawPlayer(s.getName(), renameFee);
							Probending.econ.depositPlayer(serverAccount, renameFee);
							s.sendMessage(Strings.Prefix + Strings.MoneyWithdrawn.replace("%amount", renameFee.toString()).replace("%currency", currency));
						}

						int Wins = PBMethods.getWins(teamName);
						int Losses = PBMethods.getLosses(teamName);

						PBMethods.createTeam(newName, uuid);
						
						OfflinePlayer airbender = null;
						OfflinePlayer waterbender = null;
						OfflinePlayer earthbender = null;
						OfflinePlayer firebender = null;
						OfflinePlayer chiblocker = null;
						
						if (PBMethods.getTeamAirbender(teamName) != null) {
							airbender = Bukkit.getOfflinePlayer(PBMethods.getTeamAirbender(teamName));
						}
						
						if (PBMethods.getTeamWaterbender(teamName) != null) {
							waterbender = Bukkit.getOfflinePlayer(PBMethods.getTeamWaterbender(teamName));
						}
						
						if (PBMethods.getTeamEarthbender(teamName) != null) {
							earthbender = Bukkit.getOfflinePlayer(PBMethods.getTeamEarthbender(teamName));
						}
						
						if (PBMethods.getTeamFirebender(teamName) != null) {
							firebender = Bukkit.getOfflinePlayer(PBMethods.getTeamFirebender(teamName));
						}
						
						if (PBMethods.getTeamChiblocker(teamName) != null) {
							chiblocker = Bukkit.getOfflinePlayer(PBMethods.getTeamChiblocker(teamName));
						}
						
						if (airbender != null) {
							PBMethods.removePlayerFromTeam(teamName, airbender.getUniqueId(), "Air");
							PBMethods.addPlayerToTeam(newName, airbender.getUniqueId(), "Air");
						}
						if (waterbender != null) {
							PBMethods.removePlayerFromTeam(teamName, waterbender.getUniqueId(), "Water");
							PBMethods.addPlayerToTeam(newName, waterbender.getUniqueId(), "Water");
						}
						if (earthbender != null) {
							PBMethods.removePlayerFromTeam(teamName, earthbender.getUniqueId(), "Earth");
							PBMethods.addPlayerToTeam(newName, earthbender.getUniqueId(), "Earth");
						}
						if (firebender != null) {
							PBMethods.removePlayerFromTeam(teamName, firebender.getUniqueId(), "Fire");
							PBMethods.addPlayerToTeam(newName, firebender.getUniqueId(), "Fire");
						}
						if (chiblocker != null) {
							PBMethods.removePlayerFromTeam(teamName, chiblocker.getUniqueId(), "Chi");
							PBMethods.addPlayerToTeam(newName, chiblocker.getUniqueId(), "Chi");
						}

						PBMethods.setLosses(Losses, newName);
						PBMethods.setWins(Wins, newName);

						s.sendMessage(Strings.Prefix + Strings.TeamRenamed.replace("%newname", newName));
						PBMethods.setOwner(uuid, newName);
						PBMethods.deleteTeam(teamName);
						plugin.saveConfig();
						return true;
					}
					if (args[1].equalsIgnoreCase("list")) {
						if (!s.hasPermission("probending.team.list")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						Set<String> teams = PBMethods.getTeams();
						s.sendMessage("§cTeams: §a" + teams.toString());
						return true;
					}
					if (args[1].equalsIgnoreCase("disband")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (!s.hasPermission("probending.team.disband")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length > 3 || args.length < 2) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team disband [Team]");
							return true;
						}
						String teamName = null;
						if (args.length == 3) {
							if (!s.hasPermission("probending.team.disband.other")) {
								s.sendMessage(Strings.Prefix + Strings.noPermission);
								return true;
							}
							teamName = args[2];
						} else {
							teamName = PBMethods.getPlayerTeam(uuid);
						}
						
						if (teamName == null) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}
						if (!PBMethods.isPlayerOwner(uuid, teamName)) {
							s.sendMessage(Strings.Prefix + Strings.NotOwnerOfTeam);
							return true;
						}
						for (Player player: Bukkit.getOnlinePlayers()) {
							if (PBMethods.getPlayerTeam(player.getUniqueId()) == null) continue;
							if (PBMethods.getPlayerTeam(player.getUniqueId()).equals(teamName)) {
								s.sendMessage(Strings.Prefix + Strings.TeamDisbanded.replace("%team", teamName));
							}
						}
						String playerElement = PBMethods.getPlayerElementAsString(uuid);

						PBMethods.removePlayerFromTeam(teamName, uuid, playerElement);
						Set<String> teamelements = PBMethods.getTeamElements(teamName);
						if (teamelements != null) {
							if (teamelements.contains("Air")) {
								PBMethods.removePlayerFromTeam(teamName, UUID.fromString(PBMethods.getTeamAirbender(teamName)), "Air");
							}
							if (teamelements.contains("Water")) {
								PBMethods.removePlayerFromTeam(teamName, UUID.fromString(PBMethods.getTeamWaterbender(teamName)), "Water");
							}
							if (teamelements.contains("Earth")) {
								PBMethods.removePlayerFromTeam(teamName, UUID.fromString(PBMethods.getTeamEarthbender(teamName)), "Earth");
							}
							if (teamelements.contains("Fire")) {
								PBMethods.removePlayerFromTeam(teamName, UUID.fromString(PBMethods.getTeamFirebender(teamName)), "Fire");
							}
							if (teamelements.contains("Chi")) {
								PBMethods.removePlayerFromTeam(teamName, UUID.fromString(PBMethods.getTeamChiblocker(teamName)), "Chi");
							}
						}
						PBMethods.deleteTeam(teamName);
						return true;
					}
					if (args[1].equalsIgnoreCase("quit")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (!s.hasPermission("probending.team.quit")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 2) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team quit");
							return true;
						}

						String teamName = PBMethods.getPlayerTeam(uuid);
						if (teamName == null) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
							return true;
						}
						if (PBMethods.isPlayerOwner(uuid, teamName)) {
							s.sendMessage(Strings.Prefix + Strings.CantBootFromOwnTeam);
							return true;
						}
						String playerElement = PBMethods.getPlayerElementAsString(uuid);

						PBMethods.removePlayerFromTeam(teamName, uuid, playerElement);
						s.sendMessage(Strings.Prefix + Strings.YouHaveQuit.replace("%team", teamName));
						for (Player player: Bukkit.getOnlinePlayers()) {
							if (PBMethods.getPlayerTeam(player.getUniqueId()) == null) continue;
							if (PBMethods.getPlayerTeam(player.getUniqueId()).equals(teamName)) {
								s.sendMessage(Strings.Prefix + Strings.PlayerHasQuit.replace("%team", teamName).replace("%player", s.getName()));
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("kick")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (!s.hasPermission("probending.team.kick")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						if (args.length != 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team kick <Name>");
							return true;
						}
						String teamName = PBMethods.getPlayerTeam(uuid);
						if (teamName == null) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
							return true;
						}
						if (!PBMethods.isPlayerOwner(uuid, teamName)) {
							s.sendMessage(Strings.Prefix + Strings.NotOwnerOfTeam);
							return true;
						}
						String playerName = args[2];
						if (playerName.equals(s.getName())) {
							s.sendMessage(Strings.Prefix + Strings.CantBootFromOwnTeam);
							return true;
						}
						OfflinePlayer p3 = Bukkit.getOfflinePlayer(args[2]);
						String playerTeam = null;
						String playerElement = null;

						if (p3 != null) {
							playerElement = PBMethods.getPlayerElementInTeam(p3.getUniqueId(), teamName);
							playerTeam = PBMethods.getPlayerTeam(p3.getUniqueId());
						}

						if (playerTeam == null) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotOnThisTeam);
							return true;
						}
						if (!playerTeam.equals(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotOnThisTeam);
							return true;
						}
						PBMethods.removePlayerFromTeam(teamName, p3.getUniqueId(), playerElement);
						Player player = Bukkit.getPlayer(playerName);
						if (player != null) {
							player.sendMessage(Strings.Prefix + Strings.YouHaveBeenBooted.replace("%team", teamName));
						}
						for (Player player2: Bukkit.getOnlinePlayers()) {
							if (PBMethods.getPlayerTeam(player2.getUniqueId()) == null) continue;
							if (PBMethods.getPlayerTeam(player2.getUniqueId()).equals(teamName)) {
								player2.sendMessage(Strings.Prefix + Strings.PlayerHasBeenBooted.replace("%player", playerName).replace("%team", teamName));
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("join")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (!s.hasPermission("probending.team.join")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}

						if (args.length != 3) {
							s.sendMessage("§cProper Usage: §3/pb team join [TeamName]");
							return true;
						}
						if (PBMethods.playerInTeam(uuid)) {
							s.sendMessage(Strings.Prefix + Strings.PlayerAlreadyInTeam);
							return true;
						}
						String teamName = args[2];
						if (teamInvites.get(s.getName()) == null) {
							s.sendMessage(Strings.Prefix + Strings.NoInviteFromTeam);
							return true;
						}
						if (!teamInvites.get(s.getName()).contains(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.NoInviteFromTeam);
							return true;
						}
						String playerElement = PBMethods.getPlayerElementAsString(uuid);

						if (playerElement == null) {
							s.sendMessage(Strings.Prefix + Strings.noBendingType);
							return true;
						}
						Set<String> teamelements = PBMethods.getTeamElements(teamName);
						if (teamelements != null) {
							if (teamelements.contains(playerElement)) {
								s.sendMessage(Strings.Prefix + Strings.TeamAlreadyHasElement);
								return true;
							}
							if (!plugin.getConfig().getBoolean("TeamSettings.Allow" + playerElement)) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", playerElement));
								return true;
							}
							PBMethods.addPlayerToTeam(teamName, uuid, playerElement);
							for (Player player: Bukkit.getOnlinePlayers()) {
								String teamName2 = PBMethods.getPlayerTeam(player.getUniqueId());
								if (teamName2 != null) {
									if (PBMethods.getPlayerTeam(player.getUniqueId()).equalsIgnoreCase(teamName)) {
										player.sendMessage(Strings.Prefix + Strings.PlayerJoinedTeam.replace("%player", s.getName()).replace("%team", teamName));
									}
								}
							}
						}
						teamInvites.remove(s.getName());
						return true;
					}
					if (args[1].equalsIgnoreCase("info")) {
						if (!s.hasPermission("probending.team.info")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						UUID uuid = ((Player) s).getUniqueId();
						String teamName = null;
						if (args.length == 2) {
							teamName = PBMethods.getPlayerTeam(uuid);
						}
						if (args.length == 3) {
							teamName = args[2];
						}

						if (!PBMethods.teamExists(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.TeamDoesNotExist);
							return true;
						}

						Set<String> teams = PBMethods.getTeams();
						for (String team: teams) {
							if (team.equalsIgnoreCase(teamName)) {
								teamName = team;
							}
						}
						String teamOwner = PBMethods.getOwner(teamName);
						s.sendMessage("§3Team Name:§e " + teamName);
						s.sendMessage("§3Team Owner:§5 " + teamOwner);

						String air = PBMethods.getTeamAirbender(teamName);
						String water = PBMethods.getTeamWaterbender(teamName);
						String earth = PBMethods.getTeamEarthbender(teamName);
						String fire = PBMethods.getTeamFirebender(teamName);
						String chi = PBMethods.getTeamChiblocker(teamName);

						int wins = PBMethods.getWins(teamName);
						int losses = PBMethods.getLosses(teamName);

						if (PBMethods.getAirAllowed()) {
							if (air != null) {
								s.sendMessage("§3Airbender: §7" + air);
							}
						}
						if (PBMethods.getWaterAllowed()) {
							if (water != null) {
								s.sendMessage("§3Waterbender: §b" + water);
							}
						}
						if (PBMethods.getEarthAllowed()) {
							if (earth != null) {
								s.sendMessage("§3Earthbender: §a" + earth);
							}
						}
						if (PBMethods.getFireAllowed()) {
							if (fire != null) {
								s.sendMessage("§3Firebender: §c" + fire);
							}
						}
						if (PBMethods.getChiAllowed()) {
							if (chi != null) {
								s.sendMessage("§3Chiblocker: §6" + chi);
							}
						}
						s.sendMessage("§3Wins: §e" + wins);
						s.sendMessage("§3Losses: §e" + losses);

					}
					if (args[1].equalsIgnoreCase("invite")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (args.length != 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team invite [Name]");
							return true;
						}
						if (!s.hasPermission("probending.team.invite")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}

						if (!PBMethods.playerInTeam(uuid)) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotInTeam);
							return true;
						}

						String playerTeam = PBMethods.getPlayerTeam(uuid);
						if (!PBMethods.isPlayerOwner(uuid, playerTeam)) {
							s.sendMessage(Strings.Prefix + Strings.NotOwnerOfTeam);
							return true;
						}

						Player player = Bukkit.getPlayer(args[2]);

						if (player == null) {
							s.sendMessage(Strings.Prefix + Strings.PlayerNotOnline);
							return true;
						}

						if (PBMethods.playerInTeam(player.getUniqueId())) {
							s.sendMessage(Strings.Prefix + Strings.PlayerAlreadyInTeam);
							return true;
						}

						if (!teamInvites.containsKey(player.getName())) {
							teamInvites.put(player.getName(), new LinkedList<String>());
						}

						int maxSize = plugin.getConfig().getInt("TeamSettings.MaxTeamSize");
						if (PBMethods.getTeamSize(playerTeam) >= maxSize) {
							s.sendMessage(Strings.Prefix + Strings.MaxSizeReached);
							return true;
						}
						String playerElement = PBMethods.getPlayerElementAsString(player.getUniqueId());

						if (playerElement == null) {
							s.sendMessage(Strings.Prefix + Strings.noBendingType);
							return true;
						}
						if (!PBMethods.getAirAllowed()) {
							if (playerElement.equals("Air")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Airbenders"));
								return true;
							}
						}
						if (!PBMethods.getWaterAllowed()) {
							if (playerElement.equals("Water")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Waterbenders"));
								return true;
							}
						}
						if (!PBMethods.getEarthAllowed()) {
							if (playerElement.equals("Earth")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Earthbenders"));
								return true;
							}
						}
						if (!PBMethods.getFireAllowed()) {
							if (playerElement.equals("Fire")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Firebenders"));
								return true;
							}
						}
						if (!PBMethods.getChiAllowed()) {
							if (playerElement.equals("Chi")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Chiblockers"));
								return true;
							}
						}
						Set<String> teamelements = PBMethods.getTeamElements(playerTeam);
						if (teamelements != null) {
							if (teamelements.contains(playerElement)) {
								s.sendMessage(Strings.Prefix + Strings.TeamAlreadyHasElement);
								return true;
							}
						}

						teamInvites.get(player.getName()).add(playerTeam);
						s.sendMessage(Strings.Prefix + Strings.PlayerInviteSent.replace("%team", playerTeam).replace("%player", player.getName()));
						player.sendMessage(Strings.Prefix + Strings.PlayerInviteReceived.replace("%team", playerTeam).replace("%player", player.getName()));
						player.sendMessage(Strings.Prefix + Strings.InviteInstructions.replace("%team", playerTeam).replace("%player", player.getName()));
						return true;
					}
					if (args[1].equalsIgnoreCase("create")) {
						UUID uuid = ((Player) s).getUniqueId();
						if (args.length != 3) {
							s.sendMessage(Strings.Prefix + "§cProper Usage: §3/pb team create [Name]");
							return true;
						}
						if (!s.hasPermission("probending.team.create")) {
							s.sendMessage(Strings.Prefix + Strings.noPermission);
							return true;
						}
						String teamName = args[2];
						if (PBMethods.teamExists(teamName)) {
							s.sendMessage(Strings.Prefix + Strings.teamAlreadyExists);
							return true;
						}

						if (teamName.length() > 15) {
							s.sendMessage(Strings.Prefix + Strings.NameTooLong);
							return true;
						}

						if (GeneralMethods.getBendingPlayer(uuid).getElements().size() == 0) {
							s.sendMessage(Strings.Prefix + Strings.noBendingType);
							return true;
						}

						if (PBMethods.playerInTeam(uuid)) {
							s.sendMessage(Strings.Prefix + Strings.PlayerAlreadyInTeam);
							return true;
						}
						Double creationCost = plugin.getConfig().getDouble("Economy.TeamCreationFee");
						String serverAccount = plugin.getConfig().getString("Economy.ServerAccount");
						boolean econEnabled = plugin.getConfig().getBoolean("Economy.Enabled");

						String playerElement = PBMethods.getPlayerElementAsString(uuid);

						if (!PBMethods.getAirAllowed()) {
							if (playerElement.equals("Air")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Airbenders"));
								return true;
							}
						}
						if (!PBMethods.getWaterAllowed()) {
							if (playerElement.equals("Water")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Waterbenders"));
								return true;
							}
						}
						if (!PBMethods.getEarthAllowed()) {
							if (playerElement.equals("Earth")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Earthbenders"));
								return true;
							}
						}
						if (!PBMethods.getFireAllowed()) {
							if (playerElement.equals("Fire")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Firebenders"));
								return true;
							}
						}
						if (!PBMethods.getChiAllowed()) {
							if (playerElement.equals("Chi")) {
								s.sendMessage(Strings.Prefix + Strings.ElementNotAllowed.replace("%element", "Chiblockers"));
								return true;
							}
						}

						if (econEnabled) {
							String currencyName = Probending.econ.currencyNamePlural();
							Double playerBalance = Probending.econ.getBalance(s.getName());
							if (playerBalance < creationCost) {
								s.sendMessage(Strings.Prefix + Strings.NotEnoughMoney.replace("%currency", currencyName));
								return true;
							}
							Probending.econ.withdrawPlayer(s.getName(), creationCost);
							Probending.econ.depositPlayer(serverAccount, creationCost);
							s.sendMessage(Strings.Prefix + Strings.MoneyWithdrawn.replace("%amount", creationCost.toString()).replace("%currency", currencyName));
						}


						PBMethods.createTeam(teamName, uuid);
						PBMethods.addPlayerToTeam(teamName, uuid, playerElement);
						s.sendMessage(Strings.Prefix + Strings.TeamCreated.replace("%team", teamName));
						return true;
					}
				}
				return true;
			}
		}; probending.setExecutor(exe);
	}
}