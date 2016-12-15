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

    private String oldText;

    private Team team1, team2;

    public PBScoreboard(JavaPlugin plugin) {
        this.board = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.sideBar = board.registerNewObjective("sidebar", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.team1 = board.registerNewTeam("Team1");
        this.team2 = board.registerNewTeam("Team2");
        this.oldScoreboards = new HashMap<>();
        setData();
    }

    private void setData() {
        sideBar.getScore(ChatColor.RED + "Information:").setScore(2);
        oldText = ChatColor.GOLD + "Time: " + ChatColor.AQUA + "?";
        sideBar.getScore(oldText).setScore(1);
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
        board.resetScores(oldText);
        oldText = ChatColor.GOLD + "Time: " + ChatColor.AQUA + time;
        sideBar.getScore(oldText).setScore(1);
    }
}
