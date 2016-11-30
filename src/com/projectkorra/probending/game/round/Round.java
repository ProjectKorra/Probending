package com.projectkorra.probending.game.round;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.scoreboard.PBScoreboard;
import com.projectkorra.probending.libraries.Timer;
import com.projectkorra.probending.libraries.Title;

import net.md_5.bungee.api.ChatColor;

public class Round {

	private final JavaPlugin plugin;
	private final Game game;

	private Timer timer;

	private Set<Player> team1;
	private Set<Player> team2;

	private Boolean isPaused;
	private Boolean isOnCountdown;
	private Integer countdownDuration;
	private Integer roundDuration;

	private PBScoreboard pbScoreboard;

	public Round(JavaPlugin plugin, Game game) {
		this.plugin = plugin;
		this.game = game;
		this.team1 = game.getTeam1Players();
		this.team2 = game.getTeam2Players();
		this.isPaused = false;
		this.isOnCountdown = true;
		this.countdownDuration = 5;
		this.roundDuration = 180;
		this.pbScoreboard = new PBScoreboard(plugin);
	}

	public Round setRoundDuration(Integer timeInSecs) {
		this.roundDuration = timeInSecs;
		return this;
	}

	public Round setCountdownDuration(Integer timeInSecs) {
		this.countdownDuration = timeInSecs;
		return this;
	}

	public void start() {
		for (Player p : team1) {
			pbScoreboard.addPlayerToScoreboard(p);
			pbScoreboard.addPlayerToTeam1(p);
		}
		for (Player p : team2) {
			pbScoreboard.addPlayerToScoreboard(p);
			pbScoreboard.addPlayerToTeam2(p);
		}
		timer = new Timer(plugin) {

			@Override
			public void secondExecute(int curTime) {
				pbScoreboard.setNewTime(curTime);
				if (curTime <= 3) {
					sendTitle(ChatColor.RED + "" + curTime);
				}
			}

			@Override
			public void execute() {
				if (isOnCountdown) {
					//Game has started!
					isOnCountdown = false;
					this.setTime(roundDuration);
					pbScoreboard.setNewTime(roundDuration);
					sendTitle(ChatColor.GREEN + "FIGHT!");
				} else {
					stopGame();
				}
			}
		};
		timer.start(countdownDuration, 20l);
	}

	private void sendTitle(String message) {
		Title title = new Title("", message, 0, 1, 0);
		for (Player p : team1) {
			title.send(p);
		}
		for (Player p : team2) {
			title.send(p);
		}
	}

	private void resetPlayers() {
		for (Player p : team1) {
			pbScoreboard.removePlayerFromScorebard(p);
		}
		for (Player p : team2) {
			pbScoreboard.removePlayerFromScorebard(p);
		}
	}

	public void stopGame() {
		resetPlayers();
		game.timerEnded();
		timer.stop();
	}

	public void forceStop() {
		//FORCESTOP SHOULD BE IMPLEMENTED!
		resetPlayers();
		timer.stop();
	}

	public boolean pauseGame() {
		if (isOnCountdown) {
			if (!this.isPaused) {
				timer.setPaused(true);
				isPaused = true;
				return true;
			}
		}
		return false;
	}

	public boolean unPauseGame() {
		if (isPaused) {
			timer.setPaused(false);
			isPaused = false;
			return true;
		}
		return false;
	}

	public boolean canDoSomething() {
		if (isOnCountdown || isPaused) {
			return false;
		}
		return true;
	}
}
