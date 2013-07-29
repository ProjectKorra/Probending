package com.etriacraft.probending;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

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

	public static String RemovedFromTeamBecauseDifferentElement;
	public static String SetTeamColor;

	// Match Stuff
	public static String CantEnterField;
	public static String PlayerEliminated;
	public static String PlayerFouled;

	public static String MatchEnded;
	public static String TeamWon;
	public static String MoveUpOneZone;


	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Set<String> fromRegions = Methods.RegionsAtLocation(from);
		Set<String> toRegions = Methods.RegionsAtLocation(to);
		String teamSide = null;
		
		if (Methods.matchStarted && Methods.getWorldGuard() != null && Methods.AutomateMatches) {
			if (Methods.getPlayerTeam(p.getName()) != null) {
				if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamOne)) teamSide = Methods.TeamOne;
				if (Methods.getPlayerTeam(p.getName()).equalsIgnoreCase(Methods.TeamTwo))teamSide = Methods.TeamTwo; 
			}
			
			if (!Methods.allowedZone.containsKey(p.getName())) return;

			
			if (Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z3)&& !toRegions.contains(Methods.t2z3)) {
				Methods.allowedZone.remove(p.getName());
				Methods.sendPBChat(PlayerEliminated.replace("%player", p.getName()));
				if (Methods.playersInZone(Methods.t2z3).size() == 0) {
					Methods.endMatch(Methods.TeamOne);
				}
				return;
			}
			if (Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z3) && !toRegions.contains(Methods.t1z3)) {
				Methods.allowedZone.remove(p.getName());
				Methods.sendPBChat(PlayerEliminated.replace("%player", p.getName()));
				if (Methods.playersInZone(Methods.t1z3).size() == 0) {
					Methods.endMatch(Methods.TeamTwo);
				}
				return;
			}
			if (toRegions.contains(Methods.t2z3)) {
				if (!Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z3)) {
					// Check Team Two Zone 2
					if (fromRegions.contains(Methods.t2z2) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t2z2)) {
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z3);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z2).size() == 0) {
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
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.put(p.getName(), Methods.t1z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z2).size() == 0) {
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
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamOne, "One");
							}
						}
					}
					// Check Team One Zone Three
					if (fromRegions.contains(Methods.t1z3) && Methods.allowedZone.get(p.getName()).equalsIgnoreCase(Methods.t1z3)) {
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
							Methods.allowedZone.remove(p.getName());
							Methods.sendPBChat(PlayerEliminated.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z3).size() == 0) {
								Methods.endMatch(Methods.TeamTwo);
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
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamOne, "One");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)){
							Methods.allowedZone.put(p.getName(), Methods.t1z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
					}
					// Check TeamTwoZoneThree
					if (fromRegions.contains(Methods.t2z3) && Methods.allowedZone.get(p.getName()).equals(Methods.t2z3)) {
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.remove(p.getName());
							Methods.sendPBChat(PlayerEliminated.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z3).size() == 0) {
								Methods.endMatch(Methods.TeamTwo);
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
							p.teleport(from);
							Methods.allowedZone.put(p.getName(), Methods.t2z3); 
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamOne, "One");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
					}
					// Check Team One Zone One
					if (fromRegions.contains(Methods.t1z1) && Methods.allowedZone.get(p.getName()).equals(Methods.t1z1)) { // They are coming from Team One Zone One
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)) {
							Methods.allowedZone.put(p.getName(), Methods.t1z2);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z1);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t1z1).size() == 0) {
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
							p.teleport(from); // Sends them back to Zone 2.
							Methods.allowedZone.put(p.getName(), Methods.t1z3); // They were fouled back to Zone 3.
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName())); // Notify Probending Chat.
							if (Methods.playersInZone(Methods.t1z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) { // They are on Team Two
							Methods.allowedZone.put(p.getName(), Methods.t1z1); // Send them back to Team One Zone 1.
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName())); // Notify Probending Chat.
							if (Methods.playersInZone(Methods.t1z2).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamOne, "One");
							}
						}
					}
					// Check Team Two Zone One
					if (fromRegions.contains(Methods.t2z1) && Methods.allowedZone.get(p.getName()).equals(Methods.t2z1)) { // They are coming from Team Two's First Zone.
						if (teamSide.equalsIgnoreCase(Methods.TeamOne)) { // Team One Player
							Methods.allowedZone.put(p.getName(),  Methods.t1z1); // Stick them to Zone One on Team One's Side.
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName())); // Notify PB Chat.
							if (Methods.playersInZone(Methods.t2z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamTwo, "Two");
							}
						}
						if (teamSide.equalsIgnoreCase(Methods.TeamTwo)) {
							Methods.allowedZone.put(p.getName(), Methods.t2z2);
							Methods.sendPBChat(PlayerFouled.replace("%player", p.getName()));
							if (Methods.playersInZone(Methods.t2z1).size() == 0) {
								Methods.MovePlayersUp(Methods.TeamOne, "One");
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
										player.sendMessage(Commands.Prefix + CantEnterField);
										player.teleport(locFrom);
										e.setCancelled(true);
									}
								}
								if (teamName == null) {
									player.sendMessage(Commands.Prefix + CantEnterField);
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
		Methods.sendPBChat(e.getMessage());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();

		if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (block.getState() instanceof Sign) {
				Sign s = (Sign) block.getState();

				String line1 = s.getLine(0);
				String teamColor = s.getLine(1);

				if (line1.equalsIgnoreCase("[probending]")) {
					if (!player.hasPermission("probending.team.sign.use")) {
						player.sendMessage(Commands.Prefix + Commands.noPermission);
						return;
					}

					if (!SignListener.colors.contains(teamColor)) {
						player.sendMessage(Commands.Prefix + SignListener.InvalidSign);
						return;
					}

					ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
					ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
					ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
					ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

					player.getInventory().setHelmet(Methods.createColorArmor(helmet, Methods.getColorFromString(teamColor)));
					player.getInventory().setChestplate(Methods.createColorArmor(chestplate, Methods.getColorFromString(teamColor)));
					player.getInventory().setLeggings(Methods.createColorArmor(leggings, Methods.getColorFromString(teamColor)));
					player.getInventory().setBoots(Methods.createColorArmor(boots, Methods.getColorFromString(teamColor)));
					e.setUseItemInHand(Result.DENY);
					e.setUseInteractedBlock(Result.DENY);
					player.updateInventory();
					player.sendMessage(Commands.Prefix + SetTeamColor.replace("%color", teamColor));
					return;
				}

			}
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
						player.sendMessage(Commands.Prefix + RemovedFromTeamBecauseDifferentElement);
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