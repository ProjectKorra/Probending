package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;

public class ArenaDelete extends PBCommand {
	
	public ArenaDelete() {
		super ("arena-delete", "/pb arena delete [Arena]", "Deletes a Probending arena.", new String[] {"delete, del"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		Arena arena = PBMethods.getArena(args.get(1));
		if (arena == null) {
			sender.sendMessage(PBMethods.Prefix + "An arena by that name does not exist.");
			return;
		}
		
		arena.delete();
		sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Successfully deleted " + ChatColor.DARK_AQUA + arena.getName());
		return;
	}

}
