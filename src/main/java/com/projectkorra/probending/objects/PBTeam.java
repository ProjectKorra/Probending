package com.projectkorra.probending.objects;

public class PBTeam {

    private String name;
    private PBPlayer airbender;
    private PBPlayer waterbender;
    private PBPlayer earthbender;
    private PBPlayer firebender;
    private PBPlayer chiblocker;
    private PBPlayer leader;

    public PBTeam(String name, PBPlayer airbender, PBPlayer waterbender, PBPlayer earthbender, PBPlayer firebender, PBPlayer chiblocker, PBPlayer leader) {
        this.name = name;
        this.airbender = airbender;
        this.waterbender = waterbender;
        this.earthbender = earthbender;
        this.firebender = firebender;
        this.chiblocker = chiblocker;
        this.leader = leader;
    }

    public String getTeamName() {
        return this.name;
    }

    public PBPlayer getAirbender() {
        return this.airbender;
    }

    public PBPlayer getWaterbender() {
        return this.waterbender;
    }

    public PBPlayer getEarthbender() {
        return this.earthbender;
    }

    public PBPlayer getFireBender() {
        return this.firebender;
    }

    public PBPlayer getChiblocker() {
        return this.chiblocker;
    }

    public PBPlayer getLeader() {
        return this.leader;
    }

    public void setLeader(PBPlayer player) {
        leader = player;
    }

    public void setAirbender(PBPlayer player) {
        airbender = player;
    }

    public void setWaterbender(PBPlayer player) {
        waterbender = player;
    }

    public void setEarthbender(PBPlayer player) {
        earthbender = player;
    }

    public void setFirebender(PBPlayer player) {
        firebender = player;
    }

    public void setChiblocker(PBPlayer player) {
        chiblocker = player;
    }
}
