package com.projectkorra.probending.objects;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.enums.GamePlayerMode;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.TeamGame;

public class PBPlayer {

    private int _id, _1v1_wins, _3v3_wins, _gamesPlayed, _teamWins, _teamGamesPlayed, _rating;
    private UUID _uuid;

    public PBPlayer(int id, UUID uuid, int soloWin, int groupWin, int gamesPlayed, int teamWins, int teamGamesPlayed, int rating) {
        _id = id;
        _uuid = uuid;
        _1v1_wins = soloWin;
        _3v3_wins = groupWin;
        _gamesPlayed = gamesPlayed;
        _teamWins = teamWins;
        _teamGamesPlayed = teamGamesPlayed;
        _rating = rating;
    }

    public int getID() {
        return _id;
    }

    public UUID getUUID() {
        return _uuid;
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
    
    public int getRating() {
    	return _rating;
	}

    /**
     * Currently does nothing, added for future use.
     *
     * @param ending 1:lose|2:win|3:draw
     * @param input some score that will be put into an algorithm
     */
    public void adjustRating(int ending, long input) {
    	//Put some rating algorithm here
    	//Probending.get().getProbendingHandler().updatePBPlayer(this);
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
            if (game.getGamePlayerMode().equals(GamePlayerMode.SINGLE)) {
                _1v1_wins += 1;
            } else {
                _3v3_wins += 1;
            }
        } else if (winners.isEmpty()) {
            ending = 3;
        }
        //adjustRating(ending, input);
    	Probending.get().getProbendingHandler().updatePBPlayer(this);
    }
    
    public void updateTeamStats(TeamGame game, boolean winningTeam) {
    	_teamGamesPlayed += 1;
    	if (winningTeam) {
    		_teamWins += 1;
    	}
    	Probending.get().getProbendingHandler().updatePBPlayer(this);
    }
}
