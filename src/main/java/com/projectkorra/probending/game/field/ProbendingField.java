package com.projectkorra.probending.game.field;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class ProbendingField {

    private static Integer INDEX = 0;

    private Integer fieldNumber;

    //These are all teleport locations
    private Map<Integer, Location> team1StartLocs, team2StartLocs;
    private Location team1Location1, team1Location2, team1Location3, team1KnockedOffLocation;
    private Location team2Location1, team2Location2, team2Location3, team2KnockedOffLocation;

    //These are all names of the regions
    private String team1Field1, team1Field2, team1Field3;
    private String team2Field1, team2Field2, team2Field3;
    private String kockOffArea;

    public ProbendingField() {
        this.fieldNumber = INDEX;
        this.team1StartLocs = new HashMap<>();
        this.team2StartLocs = new HashMap<>();
        INDEX++;
    }

    public ProbendingField(Integer fieldNumber, Map<Integer, Location> team1StartLocs, Map<Integer, Location> team2StartLocs,
            Location team1Location1, Location team1Location2, Location team1Location3, Location team1KnockedOffLocation,
            Location team2Location1, Location team2Location2, Location team2Location3, Location team2KnockedOffLocation,
            String team1Field1, String team1Field2, String team1Field3, String team2Field1, String team2Field2, String team2Field3,
            String kockOffArea) {
        this.fieldNumber = fieldNumber;
        this.team1StartLocs = team1StartLocs;
        this.team2StartLocs = team2StartLocs;
        this.team1Location1 = team1Location1;
        this.team1Location2 = team1Location2;
        this.team1Location3 = team1Location3;
        this.team1KnockedOffLocation = team1KnockedOffLocation;
        this.team2Location1 = team2Location1;
        this.team2Location2 = team2Location2;
        this.team2Location3 = team2Location3;
        this.team2KnockedOffLocation = team2KnockedOffLocation;
        this.team1Field1 = team1Field1;
        this.team1Field2 = team1Field2;
        this.team1Field3 = team1Field3;
        this.team2Field1 = team2Field1;
        this.team2Field2 = team2Field2;
        this.team2Field3 = team2Field3;
        this.kockOffArea = kockOffArea;
        if (fieldNumber >= INDEX) {
            INDEX = fieldNumber + 1;
        }
    }

    public Integer FieldNumber() {
        return fieldNumber;
    }

    public Boolean isFieldDone() {
        if (team1StartLocs.isEmpty() || team2StartLocs.isEmpty() || team1Location1 == null
                || team1Location2 == null || team1Location3 == null || team1KnockedOffLocation == null
                || team2Location1 == null || team2Location2 == null || team2Location3 == null
                || team2KnockedOffLocation == null || team1Field1 == null || team1Field2 == null
                || team1Field3 == null || team2Field1 == null || team2Field2 == null
                || team2Field3 == null || kockOffArea == null) {
            return false;
        }
        return true;
    }

    public Location getStartPointTeam1(Integer pNR) {
        if (team1StartLocs.containsKey(pNR)) {
            return team1StartLocs.get(pNR);
        }
        return null;
    }

    public Location getStartPointTeam2(Integer pNR) {
        if (team2StartLocs.containsKey(pNR)) {
            return team2StartLocs.get(pNR);
        }
        return null;
    }

    public Location getTeam1Location1() {
        return team1Location1;
    }

    public Location getTeam1Location2() {
        return team1Location2;
    }

    public Location getTeam1Location3() {
        return team1Location3;
    }

    public Location getTeam1KnockedOffLocation() {
        return team1KnockedOffLocation;
    }

    public Location getTeam2Location1() {
        return team2Location1;
    }

    public Location getTeam2Location2() {
        return team2Location2;
    }

    public Location getTeam2Location3() {
        return team2Location3;
    }

    public Location getTeam2KnockedOffLocation() {
        return team2KnockedOffLocation;
    }

    public String getTeam1Field1() {
        return team1Field1;
    }

    public String getTeam1Field2() {
        return team1Field2;
    }

    public String getTeam1Field3() {
        return team1Field3;
    }

    public String getTeam2Field1() {
        return team2Field1;
    }

    public String getTeam2Field2() {
        return team2Field2;
    }

    public String getTeam2Field3() {
        return team2Field3;
    }

    public String getKockOffArea() {
        return kockOffArea;
    }

    public void setStartPointTeam1(Integer pNR, Location location) {
        team1StartLocs.put(pNR, location);
    }

    public void setStartPointTeam2(Integer pNR, Location location) {
        team2StartLocs.put(pNR, location);
    }

    public void setTeam1Location1(Location team1Location1) {
        this.team1Location1 = team1Location1;
    }

    public void setTeam1Location2(Location team1Location2) {
        this.team1Location2 = team1Location2;
    }

    public void setTeam1Location3(Location team1Location3) {
        this.team1Location3 = team1Location3;
    }

    public void setTeam1KnockedOffLocation(Location team1KnockedOffLocation) {
        this.team1KnockedOffLocation = team1KnockedOffLocation;
    }

    public void setTeam2Location1(Location team2Location1) {
        this.team2Location1 = team2Location1;
    }

    public void setTeam2Location2(Location team2Location2) {
        this.team2Location2 = team2Location2;
    }

    public void setTeam2Location3(Location team2Location3) {
        this.team2Location3 = team2Location3;
    }

    public void setTeam2KnockedOffLocation(Location team2KnockedOffLocation) {
        this.team2KnockedOffLocation = team2KnockedOffLocation;
    }

    public void setTeam1Field1(String team1Field1) {
        this.team1Field1 = team1Field1;
    }

    public void setTeam1Field2(String team1Field2) {
        this.team1Field2 = team1Field2;
    }

    public void setTeam1Field3(String team1Field3) {
        this.team1Field3 = team1Field3;
    }

    public void setTeam2Field1(String team2Field1) {
        this.team2Field1 = team2Field1;
    }

    public void setTeam2Field2(String team2Field2) {
        this.team2Field2 = team2Field2;
    }

    public void setTeam2Field3(String team2Field3) {
        this.team2Field3 = team2Field3;
    }

    public void setKockOffArea(String kockOffArea) {
        this.kockOffArea = kockOffArea;
    }
}
