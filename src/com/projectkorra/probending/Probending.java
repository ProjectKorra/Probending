package com.projectkorra.probending;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.commands.Commands;
import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.PBTeamManager;
import com.projectkorra.probending.managers.ProbendingHandler;
import com.projectkorra.probending.storage.DBProbendingTeam;
import com.projectkorra.probending.storage.DatabaseHandler;

public class Probending extends JavaPlugin {

	private static Probending plugin;

	private ProbendingHandler pHandler;
	private FieldCreationManager cManager;
	private PBTeamManager tManager;

	public void onEnable() {
		plugin = this;
		try {
			new MetricsLite(this);
		}
		catch (IOException ex) {
			Logger.getLogger(Probending.class.getName()).log(Level.SEVERE, null, ex);
		}
		new DatabaseHandler(this);

		pHandler = new ProbendingHandler(this);
		cManager = new FieldCreationManager(pHandler);
		this.getServer().getPluginManager().registerEvents(cManager, this);
		new Commands(pHandler, cManager);
		tManager = new PBTeamManager(new DBProbendingTeam(this));
	}

	public void onDisable() {
	}

	public static Probending get() {
		return plugin;
	}

	public ProbendingHandler getProbendingHandler() {
		return pHandler;
	}

	public FieldCreationManager getFieldCreationManager() {
		return cManager;
	}

	public PBTeamManager getTeamManager() {
		return tManager;
	}
}
