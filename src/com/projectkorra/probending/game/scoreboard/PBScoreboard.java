package com.projectkorra.probending.game.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PBScoreboard {

    private Map<Player, Scoreboard> oldScoreboards;

    private Scoreboard board;
    private Objective sideBar;

    private String oldTeam1Text;
    private String oldTeam2Text;
    private String oldTimeText;

    private Team team1, team2;

    public PBScoreboard(JavaPlugin plugin) {
        this.board = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.sideBar = board.registerNewObjective("sidebar", "dummy");
        this.sideBar.setDisplayName(ChatColor.RED + "Match Information");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.team1 = board.registerNewTeam("Team1");
        this.team2 = board.registerNewTeam("Team2");
        this.oldScoreboards = new HashMap<>();
        setData();
    }

    private void setData() {
    	oldTeam1Text = ChatColor.BLUE + "Team 1: " + ChatColor.AQUA + "0";
    	oldTeam2Text = ChatColor.RED + "Team 2: " + ChatColor.AQUA + "0";
        oldTimeText = ChatColor.GOLD + "Time: " + ChatColor.AQUA + "?";
        sideBar.getScore(oldTeam1Text).setScore(3);
        sideBar.getScore(oldTeam2Text).setScore(2);
        sideBar.getScore(oldTimeText).setScore(1);
    }

    public void addPlayerToScoreboard(Player player) {
        oldScoreboards.put(player, player.getScoreboard());
        player.setScoreboard(board);
    }

    public void removePlayerFromScorebard(Player player) {
        if (oldScoreboards.containsKey(player)) {
            player.setScoreboard(oldScoreboards.get(player));
            oldScoreboards.remove(player);
        }
    }

    public void addPlayerToTeam1(Player player) {
        team1.addEntry(player.getName());
    }

    public void addPlayerToTeam2(Player player) {
        team2.addEntry(player.getName());
    }

    public void setNewTime(Integer time) {
        board.resetScores(oldTimeText);
        oldTimeText = ChatColor.GOLD + "Time: " + ChatColor.AQUA + time;
        sideBar.getScore(oldTimeText).setScore(1);
    }
    
    public void setTeamScore(int team, int score)
    {
    	if (team == 1)
    	{
    		board.resetScores(oldTeam1Text);
    		oldTeam2Text = ChatColor.BLUE + "Team 1: " + ChatColor.AQUA + score;
    		sideBar.getScore(oldTeam1Text).setScore(3);
    	}
    	else
    	{
    		board.resetScores(oldTeam2Text);
    		oldTeam2Text = ChatColor.RED + "Team 2: " + ChatColor.AQUA + score;
    		sideBar.getScore(oldTeam2Text).setScore(2);
    	}
    }
}