package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DisbandCommand extends PBCommand {
	
	public DisbandCommand() {
		super ("disband", "/pb team disband [Team]", "Disband your team/another team.", new String[] {"disband"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();

		String teamName = null;
		if (args.size() == 2) {
			if (!sender.hasPermission("probending.team.disband.other")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.noPermission);
				return;
			}
			if (PBMethods.teamExists(args.get(1))) {
				teamName = args.get(1);
			}
		} else {
			teamName = PBMethods.getPlayerTeam(uuid);
		}
		
		Team team = PBMethods.getTeam(teamName);
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}
		if (!PBMethods.isPlayerOwner(uuid, teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (PBMethods.getPlayerTeam(player.getUniqueId()) == null) continue;
			if (PBMethods.getPlayerTeam(player.getUniqueId()).equals(teamName)) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDisbanded.replace("%team", teamName));
			}
		}
		String playerElement = PBMethods.getPlayerElementAsString(uuid);

		PBMethods.removePlayerFromTeam(team, uuid, playerElement);
		Set<String> teamelements = PBMethods.getTeamElements(teamName);
		if (teamelements != null) {
			if (teamelements.contains("Air")) {
				PBMethods.removePlayerFromTeam(team, team.getAirbender(), "Air");
			}
			if (teamelements.contains("Water")) {
				PBMethods.removePlayerFromTeam(team, team.getWaterbender(), "Water");
			}
			if (teamelements.contains("Earth")) {
				PBMethods.removePlayerFromTeam(team, team.getEarthbender(), "Earth");
			}
			if (teamelements.contains("Fire")) {
				PBMethods.removePlayerFromTeam(team, team.getFirebender(), "Fire");
			}
			if (teamelements.contains("Chi")) {
				PBMethods.removePlayerFromTeam(team, team.getChiblocker(), "Chi");
			}
		}
		PBMethods.deleteTeam(teamName);
		return;
	}
}
