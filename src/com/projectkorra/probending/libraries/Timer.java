package com.projectkorra.probending.libraries;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Timer {

	private JavaPlugin plugin;
	private BukkitRunnable updater;
	private int curTime;
	private boolean paused;

	public Timer(JavaPlugin plugin) {
		this.plugin = plugin;
		this.paused = false;
		updater = new BukkitRunnable() {
			@Override
			public void run() {
				updateTime();
			}
		};
	}

	public void start(int startTime, long interval) {
		curTime = startTime;
		updater.runTaskTimer(plugin, 0L, interval);
	}

	public void stop() {
		updater.cancel();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setTime(int time) {
		this.curTime = time;
	}

	private void updateTime() {
		if (!paused) {
			curTime--;
			if (curTime != 0) {
				secondExecute(curTime);
			} else {
				execute();
			}
		}
	}

	public abstract void secondExecute(int curTime);

	public abstract void execute();
}
