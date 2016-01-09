package com.projectkorra.probending.command.team;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.Element;

public class InviteCommand extends PBCommand {

	public InviteCommand() {
		super("team-invite", "/pb team invite <Player> <Element>", "Invite a player to your team.",
				new String[] { "invite", "i" }, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 3, 3)) {
			return;
		}

		UUID uuid = ((Player) sender).getUniqueId();
		if (!PBMethods.playerInTeam(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}

		Team team = PBMethods.getPlayerTeam(uuid);
		if (!team.isOwner(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}

		Player player = Bukkit.getPlayer(args.get(1));

		if (player == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotOnline);
			return;
		}

		if (PBMethods.playerInTeam(player.getUniqueId())) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerAlreadyInTeam);
			return;
		}

		int maxSize = Probending.plugin.getConfig().getInt("TeamSettings.MaxTeamSize");
		if (team.getSize() >= maxSize) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.MaxSizeReached);
			return;
		}

		Element element = Element.getType(args.get(2));
		List<String> playerElements = PBMethods.getPlayerElementsAsString(player.getUniqueId());

		if (element == null) {
			return;
		}

		if (playerElements == null) {
			player.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}

		for (String e : playerElements) {
			if (Element.getType(e) == element) {
				if (element == Element.Air) {
					if (!PBMethods.getAirAllowed()) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Airbenders"));
						return;
					}
				} else if (Element.getType(e) == Element.Water) {
					if (!PBMethods.getWaterAllowed()) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Waterbenders"));
						return;
					}
				} else if (Element.getType(e) == Element.Earth) {
					if (!PBMethods.getEarthAllowed()) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Earthbenders"));
						return;
					}
				} else if (Element.getType(e) == Element.Fire) {
					if (!PBMethods.getFireAllowed()) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Firebenders"));
						return;
					}
				} else if (Element.getType(e) == Element.Chi) {
					if (!PBMethods.getAirAllowed()) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Chiblockers"));
						return;
					}
				} else {
					return;
				}

				Set<Element> elements = team.getElements();
				if (elements != null) {
					if (elements.contains(Element.getType(e))) {
						sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
						return;
					}
				}

				team.invites.put(player, Element.getType(e));
				sender.sendMessage(PBMethods.Prefix
						+ PBMethods.PlayerInviteSent.replace("%team", team.getName()).replace("%player", player.getName()));
				player.sendMessage(PBMethods.Prefix
						+ PBMethods.PlayerInviteReceived.replace("%team", team.getName()).replace("%player", player.getName()));
				player.sendMessage(PBMethods.Prefix
						+ PBMethods.InviteInstructions.replace("%team", team.getName()).replace("%player", player.getName()));
				return;
			}
		}
	}
}