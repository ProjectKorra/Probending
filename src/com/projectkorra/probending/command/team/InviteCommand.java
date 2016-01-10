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
import com.projectkorra.projectkorra.GeneralMethods;

public class InviteCommand extends PBCommand {

	public InviteCommand() {
		super("team-invite", "/pb team invite <Player> [Element]", "Invite a player to your team.",
				new String[] { "invite", "i" }, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 3)) {
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
		
		List<Element> elements = GeneralMethods.getBendingPlayer(player.getName()).getElements();
		if (elements.size() == 0) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}

		String element = null;
		if (elements.size() > 0) {
			if (args.size() == 3) {
				element = args.get(2);
				if (!elements.contains(Element.getType(element))) {
					sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotElement);
					return;
				}
			} else {
				sender.sendMessage(PBMethods.Prefix + PBMethods.multiBendingTypes);
				return;
			}
		} else {
			element = elements.get(0).name();
		}

		if (element == null) {
			return;
		}

		if (!PBMethods.getAirAllowed() && element.equalsIgnoreCase("Air")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Airbenders"));
			return;
		}

		if (!PBMethods.getWaterAllowed() && element.equalsIgnoreCase("Water")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Waterbenders"));
			return;
		}

		if (!PBMethods.getEarthAllowed() && element.equalsIgnoreCase("Earth")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Earthbenders"));
			return;
		}

		if (!PBMethods.getFireAllowed() && element.equalsIgnoreCase("Fire")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Firebenders"));
			return;
		}

		if (!PBMethods.getChiAllowed() && element.equalsIgnoreCase("Chi")) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Chiblockers"));
			return;
		}

		Set<Element> teamelements = team.getElements();
		if (teamelements != null) {
			if (teamelements.contains(Element.getType(element))) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
				return;
			}
		}

		team.invites.put(player, Element.getType(element));
		sender.sendMessage(PBMethods.Prefix
				+ PBMethods.PlayerInviteSent.replace("%team", team.getName()).replace("%player", player.getName()));
		player.sendMessage(PBMethods.Prefix
				+ PBMethods.PlayerInviteReceived.replace("%team", team.getName()).replace("%player", player.getName()));
		player.sendMessage(PBMethods.Prefix
				+ PBMethods.InviteInstructions.replace("%team", team.getName()).replace("%player", player.getName()));
		return;
	}
}