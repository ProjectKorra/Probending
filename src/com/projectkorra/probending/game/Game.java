package com.projectkorra.probending.game;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.enums.GameMode;
import com.projectkorra.probending.enums.WinningType;
import com.projectkorra.probending.game.field.FieldManager;
import com.projectkorra.probending.game.round.Round;
import com.projectkorra.probending.libraries.Title;
import com.projectkorra.probending.managers.ProbendingHandler;
import com.projectkorra.probending.objects.ProbendingField;

public class Game {

    private JavaPlugin plugin;
    private ProbendingHandler handler;

    private ProbendingField field;
    private FieldManager fieldManager;
    private GameListener listener;

    private Integer waitTime = 5;
    private Integer rounds;
    private Integer curRound;
    private Integer playTime;

    private Boolean suddenDeath;
    private Boolean forcedSuddenDeath;
    private boolean _1v1;

    private Set<Player> team1Players;
    private Set<Player> team2Players;

    private Integer team1Score;
    private Integer team2Score;

    private Round round;
    
    private GameType type;
    private GameMode mode;

    public Game(JavaPlugin plugin, ProbendingHandler handler, GameType type, GameMode mode,
            ProbendingField field, Set<Player> team1, Set<Player> team2) {
        this.plugin = plugin;
        this.handler = handler;
        this.type = type;
        this.mode = mode;
        this.rounds = type.getRounds();
        this.curRound = 1;
        this.playTime = type.getPlayTime();
        this.forcedSuddenDeath = type.isForcedSuddenDeath();
        this.suddenDeath = false;
        this.field = field;
        this.team1Players = team1;
        this.team2Players = team2;
        this.team1Score = 0;
        this.team2Score = 0;
        this.fieldManager = new FieldManager(this, field);
        this.listener = new GameListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
    
    public GameType getGameType() {
    	return type;
    }
    
    public GameMode getGameMode() {
    	return mode;
    }

    public ProbendingField getField() {
        return field;
    }

    public Set<Player> getTeam1Players() {
        return team1Players;
    }

    public Set<Player> getTeam2Players() {
        return team2Players;
    }
    
    /**
     * 
     * @return The amount of time in {@link Integer} that the match can run.
     */
    public Integer getPlayTime() {
    	return playTime;
    }

    public Boolean isSuddenDeath() {
        if (forcedSuddenDeath) {
            return true;
        }
        return suddenDeath;
    }

    public void startNewRound() {
        if (round != null) {
            round.forceStop();
        }
        fieldManager.reset();
        if (!isSuddenDeath()) {
            round = new Round(plugin, this);
            round.start();
        }
    }

    public void timerEnded() {
        WinningType winningTeam = fieldManager.getWinningTeam();
        manageEnd(winningTeam);
    }

    /**
     * The field will trigger this function as soon as a team wins the game!
     */
    public void endRound(WinningType winningTeam) {
        round.forceStop();
        manageEnd(winningTeam);
    }

    private void manageEnd(WinningType winningTeam) {
        round = null;
        if (winningTeam.equals(WinningType.TEAM1)) {
            team1Score++;
        } else if (winningTeam.equals(WinningType.TEAM2)) {
            team2Score++;
        } else {
            //Should be implemented soon!
//            suddenDeath = true;
//            startNewRound();
//            return;
        }
        curRound++;
        Title title = new Title(ChatColor.RED + "" + team1Score + ChatColor.WHITE + " - " + ChatColor.BLUE + "" + team2Score, "", 1, 1, 1);
        for (Player p : team1Players) {
            title.send(p);
        }
        for (Player p : team2Players) {
            title.send(p);
        }
        if (curRound > rounds) {
            handler.gameEnded(this, team1Score, team2Score);
            return;
        }
        startNewRound();
    }

    protected boolean isPlayerAProbender(Player player) {
        if (team1Players.contains(player) || team2Players.contains(player)) {
            return true;
        }
        return false;
    }

    protected boolean canPlayerMove(Player player) {
        if (team1Players.contains(player) || team2Players.contains(player)) {
            if (round != null) {
                return round.canDoSomething();
            }
        }
        return true;
    }

    protected void playerMove(Player player, Location from, Location to) {
        if (round != null) {
            fieldManager.playerMove(player, from, to);
        }
    }
    
    public boolean is1v1() {
    	return _1v1;
    }

    public enum GameType {

        DEFAULT(1, 180, false, false),
        PRACTICE(1, 90, false, false),
        RANKED(5, 180, true, false);

        private Integer rounds;
        private Integer playTime;
        private Boolean hasSuddenDeath;
        private Boolean forcedSuddenDeath;

        private GameType(Integer rounds, Integer playTime, Boolean hasSuddenDeath, Boolean forcedSuddenDeath) {
            this.rounds = rounds;
            this.playTime = playTime;
            this.hasSuddenDeath = hasSuddenDeath;
            this.forcedSuddenDeath = forcedSuddenDeath;
        }

        public Integer getRounds() {
            return rounds;
        }

        public Integer getPlayTime() {
            return playTime;
        }

        public Boolean hasSuddenDeath() {
            return hasSuddenDeath;
        }

        public Boolean isForcedSuddenDeath() {
            return forcedSuddenDeath;
        }
    }
}
