package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StartCommand extends PBCommand {

	public StartCommand() {
		super ("round-start", "/pb round start <Team 1> <Team 2>", "Starts round.", new String[] {"start"}, true, Commands.roundaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasRoundPermission(sender) || !correctLength(sender, args.size(), 3, 3)) {
			return;
		}

		if (PBMethods.matchStarted) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.RoundAlreadyGoing);
			return;
		}

		String team1 = args.get(1); // Team 1
		String team2 = args.get(2); // Team 2

		if (!PBMethods.teamExists(team1) || !PBMethods.teamExists(team2)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}

		int minSize = Probending.plugin.getConfig().getInt("TeamSettings.MinTeamSize");

		// Checks to make sure the team has enough players.
		if (PBMethods.getOnlineTeamSize(team1) < minSize || PBMethods.getOnlineTeamSize(team2) < minSize) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.InvalidTeamSize);
			return;
		}
		
		if (team1.equalsIgnoreCase(team2)) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "The same team cannot play against itself.");
			return;
		}

		PBMethods.playingTeams.add(team1.toLowerCase());
		PBMethods.playingTeams.add(team2.toLowerCase());
		PBMethods.TeamOne = team1.toLowerCase();
		PBMethods.TeamTwo = team2.toLowerCase();

		for (Player player: Bukkit.getOnlinePlayers()) {
			String playerTeam = PBMethods.getPlayerTeam(player.getUniqueId());
			Color teamColor = null;
			if (playerTeam != null) {
				if (playerTeam.equalsIgnoreCase(team1)) teamColor = PBMethods.getColorFromString(Probending.plugin.getConfig().getString("TeamSettings.TeamOneColor"));
				if (playerTeam.equalsIgnoreCase(team2)) teamColor = PBMethods.getColorFromString(Probending.plugin.getConfig().getString("TeamSettings.TeamTwoColor"));
				if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne)) {
					PBMethods.teamOnePlayers.add(player.getName());
					Commands.pbChat.add(player);
					player.teleport(PBMethods.getTeamOneSpawn());
				}
				if (playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
					Commands.pbChat.add(player);
					player.teleport(PBMethods.getTeamTwoSpawn());
					PBMethods.teamTwoPlayers.add(player.getName());
				}
				if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
					Commands.tmpArmor.put(player, player.getInventory().getArmorContents()); // Backs up their armor.
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


		int roundTime = Probending.plugin.getConfig().getInt("RoundSettings.Time");
		Commands.currentNumber = roundTime * 20;
		Commands.startingNumber = roundTime * 20;

		Commands.clockTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Probending.plugin, new Runnable() {
			public void run() {
				PBMethods.matchStarted = true;
				Commands.currentNumber--;

				if (Commands.currentNumber == Commands.startingNumber - 1) {
					PBMethods.sendPBChat(PBMethods.RoundStarted.replace("%seconds", String.valueOf(Commands.startingNumber / 20)).replace("%team1", PBMethods.TeamOne).replace("%team2", PBMethods.TeamTwo));
				}
				if (Commands.currentNumber == 1200) {
					PBMethods.sendPBChat(PBMethods.Prefix + PBMethods.OneMinuteRemaining);
				}
				if (Commands.currentNumber == 0) {
					PBMethods.sendPBChat(PBMethods.RoundComplete);
					PBMethods.matchStarted = false;
					Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
					PBMethods.restoreArmor();
				}

			}
		}, 0L, 1L);

		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
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
