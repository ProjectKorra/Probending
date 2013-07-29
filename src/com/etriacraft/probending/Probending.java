package com.etriacraft.probending;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.java.JavaPlugin;

public class Probending extends JavaPlugin {
	
	public static Probending plugin;
	protected static Logger log;
	
	public static Economy econ;
	
	Commands cmd;
	Methods methods;
	public void onEnable() {
		Probending.log = this.getLogger();
		plugin = this;
		
		configCheck();
		cmd = new Commands(this);
		
		//Database Stuff
		DBConnection.engine = getConfig().getString("General.Storage");
		DBConnection.host = getConfig().getString("MySQL.host", "localhost");
		DBConnection.pass = getConfig().getString("MySQL.pass", "");
		DBConnection.port = getConfig().getInt("MySQL.port", 3306);
		DBConnection.db = getConfig().getString("MySQL.db", "minecraft");
		DBConnection.user = getConfig().getString("MySQL.user", "root");
		
		
		Commands.ElementNotAllowed = Methods.colorize(getConfig().getString("messages.ElementNotAllowed"));
		Commands.noBendingType = Methods.colorize(getConfig().getString("messages.noBendingType"));
		Commands.PlayerAlreadyInTeam = Methods.colorize(getConfig().getString("messages.PlayerAlreadyInTeam"));
		Commands.Prefix = Methods.colorize(getConfig().getString("messages.Prefix"));
		Commands.teamAlreadyExists = Methods.colorize(getConfig().getString("messages.teamAlreadyExists"));
		Commands.TeamCreated = Methods.colorize(getConfig().getString("messages.TeamCreated"));
		Commands.noPermission = Methods.colorize(getConfig().getString("messages.noPermission"));
		Commands.PlayerNotInTeam = Methods.colorize(getConfig().getString("messages.PlayerNotInTeam"));
		Commands.NotOwnerOfTeam = Methods.colorize(getConfig().getString("messages.NotOwnerOfTeam"));
		Commands.MaxSizeReached = Methods.colorize(getConfig().getString("messages.MaxSizeReached"));
		Commands.TeamAlreadyHasElement = Methods.colorize(getConfig().getString("messages.TeamAlreadyHasElement"));
		Commands.PlayerInviteSent = Methods.colorize(getConfig().getString("messages.PlayerInviteSent"));
		Commands.PlayerInviteReceived = Methods.colorize(getConfig().getString("messages.PlayerInviteReceived"));
		Commands.InviteInstructions = Methods.colorize(getConfig().getString("messages.InviteInstructions"));
		Commands.PlayerNotOnline = Methods.colorize(getConfig().getString("messages.PlayerNotOnline"));
		Commands.TeamDoesNotExist = Methods.colorize(getConfig().getString("messages.TeamDoesNotExist"));
		Commands.NoInviteFromTeam = Methods.colorize(getConfig().getString("messages.NoInviteFromTeam"));
		Commands.PlayerJoinedTeam = Methods.colorize(getConfig().getString("messages.PlayerJoinedTeam"));
		Commands.CantBootFromOwnTeam = Methods.colorize(getConfig().getString("messages.CantBootFromOwnTeam"));
		Commands.PlayerNotOnThisTeam = Methods.colorize(getConfig().getString("messages.PlayerNotOnThisTeam"));
		Commands.YouHaveBeenBooted = Methods.colorize(getConfig().getString("messages.YouHaveBeenBooted"));
		Commands.PlayerHasBeenBooted = Methods.colorize(getConfig().getString("messages.PlayerHasBeenBooted"));
		Commands.YouHaveQuit = Methods.colorize(getConfig().getString("messages.YouHaveQuit"));
		Commands.PlayerHasQuit = Methods.colorize(getConfig().getString("messages.PlayerHasQuit"));
		Commands.TeamDisbanded = Methods.colorize(getConfig().getString("messages.TeamDisbanded"));
		Commands.ArenaAlreadyExists = Methods.colorize(getConfig().getString("messages.ArenaAlreadyExists"));
		Commands.ArenaCreated = Methods.colorize(getConfig().getString("messages.ArenaCreated"));
		Commands.ArenaDoesNotExist = Methods.colorize(getConfig().getString("messages.ArenaDoesNotExist"));
		Commands.ArenaDeleted = Methods.colorize(getConfig().getString("messages.ArenaDeleted"));
		Commands.SpectatorSpawnSet = Methods.colorize(getConfig().getString("messages.SpectatorSpawnSet"));
		Commands.fieldSpawnSet = Methods.colorize(getConfig().getString("messages.fieldSpawnSet"));
		Commands.SentToArena = Methods.colorize(getConfig().getString("messages.SentToArena"));
		Commands.configReloaded = Methods.colorize(getConfig().getString("messages.configReloaded"));
		PlayerListener.RemovedFromTeamBecauseDifferentElement = Methods.colorize(getConfig().getString("messages.RemovedFromTeamBecauseDifferentElement"));
		Commands.NotEnoughMoney = Methods.colorize(getConfig().getString("messages.NotEnoughMoney"));
		Commands.MoneyWithdrawn = Methods.colorize(getConfig().getString("messages.MoneyWithdrawn"));
		Commands.NameTooLong = Methods.colorize(getConfig().getString("messages.NameTooLong"));
		Commands.TeamRenamed = Methods.colorize(getConfig().getString("messages.TeamRenamed"));
		Commands.TeamAlreadyNamedThat = Methods.colorize(getConfig().getString("messages.TeamAlreadyNamedThat"));
		Commands.ChatEnabled = Methods.colorize(getConfig().getString("messages.ChatEnabled"));
		Commands.ChatDisabled = Methods.colorize(getConfig().getString("messages.ChatDisabled"));
		Commands.OneMinuteRemaining = Methods.colorize(getConfig().getString("messages.round.OneMinuteRemaining"));
		Commands.RoundComplete = Methods.colorize(getConfig().getString("messages.round.RoundComplete"));
		Commands.RoundStarted = Methods.colorize(getConfig().getString("messages.round.RoundStarted"));
		Commands.NoArenaPermissions = Methods.colorize(getConfig().getString("messages.NoArenaPermissions"));
		Commands.NoTeamPermissions = Methods.colorize(getConfig().getString("messages.NoTeamPermissions"));
		Commands.NoClockPermissions = Methods.colorize(getConfig().getString("messages.NoClockPermissions"));
		PlayerListener.SetTeamColor = Methods.colorize(getConfig().getString("messages.SetTeamColor"));
		Commands.WinAddedToTeam = Methods.colorize(getConfig().getString("messages.WinAddedToTeam"));
		Commands.LossAddedToTeam = Methods.colorize(getConfig().getString("messages.LossAddedToTeam"));
		Commands.MatchAlreadyGoing = Methods.colorize(getConfig().getString("messages.match.MatchAlreadyGoing"));
		Commands.InvalidTeamSize = Methods.colorize(getConfig().getString("messages.match.InvalidTeamSize"));
		Commands.MatchStarted = Methods.colorize(getConfig().getString("messages.match.MatchStarted"));
		PlayerListener.PlayerFouled = Methods.colorize(getConfig().getString("messages.match.PlayerFouled"));
		PlayerListener.PlayerEliminated = Methods.colorize(getConfig().getString("messages.match.PlayerEliminated"));
		PlayerListener.CantEnterField = Methods.colorize(getConfig().getString("messages.match.CantEnterField"));
		PlayerListener.MatchEnded =  Methods.colorize(getConfig().getString("messages.match.MatchEnded"));
		PlayerListener.TeamWon = Methods.colorize(getConfig().getString("messages.match.TeamWon"));
		PlayerListener.MoveUpOneZone = Methods.colorize(getConfig().getString("messages.match.MoveUpOneZone"));
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		if (getConfig().getBoolean("Economy.Enabled")) {
			Methods.setupEconomy();
		}
		
		Methods.populateColors();
		
		DBConnection.init();
		
		Methods.loadTeams();
		Set<String> teamList = Methods.getTeams();
		Methods.loadPlayers();
		Probending.log.info("Loaded " + teamList.size() + " teams");
		Probending.log.info("Loaded " + Methods.players.size() + " players.");
	}
	
