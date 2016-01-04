package com.projectkorra.probending;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.storage.DBConnection;

public class Probending extends JavaPlugin {
	
	public static Probending plugin;
	public static Logger log;
	
	public static Economy econ;
	
	Commands cmd;
	PBMethods methods;
	public void onEnable() {
		Probending.log = this.getLogger();
		plugin = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		new Commands(this);
		
		//Database Stuff
		DBConnection.engine = getConfig().getString("General.Storage");
		DBConnection.host = getConfig().getString("MySQL.host", "localhost");
		DBConnection.pass = getConfig().getString("MySQL.pass", "");
		DBConnection.port = getConfig().getInt("MySQL.port", 3306);
		DBConnection.db = getConfig().getString("MySQL.db", "minecraft");
		DBConnection.user = getConfig().getString("MySQL.user", "root");
		
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		if (getConfig().getBoolean("Economy.Enabled")) {
			PBMethods.setupEconomy();
		}
		
		PBMethods.populateColors();
		
		DBConnection.init();
		
		PBMethods.loadTeams();
		Set<String> teamList = PBMethods.getTeams();
		PBMethods.loadPlayers();
		Probending.log.info("Loaded " + teamList.size() + " teams");
		Probending.log.info("Loaded " + PBMethods.players.size() + " players.");
		
		/*
		 * initialize strings
		 */
		PBMethods.Prefix = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.general.Prefix"));
		PBMethods.noPermission = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.general.noPermission"));
		PBMethods.configReloaded = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.general.configReloaded"));
		PBMethods.NoTeamPermissions = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.general.NoTeamPermissions"));
		
		/*
		 * Player
		 */
		PBMethods.noBendingType = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.noBendingType"));
		PBMethods.PlayerAlreadyInTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.PlayerAlreadyInTeam"));
		PBMethods.ElementNotAllowed = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.ElementNotAllowed"));
		PBMethods.PlayerNotInTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.PlayerNotInTeam"));
		PBMethods.PlayerNotOnline = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.PlayerNotOnline"));
		PBMethods.PlayerInviteSent = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.PlayerInviteSent"));
		PBMethods.PlayerInviteReceived = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.PlayerInviteReceived"));
		PBMethods.InviteInstructions = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.InviteInstructions"));
		PBMethods.NoInviteFromTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.NoInviteFromTeam"));
		PBMethods.YouHaveBeenBooted = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.YouHaveBeenBooted"));
		PBMethods.YouHaveQuit = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.YouHaveQuit"));
		PBMethods.RemovedFromTeamBecauseDifferentElement = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.player.RemovedFromTeamBecauseDifferentElement"));
		
		/*
		 * Team
		 */
		PBMethods.teamAlreadyExists = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.teamAlreadyExists"));
		PBMethods.TeamCreated = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamCreated"));
		PBMethods.NotOwnerOfTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.NotOwnerOfTeam"));
		PBMethods.MaxSizeReached = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.MaxSizeReached"));
		PBMethods.TeamAlreadyHasElement = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamAlreadyHasElement"));
		PBMethods.TeamDoesNotExist = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamDoesNotExist"));
		PBMethods.PlayerJoinedTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.PlayerJoinedTeam"));
		PBMethods.CantBootFromOwnTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.CantBootFromOwnTeam"));
		PBMethods.PlayerNotOnThisTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.PlayerNotOnThisTeam"));
		PBMethods.PlayerHasBeenBooted = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.PlayerHasBeenBooted"));
		PBMethods.PlayerHasQuit = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.PlayerHasQuit"));
		PBMethods.TeamDisbanded = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamDisbanded"));
		PBMethods.NameTooLong = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.NameTooLong"));
		PBMethods.TeamRenamed = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamRenamed"));
		PBMethods.TeamAlreadyNamedThat = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.TeamAleadyNamedThat"));
		PBMethods.OwnerNotOnline = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.team.OwnerNotOnline"));
		
		/*
		 * Economy
		 */
		
		PBMethods.NotEnoughMoney = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.economy.NotEnoughMoney"));
		PBMethods.MoneyWithdrawn = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.economy.MoneyWithdrawn"));
		
		/*
		 * Round
		 */
		
		PBMethods.OneMinuteRemaining = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.OneMinuteRemaining"));
		PBMethods.RoundComplete = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundComplete"));
		PBMethods.RoundAlreadyGoing = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundAlreadyGoing"));
		PBMethods.InvalidTeamSize = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.InvalidTeamSize"));
		PBMethods.RoundStarted = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundStarted"));
		PBMethods.RoundStopped = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundStopped"));
		PBMethods.RoundPaused = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundPaused"));
		PBMethods.RoundResumed = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundResumed"));
		PBMethods.NoOngoingRound = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.NoOngoingRound"));
		PBMethods.PlayerEliminated = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.PlayerEliminated"));
		PBMethods.PlayerFouled = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.PlayerFouled"));
		PBMethods.RoundEnded = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.RoundEnded"));
		PBMethods.TeamWon = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.TeamWon"));
		PBMethods.MoveUpOneZone = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.MoveUpOneZone"));
		PBMethods.CantEnterField = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.CantEnterField"));
		PBMethods.CantTeleportDuringMatch = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.CantTeleportDuringMatch"));
		PBMethods.MoveNotAllowed = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.round.MoveNotAllowed"));

		/*
		 * Misc
		 */

		PBMethods.ChatEnabled = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.misc.ChatEnabled"));
		PBMethods.ChatDisabled = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.misc.ChatDisabled"));
		PBMethods.WinAddedToTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.misc.WinAddedToTeam"));
		PBMethods.LossAddedToTeam = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.misc.LossAddedToTeam"));
		PBMethods.TeamSpawnSet = PBMethods.colorize(Probending.plugin.getConfig().getString("messages.misc.TeamSpawnSet"));

		
	}

}
