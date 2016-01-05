package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResumeCommand extends PBCommand {

	public ResumeCommand() {
		super ("resume", "/pb round resume", "Resume round.", new String[] {"resume", "r"}, true, Commands.roundaliases);
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
		if (!PBMethods.matchPaused) {

			return;
		}
		PBMethods.matchPaused = false;
		PBMethods.sendPBChat(PBMethods.RoundResumed.replace("%seconds", String.valueOf(Commands.currentNumber / 20)));

		Commands.clockTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Probending.plugin, new Runnable() {
			public void run() {
				Commands.currentNumber--;
				if (Commands.currentNumber == 1200) {
					PBMethods.sendPBChat(PBMethods.OneMinuteRemaining);
				}
				if (Commands.currentNumber == 0) {
					PBMethods.sendPBChat(PBMethods.RoundComplete);
					PBMethods.matchStarted = false;
					Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
					PBMethods.restoreArmor();
				}
			}
		}, 0L, 1L);
	}
}
