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
		super("team-create", "/pb team create <Team Name> <Element>", "Create a team.", new String[] { "create", "c" }, true,
				Commands.teamaliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 3, 3)) {
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

		if (GeneralMethods.getBendingPlayer(sender.getName()).getElements().size() == 0) {
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

		Element element = Element.getType(args.get(2));
		List<String> playerElements = PBMethods.getPlayerElementsAsString(uuid);

		if (element == null) {
			return;
		}

		if (playerElements == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noBendingType);
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
				team.addPlayer(uuid, Element.getType(e));
				sender.sendMessage(PBMethods.Prefix + PBMethods.TeamCreated.replace("%team", teamName));
			}
		}
	}
}