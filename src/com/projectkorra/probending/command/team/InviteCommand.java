package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class InviteCommand extends PBCommand {
	
	public InviteCommand() {
		super ("team-invite", "/pb team invite <Player>", "Invite a player to your team.", new String[] {"invite", "i"}, true, Commands.teamaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		if (!PBMethods.playerInTeam(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}

		String playerTeam = PBMethods.getPlayerTeam(uuid);
		if (!PBMethods.isPlayerOwner(uuid, playerTeam)) {
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

		if (!Commands.teamInvites.containsKey(player.getName())) {
			Commands.teamInvites.put(player.getName(), new LinkedList<String>());
		}

		int maxSize = Probending.plugin.getConfig().getInt("TeamSettings.MaxTeamSize");
		if (PBMethods.getTeamSize(playerTeam) >= maxSize) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.MaxSizeReached);
			return;
		}
		String playerElement = PBMethods.getPlayerElementAsString(player.getUniqueId());

		if (playerElement == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}
		if (!PBMethods.getAirAllowed()) {
			if (playerElement.equals("Air")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Airbenders"));
				return;
			}
		}
		if (!PBMethods.getWaterAllowed()) {
			if (playerElement.equals("Water")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Waterbenders"));
				return;
			}
		}
		if (!PBMethods.getEarthAllowed()) {
			if (playerElement.equals("Earth")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Earthbenders"));
				return;
			}
		}
		if (!PBMethods.getFireAllowed()) {
			if (playerElement.equals("Fire")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Firebenders"));
				return;
			}
		}
		if (!PBMethods.getChiAllowed()) {
			if (playerElement.equals("Chi")) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.ElementNotAllowed.replace("%element", "Chiblockers"));
				return;
			}
		}
		Set<String> teamelements = PBMethods.getTeamElements(playerTeam);
		if (teamelements != null) {
			if (teamelements.contains(playerElement)) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyHasElement);
				return;
			}
		}

		Commands.teamInvites.get(player.getName()).add(playerTeam);
		sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerInviteSent.replace("%team", playerTeam).replace("%player", player.getName()));
		player.sendMessage(PBMethods.Prefix + PBMethods.PlayerInviteReceived.replace("%team", playerTeam).replace("%player", player.getName()));
		player.sendMessage(PBMethods.Prefix + PBMethods.InviteInstructions.replace("%team", playerTeam).replace("%player", player.getName()));
		return;
	}
}