package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.command.CommandSender;

import java.util.List;

public class AddWinCommand extends PBCommand {
	
	public AddWinCommand() {
		super ("addwin", "/pb team addwin [Team]", "Adds a win to a team.", new String[] {"addwin", "win"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}

		String teamName = args.get(1);
		Team team = PBMethods.getTeam(args.get(1));
		if (!PBMethods.teamExists(teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}
		
		if (team != null) {
			team.addWin();
			sender.sendMessage(PBMethods.Prefix + PBMethods.WinAddedToTeam.replace("%team", team.getName()));
		}
		return;
	}
}