package com.projectkorra.probending;

import net.milkbowl.vault.economy.Economy;

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.objects.Round;
import com.projectkorra.probending.storage.DBConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

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
		PBMethods.Prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.General.Prefix"));
		PBMethods.noPermission = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.General.NoPermission"));
		PBMethods.configReloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.General.ConfigReloaded"));
		PBMethods.NoTeamPermissions = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.General.NoTeamPermissions"));
		
		/*
		 * Player
		 */
		PBMethods.noBendingType = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.NoBendingType"));
		PBMethods.multiBendingTypes = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.MultipleBendingTypes"));
		PBMethods.PlayerNotElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerNotElement"));
		PBMethods.PlayerAlreadyInTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerAlreadyInTeam"));
		PBMethods.ElementNotAllowed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.ElementNotAllowed"));
		PBMethods.PlayerNotInTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerNotInTeam"));
		PBMethods.PlayerNotOnline = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerNotOnline"));
		PBMethods.PlayerInviteSent = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerInviteSent"));
		PBMethods.PlayerInviteReceived = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerInviteReceived"));
		PBMethods.InviteInstructions = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.InviteInstructions"));
		PBMethods.NoInviteFromTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.NoInviteFromTeam"));
		PBMethods.YouHaveBeenBooted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.YouHaveBeenBooted"));
		PBMethods.YouHaveQuit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.YouHaveQuit"));
		PBMethods.RemovedFromTeamBecauseDifferentElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.RemovedFromTeamBecauseDifferentElement"));
		PBMethods.RemovedFromTeamBecauseNoElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.RemovedFromTeamBecauseNoElement"));
		PBMethods.ElementChanged = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.ElementChanged"));
		PBMethods.PlayerAddedElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Player.PlayerAddedElement"));
		
		/*
		 * Team
		 */
		PBMethods.teamAlreadyExists = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamAlreadyExists"));
		PBMethods.TeamCreated = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamCreated"));
		PBMethods.NotOwnerOfTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.NotOwnerOfTeam"));
		PBMethods.MaxSizeReached = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.MaxSizeReached"));
		PBMethods.TeamAlreadyHasElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamAlreadyHasElement"));
		PBMethods.TeamDoesNotExist = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamDoesNotExist"));
		PBMethods.PlayerJoinedTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.PlayerJoinedTeam"));
		PBMethods.CantBootFromOwnTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.CantBootFromOwnTeam"));
		PBMethods.PlayerNotOnThisTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.PlayerNotOnThisTeam"));
		PBMethods.PlayerHasBeenBooted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.PlayerHasBeenBooted"));
		PBMethods.PlayerHasQuit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.PlayerHasQuit"));
		PBMethods.TeamDisbanded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamDisbanded"));
		PBMethods.NameTooLong = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.NameTooLong"));
		PBMethods.TeamRenamed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamRenamed"));
		
		PBMethods.TeamAlreadyNamedThat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.TeamAlreadyNamedThat"));
		PBMethods.OwnerNotOnline = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Team.OwnerNotOnline"));
		
		/*
		 * Economy
		 */
		
		PBMethods.NotEnoughMoney = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Economy.NotEnoughMoney"));
		PBMethods.MoneyWithdrawn = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Economy.MoneyWithdrawn"));
		
		/*
		 * Round
		 */
		
		PBMethods.OneMinuteRemaining = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.OneMinuteRemaining"));
		PBMethods.RoundComplete = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundComplete"));
		PBMethods.RoundAlreadyGoing = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundAlreadyGoing"));
		PBMethods.InvalidTeamSize = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.InvalidTeamSize"));
		PBMethods.RoundStarted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundStarted"));
		PBMethods.RoundStopped = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundStopped"));
		PBMethods.RoundPaused = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundPaused"));
		PBMethods.RoundResumed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundResumed"));
		PBMethods.NoOngoingRound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.NoOngoingRound"));
		PBMethods.PlayerEliminated = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.PlayerEliminated"));
		PBMethods.PlayerFouled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.PlayerFouled"));
		PBMethods.RoundEnded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.RoundEnded"));
		PBMethods.TeamWon = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.TeamWon"));
		PBMethods.MoveUpOneZone = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.MoveUpOneZone"));
		PBMethods.CantEnterField = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.CantEnterField"));
		PBMethods.CantTeleportDuringMatch = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.CantTeleportDuringMatch"));
		PBMethods.MoveNotAllowed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Round.MoveNotAllowed"));

		/*
		 * Misc
		 */

		PBMethods.ChatEnabled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Misc.ChatEnabled"));
		PBMethods.ChatDisabled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Misc.ChatDisabled"));
		PBMethods.WinAddedToTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Misc.WinAddedToTeam"));
		PBMethods.LossAddedToTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Misc.LossAddedToTeam"));
		PBMethods.TeamSpawnSet = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Misc.TeamSpawnSet"));
		
		
		//Database Stuff
		DBConnection.engine = getConfig().getString("General.Storage");
		DBConnection.host = getConfig().getString("MySQL.Host", "localhost");
		DBConnection.pass = getConfig().getString("MySQL.Pass", "");
		DBConnection.port = getConfig().getInt("MySQL.Port", 3306);
		DBConnection.db = getConfig().getString("MySQL.DB", "minecraft");
		DBConnection.user = getConfig().getString("MySQL.User", "root");
		
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
		Probending.log.info("Loaded " + PBMethods.getTeams().size() + " teams.");
		Probending.log.info("Loaded " + PBMethods.players.size() + " players.");
		Probending.log.info("Loaded " + PBMethods.getArenas().size() + " arenas.");
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Round.progressAll();
			}
		}, 0, 1);
	}

}
