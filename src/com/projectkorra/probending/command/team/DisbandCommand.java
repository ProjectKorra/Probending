package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class DisbandCommand extends PBCommand {
	
	public DisbandCommand() {
		super ("team-disband", "/pb team disband [Team]", "Disband your team/another team.", new String[] {"disband", "dis", "delete", "del"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();

		Team team = null;
		if (args.size() == 2) {
			if (!sender.hasPermission("probending.command.team.disband.other")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.noPermission);
				return;
			}
			if (PBMethods.getTeam(args.get(1)) != null) {
				team = PBMethods.getTeam(args.get(1));
			}
		} else {
			team = PBMethods.getPlayerTeam(uuid);
		}
		
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}
		
		if (!team.isOwner(uuid) && !sender.hasPermission("probending.command.team.disband.other")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}
		
		for (UUID playeruuid : team.getPlayerUUIDs()) {
			if (PBMethods.getPlayerTeam(playeruuid) == null) continue;
			if (PBMethods.getPlayerTeam(playeruuid) == team) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDisbanded.replace("%team", team.getName()));
				team.removePlayer(playeruuid);
			}
		}
		
		team.delete();
		return;
	}
}
