package com.projectkorra.probending.command;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Executor for /probending reload. Extends {@link PBCommand}.
 */
public class ReloadCommand extends PBCommand {
	
	public ReloadCommand() {
		super ("reload", "/probending reload", "Reloads the Probending plugin.", new String[] {"reload"});
	}
	
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasPermission(sender) || !correctLength(sender, args.size(), 0, 0)) {
			return;
		}
		Probending.plugin.reloadConfig();
		sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "Version: " + Probending.plugin.getDescription().getVersion());
		sender.sendMessage(PBMethods.Prefix + PBMethods.configReloaded);
	}
}
