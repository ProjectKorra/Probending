package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CreateCommand extends PBCommand {

	public CreateCommand() {
		super("create", "/pb team create <Team Name>", "Create a team.", new String[] {"create", "c"}, true, Commands.teamaliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();
		
		String teamName = args.get(1);
		if (PBMethods.teamExists(teamName)) {
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

		String playerElement = PBMethods.getPlayerElementAsString(uuid);

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

		if (econEnabled) {
			String currencyName = Probending.econ.currencyNamePlural();
			Double playerBalance = Probending.econ.getBalance((Player) sender);
			if (playerBalance < creationCost) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.NotEnoughMoney.replace("%currency", currencyName));
				return;
			}
			Probending.econ.withdrawPlayer((Player) sender, creationCost);
			Probending.econ.depositPlayer(serverAccount, creationCost);
			sender.sendMessage(PBMethods.Prefix + PBMethods.MoneyWithdrawn.replace("%amount", creationCost.toString()).replace("%currency", currencyName));
		}


		PBMethods.createTeam(teamName, uuid);
		PBMethods.addPlayerToTeam(teamName, uuid, playerElement);
		sender.sendMessage(PBMethods.Prefix + PBMethods.TeamCreated.replace("%team", teamName));
	}

}