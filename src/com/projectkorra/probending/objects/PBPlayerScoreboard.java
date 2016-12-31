package com.projectkorra.probending.objects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.projectkorra.probending.Probending;

public class PBPlayerScoreboard{
	
	private static Map<Player, PBPlayerScoreboard> manager = new HashMap<>();

	private Player player;
	private Scoreboard oldBoard;

	public PBPlayerScoreboard(Player player) {
		this.player = player;
		manager.put(player, this);
	}

	public void displayInfo(Player infoPlayer) {
		PBPlayer pbPlayer = Probending.get().getProbendingHandler().getPBPlayer(infoPlayer.getUniqueId());
		if (pbPlayer == null) {
			return;
		}
		
		if (oldBoard != null) {
			revert();
		}
		
		oldBoard = player.getScoreboard();
		
		Scoreboard newBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(newBoard);
		Objective objectiveSidebar = newBoard.registerNewObjective("PBINFO", "dummy");
		objectiveSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		objectiveSidebar.setDisplayName(infoPlayer.getName() + "'s Profile");
		objectiveSidebar.getScore(ChatColor.YELLOW + "- Wins1: " + ChatColor.AQUA + pbPlayer.getIndividualWins(true)).setScore(9);
		objectiveSidebar.getScore(ChatColor.YELLOW + "- Wins3: " + ChatColor.AQUA + pbPlayer.getIndividualWins(false)).setScore(8);
		objectiveSidebar.getScore(ChatColor.YELLOW + "- Games: " + ChatColor.AQUA + pbPlayer.getGamesPlayed()).setScore(7);
		objectiveSidebar.getScore(ChatColor.YELLOW + "- Rating: " + ChatColor.AQUA + "N/A").setScore(6);
		objectiveSidebar.getScore(ChatColor.BOLD + "Team stats:").setScore(5);

		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(infoPlayer);
		if (team != null) {
			objectiveSidebar.getScore(ChatColor.YELLOW + "- Role: " + ChatColor.AQUA + team.getMembers().get(player.getUniqueId()).getDisplay()).setScore(4);
			objectiveSidebar.getScore(ChatColor.YELLOW + "- Name: " + ChatColor.AQUA + team.getTeamName()).setScore(3);
			objectiveSidebar.getScore(ChatColor.YELLOW + "- Wins: " + ChatColor.AQUA + team.getWins()).setScore(2);
			objectiveSidebar.getScore(ChatColor.YELLOW + "- TGames: " + ChatColor.AQUA + team.getGamesPlayed()).setScore(1);
		} else {
			objectiveSidebar.getScore(ChatColor.RED + "No Team").setScore(4);
		}
		
		new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isOnline()) {
                	revert();
                }
            }
        }.runTaskLater(Probending.get(), 500l);
	}

	public void displayInfo(PBTeam infoTeam) {
		if (infoTeam == null) {
			return;
		}
		
		if (oldBoard != null) {
			revert();
		}
		oldBoard = player.getScoreboard();
		
		
		Scoreboard newBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(newBoard);
		Objective objectiveSidebar = newBoard.registerNewObjective("PBINFO", "dummy");
		objectiveSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		objectiveSidebar.setDisplayName(infoTeam.getTeamName() + "'s Team Profile");
		objectiveSidebar.getScore(ChatColor.YELLOW + "- Wins: " + ChatColor.AQUA + infoTeam.getWins()).setScore(2);
		objectiveSidebar.getScore(ChatColor.YELLOW + "- TGames: " + ChatColor.AQUA + infoTeam.getGamesPlayed()).setScore(1);
		new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isOnline()) {
                	revert();
                }
            }
        }.runTaskLater(Probending.get(), 500l);
	}

	public void revert() {
		player.setScoreboard(oldBoard);
	}
	
	public static void removePlayer(Player player) {
		if (manager.containsKey(player)) {
			manager.remove(player);
		}
	}
	
	public static PBPlayerScoreboard getFromPlayer(Player player) {
		if (player == null) {
			return null;
		}
		return manager.containsKey(player) ? manager.get(player) : null;
	}
}
