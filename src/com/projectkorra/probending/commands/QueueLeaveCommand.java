package com.projectkorra.probending.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.PBMessenger.PBMessage;
import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.ProbendingHandler;

import net.md_5.bungee.api.ChatColor;

public class QueueLeaveCommand extends PBCommand{

	public QueueLeaveCommand() {
		super("leave", "Leave the probending match queue!", "/probending leave", new String[] {"leave", "l"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args, ProbendingHandler pHandler, FieldCreationManager cManager) {
		if (!sender.hasPermission("probending.command.leave")) {
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions");
			return;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.size() > 0) {
				PBMessenger.sendMessage(player, PBMessage.ERROR);
			} else {
				pHandler.removePlayerFromQueue(player);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
		}
	}

}
