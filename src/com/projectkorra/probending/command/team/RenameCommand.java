package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RenameCommand extends PBCommand {
	
	public RenameCommand() {
		super ("team-rename", "/pb team rename <Name>", "Rename your team.", new String[] {"rename", "name"}, true, Commands.teamaliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();

		Team team = PBMethods.getPlayerTeam(uuid);
		if (!PBMethods.playerInTeam(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}
		if (!team.isOwner(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}
		boolean econEnabled = Probending.plugin.getConfig().getBoolean("Economy.Enabled");

		String newName = args.get(1);
		if (newName.length() > 15) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NameTooLong);
			return;
		}
		if (newName.equalsIgnoreCase(team.getName())) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamAlreadyNamedThat.replace("%newname", team.getName()));
			return;
		}
		if (econEnabled) {
			Double playerBalance = Probending.econ.getBalance((Player) sender);
			Double renameFee = Probending.plugin.getConfig().getDouble("Economy.TeamRenameFee");
			String serverAccount = Probending.plugin.getConfig().getString("Economy.ServerAccount");
			String currency = Probending.econ.currencyNamePlural();
			if (playerBalance < renameFee) {
				sender.sendMessage(PBMethods.Prefix + PBMethods.NotEnoughMoney.replace("%amount", renameFee.toString()).replace("%currency", currency));
				return;
			}
			Probending.econ.withdrawPlayer((Player) sender, renameFee);
			Probending.econ.depositPlayer(serverAccount, renameFee);
			sender.sendMessage(PBMethods.Prefix + PBMethods.MoneyWithdrawn.replace("%amount", renameFee.toString()).replace("%currency", currency));
		}

		int Wins = team.getWins();
		int Losses = team.getLosses();

		PBMethods.createTeam(newName, uuid);
		Team newTeam = PBMethods.getTeam(newName);

		UUID air = team.getAirbender();
		UUID water = team.getWaterbender();
		UUID earth = team.getEarthbender();
		UUID fire = team.getFirebender();
		UUID chi = team.getChiblocker();

		if (team.hasAirbender()) {
			team.removePlayer(team.getAirbender());
			newTeam.addPlayer(air, "Air");
		}
		if (team.hasWaterbender()) {
			team.removePlayer(team.getWaterbender());
			newTeam.addPlayer(water, "Water");
		}
		if (team.hasEarthbender()) {
			team.removePlayer(team.getEarthbender());
			newTeam.addPlayer(earth, "Earth");
		}
		if (team.hasFirebender()) {
			team.removePlayer(team.getFirebender());
			newTeam.addPlayer(fire, "Fire");
		}
		if (team.hasChiblocker()) {
			team.removePlayer(team.getChiblocker());
			newTeam.addPlayer(chi, "Chi");
		}
		
		newTeam.setLosses(Losses);
		newTeam.setWins(Wins);

		sender.sendMessage(PBMethods.Prefix + PBMethods.TeamRenamed.replace("%newname", newName));
		newTeam.setOwner(uuid);
		team.delete();
		return;
	}
}
