package com.projectkorra.probending.command;

import com.projectkorra.probending.PBMethods;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Executor for /probending chat. Extends {@link PBCommand}.
 */
public class ChatCommand extends PBCommand {
	
	public ChatCommand() {
		super ("chat", "/probending chat", "Toggles Probending chat on or off.", new String[] {"chat", "c"});
	}
	
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasPermission(sender) || !correctLength(sender, args.size(), 0, 0)) {
			return;
		}
		
		Player p = (Player) sender;
		if (!Commands.pbChat.contains(p)) {
			Commands.pbChat.add(p);
			sender.sendMessage(PBMethods.Prefix + PBMethods.ChatEnabled);
			return;
		}
		if (Commands.pbChat.contains(p)) {
			Commands.pbChat.remove(p);
			sender.sendMessage(PBMethods.Prefix + PBMethods.ChatDisabled);
			return;
		}
	}
}
