package com.projectkorra.probending;

import net.md_5.bungee.api.ChatColor;

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
		PBMethods.Prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.general.Prefix"));
		PBMethods.noPermission = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.general.noPermission"));
		PBMethods.configReloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.general.configReloaded"));
		PBMethods.NoTeamPermissions = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.general.NoTeamPermissions"));
		
		/*
		 * Player
		 */
		PBMethods.noBendingType = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.noBendingType"));
		PBMethods.multiBendingTypes = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.MultipleBendingTypes"));
		PBMethods.PlayerNotElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerNotElement"));
		PBMethods.PlayerAlreadyInTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerAlreadyInTeam"));
		PBMethods.ElementNotAllowed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.ElementNotAllowed"));
		PBMethods.PlayerNotInTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerNotInTeam"));
		PBMethods.PlayerNotOnline = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerNotOnline"));
		PBMethods.PlayerInviteSent = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerInviteSent"));
		PBMethods.PlayerInviteReceived = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.PlayerInviteReceived"));
		PBMethods.InviteInstructions = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.InviteInstructions"));
		PBMethods.NoInviteFromTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.NoInviteFromTeam"));
		PBMethods.YouHaveBeenBooted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.YouHaveBeenBooted"));
		PBMethods.YouHaveQuit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.YouHaveQuit"));
		PBMethods.RemovedFromTeamBecauseDifferentElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.player.RemovedFromTeamBecauseDifferentElement"));
		
		/*
		 * Team
		 */
		PBMethods.teamAlreadyExists = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.teamAlreadyExists"));
		PBMethods.TeamCreated = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamCreated"));
		PBMethods.NotOwnerOfTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.NotOwnerOfTeam"));
		PBMethods.MaxSizeReached = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.MaxSizeReached"));
		PBMethods.TeamAlreadyHasElement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamAlreadyHasElement"));
		PBMethods.TeamDoesNotExist = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamDoesNotExist"));
		PBMethods.PlayerJoinedTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.PlayerJoinedTeam"));
		PBMethods.CantBootFromOwnTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.CantBootFromOwnTeam"));
		PBMethods.PlayerNotOnThisTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.PlayerNotOnThisTeam"));
		PBMethods.PlayerHasBeenBooted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.PlayerHasBeenBooted"));
		PBMethods.PlayerHasQuit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.PlayerHasQuit"));
		PBMethods.TeamDisbanded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamDisbanded"));
		PBMethods.NameTooLong = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.NameTooLong"));
		PBMethods.TeamRenamed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamRenamed"));
		
		PBMethods.TeamAlreadyNamedThat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.TeamAlreadyNamedThat"));
		PBMethods.OwnerNotOnline = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.team.OwnerNotOnline"));
		
		/*
		 * Economy
		 */
		
		PBMethods.NotEnoughMoney = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.economy.NotEnoughMoney"));
		PBMethods.MoneyWithdrawn = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.economy.MoneyWithdrawn"));
		
		/*
		 * Round
		 */
		
		PBMethods.OneMinuteRemaining = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.OneMinuteRemaining"));
		PBMethods.RoundComplete = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundComplete"));
		PBMethods.RoundAlreadyGoing = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundAlreadyGoing"));
		PBMethods.InvalidTeamSize = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.InvalidTeamSize"));
		PBMethods.RoundStarted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundStarted"));
		PBMethods.RoundStopped = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundStopped"));
		PBMethods.RoundPaused = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundPaused"));
		PBMethods.RoundResumed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundResumed"));
		PBMethods.NoOngoingRound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.NoOngoingRound"));
		PBMethods.PlayerEliminated = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.PlayerEliminated"));
		PBMethods.PlayerFouled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.PlayerFouled"));
		PBMethods.RoundEnded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.RoundEnded"));
		PBMethods.TeamWon = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.TeamWon"));
		PBMethods.MoveUpOneZone = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.MoveUpOneZone"));
		PBMethods.CantEnterField = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.CantEnterField"));
		PBMethods.CantTeleportDuringMatch = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.CantTeleportDuringMatch"));
		PBMethods.MoveNotAllowed = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.round.MoveNotAllowed"));

		/*
		 * Misc
		 */

		PBMethods.ChatEnabled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.misc.ChatEnabled"));
		PBMethods.ChatDisabled = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.misc.ChatDisabled"));
		PBMethods.WinAddedToTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.misc.WinAddedToTeam"));
		PBMethods.LossAddedToTeam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.misc.LossAddedToTeam"));
		PBMethods.TeamSpawnSet = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.misc.TeamSpawnSet"));
		
		
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
