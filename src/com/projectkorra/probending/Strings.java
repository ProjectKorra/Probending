package com.projectkorra.probending;

import org.bukkit.configuration.file.FileConfiguration;

public class Strings {

	public static FileConfiguration config = Probending.plugin.getConfig();
	
	/*
	 * General
	 */
	public static String Prefix = PBMethods.colorize(config.getString("messages.general.Prefix"));
	public static String noPermission = PBMethods.colorize(config.getString("messages.general.noPermission"));
	public static String configReloaded = PBMethods.colorize(config.getString("messages.general.configReloaded"));
	public static String NoTeamPermissions = PBMethods.colorize(config.getString("messages.general.NoTeamPermissions"));
	
	/*
	 * Player
	 */
	public static String noBendingType = PBMethods.colorize(config.getString("messages.player.noBendingType"));
	public static String PlayerAlreadyInTeam = PBMethods.colorize(config.getString("messages.player.PlayerAlreadyInTeam"));
	public static String ElementNotAllowed = PBMethods.colorize(config.getString("messages.player.ElementNotAllowed"));
	public static String PlayerNotInTeam = PBMethods.colorize(config.getString("messages.player.PlayerNotInTeam"));
	public static String PlayerNotOnline = PBMethods.colorize(config.getString("messages.player.PlayerNotOnline"));
	public static String PlayerInviteSent = PBMethods.colorize(config.getString("messages.player.PlayerInviteSent"));
	public static String PlayerInviteReceived = PBMethods.colorize(config.getString("messages.player.PlayerInviteReceived"));
	public static String InviteInstructions = PBMethods.colorize(config.getString("messages.player.InviteInstructions"));
	public static String NoInviteFromTeam = PBMethods.colorize(config.getString("messages.player.NoInviteFromTeam"));
	public static String YouHaveBeenBooted = PBMethods.colorize(config.getString("messages.player.YouHaveBeenBooted"));
	public static String YouHaveQuit = PBMethods.colorize(config.getString("messages.player.YouHaveQuit"));
	public static String RemovedFromTeamBecauseDifferentElement = PBMethods.colorize(config.getString("messages.player.RemovedFromTeamBecauseDifferentElement"));
	
	/*
	 * Team
	 */
	public static String teamAlreadyExists = PBMethods.colorize(config.getString("messages.team.teamAlreadyExists"));
	public static String TeamCreated = PBMethods.colorize(config.getString("messages.team.TeamCreated"));
	public static String NotOwnerOfTeam = PBMethods.colorize(config.getString("messages.team.NotOwnerOfTeam"));
	public static String MaxSizeReached = PBMethods.colorize(config.getString("messages.team.MaxSizeReached"));
	public static String TeamAlreadyHasElement = PBMethods.colorize(config.getString("messages.team.TeamAlreadyHasElement"));
	public static String TeamDoesNotExist = PBMethods.colorize(config.getString("messages.team.TeamDoesNotExist"));
	public static String PlayerJoinedTeam = PBMethods.colorize(config.getString("messages.team.PlayerJoinedTeam"));
	public static String CantBootFromOwnTeam = PBMethods.colorize(config.getString("messages.team.CantBootFromOwnTeam"));
	public static String PlayerNotOnThisTeam = PBMethods.colorize(config.getString("messages.team.PlayerNotOnThisTeam"));
	public static String PlayerHasBeenBooted = PBMethods.colorize(config.getString("messages.team.PlayerHasBeenBooted"));
	public static String PlayerHasQuit = PBMethods.colorize(config.getString("messages.team.PlayerHasQuit"));
	public static String TeamDisbanded = PBMethods.colorize(config.getString("messages.team.TeamDisbanded"));
	public static String NameTooLong = PBMethods.colorize(config.getString("messages.team.NameTooLong"));
	public static String TeamRenamed = PBMethods.colorize(config.getString("messages.team.TeamRenamed"));
	public static String TeamAlreadyNamedThat = PBMethods.colorize(config.getString("messages.team.TeamAleadyNamedThat"));
	public static String OwnerNotOnline = PBMethods.colorize(config.getString("messages.team.OwnerNotOnline"));
	
	/*
	 * Economy
	 */
	
	public static String NotEnoughMoney = PBMethods.colorize(config.getString("messages.economy.NotEnoughMoney"));
	public static String MoneyWithdrawn = PBMethods.colorize(config.getString("messages.economy.MoneyWithdrawn"));
	
	/*
	 * Round
	 */
	
	public static String OneMinuteRemaining = PBMethods.colorize(config.getString("messages.round.OneMinuteRemaining"));
	public static String RoundComplete = PBMethods.colorize(config.getString("messages.round.RoundComplete"));
	public static String RoundAlreadyGoing = PBMethods.colorize(config.getString("messages.round.RoundAlreadyGoing"));
	public static String InvalidTeamSize = PBMethods.colorize(config.getString("messages.round.InvalidTeamSize"));
	public static String RoundStarted = PBMethods.colorize(config.getString("messages.round.RoundStarted"));
	public static String RoundStopped = PBMethods.colorize(config.getString("messages.round.RoundStopped"));
	public static String RoundPaused = PBMethods.colorize(config.getString("messages.round.RoundPaused"));
	public static String RoundResumed = PBMethods.colorize(config.getString("messages.round.RoundResumed"));
	public static String NoOngoingRound = PBMethods.colorize(config.getString("messages.round.NoOngoingRound"));
	public static String PlayerEliminated = PBMethods.colorize(config.getString("messages.round.PlayerEliminated"));
	public static String PlayerFouled = PBMethods.colorize(config.getString("messages.round.PlayerFouled"));
	public static String RoundEnded = PBMethods.colorize(config.getString("messages.round.RoundEnded"));
	public static String TeamWon = PBMethods.colorize(config.getString("messages.round.TeamWon"));
	public static String MoveUpOneZone = PBMethods.colorize(config.getString("messages.round.MoveUpOneZone"));
	public static String CantEnterField = PBMethods.colorize(config.getString("messages.round.CantEnterField"));
	public static String CantTeleportDuringMatch = PBMethods.colorize(config.getString("messages.round.CantTeleportDuringMatch"));
	public static String MoveNotAllowed = PBMethods.colorize(config.getString("messages.round.MoveNotAllowed"));

	/*
	 * Misc
	 */

	public static String ChatEnabled = PBMethods.colorize(config.getString("messages.misc.ChatEnabled"));
	public static String ChatDisabled = PBMethods.colorize(config.getString("messages.misc.ChatDisabled"));
	public static String WinAddedToTeam = PBMethods.colorize(config.getString("messages.misc.WinAddedToTeam"));
	public static String LossAddedToTeam = PBMethods.colorize(config.getString("messages.misc.LossAddedToTeam"));
	public static String TeamSpawnSet = PBMethods.colorize(config.getString("messages.misc.TeamSpawnSet"));
	
}
