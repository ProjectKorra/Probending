package com.projectkorra.probending.command.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;

public class ArenaListCommand extends PBCommand {

	public ArenaListCommand() {
		super ("arena-list", "/pb arena list", "Lists all arenas on this server.", new String[] {"list", "l"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		
		List<String> arenas = new ArrayList<String>();
		arenas.addAll(Arena.arenas.keySet());
		if (arenas.isEmpty()) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "There are no Probending Arenas!");
		} else {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GOLD + "List of Probending Arenas: ");
			sender.sendMessage(ChatColor.GREEN + arenas.toString().replace("[", "").replace("]", ""));
		}
		return;
	}
}
