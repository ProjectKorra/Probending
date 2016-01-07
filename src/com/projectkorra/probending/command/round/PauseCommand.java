package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PauseCommand extends PBCommand {

	public PauseCommand() {
		super ("round-pause", "/pb round pause", "Pauses round.", new String[] {"pause", "pa"}, true, Commands.roundaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasRoundPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}

		if (!PBMethods.matchStarted) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NoOngoingRound);
			return;
		}
		Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
		PBMethods.matchPaused = true;
		PBMethods.sendPBChat(PBMethods.RoundPaused.replace("%seconds", String.valueOf(Commands.currentNumber / 20)));
	}
}
