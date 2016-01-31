package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JoinCommand extends PBCommand {

	public JoinCommand() {
		super("team-join", "/pb team join <Team>", "Join a team.", new String[] { "join", "j" }, true, Commands.teamaliases);
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
		Team team = PBMethods.getTeam(args.get(1));

		if (!team.invites.containsKey(Bukkit.getPlayer(uuid))) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NoInviteFromTeam);
			return;
		}
		
		List<Element> elements = BendingPlayer.getBendingPlayer(sender.getName()).getElements();
		if (elements.size() == 0) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}

		for (Element e : elements) {
			if (e == team.invites.get(Bukkit.getPlayer(uuid))) {
				Set<Element> teamelements = team.getElements();
				if (teamelements != null) {
					if (teamelements.contains(e)) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
						return;
					}
					if (!Probending.plugin.getConfig().getBoolean("TeamSettings.Allow" + e.getName())) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", e.getName()));
						return;
					}
					team.addPlayer(uuid, team.invites.get(Bukkit.getPlayer(uuid)));
					for (Player player : Bukkit.getOnlinePlayers()) {
						Team team2 = PBMethods.getPlayerTeam(player.getUniqueId());
						if (team2 != null) {
							if (PBMethods.getPlayerTeam(player.getUniqueId()).getName().equalsIgnoreCase(team.getName())) {
								player.sendMessage(PBMethods.Prefix + PBMethods.PlayerJoinedTeam
										.replace("%player", sender.getName()).replace("%team", team.getName()));
							}
						}
					}
				}
				team.invites.remove(Bukkit.getPlayer(uuid));
				return;
			}
		}
	}
}
