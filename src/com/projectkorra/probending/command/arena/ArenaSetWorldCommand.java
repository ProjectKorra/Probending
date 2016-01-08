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
		super ("arena-setworld", "/pb arena setworld [Arena] [World]", "Sets the world an arena is located in.", new String[] {"setworld", "sw"}, true, Commands.arenaaliases);
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
		if (arena.getTeamOneSpawn() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] TeamOne " + ChatColor.GREEN + " to set the TeamOne spawn.");
		}
		if (arena.getTeamTwoSpawn() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] TeamTwo " + ChatColor.GREEN + " to set the TeamTwo spawn.");
		}
		if (arena.getSpectatorSpawn() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] Spectator " + ChatColor.GREEN + " to set the Spectator spawn.");
		}
		if (arena.getField() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "pb arena setzone [Arena] Field [Region] " + ChatColor.GREEN + " to set the region for the Probending field.");
		}
		if (arena.getTeamOneZoneOne() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneOne [Region] " + ChatColor.GREEN + " to set the region for Team One Zone One.");
		}
		if (arena.getTeamOneZoneTwo() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneTwo [Region] " + ChatColor.GREEN + " to set the region for Team One Zone Two.");
		}
		if (arena.getTeamOneZoneThree() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneThree [Region] " + ChatColor.GREEN + " to set the region for Team One Zone Three.");
		}
		if (arena.getTeamTwoZoneOne() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneOne [Region] " + ChatColor.GREEN + " to set the region for Team Two Zone One.");
		}
		if (arena.getTeamTwoZoneTwo() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneTwo [Region] " + ChatColor.GREEN + "to set the region for Team Two Zone Two.");
		}
		if (arena.getTeamTwoZoneThree() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneThree [Region] " + ChatColor.GREEN + "to set the region for Team Two Zone Three.");
		}
		if (arena.getDivider() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] Divider " + ChatColor.GREEN + " to set the region for the dividing line.");
		}
		return;
	}
}
