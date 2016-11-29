package com.projectkorra.probending.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.objects.PBTeam;
import org.bukkit.ChatColor;


public class InfoCommand extends PBCommand{

	public InfoCommand() {
		super("info", "Shows the probending stats for a player!", "/probending info [player]", new String[] {"info", "i"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!sender.hasPermission("probending.command.info")) {
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions");
			return;
		}
		if (args.size() != 1) {
			sender.sendMessage(ChatColor.RED + "Incorrect argument length! Try: " + getProperUse());
		} else {
			Player target = Bukkit.getPlayer(args.get(0));
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player not found!");
			} else {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					Commands.getProbendingHandler().getPlayerInfo(player, target);
				} else {
					PBPlayer pbTarget = Commands.getProbendingHandler().getPBPlayer(target.getUniqueId());
					PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(target);
					sender.sendMessage("Solo Mode Wins: " + pbTarget.getIndividualWins(true));
					sender.sendMessage("Triple Mode Wins: " + pbTarget.getIndividualWins(false));
					sender.sendMessage("Total Games Played: " + pbTarget.getGamesPlayed());
					sender.sendMessage("Rating: disabled");
					sender.sendMessage("Team Profile:");
					sender.sendMessage("- Team Name: " + (team != null ? team.getTeamName() : "N/A"));
					sender.sendMessage("- Elemental Role: " + (team != null ? team.getMembers().get(target.getUniqueId()).getElement() : "N/A"));
					sender.sendMessage("- Total Wins: " + (team != null ? team.getWins() : "0"));
					sender.sendMessage("- Total Games Played: " + (team != null ? team.getGamesPlayed() : "0"));
				}
			}
		}
	}

}
