package com.projectkorra.probending.objects;

import java.util.UUID;

public class PBPlayer {

    private int _id;
    private UUID _uuid;
    private Double _points;

    public PBPlayer(int id, UUID uuid, Double points) {
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

    public Double getPointsEarned() {
        return _points;
    }
}
