package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class KickCommand extends PBCommand {
	
	public KickCommand() {
		super ("kick", "/pb team kick <Player>", "Kick a player from your team.", new String[] {"kick", "k"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();

		String teamName = PBMethods.getPlayerTeam(uuid);
		if (teamName == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}
		if (!PBMethods.isPlayerOwner(uuid, teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}
		String playerName = args.get(1);
		if (playerName.equals(sender.getName())) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.CantBootFromOwnTeam);
			return;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer p3 = Bukkit.getOfflinePlayer(args.get(1));
		String playerTeam = null;
		String playerElement = null;

		if (p3 != null) {
			playerElement = PBMethods.getPlayerElementInTeam(p3.getUniqueId(), teamName);
			playerTeam = PBMethods.getPlayerTeam(p3.getUniqueId());
		}

		if (playerTeam == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotOnThisTeam);
			return;
		}
		if (!playerTeam.equals(teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotOnThisTeam);
			return;
		}
		PBMethods.removePlayerFromTeam(teamName, p3.getUniqueId(), playerElement);
		Player player = Bukkit.getPlayer(playerName);
		if (player != null) {
			player.sendMessage(PBMethods.Prefix + PBMethods.YouHaveBeenBooted.replace("%team", teamName));
		}
		for (Player player2: Bukkit.getOnlinePlayers()) {
			if (PBMethods.getPlayerTeam(player2.getUniqueId()) == null) continue;
			if (PBMethods.getPlayerTeam(player2.getUniqueId()).equals(teamName)) {
				player2.sendMessage(PBMethods.Prefix + PBMethods.PlayerHasBeenBooted.replace("%player", playerName).replace("%team", teamName));
			}
		}
		return;
	}
}