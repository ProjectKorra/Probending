package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;
import com.projectkorra.probending.objects.Round;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StopCommand extends PBCommand {

	public StopCommand() {
		super ("round-stop", "/pb round stop [Arena]", "Stops round.", new String[] {"stop"}, true, Commands.roundaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasRoundPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		Arena arena = PBMethods.getArena(args.get(1));
		if (!PBMethods.isRoundAtArena(arena)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NoOngoingRound);
			return;
		}
		
		Round round = PBMethods.getRoundAtArena(arena);
		for (Player player: round.getRoundPlayers()) {
			player.teleport(round.getArena().getSpectatorSpawn());
			PBMethods.restoreArmor(player);
			round.stop();
		}
		PBMethods.sendPBChat(PBMethods.RoundStopped);
		return;
	}
}
