package com.etriacraft.probending;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
				String playerElementInTeam = null;
				if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Air") != null) {
					if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Air").equals(player.getName())) {
						playerElementInTeam = "Air";
					}
				}
				if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Water") != null) {
					if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Water").equals(player.getName())) {
						playerElementInTeam = "Water";
					}
				}
				if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Earth") != null) {
					if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Earth").equals(player.getName())) {
						playerElementInTeam = "Earth";
					}
				}
				if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Fire") != null) {
					if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Fire").equals(player.getName())) {
						playerElementInTeam = "Fire";
					}
				}
				if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Chi") != null) {
					if (Probending.plugin.getConfig().getString("TeamInfo." + team + ".Chi").equals(player.getName())) {
						playerElementInTeam = "Chi";
					}
				}
				if (playerElementInTeam != null) {
					if (!playerElementInTeam.equals(playerElement)) {
						player.sendMessage(Commands.Prefix + RemovedFromTeamBecauseDifferentElement);
						Methods.removePlayerFromTeam(team, player.getName(), playerElementInTeam);
						Set<String> teamElements = Methods.getTeamElements(team);
						if (teamElements.contains("Air")) {
							String airbender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Air");
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", airbender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Water")) {
							String bender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Water");
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Earth")) {
							String bender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Earth");
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Fire")) {
							String bender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Fire");
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						}
						if (teamElements.contains("Chi")) {
							String bender = Probending.plugin.getConfig().getString("TeamInfo." + team + ".Chi");
							Probending.plugin.getConfig().set("TeamInfo." + team + ".Owner", bender);
							Probending.plugin.saveConfig();
							return;
						} else {
							Probending.plugin.getConfig().set("TeamInf." + team, null);
							Probending.plugin.saveConfig();
						}
					}
				}
			}
		}
	}
}
