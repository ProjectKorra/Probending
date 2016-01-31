package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.Element;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class InfoCommand extends PBCommand {
	
	public InfoCommand() {
		super ("team-info", "/pb team info", "View info on a team.", new String[] {"info"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		
		Team team = null;
		if (args.size() == 1) {
			team = PBMethods.getPlayerTeam(uuid);
		}
		if (args.size() == 2) {
			team = PBMethods.getTeam(args.get(1));
		}

		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}

		String teamOwner = Bukkit.getOfflinePlayer(team.getOwner()).getName();
		sender.sendMessage(ChatColor.DARK_AQUA + "Team Name: " + ChatColor.YELLOW + team.getName());
		sender.sendMessage(ChatColor.DARK_AQUA + "Team Owner: " + ChatColor.DARK_PURPLE + teamOwner);

		OfflinePlayer air = null;
		OfflinePlayer water = null;
		OfflinePlayer earth = null;
		OfflinePlayer fire = null;
		OfflinePlayer chi = null;
		
		if (team.hasAirbender()) {
			air = Bukkit.getOfflinePlayer(team.getAirbender());
		}
		if (team.hasWaterbender()) {
			water = Bukkit.getOfflinePlayer(team.getWaterbender());
		}
		if (team.hasEarthbender()) {
			earth = Bukkit.getOfflinePlayer(team.getEarthbender());
		}
		if (team.hasFirebender()) {
			fire = Bukkit.getOfflinePlayer(team.getFirebender());
		}
		if (team.hasChiblocker()) {
			chi = Bukkit.getOfflinePlayer(team.getChiblocker());
		}

		int wins = team.getWins();
		int losses = team.getLosses();

		if (PBMethods.getAirAllowed()) {
			if (air != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Airbender: " + Element.AIR.getColor() + air.getName());
			}
		}
		if (PBMethods.getWaterAllowed()) {
			if (water != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Waterbender: " + Element.WATER.getColor() + water.getName());
			}
		}
		if (PBMethods.getEarthAllowed()) {
			if (earth != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Earthbender: " + Element.EARTH.getColor() + earth.getName());
			}
		}
		if (PBMethods.getFireAllowed()) {
			if (fire != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Firebender: " + Element.FIRE.getColor() + fire.getName());
			}
		}
		if (PBMethods.getChiAllowed()) {
			if (chi != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Chiblocker: " + Element.CHI.getColor() + chi.getName());
			}
		}
		sender.sendMessage(ChatColor.DARK_AQUA + "Wins: " + ChatColor.YELLOW + wins);
		sender.sendMessage(ChatColor.DARK_AQUA + "Losses: " + ChatColor.YELLOW + losses);
	}
}
