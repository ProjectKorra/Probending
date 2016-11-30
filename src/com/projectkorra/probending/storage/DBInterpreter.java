package com.projectkorra.probending.storage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DBInterpreter {

	private final JavaPlugin _plugin;

	public DBInterpreter(JavaPlugin plugin) {
		_plugin = plugin;
	}

	public final void runSync(Runnable r) {
		Bukkit.getScheduler().runTask(_plugin, r);
	}

	public final void runAsync(Runnable r) {
		Bukkit.getScheduler().runTaskAsynchronously(_plugin, r);
	}
}
