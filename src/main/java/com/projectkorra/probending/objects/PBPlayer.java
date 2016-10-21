package com.projectkorra.probending.objects;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PBPlayer {

    private UUID uuid;
    private OfflinePlayer player;
    private PBTeam team;
    private Double points;

    public PBPlayer(UUID uuid, PBTeam team, Double points) {
        this.uuid = uuid;
        this.player = Bukkit.getOfflinePlayer(uuid);
        this.team = team;
        this.points = points;
    }
    
    public OfflinePlayer getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PBTeam getTeam() {
        return this.team;
    }

    public Double getPoints() {
        return points;
    }
}