	public void configCheck() {
		// Set Default General Settings
		getConfig().addDefault("General.Storage", "flatfile");
		// Set MySQL Settings
		getConfig().addDefault("MySQL.host", "localhost");
		getConfig().addDefault("MySQL.port", 3306);
		getConfig().addDefault("MySQL.db", "minecraft");
		getConfig().addDefault("MySQL.user", "root");
		getConfig().addDefault("MySQL.pass", "");
		// Set Team Settings
		getConfig().addDefault("TeamSettings.AllowFire", true);
		getConfig().addDefault("TeamSettings.AllowAir", false);
		getConfig().addDefault("TeamSettings.AllowWater", true);
		getConfig().addDefault("TeamSettings.AllowEarth", true);
		getConfig().addDefault("TeamSettings.AllowChi", false);
		getConfig().addDefault("TeamSettings.MaxTeamSize", 4);
		getConfig().addDefault("TeamSettings.MinTeamSize", 2);
		getConfig().addDefault("TeamSettings.TeamOneColor", "Red");
		getConfig().addDefault("TeamSettings.TeamTwoColor", "Cyan");
		// Set WorldGuard Settings
		getConfig().addDefault("WorldGuard.EnableSupport", false);
		getConfig().addDefault("WorldGuard.ProbendingField", "ProbendingField");
		getConfig().addDefault("WorldGuard.AutomateMatches", false);
		getConfig().addDefault("WorldGuard.DisableBuildDuringMatches", true);
		getConfig().addDefault("WorldGuard.TeamOneZoneOne", "RedZone1");
		getConfig().addDefault("WorldGuard.TeamOneZoneTwo", "RedZone2");
		getConfig().addDefault("WorldGuard.TeamOneZoneThree", "RedZone3");
		getConfig().addDefault("WorldGuard.TeamTwoZoneOne", "BlueZone1");
		getConfig().addDefault("WorldGuard.TeamTwoZoneTwo", "BlueZone2");
		getConfig().addDefault("WorldGuard.TeamTwoZoneThree", "BlueZone3");
		// Set Economy Settings
		getConfig().addDefault("Economy.Enabled", false);
		getConfig().addDefault("Economy.ServerAccount", "Server");
		getConfig().addDefault("Economy.TeamCreationFee", 350.0);
		getConfig().addDefault("Economy.TeamRenameFee", 150.0);
		// Set Messages
		getConfig().addDefault("messages.Prefix", "&7[&6Probending&7] ");
		getConfig().addDefault("messages.noPermission", "&cYou don't have permission to do that.");
		getConfig().addDefault("messages.teamAlreadyExists", "&cThat team already exists.");
		getConfig().addDefault("messages.noBendingType", "&cPlayer does not have a bending type.");
		getConfig().addDefault("messages.PlayerAlreadyInTeam", "&cThat player is already in a team.");
		getConfig().addDefault("messages.ElementNotAllowed",  "&c%element are not allowed to participate in probending events.");
		getConfig().addDefault("messages.TeamCreated", "&c%team &ahas been created.");
		getConfig().addDefault("messages.PlayerNotInTeam", "&cYou do not have a team.");
		getConfig().addDefault("messages.NotOwnerOfTeam", "&cYou dont own this team.");
		getConfig().addDefault("messages.PlayerNotOnline", "&cThat player is not online.");
		getConfig().addDefault("messages.MaxSizeReached", "&cThis team has reached the maximum size.");
		getConfig().addDefault("messages.TeamAlreadyHasElement", "&cThis team already has this type of Bender.");
		getConfig().addDefault("messages.PlayerInviteSent", "&aYou have invited &e%player &ato join &e%team&a.");
		getConfig().addDefault("messages.PlayerInviteReceived", "&aYou have been invited to join &e%team&a.");
		getConfig().addDefault("messages.InviteInstructions", "&aAccept this invitation using &3/pb team join %team&a.");
		getConfig().addDefault("messages.TeamDoesNotExist", "&cThat team does not exist.");
		getConfig().addDefault("messages.NoInviteFromTeam", "&cYou dont have an invite from that team.");
		getConfig().addDefault("messages.PlayerJoinedTeam", "&e%player &ahas joined &e%team&a.");
		getConfig().addDefault("messages.CantBootFromOwnTeam", "&cYou cant boot yourself from your own team.");
		getConfig().addDefault("messages.PlayerNotOnThisTeam", "&cThat player is not on this team.");
		getConfig().addDefault("messages.YouHaveBeenBooted", "&cYou have been booted from &3%team&c.");
		getConfig().addDefault("messages.PlayerHasBeenBooted", "&e%player &chas been booted from &e%team&c.");
		getConfig().addDefault("messages.YouHaveQuit", "&cYou have quit &e%team&c.");
		getConfig().addDefault("messages.PlayerHasQuit", "&e%player &chas quit &e%team&c.");
		getConfig().addDefault("messages.TeamDisbanded", "&e%team &chas been disbanded.");
		getConfig().addDefault("messages.ArenaAlreadyExists", "&cThe arena &e%arena &calready exists.");
		getConfig().addDefault("messages.ArenaCreated", "&aArena &e%arena &acreated.");
		getConfig().addDefault("messages.ArenaDoesNotExist", "&cThe arena &e%arena &cdoes not exist.");
		getConfig().addDefault("messages.ArenaDeleted", "&e%arena has been deleted.");
		getConfig().addDefault("messages.SpectatorSpawnSet", "&aSet spectator spawn in &e%arena&a.");
		getConfig().addDefault("messages.fieldSpawnSet", "&aSet field spawn in &e%arena§a.");
		getConfig().addDefault("messages.SentToArena", "&aSent to &e%arena&a.");
		getConfig().addDefault("messages.configReloaded", "&aConfiguration Reloaded.");
		getConfig().addDefault("messages.RemovedFromTeamBecauseDifferentElement", "&cYou have been removed from your team because your element has changed.");
		getConfig().addDefault("messages.NotEnoughMoney", "&cYou do not have the &e%amount %currency &crequired to do that.");
		getConfig().addDefault("messages.MoneyWithdrawn", "&e%amount %currency &awithdrawn from your account.");
		getConfig().addDefault("messages.NameTooLong", "&cTeam names cant be more than 15 characters long.");
		getConfig().addDefault("messages.TeamRenamed", "&aYour team has been renamed to &e%newname&a.");
		getConfig().addDefault("messages.TeamAlreadyNamedThat", "&aYour team is already named &e%newname&a.");
		getConfig().addDefault("messages.ChatEnabled", "&aYou have enabled Probending Chat. To disable it, run the command again.");
		getConfig().addDefault("messages.ChatDisabled", "&cYou have disabled Probending Chat.");
		getConfig().addDefault("messages.OneMinuteRemaining", "&cThere is one minute remaining in this round.");
		getConfig().addDefault("messages.RoundComplete", "&cThis round is now complete.");
		getConfig().addDefault("messages.ClockAlreadyRunning", "&cThe clock is already running.");
		getConfig().addDefault("messages.RoundStarted", "&cA probending round has been started for %seconds seconds.");
		getConfig().addDefault("messages.ClockNotRunning", "&cThe clock is currently not running.");
		getConfig().addDefault("messages.ClockPaused", "&cThe clock has been paused with &3%seconds seconds &cleft.");
		getConfig().addDefault("messages.ClockResumed", "&cThe clock has been resumed with &3%seconds seconds &cleft.");
		getConfig().addDefault("messages.ClockNotPaused", "&cThe clock is not paused.");
		getConfig().addDefault("messages.ClockStopped", "&cThe clock has been stopped.");
		getConfig().addDefault("messages.NoArenaPermissions", "&cYou dont have permission for any arena commands.");
		getConfig().addDefault("messages.NoClockPermissions", "&cYou dont have permission for any clock commands.");
		getConfig().addDefault("messages.NoTeamPermissions", "&cYou dont have permission for any team commands.");
		getConfig().addDefault("messages.LossAddedToTeam", "&aAdded one loss to &e%team&a.");
		getConfig().addDefault("messages.WinAddedToTeam", "&aAdded one win to &3%team&a.");
		
		// Match Messages
		getConfig().addDefault("messages.round.MatchAlreadyGoing", "&cThere is already a match going.");
		getConfig().addDefault("messages.round.InvalidTeamSize", "&cThe team size of the team is invalid.");
		getConfig().addDefault("messages.round.MatchStarted", "&cA match has been started between &e%team1 &cand &e%team2&c.");
		getConfig().addDefault("messages.round.NoOngoingMatch", "&cThere is no ongoing match.");
		getConfig().addDefault("messages.round.MatchStopped", "&cThe ongoing match has been stopped.");
		getConfig().addDefault("messages.round.CantEnterField", "&cYou cant enter the Probending Field during a match.");
		getConfig().addDefault("messages.round.PlayerEliminated", "&4&l%player &r&chas been eliminated.");
		getConfig().addDefault("messages.round.PlayerFouled", "&4&lFOUL: &r&e%player &cmust move back one zone.");
		getConfig().addDefault("messages.round.MatchEnded", "&cThis probending match has ended.");
		getConfig().addDefault("messages.round.TeamWon", "&e%team &chas won this match.");
		getConfig().addDefault("messages.round.MoveUpOneZone", "&aYou may move up one zone.");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
