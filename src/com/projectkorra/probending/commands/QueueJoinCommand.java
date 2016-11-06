package com.projectkorra.probending.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.PBMessenger.PBMessage;
import com.projectkorra.probending.enums.GamePlayerMode;
import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.ProbendingHandler;

import net.md_5.bungee.api.ChatColor;

public class QueueJoinCommand extends PBCommand {

	public QueueJoinCommand() {
		super("queue", "Join the queue for 1v1 or 3v3 matches", "/probending queue {1/3}", new String[] {"queue", "q"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args, ProbendingHandler pHandler, FieldCreationManager cManager) {
		if (!sender.hasPermission("probending.command.join")) {
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions");
			return;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.size() > 0) {
				if (args.get(0).equalsIgnoreCase("1")) {
					pHandler.quePlayer(player, GamePlayerMode.SINGLE);
				} else if (args.get(0).equalsIgnoreCase("3")) {
					pHandler.quePlayer(player, GamePlayerMode.TRIPLE);
				} else {
					PBMessenger.sendMessage(player, PBMessage.ERROR);
				}
			} else {
				pHandler.quePlayer(player, GamePlayerMode.ANY);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
		}
	}
}
