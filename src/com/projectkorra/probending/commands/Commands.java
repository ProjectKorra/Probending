package com.projectkorra.probending.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.managers.PBFieldCreationManager;
import com.projectkorra.probending.managers.PBQueueManager;
import com.projectkorra.probending.managers.ProbendingHandler;

import net.md_5.bungee.api.ChatColor;

public class Commands {
	
	private static ProbendingHandler _pHandler;
	private static PBFieldCreationManager _cManager;
    private static PBQueueManager _qManager;
	
	private List<String> help;

	public Commands(ProbendingHandler pHandler, PBFieldCreationManager cManager, PBQueueManager qManager) {
		_pHandler = pHandler;
		_cManager = cManager;
        _qManager = qManager;
		init();
	}
	
	public void init() {
		PluginCommand probending = Probending.get().getServer().getPluginCommand("probending");
		
		//Load commands
		new AdminCommand();
		new InfoCommand();
		new QueueJoinCommand();
		new QueueLeaveCommand();
		new TeamCommand();
		
		help = Arrays.asList("&6/probending queue {1/3} &eQueue up!", 
				"&6/probending leave &eLeave the queue!", 
				"&6/probending info [player] &eShow the stats of a player!", 
				"&6/probending admin &eShows administrator commands!",
				"&6/probending team &eTeam Commands!");
		
		CommandExecutor exe = new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				if (args.length == 0) {
					for (String s : help) {
						if (sender instanceof Player) {
							PBMessenger.sendMessage((Player)sender, ChatColor.translateAlternateColorCodes('&', s), false);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
						}
					}
					return true;
				}
				
				List<String> sendingArgs = Arrays.asList(args).subList(1, args.length);
				for (PBCommand command : PBCommand.getCommands().values()) {
					if (Arrays.asList(command.getAliases()).contains(args[0].toLowerCase())) {
						command.execute(sender, sendingArgs);
						return true;
					}
				}
				return false;
			}
			
		};
		probending.setExecutor(exe);
	}

    public static PBFieldCreationManager getFieldCreationManager() {
        return _cManager;
    }

    public static ProbendingHandler getProbendingHandler() {
        return _pHandler;
    }

    public static PBQueueManager getQueueManager() {
        return _qManager;
    }
}
