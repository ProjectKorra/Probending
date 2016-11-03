/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.enums;

/**
 *
 * @author Ivo
 */
public enum GameType {

    DEFAULT(3, 180, false, false),
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
