package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.airbending.AirMethods;
import com.projectkorra.projectkorra.chiblocking.ChiMethods;
import com.projectkorra.projectkorra.earthbending.EarthMethods;
import com.projectkorra.projectkorra.firebending.FireMethods;
import com.projectkorra.projectkorra.waterbending.WaterMethods;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class InfoCommand extends PBCommand {
	
	public InfoCommand() {
		super ("info", "/pb team info", "View info on a team.", new String[] {"info"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		
		String teamName = null;
		if (args.size() == 1) {
			teamName = PBMethods.getPlayerTeam(uuid);
		}
		if (args.size() == 2) {
			teamName = args.get(1);
		}

		if (!PBMethods.teamExists(teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}
		
		Team team = PBMethods.getTeam(teamName);
		
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		} else {
			teamName = team.getName();
		}

		String teamOwner = PBMethods.getOwner(teamName);
		sender.sendMessage(ChatColor.DARK_AQUA + "Team Name: " + ChatColor.YELLOW + teamName);
		sender.sendMessage(ChatColor.DARK_AQUA + "Team Owner: " + ChatColor.DARK_PURPLE + teamOwner);

		OfflinePlayer air = PBMethods.getTeamAirbender(teamName);
		OfflinePlayer water = PBMethods.getTeamWaterbender(teamName);
		OfflinePlayer earth = PBMethods.getTeamEarthbender(teamName);
		OfflinePlayer fire = PBMethods.getTeamFirebender(teamName);
		OfflinePlayer chi = PBMethods.getTeamChiblocker(teamName);

		int wins = PBMethods.getWins(teamName);
		int losses = PBMethods.getLosses(teamName);

		if (PBMethods.getAirAllowed()) {
			if (air != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Airbender: " + AirMethods.getAirColor() + air.getName());
			}
		}
		if (PBMethods.getWaterAllowed()) {
			if (water != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Waterbender: " + WaterMethods.getWaterColor() + water.getName());
			}
		}
		if (PBMethods.getEarthAllowed()) {
			if (earth != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Earthbender: " + EarthMethods.getEarthColor() + earth.getName());
			}
		}
		if (PBMethods.getFireAllowed()) {
			if (fire != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Firebender: " + FireMethods.getFireColor() + fire.getName());
			}
		}
		if (PBMethods.getChiAllowed()) {
			if (chi != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Chiblocker: " + ChiMethods.getChiColor() + chi.getName());
			}
		}
		sender.sendMessage(ChatColor.DARK_AQUA + "Wins: " + ChatColor.YELLOW + wins);
		sender.sendMessage(ChatColor.DARK_AQUA + "Losses: " + ChatColor.YELLOW + losses);
	}
}
