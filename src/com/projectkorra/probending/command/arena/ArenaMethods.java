package com.projectkorra.probending.command.arena;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.objects.Arena;

import org.bukkit.ChatColor;

public class ArenaMethods {
	
	public static String getArenaSetupHelp(Arena arena) {
		if (arena.getTeamOneSpawn() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] TeamOne " + ChatColor.GREEN + " to set the TeamOne spawn.";
		} else if (arena.getTeamTwoSpawn() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] TeamTwo " + ChatColor.GREEN + " to set the TeamTwo spawn.";
		} else if (arena.getSpectatorSpawn() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setspawn [Arena] Spectator " + ChatColor.GREEN + " to set the Spectator spawn.";
		} else if (arena.getField() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "pb arena setzone [Arena] Field [Region] " + ChatColor.GREEN + " to set the region for the Probending field.";
		} else if (arena.getTeamOneZoneOne() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneOne [Region] " + ChatColor.GREEN + " to set the region for Team One Zone One.";
		} else if (arena.getTeamOneZoneTwo() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneTwo [Region] " + ChatColor.GREEN + " to set the region for Team One Zone Two.";
		} else if (arena.getTeamOneZoneThree() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamOneZoneThree [Region] " + ChatColor.GREEN + " to set the region for Team One Zone Three.";
		} else if (arena.getTeamTwoZoneOne() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneOne [Region] " + ChatColor.GREEN + " to set the region for Team Two Zone One.";
		} else if (arena.getTeamTwoZoneTwo() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneTwo [Region] " + ChatColor.GREEN + "to set the region for Team Two Zone Two.";
		} else if (arena.getTeamTwoZoneThree() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] TeamTwoZoneThree [Region] " + ChatColor.GREEN + "to set the region for Team Two Zone Three.";
		} else if (arena.getDivider() == null) {
			return PBMethods.Prefix + ChatColor.GREEN + "Use " + ChatColor.DARK_AQUA + "/pb arena setzone [Arena] Divider " + ChatColor.GREEN + " to set the region for the dividing line.";
		}
		return PBMethods.Prefix + ChatColor.GREEN + "The arena " + ChatColor.YELLOW + arena.getName() + ChatColor.GREEN + " is now setup!";
	}

}
