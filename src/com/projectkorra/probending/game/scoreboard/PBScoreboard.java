/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.game.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.objects.PBTeam;

/**
 *
 * @author Ivo
 */
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

    public static void showInformation(final Player player, PBPlayer pbPlayer, JavaPlugin plugin) {
        final Scoreboard oldBoard = player.getScoreboard();

        Scoreboard newBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(newBoard);
        Objective objectiveSidebar = newBoard.registerNewObjective("sidebar", "dummy");
        objectiveSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        objectiveSidebar.setDisplayName("Probending Scoreboard");
        objectiveSidebar.getScore(ChatColor.BOLD + Bukkit.getOfflinePlayer(pbPlayer.getUUID()).getName() + "'s profile:").setScore(10);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Wins1: " + ChatColor.AQUA + pbPlayer.getIndividualWins(true)).setScore(9);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Wins3: " + ChatColor.AQUA + pbPlayer.getIndividualWins(false)).setScore(8);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Games: " + ChatColor.AQUA + pbPlayer.getGamesPlayed()).setScore(7);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Rating: " + ChatColor.AQUA + "N/A").setScore(6);
        objectiveSidebar.getScore(ChatColor.BOLD + "Team profile:").setScore(5);
        PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(player);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Role: " + ChatColor.AQUA + (team != null ? team.getMembers().get(player.getUniqueId()).getDisplay() : "No team")).setScore(4);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Name: " + ChatColor.AQUA + (team != null ? team.getTeamName() : "No team")).setScore(3);
        objectiveSidebar.getScore(ChatColor.YELLOW + "Wins: " + ChatColor.AQUA + (team != null ? team.getWins() : "No team")).setScore(2);
        objectiveSidebar.getScore(ChatColor.YELLOW + "TGames: " + ChatColor.AQUA + (team != null ? team.getGamesPlayed() : "No team")).setScore(1);
        new BukkitRunnable() {

            @Override
            public void run() {
                //Revert the scoreboard!
                if (player.isOnline()) {
                    player.setScoreboard(oldBoard);
                }
            }
        }.runTaskLater(plugin, 500l);
    }
}
