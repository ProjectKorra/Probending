package com.etriacraft.probending;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		
		// General Strings
		Strings.Prefix = Methods.colorize(getConfig().getString("messages.general.Prefix"));
		Strings.noPermission = Methods.colorize(getConfig().getString("messages.general.noPermission"));
		Strings.configReloaded = Methods.colorize(getConfig().getString("messages.general.configReloaded"));
		Strings.NoTeamPermissions = Methods.colorize(getConfig().getString("messages.general.NoTeamPermissions"));
		
		// Player Strings
		Strings.noBendingType = Methods.colorize(getConfig().getString("messages.player.noBendingType"));
		Strings.PlayerAlreadyInTeam = Methods.colorize(getConfig().getString("messages.player.PlayerAlreadyInTeam"));
		Strings.ElementNotAllowed = Methods.colorize(getConfig().getString("messages.player.ElementNotAllowed"));
		Strings.PlayerNotInTeam = Methods.colorize(getConfig().getString("messages.player.PlayerNotInTeam"));
		Strings.PlayerNotOnline = Methods.colorize(getConfig().getString("messages.player.PlayerNotOnline"));
		Strings.PlayerInviteSent = Methods.colorize(getConfig().getString("messages.player.PlayerInviteSent"));
		Strings.PlayerInviteReceived = Methods.colorize(getConfig().getString("messages.player.PlayerInviteReceived"));
		Strings.InviteInstructions = Methods.colorize(getConfig().getString("messages.player.InviteInstructions"));
		Strings.NoInviteFromTeam = Methods.colorize(getConfig().getString("messages.player.NoInviteFromTeam"));
		Strings.YouHaveBeenBooted = Methods.colorize(getConfig().getString("messages.player.YouHaveBeenBooted"));
		Strings.YouHaveQuit = Methods.colorize(getConfig().getString("messages.player.YouHaveQuit"));
		Strings.RemovedFromTeamBecauseDifferentElement = Methods.colorize(getConfig().getString("messages.player.RemovedFromTeamBecauseDifferentElement"));
		
		// Team Strings
		Strings.teamAlreadyExists = Methods.colorize(getConfig().getString("messages.team.teamAlreadyExists"));
		Strings.TeamCreated = Methods.colorize(getConfig().getString("messages.team.TeamCreated"));
		Strings.NotOwnerOfTeam = Methods.colorize(getConfig().getString("messages.team.NotOwnerOfTeam"));
		Strings.MaxSizeReached = Methods.colorize(getConfig().getString("messages.team.MaxSizeReached"));
		Strings.TeamAlreadyHasElement = Methods.colorize(getConfig().getString("messages.team.TeamAlreadyHasElement"));
		Strings.TeamDoesNotExist = Methods.colorize(getConfig().getString("messages.team.TeamDoesNotExist"));
		Strings.PlayerJoinedTeam = Methods.colorize(getConfig().getString("messages.team.PlayerJoinedTeam"));
		Strings.CantBootFromOwnTeam = Methods.colorize(getConfig().getString("messages.team.CantBootFromOwnTeam"));
		Strings.PlayerNotOnThisTeam = Methods.colorize(getConfig().getString("messages.team.PlayerNotOnThisTeam"));
		Strings.PlayerHasBeenBooted = Methods.colorize(getConfig().getString("messages.team.PlayerHasBeenBooted"));
		Strings.PlayerHasQuit = Methods.colorize(getConfig().getString("messages.team.PlayerHasQuit"));
		Strings.TeamDisbanded = Methods.colorize(getConfig().getString("messages.team.TeamDisbanded"));
		Strings.NameTooLong = Methods.colorize(getConfig().getString("messages.team.NameTooLong"));
		Strings.TeamRenamed = Methods.colorize(getConfig().getString("messages.team.TeamRenamed"));
		Strings.TeamAlreadyNamedThat = Methods.colorize(getConfig().getString("messages.team.TeamAlreadyNamedThat"));
		Strings.OwnerNotOnline = Methods.colorize(getConfig().getString("messages.team.OwnerNotOnline"));
		// Economy Strings
		Strings.NotEnoughMoney = Methods.colorize(getConfig().getString("messages.economy.NotEnoughMoney"));
		Strings.MoneyWithdrawn = Methods.colorize(getConfig().getString("messages.economy.MoneyWithdrawn"));
		
		// Round Strings
		Strings.RoundStarted = Methods.colorize(getConfig().getString("messages.round.RoundStarted"));
		Strings.OneMinuteRemaining = Methods.colorize(getConfig().getString("messages.round.OneMinuteRemaining"));
		Strings.RoundComplete = Methods.colorize(getConfig().getString("messages.round.RoundComplete"));
		Strings.RoundAlreadyGoing = Methods.colorize(getConfig().getString("messages.round.RoundAlreadyGoing"));
		Strings.InvalidTeamSize = Methods.colorize(getConfig().getString("messages.round.InvalidTeamSize"));
		Strings.RoundStarted = Methods.colorize(getConfig().getString("messages.round.RoundStarted"));
		Strings.RoundStopped = Methods.colorize(getConfig().getString("messages.round.RoundStopped"));
		Strings.NoOngoingRound = Methods.colorize(getConfig().getString("messages.round.NoOngoingRound"));
		Strings.PlayerEliminated = Methods.colorize(getConfig().getString("messages.round.PlayerEliminated"));
		Strings.PlayerFouled = Methods.colorize(getConfig().getString("messages.round.PlayerFouled"));
		Strings.TeamWon = Methods.colorize(getConfig().getString("messages.round.TeamWon"));
		Strings.MoveUpOneZone = Methods.colorize(getConfig().getString("messages.round.MoveUpOneZone"));
		Strings.RoundPaused = Methods.colorize(getConfig().getString("messages.round.RoundPaused"));
		Strings.RoundResumed = Methods.colorize(getConfig().getString("messages.round.RoundResumed"));
		Strings.CantEnterField = Methods.colorize(getConfig().getString("messages.round.CantEnterField"));
		Strings.CantTeleportDuringMatch = Methods.colorize(getConfig().getString("messages.round.CantTeleportDuringMatch"));
		Strings.ChallengeSent = Methods.colorize(getConfig().getString("messages.round.ChallengeSent"));
		Strings.ChallengeReceived = Methods.colorize(getConfig().getString("messages.round.ChallengeReceived"));
		Strings.NoChallengeFromTeam = Methods.colorize(getConfig().getString("messages.round.NoChallengeFromTeam"));
		Strings.MoveNotAllowed = Methods.colorize(getConfig().getString("messages.round.MoveNotAllowed"));
		// Misc Strings
		Strings.ChatEnabled = Methods.colorize(getConfig().getString("messages.misc.ChatEnabled"));
		Strings.ChatDisabled = Methods.colorize(getConfig().getString("messages.misc.ChatDisabled"));
		Strings.WinAddedToTeam = Methods.colorize(getConfig().getString("messages.misc.WinAddedToTeam"));
		Strings.LossAddedToTeam = Methods.colorize(getConfig().getString("messages.misc.LossAddedToTeam"));
		Strings.TeamSpawnSet = Methods.colorize(getConfig().getString("messages.misc.TeamSpawnSet"));
		
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
		// Set Round Settings
		getConfig().addDefault("RoundSettings.Time", 180);
		getConfig().addDefault("RoundSettings.WinValue", 2);
		getConfig().addDefault("RoundSettings.LossValue", 1);
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
		
		List<String> defaultAllowedMoves = new ArrayList<String>();
		defaultAllowedMoves.add("AirSwipe");
		defaultAllowedMoves.add("WaterManipulation");
		defaultAllowedMoves.add("EarthBlast");
		defaultAllowedMoves.add("FireBlast");
		
		getConfig().addDefault("TeamSettings.AllowedMoves", defaultAllowedMoves);
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
		// Set General Messages
		getConfig().addDefault("messages.general.Prefix", "&7[&6Probending&7] ");
		getConfig().addDefault("messages.general.noPermission", "&cYou don't have permission to do that.");
		getConfig().addDefault("messages.general.configReloaded", "&aConfig reloaded.");
		getConfig().addDefault("messages.general.NoTeamPermissions", "&cYou dont have permission for any team commands.");
		
		//Player Messages
		getConfig().addDefault("messages.player.noBendingType", "&cPlayer does not have a Bending Type.");
		getConfig().addDefault("messages.player.PlayerAlreadyInTeam", "&cThat player is already in a team.");
		getConfig().addDefault("messages.player.ElementNotAllowed", "&c%element are not allowed to participate in Probending events.");
		getConfig().addDefault("messages.player.PlayerNotInTeam", "&cYou dont have a team.");
		getConfig().addDefault("messages.player.PlayerNotOnline", "&cThat player is not online.");
		getConfig().addDefault("messages.player.PlayerInviteSent", "&aYou have invited &e%player &ato join &e%team&a.");
		getConfig().addDefault("messages.player.PlayerInviteReceived", "&aYou have been invited to join &e%team&a.");
		getConfig().addDefault("messages.player.InviteInstructions", "&aAccept this invitation using &3/pb team join %team&a.");
		getConfig().addDefault("messages.player.NoInviteFromTeam", "&cYou dont have an invite from that team.");
		getConfig().addDefault("messages.player.YouHaveBeenBooted", "&cYou have been booted from &3%team&c.");
		getConfig().addDefault("messages.player.YouHaveQuit", "&cYou have quit &e%team&c.");
		getConfig().addDefault("messages.player.RemovedFromTeamBecauseDifferentElement", "&cYou have been removed from your team because your element has changed.");
		
		// Team Messages
		getConfig().addDefault("messages.team.teamAlreadyExists", "&cThat team already exists.");
		getConfig().addDefault("messages.team.TeamCreated", "&e%team &ahas been created.");
		getConfig().addDefault("messages.team.NotOwnerOfTeam", "&cYou dont own this team.");
		getConfig().addDefault("messages.team.MaxSizeReached", "&cThis team has reached the maximum size.");
		getConfig().addDefault("messages.team.TeamAlreadyHasElement", "&cThis team already has this element.");
		getConfig().addDefault("messages.team.TeamDoesNotExist", "&cThat team does not exist.");
		getConfig().addDefault("messages.team.PlayerJoinedTeam", "&e%player &ahas joined &e%team&a.");
		getConfig().addDefault("messages.team.CantBootFromOwnTeam", "&cYou cant boot yourself from your own team.");
		getConfig().addDefault("messages.team.PlayerNotOnThisTeam", "&cThat player is not on this team.");
		getConfig().addDefault("messages.team.PlayerHasBeenBooted", "&e%player &chas been booted from &e%team&c.");
		getConfig().addDefault("messages.team.PlayerHasQuit", "&c%player &chas quit &e%team&c.");
		getConfig().addDefault("messages.team.TeamDisbanded", "&e%team &chas been disbanded.");
		getConfig().addDefault("messages.team.NameTooLong", "&cTeam names cant be more than 15 characters long.");
		getConfig().addDefault("messages.team.TeamRenamed", "&aYour team has been renamed to &e%newname&a.");
		getConfig().addDefault("messages.team.TeamAlreadyNamedThat", "&aYour team is already named that.");
		getConfig().addDefault("messages.team.OwnerNotOnline", "&aThe owner of that team is not online.");
		// Economy Messages
		getConfig().addDefault("messages.economy.NotEnoughMoney", "&cYou do not have the &e%amount %currency &crequired to do that.");
		getConfig().addDefault("messages.economy.MoneyWithdrawn", "&e%amount currency &2withdrawn from your account.");
		
		// Round Messages
		getConfig().addDefault("messages.round.OneMinuteRemaining", "&cThere is one minute remaining in this round.");
		getConfig().addDefault("messages.round.RoundComplete", "&cThis round is now complete.");
		getConfig().addDefault("messages.round.RoundAlreadyGoing", "&cThere is already a round going on.");
		getConfig().addDefault("messages.round.InvalidTeamSize", "&cThe size of the team is invalid.");
		getConfig().addDefault("messages.round.RoundStarted", "&cA round has been started between &e%team1 &cand &e%team2&c for &e%seconds seconds&c.");
		getConfig().addDefault("messages.round.RoundStopped", "&cThe ongoing round has ended.");
		getConfig().addDefault("messages.round.NoOngoingRound", "&cThere is no ongoing round.");
		getConfig().addDefault("messages.round.PlayerEliminated", "&4&l%player &r&chas been eliminated.");
		getConfig().addDefault("messages.round.PlayerFouled", "&4&lFOUL: &r&e%player &cmust move back to &3%zone&c.");
		getConfig().addDefault("messages.round.RoundEnded", "&cThis probending round has ended.");
		getConfig().addDefault("messages.round.TeamWon", "&e%team &chas won this round.");
		getConfig().addDefault("messages.round.MoveUpOneZone", "&aYou may move up to %zone");
		getConfig().addDefault("messages.round.RoundPaused", "&cThe ongoing round has been paused with &e%seconds seconds &3left.");
		getConfig().addDefault("messages.round.CantEnterField", "&cYou are not allowed to enter the field during a match.");
		getConfig().addDefault("messages.round.CantTeleportDuringMatch", "&cYou are not allowed to teleport during a match.");
		getConfig().addDefault("messages.round.RoundResumed", "&cThe ongoing round has been resumed with &3%seconds seconds &cleft.");
		getConfig().addDefault("messages.round.ChallengeSent", "&aYou have sent a challenge to &e%team&a.");
		getConfig().addDefault("messages.round.ChallengeReceived", "&aYou have received a challenge from &e%team&a. Use &3/pb challenge accept [Team] &ato accept.");
	    getConfig().addDefault("messages.round.NoChallengeFromTeam", "&cYou dont have a challenge from that team.");
		getConfig().addDefault("messages.round.MoveNotAllowed", "&cYou are not allowed to use &e%ability &cduring a Probending match.");
		
	    // Misc Messages
		getConfig().addDefault("messages.misc.ChatEnabled", "&aYou have enabled Probending Chat. To disable it, run the command again.");
		getConfig().addDefault("messages.misc.ChatDisabled", "&cYou have disabled Probending Chat.");
		getConfig().addDefault("messages.misc.WinAddedToTeam", "&aAdded one win to &3%team&a.");
		getConfig().addDefault("messages.misc.LossAddedToTeam", "&aAdded one loss to &3%team&a.");
		getConfig().addDefault("messages.misc.TeamSpawnSet", "&aSet spawn for &e%team&a.");
	

		// Finalize stuff
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
