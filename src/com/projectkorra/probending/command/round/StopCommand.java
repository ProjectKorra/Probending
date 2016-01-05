package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StopCommand extends PBCommand {

	public StopCommand() {
		super ("stop", "/pb round stop", "Stops round.", new String[] {"stop"}, true, Commands.roundaliases);
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
		PBMethods.restoreArmor();

		Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);

		PBMethods.matchPaused = false;
		PBMethods.playingTeams.clear();
		PBMethods.matchStarted = false;
		PBMethods.sendPBChat(PBMethods.RoundStopped);
		return;
	}
}
