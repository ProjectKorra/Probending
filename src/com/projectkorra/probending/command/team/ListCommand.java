package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends PBCommand {
	
	public ListCommand() {
		super ("list", "/pb team list", "List all teams.", new String[] {"list", "l"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		
		List<String> teams = new ArrayList<String>();
		teams.addAll(PBMethods.getTeams().keySet());
		if (teams.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "There are no Probending teams!");
		} else {
			sender.sendMessage(ChatColor.RED + "Teams: " + ChatColor.GREEN + teams.toString().replace("[", "").replace("]", ""));
		}
		return;
	}
}
