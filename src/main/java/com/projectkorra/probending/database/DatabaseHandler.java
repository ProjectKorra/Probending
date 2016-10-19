/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.database;

import com.projectkorra.probending.config.ConfigManager;
import com.projectkorra.probending.libraries.database.Database;
import com.projectkorra.probending.libraries.database.MySQL;
import com.projectkorra.probending.libraries.database.SQLite;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Ivo
 */
public class DatabaseHandler {

    private static Database database;
    private static Boolean useMySQL = false;

    public static void init(JavaPlugin plugin) {
        if (database == null) {
            if (!useMySQL) {
                database = new SQLite(plugin.getLogger(), "Probending", plugin.getDataFolder().getAbsolutePath());
            } else {
                String hostname = ConfigManager.defaultConfig.get().getString("Database.MySQL.Hostname");
                String port = ConfigManager.defaultConfig.get().getString("Database.MySQL.Port");
                String databaseName = ConfigManager.defaultConfig.get().getString("Database.MySQL.Database");
                String username = ConfigManager.defaultConfig.get().getString("Database.MySQL.Username");
                String password = ConfigManager.defaultConfig.get().getString("Database.MySQL.Password");
                database = new MySQL(plugin.getLogger(), hostname, port, databaseName, username, password);
            }
        }
    }

    public static Database getDababase() {
        return database;
    }

    public static Boolean isMysql() {
        return useMySQL;
    }
}
