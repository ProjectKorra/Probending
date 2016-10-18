package com.projectkorra.probending.game.round;

import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.libraries.Timer;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
        this.team1 = game.getTeam1();
        this.team2 = game.getTeam2();
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
        timer = new Timer(plugin) {

            @Override
            public void secondExecute(int curTime) {
                pbScoreboard.setNewTime(curTime);
            }

            @Override
            public void execute() {
                if (isOnCountdown) {
                    //Game has started!
                    isOnCountdown = false;
                    this.setTime(roundDuration);
                } else {
                    super.stop();
                }
            }
        };
        timer.start(countdownDuration, 20l);
    }

    public void stop() {
        game.timerEnded();
        timer.stop();
    }

    public void forceStop() {
        //FORCESTOP SHOULD BE IMPLEMENTED!
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
