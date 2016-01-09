package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;

public class ArenaSetSpawnCommand extends PBCommand {
	
	public ArenaSetSpawnCommand() {
		super ("arena-setspawn", "/pb arena setspawn [Arena] Spectator|TeamOne|TeamTwo", "Sets the specified spawn location for an arena.", new String[] {"setspawn", "ss"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 3, 3)) {
			return;
		}
		
		String arenaName = args.get(1);
		Arena arena = PBMethods.getArena(arenaName);
		Player player = (Player) sender;
		
		if (arena == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "An arena by that name does not exist.");
			return;
		}
		
		if (arena.getWorld() == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "You must set the arena's world first. You can do this with " + ChatColor.DARK_AQUA + "/pb arena setworld " + arena.getName()  + ChatColor.GREEN + ".");
			return;
		}
		
		if (arena.getWorld() != player.getWorld()) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "You must be in the same world the arena is in.");
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "The arena is located in " + arena.getWorld() + ", You are in " + player.getWorld().getName());
		}
		
		String spawn = args.get(2);
		if (!spawn.equalsIgnoreCase("teamone") && !spawn.equalsIgnoreCase("spectator") && !spawn.equalsIgnoreCase("teamtwo")) {
			sender.sendMessage(PBMethods.Prefix + this.getDescription());
			return;
		}
		
		if (spawn.equalsIgnoreCase("teamone")) {
			arena.setTeamOneSpawn(player.getLocation());
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have changed the location of Team One's spawn for " + arena.getName() + ".");
		}
		if (spawn.equalsIgnoreCase("teamtwo")) {
			arena.setTeamTwoSpawn(player.getLocation());
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have changed the location of Team Two's spawn for " + arena.getName() + ".");
		}
		if (spawn.equalsIgnoreCase("spectator")) {
			arena.setSpectatorSpawn(player.getLocation());
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have changed the location of the Spectator spawn for " + arena.getName() + ".");
		}
		
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
