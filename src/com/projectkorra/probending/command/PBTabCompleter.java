package com.projectkorra.probending.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.command.BendingTabComplete;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class PBTabCompleter implements TabCompleter
{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		if (args.length <= 1)
			return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, getCommandsForUser(sender));
		
		else if (args.length >= 2)
		{
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))
			{
				if (!sender.hasPermission("probending.command.help") || args.length > 2) return new ArrayList<String>();
				List<String> commands = getCommandsForUser(sender);
				commands.remove("help");
				return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, commands);
			}
			else if (args[0].equalsIgnoreCase("round") || args[0].equalsIgnoreCase("r"))
			{
				if (!sender.hasPermission("probending.command.round")) return new ArrayList<String>();
				if (args.length == 2)
				{
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, Arrays.asList(new String[] {"start", "stop"}));
				}
				else if (args[1].equalsIgnoreCase("start"))
				{
					if (args.length <= 4) return getTeams(args);
					if (args.length == 5) return getArenas(args);
				}
				else if (args[1].equalsIgnoreCase("stop"))
				{
					if (args.length == 3) return getArenas(args);
				}
			}
			else if (args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("teams") || args[0].equalsIgnoreCase("t"))
			{
				if (!sender.hasPermission("probending.command.team")) return new ArrayList<String>();
				if (args.length == 2)
				{
					List<String> commands = new ArrayList<String>();
					for (PBCommand cmd : PBCommand.instances.values()) 
					{
						if (cmd.isChild() && Arrays.asList(cmd.getParentAliases()).contains("team") && sender.hasPermission("probending.command.team." + cmd.getName())) 
						{
							commands.add(cmd.getName().replaceFirst("team\\-", ""));
						}
					}
					Collections.sort(commands);
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, commands);
				}
				else if (args[1].equalsIgnoreCase("addloss") || args[1].equalsIgnoreCase("loss"))
				{
					if (!sender.hasPermission("probending.command.team.addloss") || args.length > 3) return new ArrayList<String>();
					return getTeams(args);
				}
				else if (args[1].equalsIgnoreCase("addwin") || args[1].equalsIgnoreCase("win"))
				{
					if (!sender.hasPermission("probending.command.team.addwin") || args.length > 3) return new ArrayList<String>();
					return getTeams(args);
				}
				else if (args[1].equalsIgnoreCase("disband") || args[1].equalsIgnoreCase("dis") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete"))
				{
					if (!sender.hasPermission("probending.command.team.disband")) return new ArrayList<String>();
					if (args.length == 3 && sender.hasPermission("probending.command.team.disband.other")) return getTeams(args);
				}
				else if (args[1].equalsIgnoreCase("invite") || args[1].equalsIgnoreCase("i"))
				{
					if (!sender.hasPermission("probending.command.team.invite") || !(sender instanceof Player) || 
							!PBMethods.playerInTeam(((Player)sender).getUniqueId()) || args.length > 3) return new ArrayList<String>();
					return getPlayers(args);
				}
				else if (args[1].equalsIgnoreCase("join") || args[1].equalsIgnoreCase("j"))
				{
					if (!sender.hasPermission("probending.command.team.join") || !(sender instanceof Player) || 
							PBMethods.playerInTeam(((Player)sender).getUniqueId()) || args.length > 3) return new ArrayList<String>();
					List<String> teams = new ArrayList<String>();
					for (Team team : Team.teams.values())
					{
						if (team.invites.containsKey((Player)sender)) teams.add(team.getName());
					}
					Collections.sort(teams);
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, teams);
				}
				else if (args[1].equalsIgnoreCase("kick") || args[1].equalsIgnoreCase("kick"))
				{
					if (!sender.hasPermission("probending.command.team.kick") || !(sender instanceof Player) || 
							!PBMethods.playerInTeam(((Player)sender).getUniqueId()) || args.length > 3) return new ArrayList<String>();
					
					Team team = PBMethods.getPlayerTeam(((Player)sender).getUniqueId());
					
					if (!team.isOwner(((Player)sender).getUniqueId())) return new ArrayList<String>();
					List<String> players = new ArrayList<String>();
					for (Player p : team.getOnlinePlayers())
					{
						if (!p.getName().equalsIgnoreCase(sender.getName())) players.add(p.getName());
					}
					Collections.sort(players);
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, players);
				}
				else if (args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i"))
				{
					if (!sender.hasPermission("probending.command.team.info")) return new ArrayList<String>();
					if (args.length == 3) return getTeams(args);
				}
				else if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("c"))
				{
					if (!sender.hasPermission("probending.command.team.create") || args.length != 4) return new ArrayList<String>();
					List<String> elements = new ArrayList<String>();
					for (Element e : BendingPlayer.getBendingPlayer((Player)sender).getElements())
					{
						elements.add(e.getName());
					}
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, elements);
				}
			}
			else if (args[0].equalsIgnoreCase("arena") || args[0].equalsIgnoreCase("a"))
			{
				if (!sender.hasPermission("probending.command.arena")) return new ArrayList<String>();
				if (args.length == 2)
				{
					List<String> commands = new ArrayList<String>();
					for (PBCommand cmd : PBCommand.instances.values()) 
					{
						if (cmd.isChild() && Arrays.asList(cmd.getParentAliases()).contains("arena") && sender.hasPermission("probending.command.arena." + cmd.getName())) 
						{
							commands.add(cmd.getName().replaceFirst("arena\\-", ""));
						}
					}
					Collections.sort(commands);
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, commands);
				}
				else if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("c"))
				{
					if (!sender.hasPermission("probending.command.arena.create")) return new ArrayList<String>();
					if (args.length >= 4 && args.length <= 5)
					{
						List<String> colors = new ArrayList<String>();
						colors.addAll(PBMethods.colors);
						Collections.sort(colors);
						return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, colors);
					}
				}
				else if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("d"))
				{
					if (!sender.hasPermission("probending.command.arena.delete") || args.length > 3) return new ArrayList<String>();
					return getArenas(args);
					
				}
				else if (args[1].equalsIgnoreCase("setspawn") || args[1].equalsIgnoreCase("ss"))
				{
					if (!sender.hasPermission("probending.command.arena.setspawn")) return new ArrayList<String>();
					if (args.length == 3) return getArenas(args);
					if (args.length == 4) return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, Arrays.asList(new String[] {"Spectator", "TeamOne", "TeamTwo"}));
				}
				else if (args[1].equalsIgnoreCase("setworld") || args[1].equalsIgnoreCase("sw"))
				{
					if (!sender.hasPermission("probending.command.arena.setworld") || args.length > 3) return new ArrayList<String>();
					List<String> worlds = new ArrayList<String>();
					for (World world : Bukkit.getWorlds())
					{
						worlds.add(world.getName());
					}
					return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, worlds);
				}
				else if (args[1].equalsIgnoreCase("setzone") || args[1].equalsIgnoreCase("sz"))
				{
					if (!sender.hasPermission("probending.command.arena.setzone")) return new ArrayList<String>();
					if (args.length == 3) return getArenas(args);
					if (args.length == 4) return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, 
							Arrays.asList(new String[] {"Divider", "Field", "TeamOneZoneOne", "TeamOneZoneTwo", "TeamOneZoneThree", "TeamTwoZoneOne", "TeamTwoZoneTwo", "TeamTwoZoneThree"}));
					if (args.length == 5)
					{
						List<String> regions = new ArrayList<String>();
						regions.addAll(WorldGuardPlugin.inst().getRegionManager(((Player)sender).getWorld()).getRegions().keySet());
						Collections.sort(regions);
						return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, regions);
					}
				}
			}
		}
		return new ArrayList<String>();
	}
	
	/** Returns a list of subcommands the sender can use. */
	public static List<String> getCommandsForUser(CommandSender sender) {
		List<String> list = new ArrayList<String>();
		for (String cmd : PBCommand.instances.keySet()) {
			if (!PBCommand.instances.get(cmd).isChild() && sender.hasPermission("probending.command." + cmd.toLowerCase()))
				list.add(cmd);
		}
		Collections.sort(list);
		return list;
	}
	
	public List<String> getTeams(String[] args)
	{
		List<String> list = new ArrayList<String>();
		list.addAll(PBMethods.getTeams().keySet());
		Collections.sort(list);
		return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, list);
	}
	
	public List<String> getArenas(String[] args)
	{
		List<String> list = new ArrayList<String>();
		list.addAll(PBMethods.getArenas().keySet());
		Collections.sort(list);
		return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, list);
	}
	
	public List<String> getPlayers(String[] args)
	{
		List<String> list = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			list.add(p.getName());
		}
		Collections.sort(list);
		return BendingTabComplete.getPossibleCompletionsForGivenArgs(args, list);
	}
}
