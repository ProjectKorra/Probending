package com.projectkorra.probending.game;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.enums.GamePlayerMode;
import com.projectkorra.probending.enums.GameType;
import com.projectkorra.probending.managers.ProbendingHandler;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.ProbendingField;

public class TeamGame extends Game {

    private PBTeam team1;
    private PBTeam team2;

    public TeamGame(JavaPlugin plugin, ProbendingHandler handler, GameType type, GamePlayerMode mode, ProbendingField field, Set<Player> team1Players, Set<Player> team2Players, PBTeam team1, PBTeam team2) {
        super(plugin, handler, type, mode, field, team1Players, team2Players);
        this.team1 = team1;
        this.team2 = team2;
    }

    public PBTeam getTeam1() {
        return team1;
    }

    public PBTeam getTeam2() {
        return team2;
    }
}