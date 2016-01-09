package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.command.CommandSender;

import java.util.List;

public class AddLossCommand extends PBCommand {

	public AddLossCommand() {
		super ("team-addloss", "/pb team addloss [Team]", "Adds a loss to a team.", new String[] {"addloss", "loss"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}

		String teamName = args.get(2);
		Team team = PBMethods.getTeam(teamName);
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}

		if (team != null) {
			team.addLoss();
			sender.sendMessage(PBMethods.Prefix + PBMethods.LossAddedToTeam.replace("%team", team.getName()));
		}
		return;
	}
}
