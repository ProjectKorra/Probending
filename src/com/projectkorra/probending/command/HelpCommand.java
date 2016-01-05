package com.projectkorra.probending.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Executor for /probending help. Extends {@link PBCommand}.
 */
public class HelpCommand extends PBCommand {

	String[] teamround = {"team", "round"};
	
	public HelpCommand() {
		super ("help", "/probending help [Topic|Page]", "Displays help for Probending and Probending Commands.", new String[] {"help", "h"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		if (args.size() == 0 || isNumeric(args.get(0))) {
			int page = 1;
			if (args.size() > 1) {
				page = Integer.parseInt(args.get(0));
			}

			List<String> strings = new ArrayList<String>();
			for (PBCommand command : instances.values()) {
				if (!command.getName().equalsIgnoreCase("help") && !command.isChild()
						&& sender.hasPermission("probending.command." + command.getName())) {
					strings.add(command.getProperUse());
				}
			}
			Collections.sort(strings);
			Collections.reverse(strings);
			strings.add(instances.get("help").getProperUse());
			Collections.reverse(strings);
			for (String s : getPage(strings, ChatColor.GOLD + "Commands:", page, false)) {
				sender.sendMessage(ChatColor.DARK_AQUA + s);
			}
			return;
		}

		String arg = args.get(0);

		if (instances.keySet().contains(arg.toLowerCase())) {//bending help command
			instances.get(arg).help(sender, true);
		}
	}
}
