package com.projectkorra.probending.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoundCommand extends PBCommand {

	public RoundCommand() {
		super("round", "/probending round", "Displays help for all Round commands.", new String[] {"round", "r"}, true);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 4)) {
			return;
		}
		
		int page = 1;
		if (args.size() > 1) {
			if (isNumeric(args.get(0))) {
				page = Integer.parseInt(args.get(0));
			}
		}
		List<String> strings = new ArrayList<String>();
		for (PBCommand command : instances.values()) {
			if (command.isChild() && Arrays.asList(command.getParentAliases()).contains("arena")
					&& sender.hasPermission("probending.command.arena." + command.getName())) {
				strings.add(command.getProperUse() + ChatColor.WHITE + " - " + command.getDescription());
			}
		}
		for (String s : getPage(strings, ChatColor.GOLD + "Arena Commands:", page, true)) {
			sender.sendMessage(ChatColor.DARK_AQUA + s);
		}
		return;
	}
}