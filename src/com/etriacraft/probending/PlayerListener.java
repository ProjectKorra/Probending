package com.etriacraft.probending;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import tools.BendingType;
import tools.Tools;

public class PlayerListener implements Listener {

	Probending plugin;

	public PlayerListener(Probending plugin) {
		this.plugin = plugin;
	}

	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();

		if (Methods.allowedZone.containsKey(p.getName())) {
			if (Methods.matchStarted && Methods.getWorldGuard() != null && Methods.AutomateMatches) {
				String teamSide = null;
				
				if (Methods.getPlayerTeam(p.getName()) != null) {
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamOne)) teamSide = Methods.TeamOne;
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamTwo)) teamSide = Methods.TeamTwo; 
				}
				
				if (teamSide != null) { // Player is in a match.
					p.sendMessage(Strings.Prefix + Strings.CantTeleportDuringMatch);
					e.setCancelled(true);
				}
			}
		}
	}

	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Methods.allowedZone.containsKey(p.getName())) {
			if (Methods.matchStarted && Methods.getWorldGuard() != null && Methods.AutomateMatches) {
				String teamSide = null;
				if (Methods.getPlayerTeam(p.getName()) != null) {
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamOne)) teamSide = Methods.TeamOne;
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamTwo)) teamSide = Methods.TeamTwo;
				}
				
				if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
					Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
					Methods.allowedZone.remove(p.getName());
					p.getInventory().setArmorContents(null);
					p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
					Commands.tmpArmor.remove(p);
					if (Methods.teamOnePlayers.isEmpty()) {
						Methods.sendPBChat(Strings.RoundStopped);
						Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamTwo));
						Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
						Methods.matchStarted = false;
						Methods.playingTeams.clear();
						Methods.TeamOne = null;
						Methods.TeamTwo = null;
						Methods.allowedZone.clear();
						Methods.restoreArmor();
					}
				}
				if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
					Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
					Methods.allowedZone.remove(p.getName());
					p.getInventory().setArmorContents(null);
					p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
					Commands.tmpArmor.remove(p);
					if (Methods.teamTwoPlayers.isEmpty()) {
						Methods.sendPBChat(Strings.RoundStopped);
						Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamOne));
						Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
						Methods.matchStarted = false;
						Methods.playingTeams.clear();
						Methods.TeamOne = null;
						Methods.TeamTwo = null;
						Methods.allowedZone.clear();
						Methods.restoreArmor();
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Set<String> fromRegions = Methods.RegionsAtLocation(from);
		Set<String> toRegions = Methods.RegionsAtLocation(to);
		String teamSide = null;
		if (Methods.allowedZone.containsKey(p.getName())) { 
			
			if (Methods.matchStarted && Methods.getWorldGuard() != null && Methods.AutomateMatches) {
				if (Methods.getPlayerTeam(p.getName()) != null) {
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamOne)) teamSide = Methods.TeamOne;
					if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamTwo))teamSide = Methods.TeamTwo; 
				}

				if (Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z3)) {
					if (!toRegions.contains(Methods.t2z3)|| toRegions.isEmpty()) {
						if (fromRegions.contains(Methods.t2z3)) {
							Methods.allowedZone.remove(p.getName());
							Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
							p.getInventory().setArmorContents(null);
							p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
							Commands.tmpArmor.remove(p);
							Methods.teamTwoPlayers.remove(p.getName());
							if (Methods.teamTwoPlayers.isEmpty()) {
								Methods.sendPBChat(Strings.RoundStopped);
								Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamOne));
								Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
								Methods.matchStarted = false;
								Methods.playingTeams.clear();
								Methods.TeamOne = null;
								Methods.TeamTwo = null;
								Methods.allowedZone.clear();
								Methods.restoreArmor();
							}
						}
					}
					return;
				}
				if (Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z3)) {
					if (!toRegions.contains(Methods.t1z3)|| toRegions.isEmpty()) {
						if (fromRegions.contains(Methods.t1z3)) {
							Methods.allowedZone.remove(p.getName());
							Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
							p.getInventory().setArmorContents(null);
							p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
							Commands.tmpArmor.remove(p);
							Methods.teamOnePlayers.remove(p.getName());
							if (Methods.teamOnePlayers.isEmpty()) {
								Methods.sendPBChat(Strings.RoundStopped);
								Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamTwo));
								Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
								Methods.matchStarted = false;
								Methods.playingTeams.clear();
								Methods.TeamOne = null;
								Methods.TeamTwo = null;
								Methods.allowedZone.clear();
								Methods.restoreArmor();
							}
						}
					}
					return;                                                            
				}
				if (toRegions.contains(Methods.t2z3)) {
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z3)) {
						// Check Team Two Zone 2
						if (fromRegions.contains(Methods.t2z2) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z2)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t2z2)) {
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}

							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z3);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t2z2)) { 
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
					}
				}
				if (toRegions.contains(Methods.t1z3)) {
					if (!Methods.allowedZone.containsKey(p.getName())) return;
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z3)) {
						// Check Team One Zone 2
						if (fromRegions.contains(Methods.t1z2) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z2)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.put(p.getName(), Methods.t1z3);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t1z2)) {
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}

							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t1z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t1z2)) { 
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
					}
				}

				if (toRegions.contains(Methods.t1z2)) {
					if (!Methods.allowedZone.containsKey(p.getName())) return;
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z2)) {
						// Check Team One Zone One
						if (fromRegions.contains(Methods.t1z1) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z1)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.put(p.getName(), Methods.t1z2);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t1z1)) { 
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t1z1)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
						// Check Team One Zone Three
						if (fromRegions.contains(Methods.t1z3) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z3)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.remove(p.getName());
								Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
								p.getInventory().setArmorContents(null);
								p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
								Commands.tmpArmor.remove(p);
								Methods.teamOnePlayers.remove(p.getName());
								if (Methods.teamOnePlayers.isEmpty()) {
									Methods.sendPBChat(Strings.RoundStopped);
									Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamTwo));
									Methods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
									Methods.playingTeams.clear();
									Methods.TeamOne = null;
									Methods.TeamTwo = null;
									Methods.allowedZone.clear();
									Methods.restoreArmor();
								}
							}
						}

					}
				}
				if (toRegions.contains(Methods.t2z2)) {
					if (!Methods.allowedZone.containsKey(p.getName())) return;
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z2)) {
						// Check TeamTwoZoneOne
						if (fromRegions.contains(Methods.t2z1)&& Methods.allowedZone.get(p.getName()).equals(Methods.t2z1)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z2);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t2z1)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)){
								Methods.allowedZone.put(p.getName(), Methods.t1z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t2z1)) { 
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
						}
						// Check TeamTwoZoneThree
						if (fromRegions.contains(Methods.t2z3) && Methods.allowedZone.get(p.getName()).equals(Methods.t2z3)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.remove(p.getName());
								Methods.sendPBChat(Strings.PlayerEliminated.replace("%player", p.getName()));
								p.getInventory().setArmorContents(null);
								p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
								Commands.tmpArmor.remove(p);
								Methods.teamTwoPlayers.remove(p.getName());
								if (Methods.teamTwoPlayers.isEmpty()) {
									Methods.sendPBChat(Strings.RoundStopped);
									Methods.sendPBChat(Strings.TeamWon.replace("%team", Methods.TeamOne));
									Methods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
									Methods.playingTeams.clear();
									Methods.TeamOne = null;
									Methods.TeamTwo = null;
									Methods.allowedZone.clear();
									Methods.restoreArmor();
								}
							}
						}
					}
				}
				if (toRegions.contains(Methods.t2z1)) {
					if (!Methods.allowedZone.containsKey(p.getName())) return;
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z1)) {
						// Check Team Two Zone Two
						if (fromRegions.contains(Methods.t2z2) && Methods.allowedZone.get(p.getName()).equals(Methods.t2z2)) {
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z3); 
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t2z2)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t2z2)) {
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
						}
						// Check Team One Zone One
						if (fromRegions.contains(Methods.t1z1) && Methods.allowedZone.get(p.getName()).equals(Methods.t1z1)) { // They are coming from Team One Zone One
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
								Methods.allowedZone.put(p.getName(), Methods.t1z2);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t1z1)) {
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z1);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t1z1)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
					}
				}
				if (toRegions.contains(Methods.t1z1)) {
					if (!Methods.allowedZone.containsKey(p.getName())) return;
					if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z1)) {
						//Check Team One Zone Two
						if (fromRegions.contains(Methods.t1z2) && Methods.allowedZone.get(p.getName()).equals(Methods.t1z2)) { // They are coming from Team One's Second Zone.
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) { // They are on Team One
								Methods.allowedZone.put(p.getName(), Methods.t1z3); // They were fouled back to Zone 3.
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t1z2)) { 
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) { // They are on Team Two
								Methods.allowedZone.put(p.getName(), Methods.t1z1); // Send them back to Team One Zone 1.
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t1z2)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
						// Check Team Two Zone One
						if (fromRegions.contains(Methods.t2z1) && Methods.allowedZone.get(p.getName()).equals(Methods.t2z1)) { // They are coming from Team Two's First Zone.
							if (teamSide.equalsIgnoreCase(Methods.TeamOne)) { // Team One Player
								Methods.allowedZone.put(p.getName(),  Methods.t1z1); // Stick them to Zone One on Team One's Side.
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamOne, Methods.t2z1)) { 
									Methods.MovePlayersUp(Methods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
								Methods.allowedZone.put(p.getName(), Methods.t2z2);
								Methods.sendPBChat(Strings.PlayerFouled.replace("%player", p.getName()).replace("%zone", Methods.allowedZone.get(p.getName())));
								if (Methods.isZoneEmpty(Methods.TeamTwo, Methods.t2z1)) {
									Methods.MovePlayersUp(Methods.TeamOne, "One");
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = player.getLocation();
		if (Methods.WGSupportEnabled) {
			if (Methods.getWorldGuard() != null) {
				ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
				for (ProtectedRegion region: set) {
					if (region != null) {
						if (region.getId().equalsIgnoreCase(Methods.ProbendingField)) {
							if (Methods.buildDisabled) {
								if (!player.hasPermission("probending.worldguard.buildonfield")) {
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Entity entity = e.getWhoClicked();

		if (entity instanceof Player) {
			Player p = (Player) entity;

			if (Commands.tmpArmor.containsKey(p)) { // Don't let them throw away out armor if we're storing theirs!
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Location loc = player.getLocation();
		if (Methods.WGSupportEnabled) {
			if (Methods.getWorldGuard() != null) {
				ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
				for (ProtectedRegion region: set) {
					if (region != null) {
						if (region.getId().equalsIgnoreCase(Methods.ProbendingField)) {
							if (Methods.buildDisabled) {
								if (!player.hasPermission("probending.worldguard.buildonfield")) {
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location locTo = e.getTo();
		Location locFrom = e.getFrom();
		if (Methods.WGSupportEnabled) {
			if (Methods.getWorldGuard() != null) {
				ApplicableRegionSet set = WGBukkit.getRegionManager(locTo.getWorld()).getApplicableRegions(locTo);
				for (ProtectedRegion region: set) {
					if (region != null) {
						// Checks if the player can enter the field during a Probending Match
						if (region.getId().equalsIgnoreCase(Methods.ProbendingField)) {
							if (Methods.matchStarted) {
								String teamName = Methods.getPlayerTeam(player.getName());
								if (teamName != null) {
									if (!Methods.playingTeams.contains(teamName.toLowerCase())) {
										player.sendMessage(Strings.Prefix + Strings.CantEnterField);
										player.teleport(locFrom);
										e.setCancelled(true);
									}
								}
								if (teamName == null) {
									player.sendMessage(Strings.Prefix + Strings.CantEnterField);
									player.teleport(locFrom);
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent e) {
		if (Commands.pbChat.contains(e.getPlayer())) {
			e.getRecipients().clear();
			for (Player player: Bukkit.getOnlinePlayers()) {
				if (Commands.pbChat.contains(player)) {
					e.getRecipients().add(player);
				}
			}
			e.setFormat(Strings.Prefix + e.getFormat());
		}
	} 

	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!(Tools.getBendingTypes(player).size() > 1)) {
			String team = Methods.getPlayerTeam(player.getName());
			if (team != null) {
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
				if (Tools.isBender(player.getName(), BendingType.Fire)) {
					playerElement = "Fire";
				}
				if (Tools.isBender(player.getName(), BendingType.ChiBlocker)) {
					playerElement = "Chi";
				}
				String playerElementInTeam = Methods.getPlayerElementInTeam(player.getName(), team);
				if (playerElementInTeam != null) {
					if (!playerElementInTeam.equals(playerElement)) {
						player.sendMessage(Strings.Prefix + Strings.RemovedFromTeamBecauseDifferentElement);
						Methods.removePlayerFromTeam(team, player.getName(), playerElementInTeam);
						Set<String> teamElements = Methods.getTeamElements(team);
						if (teamElements.contains("Air")) {
							String airbender = Methods.getTeamAirbender(team);
							Methods.setOwner(airbender, team);
							return;
						}
						if (teamElements.contains("Water")) {
							String bender = Methods.getTeamWaterbender(team);
							Methods.setOwner(bender, team);
							return;
						}
						if (teamElements.contains("Earth")) {
							String bender = Methods.getTeamEarthbender(team);
							Methods.setOwner(bender, team);
							return;
						}
						if (teamElements.contains("Fire")) {
							String bender = Methods.getTeamFirebender(team);
							Methods.setOwner(bender, team);
							return;
						}
						if (teamElements.contains("Chi")) {
							String bender = Methods.getTeamChiblocker(team);
							Methods.setOwner(bender, team);
							return;
						} else {
							Methods.deleteTeam(team);
						}
					}
				}
			}
		}
	}
}