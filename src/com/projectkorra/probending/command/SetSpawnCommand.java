package com.projectkorra.probending.command;

import com.projectkorra.probending.PBMethods;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Executor for /probending setspawn. Extends {@link PBCommand}.
 */
public class SetSpawnCommand extends PBCommand {
	
	public SetSpawnCommand() {
		super ("setspawn", "/probending setspawn [TeamOne|TeamTwo|Spectator]", "Sets the spawn point for the Probending arena.", new String[] {"setspawn", "set", "ss"});
	}
	
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		
		String arg = args.get(0);
		
		if (!arg.equalsIgnoreCase("teamone") && !arg.equalsIgnoreCase("teamtwo") && !arg.equalsIgnoreCase("spectator")) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "Proper Usage:  " + ChatColor.DARK_AQUA + "/pb setspawn [TeamOne|TeamTwo|Spectator]");
			return;
		}
		if (arg.equalsIgnoreCase("spectator")) {
			PBMethods.setSpectatorSpawn(((Player) sender).getLocation());
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamSpawnSet.replace("%team", "Spectator"));
			return;
		}
		if (arg.equalsIgnoreCase("teamone")) {
			PBMethods.setTeamOneSpawn(((Player) sender).getLocation());
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamSpawnSet.replace("%team", "TeamOne"));
			return;
		}
		if (arg.equalsIgnoreCase("teamtwo")) {
			PBMethods.setTeamTwoSpawn(((Player) sender).getLocation());
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamSpawnSet.replace("%team", "TeamTwo"));
			return;
		}
	}
}
