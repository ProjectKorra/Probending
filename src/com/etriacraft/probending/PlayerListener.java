package com.etriacraft.probending;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import tools.BendingType;
import tools.Tools;

public class PlayerListener implements Listener {

	Probending plugin;
	
	public PlayerListener(Probending plugin) {
		this.plugin = plugin;
	}
	
	public static String RemovedFromTeamBecauseDifferentElement;
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent e) {
		if (Commands.pbChat.contains(e.getPlayer())) {
			e.getRecipients().clear();
			for (Player player: Bukkit.getOnlinePlayers()) {
				if (Commands.pbChat.contains(player)) {
					e.getRecipients().add(player);
				}
			}
			e.setFormat(Commands.Prefix + e.getFormat());
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
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", airbender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Water")) {
							String bender = Methods.getTeamWaterbender(team);
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Earth")) {
							String bender = Methods.getTeamEarthbender(team);
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Fire")) {
							String bender = Methods.getTeamFirebender(team);
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Chi")) {
							String bender = Methods.getTeamChiblocker(team);
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						} else {
							Probending.plugin.getConfig().set("TeamInfo." + team, null);
							Probending.plugin.saveConfig();
						}
					}
				}
			}
		}
	}
}