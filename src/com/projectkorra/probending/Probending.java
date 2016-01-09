package com.projectkorra.probending;

import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.objects.Round;
import com.projectkorra.probending.storage.DBConnection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
		new PBMethods(this);
		
		/*
		 * initialize strings
		 */
		PBMethods.Prefix = PBMethods.colorize(getConfig().getString("messages.general.Prefix"));
		PBMethods.noPermission = PBMethods.colorize(getConfig().getString("messages.general.noPermission"));
		PBMethods.configReloaded = PBMethods.colorize(getConfig().getString("messages.general.configReloaded"));
		PBMethods.NoTeamPermissions = PBMethods.colorize(getConfig().getString("messages.general.NoTeamPermissions"));
		
		/*
		 * Player
		 */
		PBMethods.noBendingType = PBMethods.colorize(getConfig().getString("messages.player.noBendingType"));
		PBMethods.multiBendingTypes = PBMethods.colorize(getConfig().getString("messages.player.MultipleBendingTypes"));
		PBMethods.PlayerNotElement = PBMethods.colorize(getConfig().getString("messages.player.PlayerNotElement"));
		PBMethods.PlayerAlreadyInTeam = PBMethods.colorize(getConfig().getString("messages.player.PlayerAlreadyInTeam"));
		PBMethods.ElementNotAllowed = PBMethods.colorize(getConfig().getString("messages.player.ElementNotAllowed"));
		PBMethods.PlayerNotInTeam = PBMethods.colorize(getConfig().getString("messages.player.PlayerNotInTeam"));
		PBMethods.PlayerNotOnline = PBMethods.colorize(getConfig().getString("messages.player.PlayerNotOnline"));
		PBMethods.PlayerInviteSent = PBMethods.colorize(getConfig().getString("messages.player.PlayerInviteSent"));
		PBMethods.PlayerInviteReceived = PBMethods.colorize(getConfig().getString("messages.player.PlayerInviteReceived"));
		PBMethods.InviteInstructions = PBMethods.colorize(getConfig().getString("messages.player.InviteInstructions"));
		PBMethods.NoInviteFromTeam = PBMethods.colorize(getConfig().getString("messages.player.NoInviteFromTeam"));
		PBMethods.YouHaveBeenBooted = PBMethods.colorize(getConfig().getString("messages.player.YouHaveBeenBooted"));
		PBMethods.YouHaveQuit = PBMethods.colorize(getConfig().getString("messages.player.YouHaveQuit"));
		PBMethods.RemovedFromTeamBecauseDifferentElement = PBMethods.colorize(getConfig().getString("messages.player.RemovedFromTeamBecauseDifferentElement"));
		
		/*
		 * Team
		 */
		PBMethods.teamAlreadyExists = PBMethods.colorize(getConfig().getString("messages.team.teamAlreadyExists"));
		PBMethods.TeamCreated = PBMethods.colorize(getConfig().getString("messages.team.TeamCreated"));
		PBMethods.NotOwnerOfTeam = PBMethods.colorize(getConfig().getString("messages.team.NotOwnerOfTeam"));
		PBMethods.MaxSizeReached = PBMethods.colorize(getConfig().getString("messages.team.MaxSizeReached"));
		PBMethods.TeamAlreadyHasElement = PBMethods.colorize(getConfig().getString("messages.team.TeamAlreadyHasElement"));
		PBMethods.TeamDoesNotExist = PBMethods.colorize(getConfig().getString("messages.team.TeamDoesNotExist"));
		PBMethods.PlayerJoinedTeam = PBMethods.colorize(getConfig().getString("messages.team.PlayerJoinedTeam"));
		PBMethods.CantBootFromOwnTeam = PBMethods.colorize(getConfig().getString("messages.team.CantBootFromOwnTeam"));
		PBMethods.PlayerNotOnThisTeam = PBMethods.colorize(getConfig().getString("messages.team.PlayerNotOnThisTeam"));
		PBMethods.PlayerHasBeenBooted = PBMethods.colorize(getConfig().getString("messages.team.PlayerHasBeenBooted"));
		PBMethods.PlayerHasQuit = PBMethods.colorize(getConfig().getString("messages.team.PlayerHasQuit"));
		PBMethods.TeamDisbanded = PBMethods.colorize(getConfig().getString("messages.team.TeamDisbanded"));
		PBMethods.NameTooLong = PBMethods.colorize(getConfig().getString("messages.team.NameTooLong"));
		PBMethods.TeamRenamed = PBMethods.colorize(getConfig().getString("messages.team.TeamRenamed"));
		
		PBMethods.TeamAlreadyNamedThat = PBMethods.colorize(getConfig().getString("messages.team.TeamAlreadyNamedThat"));
		PBMethods.OwnerNotOnline = PBMethods.colorize(getConfig().getString("messages.team.OwnerNotOnline"));
		
		/*
		 * Economy
		 */
		
		PBMethods.NotEnoughMoney = PBMethods.colorize(getConfig().getString("messages.economy.NotEnoughMoney"));
		PBMethods.MoneyWithdrawn = PBMethods.colorize(getConfig().getString("messages.economy.MoneyWithdrawn"));
		
		/*
		 * Round
		 */
		
		PBMethods.OneMinuteRemaining = PBMethods.colorize(getConfig().getString("messages.round.OneMinuteRemaining"));
		PBMethods.RoundComplete = PBMethods.colorize(getConfig().getString("messages.round.RoundComplete"));
		PBMethods.RoundAlreadyGoing = PBMethods.colorize(getConfig().getString("messages.round.RoundAlreadyGoing"));
		PBMethods.InvalidTeamSize = PBMethods.colorize(getConfig().getString("messages.round.InvalidTeamSize"));
		PBMethods.RoundStarted = PBMethods.colorize(getConfig().getString("messages.round.RoundStarted"));
		PBMethods.RoundStopped = PBMethods.colorize(getConfig().getString("messages.round.RoundStopped"));
		PBMethods.RoundPaused = PBMethods.colorize(getConfig().getString("messages.round.RoundPaused"));
		PBMethods.RoundResumed = PBMethods.colorize(getConfig().getString("messages.round.RoundResumed"));
		PBMethods.NoOngoingRound = PBMethods.colorize(getConfig().getString("messages.round.NoOngoingRound"));
		PBMethods.PlayerEliminated = PBMethods.colorize(getConfig().getString("messages.round.PlayerEliminated"));
		PBMethods.PlayerFouled = PBMethods.colorize(getConfig().getString("messages.round.PlayerFouled"));
		PBMethods.RoundEnded = PBMethods.colorize(getConfig().getString("messages.round.RoundEnded"));
		PBMethods.TeamWon = PBMethods.colorize(getConfig().getString("messages.round.TeamWon"));
		PBMethods.MoveUpOneZone = PBMethods.colorize(getConfig().getString("messages.round.MoveUpOneZone"));
		PBMethods.CantEnterField = PBMethods.colorize(getConfig().getString("messages.round.CantEnterField"));
		PBMethods.CantTeleportDuringMatch = PBMethods.colorize(getConfig().getString("messages.round.CantTeleportDuringMatch"));
		PBMethods.MoveNotAllowed = PBMethods.colorize(getConfig().getString("messages.round.MoveNotAllowed"));

		/*
		 * Misc
		 */

		PBMethods.ChatEnabled = PBMethods.colorize(getConfig().getString("messages.misc.ChatEnabled"));
		PBMethods.ChatDisabled = PBMethods.colorize(getConfig().getString("messages.misc.ChatDisabled"));
		PBMethods.WinAddedToTeam = PBMethods.colorize(getConfig().getString("messages.misc.WinAddedToTeam"));
		PBMethods.LossAddedToTeam = PBMethods.colorize(getConfig().getString("messages.misc.LossAddedToTeam"));
		PBMethods.TeamSpawnSet = PBMethods.colorize(getConfig().getString("messages.misc.TeamSpawnSet"));
		
		
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
		PBMethods.loadPlayers();
		PBMethods.loadArenas();
		Probending.log.info("Loaded " + PBMethods.getTeams().size() + " teams");
		Probending.log.info("Loaded " + PBMethods.players.size() + " players.");
		Probending.log.info("Loaded " + PBMethods.getArenas().size() + " arenas");
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Round.progressAll();
			}
		}, 0, 1);
	}

}
