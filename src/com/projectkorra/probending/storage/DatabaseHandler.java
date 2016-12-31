package com.projectkorra.probending.storage;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.config.ConfigManager;
import com.projectkorra.probending.libraries.database.AbstractDatabase;
import com.projectkorra.probending.libraries.database.MySQLDatabase;
import com.projectkorra.probending.libraries.database.SQLiteDatabase;

public class DatabaseHandler {

    private static DatabaseHandler INSTANCE;

    private AbstractDatabase _database;
    private boolean _useMySQL = false;

    public DatabaseHandler(JavaPlugin plugin) {
        if (INSTANCE != null) {
            return;
        }
        
        _useMySQL = ConfigManager.defaultConfig.get().getBoolean("Database.MySQL.Enabled");
        
        if (!_useMySQL) {
            _database = new SQLiteDatabase(plugin.getLogger(), "probending.db", plugin.getDataFolder().getAbsolutePath());
        } else {
            String hostname = ConfigManager.defaultConfig.get().getString("Database.MySQL.Hostname");
            String port = ConfigManager.defaultConfig.get().getString("Database.MySQL.Port");
            String databaseName = ConfigManager.defaultConfig.get().getString("Database.MySQL.Database");
            String username = ConfigManager.defaultConfig.get().getString("Database.MySQL.Username");
            String password = ConfigManager.defaultConfig.get().getString("Database.MySQL.Password");
            _database = new MySQLDatabase(plugin.getLogger(), hostname, port, databaseName, username, password);
        }

        INSTANCE = this;
    }

    public static AbstractDatabase getDatabase() {
        return INSTANCE._database;
    }

    public static Boolean isMySQL() {
        return INSTANCE._useMySQL;
    }
}