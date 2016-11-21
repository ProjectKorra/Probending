package com.projectkorra.probending;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.commands.Commands;
import com.projectkorra.probending.managers.PBFieldCreationManager;
import com.projectkorra.probending.managers.InviteManager;
import com.projectkorra.probending.managers.PBQueueManager;
import com.projectkorra.probending.managers.PBTeamManager;
import com.projectkorra.probending.managers.ProbendingHandler;
import com.projectkorra.probending.storage.DBProbendingTeam;
import com.projectkorra.probending.storage.DatabaseHandler;

public class Probending extends JavaPlugin {

	private static Probending plugin;

	private ProbendingHandler pHandler;
	private PBFieldCreationManager cManager;
        private PBQueueManager qManager;
	private PBTeamManager tManager;
	private InviteManager iManager;

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
		cManager = new PBFieldCreationManager(pHandler);
		qManager = new PBQueueManager(this, pHandler);
		this.getServer().getPluginManager().registerEvents(cManager, this);
		new Commands(pHandler, cManager, qManager);
		DBProbendingTeam teamDb = new DBProbendingTeam(this);
		tManager = new PBTeamManager(teamDb);
		iManager = new InviteManager(this, teamDb);
	}

	public void onDisable() {
		DatabaseHandler.getDatabase().close();
	}

	public static Probending get() {
		return plugin;
	}

	public ProbendingHandler getProbendingHandler() {
		return pHandler;
	}

	public PBFieldCreationManager getFieldCreationManager() {
		return cManager;
	}

	public PBTeamManager getTeamManager() {
		return tManager;
	}
	
	public InviteManager getInviteManager() {
		return iManager;
	}
}
