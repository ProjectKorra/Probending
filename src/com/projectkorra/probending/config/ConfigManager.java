package com.projectkorra.probending.config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    public static Config defaultConfig;
    //public static Config messageConfig;

    public ConfigManager() {
        defaultConfig = new Config(new File("config.yml"));
        //messageConfig = new Config(new File("language.yml"));
        configCheck(ConfigType.DEFAULT);
        //configCheck(ConfigType.MESSAGES);
    }

    public static void configCheck(ConfigType type) {
        FileConfiguration config;
        switch (type) {
            case DEFAULT:
                config = defaultConfig.get();
                
                config.addDefault("Database.MySQL.Enabled", false);
                config.addDefault("Database.MySQL.Hostname", "localhost");
                config.addDefault("Database.MySQL.Port", 3306);
                config.addDefault("Database.MySQL.Database", "Probending");
                config.addDefault("Database.MySQL.Username", "ProbendingROOT");
                config.addDefault("Database.MySQL.Password", "ROOTPassword");
                
                defaultConfig.save();
                break;
            //case MESSAGES:
                //config = messageConfig.get();
                
                //I'll do this later...
                /*
                config.addDefault("Messages.General.Prefix", "&7[&6Probending&7] ");
                config.addDefault("Messages.General.NoPermission", "&cYou dont have permission to do that.");
                config.addDefault("Messages.General.ConfigReloaded", "&aConfiguration reloaded.");
                config.addDefault("Messages.General.NoTeamPermissions", "&cYou dont have permission for any team commands.");
                
                config.addDefault("Messages.Player.NoBendingType", "");
                config.addDefault("Messages.Player.MultipleBendingTypes", "");
                config.addDefault("Messages.Player.PlayerNotElement", "");
                config.addDefault("Messages.Player.PlayerAlreadyInTeam", "");
                config.addDefault("Messages.Player.ElementNotAllowed", "");
                config.addDefault("Messages.Player.PlayerNotInTeam", "");
                config.addDefault("Messages.Player.PlayerNotOnline", "");
                config.addDefault("Messages.Player.PlayerInviteSent", "");
                config.addDefault("Messages.Player.PlayerInviteReceived", "");
                config.addDefault("Messages.Player.InviteInstructions", "");
                config.addDefault("Messages.Player.NoInviteFromTeam", "");
                config.addDefault("Messages.Player.YouHaveBeenBooted", "");
                config.addDefault("Messages.Player.YouHaveQuit", "");
                config.addDefault("Messages.Player.RemovedFromTeamBecauseDifferentElement", "");
                config.addDefault("Messages.Player.RemovedFromTeamBecauseNoElement", "");
                config.addDefault("Messages.Player.ElementChanged", "");
                config.addDefault("Messages.Player.PlayerAddedElement", "");
                */
                        
                //messageConfig.save();
                //break;
        }
    }

    public static FileConfiguration getConfig() {
        return ConfigManager.defaultConfig.get();
    }
}