package com.etriacraft.probending;

import java.io.IOException;
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
	}
	
	public void configCheck() {
		getConfig().addDefault("TeamSettings.AllowFire", true);
		getConfig().addDefault("TeamSettings.AllowAir", false);
		getConfig().addDefault("TeamSettings.AllowWater", true);
		getConfig().addDefault("TeamSettings.AllowEarth", true);
		getConfig().addDefault("TeamSettings.AllowChi", false);
		getConfig().addDefault("TeamSettings.MaxTeamSize", 4);
		getConfig().addDefault("Economy.Enabled", false);
		getConfig().addDefault("Economy.ServerAccount", "Server");
		getConfig().addDefault("Economy.TeamCreationFee", 350.0);
		getConfig().addDefault("Economy.TeamRenameFee", 150.0);
		getConfig().addDefault("messages.Prefix", "&7[&6Probending&7] ");
		getConfig().addDefault("messages.noPermission", "&cYou don't have permission to do that.");
		getConfig().addDefault("messages.teamAlreadyExists", "&cThat team already exists.");
		getConfig().addDefault("messages.noBendingType", "&cYou dont have a bending type.");
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
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
