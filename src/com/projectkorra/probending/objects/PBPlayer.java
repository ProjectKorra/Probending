package com.projectkorra.probending.objects;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.TeamGame;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;
import com.projectkorra.projectkorra.Element;

import net.md_5.bungee.api.ChatColor;

public class PBPlayer {

    private int _id, _1v1_wins = 0, _3v3_wins = 0, _gamesPlayed = 0, _teamWins = 0, _teamGamesPlayed = 0;
    private UUID _uuid;
    private double _points;
    private long _rating;

    public PBPlayer(int id, UUID uuid, double points) {
        _id = id;
        _uuid = uuid;
        _points = points;
    }

    public int getID() {
        return _id;
    }

    public UUID getUUID() {
        return _uuid;
    }

    public double getPointsEarned() {
        return _points;
    }
    
    public int getGamesPlayed() {
    	return _gamesPlayed;
    }
    
    public int getTeamGamesPlayed() {
    	return _teamGamesPlayed;
    }
    
    public int getTeamWins() {
    	return _teamWins;
    }
    
    public long getRating() {
    	return _rating;
    }
    
    /**
     * Currently does nothing, added for future use.
     * @param ending 1:lose|2:win|3:draw
     * @param input some score that will be put into an algorithm
     */
    public void adjustRating(int ending, long input) {
    	//Put some rating algorithm here
    }
    
    public PBTeam getTeam() {
    	OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(_uuid);
    	if (oPlayer.isOnline()) {
    		return Probending.get().getTeamManager().getTeamFromPlayer((Player) oPlayer);
    	} else {
    		return Probending.get().getTeamManager().getTeamFromOfflinePlayer(oPlayer);
    	}
    }
    
    public TeamMemberRole getRole() {
    	return getTeam().getMembers().get(_uuid);
    }
    
    public Element getElementRole() {
    	return Element.fromString(ChatColor.stripColor(getRole().getDisplay()));
    }
    
    public int getIndividualWins(boolean _1v1) {
    	if (_1v1) {
    		return _1v1_wins;
    	} else {
    		return _3v3_wins;
    	}
    }
    
    public void updateIndividualStats(Player player, Game game, Set<Player> winners) {
    	_gamesPlayed += 1;
    	int ending = 1;
    	if (winners.contains(player)) {
    		ending = 2;
    		if (game.is1v1()) {
    			_1v1_wins += 1;
    		} else {
    			_3v3_wins += 1;
    		}
    	} else if (winners.isEmpty()) {
    		ending = 3;
    	}
    	//adjustRating(ending, input);
    }
    
    public void updateTeamStats(TeamGame game, PBTeam winningTeam) {
    	_teamGamesPlayed += 1;
    	if (getTeam() == winningTeam) {
    		_teamWins += 1;
    	}
    }
}
