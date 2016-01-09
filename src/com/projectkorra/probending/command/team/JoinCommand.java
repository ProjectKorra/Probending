package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
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
			return;
		}
		List<String> playerElements = PBMethods.getPlayerElementsAsString(uuid);

		if (playerElements == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}

		for (String e : playerElements) {
			if (Element.getType(e) == team.invites.get(Bukkit.getPlayer(uuid))) {
				Set<Element> elements = team.getElements();
				if (elements != null) {
					if (elements.contains(Element.getType(e))) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
						return;
					}
					if (!Probending.plugin.getConfig().getBoolean("TeamSettings.Allow" + Element.getType(e).toString())) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", Element.getType(e).toString()));
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
