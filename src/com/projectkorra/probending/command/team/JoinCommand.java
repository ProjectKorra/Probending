package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JoinCommand extends PBCommand {
	
	public JoinCommand() {
		super ("join", "/pb team join <Team>", "Join a team.", new String[] {"join", "j"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		if (PBMethods.playerInTeam(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerAlreadyInTeam);
			return;
		}
		String teamName = args.get(1);
		if (Commands.teamInvites.get(sender.getName()) == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NoInviteFromTeam);
			return;
		}
		if (!Commands.teamInvites.get(sender.getName()).contains(teamName)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NoInviteFromTeam);
			return;
		}
		String playerElement = PBMethods.getPlayerElementAsString(uuid);

		if (playerElement == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}
		Set<String> teamelements = PBMethods.getTeamElements(teamName);
		if (teamelements != null) {
			if (teamelements.contains(playerElement)) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
				return;
			}
			if (!Probending.plugin.getConfig().getBoolean("TeamSettings.Allow" + playerElement)) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", playerElement));
				return;
			}
			PBMethods.addPlayerToTeam(teamName, uuid, playerElement);
			for (Player player: Bukkit.getOnlinePlayers()) {
				String teamName2 = PBMethods.getPlayerTeam(player.getUniqueId());
				if (teamName2 != null) {
					if (PBMethods.getPlayerTeam(player.getUniqueId()).equalsIgnoreCase(teamName)) {
						player.sendMessage(PBMethods.Prefix + PBMethods.PlayerJoinedTeam.replace("%player", sender.getName()).replace("%team", teamName));
					}
				}
			}
		}
		Commands.teamInvites.remove(sender.getName());
		return;
	}
}
