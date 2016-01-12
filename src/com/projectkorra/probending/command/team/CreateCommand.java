package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CreateCommand extends PBCommand {

	public CreateCommand() {
		super("team-create", "/pb team create <Team Name> [Element]", "Create a team.", new String[] { "create", "c" }, true,
				Commands.teamaliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 3)) {
			return;
		}

		UUID uuid = ((Player) sender).getUniqueId();

		String teamName = args.get(1);
		if (PBMethods.getTeam(teamName) != null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.teamAlreadyExists);
			return;
		}

		if (teamName.length() > 15) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NameTooLong);
			return;
		}

		List<Element> elements = GeneralMethods.getBendingPlayer(sender.getName()).getElements();
		if (elements.size() == 0) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
			return;
		}

		if (PBMethods.playerInTeam(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerAlreadyInTeam);
			return;
		}

		Double creationCost = Probending.plugin.getConfig().getDouble("Economy.TeamCreationFee");
		String serverAccount = Probending.plugin.getConfig().getString("Economy.ServerAccount");
		boolean econEnabled = Probending.plugin.getConfig().getBoolean("Economy.Enabled");

		String element = null;
		if (elements.size() > 1) {
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

		if (econEnabled) {
			String currencyName = Probending.econ.currencyNamePlural();
			Double playerBalance = Probending.econ.getBalance((Player) sender);
			if (playerBalance < creationCost) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.NotEnoughMoney.replace("%currency", currencyName));
				return;
			}
			Probending.econ.withdrawPlayer((Player) sender, creationCost);
			Probending.econ.depositPlayer(serverAccount, creationCost);
			sender.sendMessage(PBMethods.Prefix + PBMethods.MoneyWithdrawn.replace("%amount", creationCost.toString())
					.replace("%currency", currencyName));
		}

		PBMethods.createTeam(teamName, uuid);
		Team team = PBMethods.getTeam(teamName);
		team.addPlayer(uuid, Element.getType(element));
		sender.sendMessage(PBMethods.Prefix + PBMethods.TeamCreated.replace("%team", teamName));
	}
}