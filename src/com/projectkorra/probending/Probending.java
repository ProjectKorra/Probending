package com.projectkorra.probending;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.storage.DBConnection;

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
		Set<String> teamList = PBMethods.getTeams();
		PBMethods.loadPlayers();
		Probending.log.info("Loaded " + teamList.size() + " teams");
		Probending.log.info("Loaded " + PBMethods.players.size() + " players.");
		
	}

}
