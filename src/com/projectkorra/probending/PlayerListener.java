package com.projectkorra.probending;

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.objects.Arena;
import com.projectkorra.probending.objects.Round;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.List;
import java.util.Set;

public class PlayerListener implements Listener {

	Probending plugin;

	public PlayerListener(Probending plugin) {
		this.plugin = plugin;
	}

	/**
	 * Removes damage if the player is in a Probending Round.
	 * @param e
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (PBMethods.isPlayerInRound(player)) {
				e.setDamage(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerShift(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		List<String> allowedMoves = Probending.plugin.getConfig().getStringList("RoundSettings.AllowedMoves");
		if (PBMethods.isPlayerInRound(p)) {
			Round round = PBMethods.getPlayerRound(p);
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(p);
			if (bPlayer.getBoundAbility() == null) {
				e.setCancelled(true);
				return;
			}
			String ability = bPlayer.getBoundAbilityName();
			if (!allowedMoves.contains(ability) && !CoreAbility.getAbility(ability).getElement().equals(PBMethods.getPlayerElementAsString(p.getUniqueId()))) {
				e.setCancelled(true); // Don't allow the player to bend if the ability is not on the allowed ability list.
			}
			Set<String> regions = PBMethods.RegionsAtLocation(p.getLocation());
			if (regions != null && !regions.isEmpty()) {
				if (!regions.contains(round.getAllowedZone(p))) { // Cancels Bending if the player is not in the right zone.
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAnimation(PlayerAnimationEvent e) {
		Player p = e.getPlayer();
		List<String> allowedMoves = Probending.plugin.getConfig().getStringList("RoundSettings.AllowedMoves");
		if (PBMethods.isPlayerInRound(p)) {
			Round round = PBMethods.getPlayerRound(p);
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(p);
			if (bPlayer.getBoundAbility() == null) {
				e.setCancelled(true);
				return;
			}
			String ability = bPlayer.getBoundAbilityName();
			if (!allowedMoves.contains(ability) && !CoreAbility.getAbility(ability).getElement().equals(PBMethods.getPlayerElementAsString(p.getUniqueId()))) {
				e.setCancelled(true); // Don't allow the player to bend if the ability is not on the allowed ability list.
			}
			Set<String> regions = PBMethods.RegionsAtLocation(p.getLocation());
			if (regions != null && !regions.isEmpty()) {
				if (!regions.contains(round.getAllowedZone(p))) { // Cancels Bending if the player is not in the right zone.
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();

		if (PBMethods.isPlayerInRound(p)) {
			Arena a = PBMethods.getPlayerRound(p).getArena();
			if (e.getTo() == a.getSpectatorSpawn() || e.getTo() == a.getTeamOneSpawn() || e.getTo() == a.getTeamTwoSpawn()) {
				return;
			}
			p.sendMessage(PBMethods.Prefix + PBMethods.CantTeleportDuringMatch);
			e.setCancelled(true);
		}
	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PBMethods.isPlayerInRound(p)) {
			Round round = PBMethods.getPlayerRound(p);
			round.eliminatePlayer(p);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		if (!PBMethods.isWorldGuardSupportEnabled() || PBMethods.getWorldGuard() == null || !PBMethods.isAutomateMatches()) {
			return;
		}
		Set<String> fromRegions = PBMethods.RegionsAtLocation(from);
		Set<String> toRegions = PBMethods.RegionsAtLocation(to);
		if (PBMethods.isPlayerInRound(p)) {
			Round round = PBMethods.getPlayerRound(p);
			Arena arena = round.getArena();
			String zone = round.getAllowedZone(p);
			String divider = arena.getDivider();
			String field = arena.getField();
			String t1z1 = arena.getTeamOneZoneOne();
			String t1z2 = arena.getTeamOneZoneTwo();
			String t1z3 = arena.getTeamOneZoneThree();
			String t2z1 = arena.getTeamTwoZoneOne();
			String t2z2 = arena.getTeamTwoZoneTwo();
			String t2z3 = arena.getTeamTwoZoneThree();
			Team team = PBMethods.getPlayerTeam(p.getUniqueId());

			if (!toRegions.contains(field)) {
				round.eliminatePlayer(p);
				return;
			}
			if (round.getAllowedZone(p).equalsIgnoreCase(arena.getTeamTwoZoneThree())) { // The player is currently supposed to be in t2z3
				if (!toRegions.contains(t2z3) || toRegions.isEmpty()) { // The player is moving somewhere not in t2z3
					if (fromRegions.contains(t2z3)) { // Moving from t2z3
						round.eliminatePlayer(p); // The player is eliminated, either from a foul or falling off the back.
					}
				}
			}
			if (zone.equalsIgnoreCase(t1z3)) { //Player is supposed to be in t1z3
				if (!toRegions.contains(t1z3) || toRegions.isEmpty()) { // Player is moving somewhere not in t1z3
					if (fromRegions.contains(t1z3)) { // Moving from t1z3
						round.eliminatePlayer(p);
					}
				}
			}
			if (divider != null) {
				if (toRegions.contains(divider) && !zone.equalsIgnoreCase(divider)) {
					if (fromRegions.contains(t1z1) && zone.equalsIgnoreCase(t1z1)) {
						if (team == round.getTeamOne()) { // The player is stepping on the divider from Team 1 Zone 1 and is on Team One
							if (round.getAllowedZone(p) == t2z1) {
								return;
							}
							round.setAllowedZone(p, t1z2);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamOne(), t1z1)) {
								round.movePlayersUp(round.getTeamTwo());
							}
						}
						if (team == round.getTeamTwo()) {
							round.setAllowedZone(p, t2z1);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamTwo(), t1z1) && round.isZoneEmpty(round.getTeamTwo(), t1z2)) {
								round.movePlayersUp(round.getTeamOne());
							}
						}
					}
					if (fromRegions.contains(t2z1) && zone.equalsIgnoreCase(t2z1)) {
						if (team == round.getTeamOne()) {
							round.setAllowedZone(p, t1z1);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamOne(), t2z1) && round.isZoneEmpty(round.getTeamOne(), t2z2)) {
								round.movePlayersUp(round.getTeamTwo());
							}
						}
						if (team == round.getTeamTwo()) {
							if (round.getAllowedZone(p) == t1z1) {
								return;
							}
							round.setAllowedZone(p, t2z2);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamTwo(), t2z1) && round.isZoneEmpty(round.getTeamTwo(), t1z1)) {
								round.movePlayersUp(round.getTeamOne());
							}
						}
					}
				}
			}
			if (toRegions.contains(t2z3) && !round.getAllowedZone(p).equalsIgnoreCase(t2z3)) {
				if (!zone.equalsIgnoreCase(t2z3)) {
					if (fromRegions.contains(t2z2) && zone.equalsIgnoreCase(t2z2)) { // They are coming from t2z2, that's where they were supposed to be. FOUL.
						if (PBMethods.getPlayerTeam(p.getUniqueId()) == round.getTeamOne()) {
							round.setAllowedZone(p, t2z1); // Move them back to team 2 zone 1.
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamOne(), t2z2) && round.isZoneEmpty(round.getTeamOne(), t2z1)) {
								// There are no team one players in t2z2 or t2z1
								round.movePlayersUp(round.getTeamTwo());
							}
						}
						if (PBMethods.getPlayerTeam(p.getUniqueId()) == round.getTeamTwo()) {
							round.setAllowedZone(p, t2z3);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamTwo(), t2z2)) {
								round.movePlayersUp(round.getTeamOne());
							}
						}
					}
				}
			}
			if (toRegions.contains(t1z3) && !round.getAllowedZone(p).equalsIgnoreCase(t1z3)) {
				if (!zone.equalsIgnoreCase(t1z3)) {
					if (fromRegions.contains(t1z2) && round.getAllowedZone(p).equalsIgnoreCase(t1z2)) {
						if (team == round.getTeamOne()) {
							round.setAllowedZone(p, t1z3);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamOne(), t1z2) && round.isZoneEmpty(round.getTeamOne(), t1z1)) {
								round.movePlayersUp(round.getTeamTwo());
							}
						}
						if (team == round.getTeamTwo()) {
							round.setAllowedZone(p, t1z1);
							PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
							if (round.isZoneEmpty(round.getTeamTwo(), t1z2)) {
								round.movePlayersUp(round.getTeamOne());
							}
						}
					}
				}
			}
			if (toRegions.contains(t1z2) && !round.getAllowedZone(p).equalsIgnoreCase(t1z2)) {
				if (fromRegions.contains(t1z1) && round.getAllowedZone(p).equalsIgnoreCase(t1z1)) {
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t1z2);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t1z1)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t2z1);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t1z1) && round.isZoneEmpty(round.getTeamTwo(), t1z2)) {
							round.movePlayersUp(round.getTeamOne());
						}
					}
				}
				if (fromRegions.contains(t1z3) && round.getAllowedZone(p).equalsIgnoreCase(t1z3)) {
					round.eliminatePlayer(p);
				}
			}
			if (toRegions.contains(t2z2) && !round.getAllowedZone(p).equalsIgnoreCase(t2z2)) {
				if (fromRegions.contains(t2z1) && round.getAllowedZone(p).equals(t2z1)) {
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t2z2);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t2z1) && round.isZoneEmpty(round.getTeamTwo(), t1z1)) {
							round.movePlayersUp(round.getTeamOne());
						}
					}
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t1z1); 
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t2z1) && round.isZoneEmpty(round.getTeamOne(), t2z2)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
				}
				if (fromRegions.contains(t2z3) && round.getAllowedZone(p).equalsIgnoreCase(t2z3)) {
					if (team == round.getTeamTwo()) {
						round.eliminatePlayer(p);
					}
				}
			}
			if (toRegions.contains(t2z1) && !zone.equalsIgnoreCase(t2z1)) {
				if (fromRegions.contains(t2z2) && zone.equalsIgnoreCase(t2z2)) {
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t2z3);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t2z2) && round.isZoneEmpty(round.getTeamTwo(), t2z1)) {
							round.movePlayersUp(round.getTeamOne());
						}
					}
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t2z1);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t2z2) && round.isZoneEmpty(round.getTeamOne(), t2z1)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
				}

				if (fromRegions.contains(t1z1) && zone.equalsIgnoreCase(t1z1)) {
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t1z2);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t1z1)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t2z1);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t1z1)) {
							round.movePlayersUp(round.getTeamOne());
						}
					}
				}
			}
			if (toRegions.contains(t1z1) && !zone.equalsIgnoreCase(t1z1)) {
				if (fromRegions.contains(t1z2) && zone.equalsIgnoreCase(t1z2)) {
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t1z3);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t1z2)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t1z1);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t1z2)) {
							round.movePlayersUp(round.getTeamOne());
						}
					}
				}
				if (fromRegions.contains(t2z1) && zone.equalsIgnoreCase(t2z1)) {
					if (team == round.getTeamOne()) {
						round.setAllowedZone(p, t1z1);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamOne(), t2z1) && round.isZoneEmpty(round.getTeamOne(), t2z2)) {
							round.movePlayersUp(round.getTeamTwo());
						}
					}
					if (team == round.getTeamTwo()) {
						round.setAllowedZone(p, t2z2);
						PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", round.getAllowedZone(p)), round);
						if (round.isZoneEmpty(round.getTeamTwo(), t2z1)) {
							round.movePlayersUp(round.getTeamOne());
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
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
			for (ProtectedRegion region: set) {
				if (region != null) {
					for (String arena: Arena.arenas.keySet())
						if (region.getId().equalsIgnoreCase(Arena.arenas.get(arena).getField())) {
							if (!player.hasPermission("probending.worldguard.buildonfield")) {
								e.setCancelled(true);
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
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
			for (ProtectedRegion region: set) {
				if (region != null) {
					for (String arena: Arena.arenas.keySet())
						if (region.getId().equalsIgnoreCase(Arena.arenas.get(arena).getField())) {
							if (!player.hasPermission("probending.worldguard.buildonfield")) {
								e.setCancelled(true);
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
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(locTo.getWorld()).getApplicableRegions(locTo);
			for (ProtectedRegion region: set) {
				if (region != null) {
					// Checks if the player can enter the field during a Probending Match
					for (String s: Arena.arenas.keySet()) {
						if (region.getId().equalsIgnoreCase(Arena.arenas.get(s).getField())) {
							if (PBMethods.isRoundAtArena(Arena.arenas.get(s))) {
								if (!PBMethods.isPlayerInRound(player)) {
									player.sendMessage(PBMethods.Prefix + PBMethods.CantEnterField);
									player.teleport(Arena.arenas.get(s).getSpectatorSpawn());
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
			e.setFormat(PBMethods.Prefix + e.getFormat());
		}
	} 
	
	@EventHandler
	public void onElementChange(PlayerChangeElementEvent event) {
		Player player = event.getTarget();
		if (player == null) return;
		Team team = PBMethods.getPlayerTeam(player.getUniqueId());
		if (team != null) {
			String teamElement = PBMethods.getPlayerElementInTeam(player.getUniqueId(), team.getName());
			if (event.getResult().equals(PlayerChangeElementEvent.Result.CHOOSE)) {
				if (!event.getElement().toString().equalsIgnoreCase(teamElement)) {
					if (team.updateRole(player.getUniqueId(), event.getElement())) {
						player.sendMessage(PBMethods.Prefix + PBMethods.ElementChanged);
						return;
					} else {
						player.sendMessage(PBMethods.Prefix + PBMethods.RemovedFromTeamBecauseDifferentElement);
						team.removePlayer(player.getUniqueId());
						team.transferOwner();
						return;
					}
				}
			}
			if (event.getResult().equals(PlayerChangeElementEvent.Result.ADD)) {
				player.sendMessage(PBMethods.Prefix + PBMethods.PlayerAddedElement);
				return;
			}
			if ((event.getResult().equals(PlayerChangeElementEvent.Result.REMOVE) 
					&& (event.getElement() == null || ChatColor.stripColor(event.getElement().toString()).equalsIgnoreCase(teamElement)))
					|| event.getResult().equals(PlayerChangeElementEvent.Result.PERMAREMOVE)) {
				player.sendMessage(PBMethods.Prefix + PBMethods.RemovedFromTeamBecauseNoElement);
				team.removePlayer(player.getUniqueId());
				team.transferOwner();
				return;
			}
		}
	}
	
	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PBMethods.createPlayer(player.getUniqueId());
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer == null) return;
		if (!(bPlayer.getElements().size() > 1)) {
			String t = PBMethods.getPlayerTeam(player.getUniqueId()).getName();
			Team team = PBMethods.getTeam(t);
			if (team != null) {
				String playerElement = null;
				if (bPlayer.hasElement(Element.AIR)) {
					playerElement = "Air";
				}
				if (bPlayer.hasElement(Element.WATER)) {
					playerElement = "Water";
				}
				if (bPlayer.hasElement(Element.EARTH)) {
					playerElement = "Earth";
				}
				if (bPlayer.hasElement(Element.FIRE)) {
					playerElement = "Fire";
				}
				if (bPlayer.hasElement(Element.CHI)) {
					playerElement = "Chi";
				}
				String playerElementInTeam = PBMethods.getPlayerElementInTeam(player.getUniqueId(), t);
				if (playerElementInTeam != null) {
					if (!playerElementInTeam.equals(playerElement)) {
						player.sendMessage(PBMethods.Prefix + PBMethods.RemovedFromTeamBecauseDifferentElement);
						team.removePlayer(player.getUniqueId());
						Set<Element> elements = team.getElements();
						if (elements.contains(Element.AIR)) {
							team.setOwner(team.getAirbender());
							return;
						}
						if (elements.contains(Element.WATER)) {
							team.setOwner(team.getWaterbender());
							return;
						}
						if (elements.contains(Element.EARTH)) {
							team.setOwner(team.getEarthbender());
							return;
						}
						if (elements.contains(Element.FIRE)) {
							team.setOwner(team.getFirebender());
							return;
						}
						if (elements.contains(Element.CHI)) {
							team.setOwner(team.getChiblocker());
							return;
						} else {
							team.delete();
						}
					}
				}
			}
		}
	}
}