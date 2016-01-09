package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

public class ArenaCreate extends PBCommand {
	
	public ArenaCreate() {
		super ("arena-create", "/pb arena create [Arena] <Team1 Color> <Team2 Color>", "Creates a new arena.", new String[] {"create", "c"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if(!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 2, 4)) {
			return;
		}
		if (args.size() == 3) {
			sender.sendMessage(ChatColor.GOLD + "Proper Usage: " + ChatColor.DARK_AQUA + "/pb create [Arena] <Team1 Color> <Team2 Color>");
			sender.sendMessage(ChatColor.GOLD + "Note: " + ChatColor.DARK_AQUA + "/pb create [Arena] " + ChatColor.GREEN + " will create an arena with the default colors of Red (Team One) and Cyan (Team 2).");
			return;
		}
		
		String teamOne = "Red";
		String teamTwo = "Blue";
		if (args.size() == 4) {
			if (PBMethods.getColorFromString(args.get(2)) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid Team Color: " + args.get(2));
				sender.sendMessage(ChatColor.RED + "Valid Team Colors: " + PBMethods.colors.toString());
				return;
			}
			
			if (PBMethods.getColorFromString(args.get(3)) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid Team Color: " + args.get(3));
				sender.sendMessage(ChatColor.RED + "Valid Team Colors: " + PBMethods.colors.toString());
				return;
			}
			
			teamOne = args.get(2);
			teamTwo = args.get(3);
		}
		
		String arena = args.get(1);
		if (PBMethods.getArena(arena) != null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "An arena by that name already exists.");
			return;
		}
		
		if (arena.length() > 16) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "That arena name is too long. Please keep it under 16 characters.");
			return;
		}
		
		PBMethods.createArena(arena, teamOne, teamTwo);
		sender.sendMessage(ChatColor.GREEN + "Created an arena named " + arena + ".");
		sender.sendMessage(ChatColor.GREEN + "Use /pb arena setworld [Arena] [World] to set the world this arena is located in.");
		return;
	}
}

