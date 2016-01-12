package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;

public class ArenaSetWorldCommand extends PBCommand {

	public ArenaSetWorldCommand() {
		super ("arena-setworld", "/pb arena setworld <Arena> [World]", "Sets the world an arena is located in.", new String[] {"setworld", "sw"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if(!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 2, 3)) {
			return;
		}
		
		Player player = (Player) sender;
		World world = player.getWorld();
		if (args.size() == 3) {
			String worldName = args.get(2);
			if (Bukkit.getWorld(worldName) == null) {
				sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "Please enter a valid world name.");
				sender.sendMessage(PBMethods.Prefix + ChatColor.GOLD + "Note: " + ChatColor.GREEN + "Running the command without specifying a world will set the world to the one you're currently in.");
				return;
			}
			world = Bukkit.getWorld(worldName);
		}
		
		Arena arena = PBMethods.getArena(args.get(1));
		if (arena == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "Please specify an existing an Arena.");
			return;
		}
		arena.setWorld(world);
		sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have changed the world of " + arena.getName() + " to " + world.getName() + ".");
		sender.sendMessage(ArenaMethods.getArenaSetupHelp(arena));
		return;
	}
}
