package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;

public class ArenaSetZoneCommand extends PBCommand {
	
	public ArenaSetZoneCommand() {
		super ("arena-setzone", "/pb arena setzone <Arena> <Zone> <Region>", "Sets the specified zone to the specified region.", new String[] {"setzone", "sz"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasArenaPermission(sender) || !correctLength(sender, args.size(), 4, 4)) {
			return;
		}
		
		Arena arena = PBMethods.getArena(args.get(1));
		if (arena == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "An arena by that name does not exist.");
			return;
		}
		
		String z = args.get(2);
		String region = args.get(3);
		if (!z.equalsIgnoreCase("field")
				&& !z.equalsIgnoreCase("divider")
				&& !z.equalsIgnoreCase("teamonezoneone")
				&& !z.equalsIgnoreCase("teamonezonetwo")
				&& !z.equalsIgnoreCase("teamonezonethree")
				&& !z.equalsIgnoreCase("teamtwozoneone")
				&& !z.equalsIgnoreCase("teamtwozonetwo")
				&& !z.equalsIgnoreCase("teamtwozonethree")) {
			sender.sendMessage(PBMethods.Prefix + this.getDescription());
			sender.sendMessage(PBMethods.Prefix + ChatColor.GOLD + "Applicable Zones: " + ChatColor.DARK_AQUA + "Field, Divider, TeamOneZoneOne, TeamOneZoneTwo, TeamOneZoneThree, TeamTwoZoneOne, TeamTwoZoneTwo, TeamTwoZoneThree");
			return;
		}
		
		if (z.equalsIgnoreCase("field")) {
			arena.setField(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Field to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("divider")) {
			arena.setDivider(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Divider to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamonezoneone")) {
			arena.setTeamOneZoneOne(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team One Zone One to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamonezonetwo")) {
			arena.setTeamOneZoneTwo(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team One Zone Two to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamonezonethree")) {
			arena.setTeamOneZoneThree(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team One Zone Three to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamtwozoneone")) {
			arena.setTeamTwoZoneOne(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team Two Zone One to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamtwozonetwo")) {
			arena.setTeamTwoZoneTwo(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team Two Zone Two to the region " + args.get(3));
		}
		if (z.equalsIgnoreCase("teamtwozonethree")) {
			arena.setTeamTwoZoneThree(region);
			sender.sendMessage(PBMethods.Prefix + ChatColor.GREEN + "You have updated " + arena.getName() + "'s Team Two Zone Three to the region " + args.get(3));
		}
		sender.sendMessage(ArenaMethods.getArenaSetupHelp(arena));
		return;
	}

}
