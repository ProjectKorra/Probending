package com.projectkorra.probending.objects;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class ProbendingField {

    private static Integer INDEX = 0;
    
    private String _fieldName;
    //These are all teleport locations
    private Map<Integer, Location> _team1StartLocs, _team2StartLocs;
    private Location _team1Location1, _team1Location2, _team1Location3, _team1KnockedOffLocation;
    private Location _team2Location1, _team2Location2, _team2Location3, _team2KnockedOffLocation;

    //These are all names of the regions
    private String _team1Field1, _team1Field2, _team1Field3;
    private String _team2Field1, _team2Field2, _team2Field3;
    private String _knockOffArea;

    public ProbendingField() {
        _fieldName = "" + INDEX;
        _team1StartLocs = new HashMap<>();
        _team2StartLocs = new HashMap<>();
        INDEX++;
    }

    public ProbendingField(String fieldName, Map<Integer, Location> team1StartLocs, Map<Integer, Location> team2StartLocs,
            Location team1Location1, Location team1Location2, Location team1Location3, Location team1KnockedOffLocation,
            Location team2Location1, Location team2Location2, Location team2Location3, Location team2KnockedOffLocation,
            String team1Field1, String team1Field2, String team1Field3, String team2Field1, String team2Field2, String team2Field3,
            String knockOffArea) {
        Integer fieldNumber;
        try { 
            fieldNumber = Integer.parseInt(fieldName);
        } catch (Exception ex) {
            return;
        }
        if (fieldNumber >= INDEX) {
            INDEX = fieldNumber + 1;
        } else {
            return;
        }
        _fieldName = fieldName;
        _team1StartLocs = team1StartLocs;
        _team2StartLocs = team2StartLocs;
        _team1Location1 = team1Location1;
        _team1Location2 = team1Location2;
        _team1Location3 = team1Location3;
        _team1KnockedOffLocation = team1KnockedOffLocation;
        _team2Location1 = team2Location1;
        _team2Location2 = team2Location2;
        _team2Location3 = team2Location3;
        _team2KnockedOffLocation = team2KnockedOffLocation;
        _team1Field1 = team1Field1;
        _team1Field2 = team1Field2;
        _team1Field3 = team1Field3;
        _team2Field1 = team2Field1;
        _team2Field2 = team2Field2;
        _team2Field3 = team2Field3;
        _knockOffArea = knockOffArea;
    }

    public String getFieldName() {
        return _fieldName;
    }

    public Boolean isFieldDone() {
        if (_fieldName.isEmpty() || _team1StartLocs.isEmpty() || _team2StartLocs.isEmpty() || _team1Location1 == null
                || _team1Location2 == null || _team1Location3 == null || _team1KnockedOffLocation == null
                || _team2Location1 == null || _team2Location2 == null || _team2Location3 == null
                || _team2KnockedOffLocation == null || _team1Field1 == null || _team1Field2 == null
                || _team1Field3 == null || _team2Field1 == null || _team2Field2 == null
                || _team2Field3 == null || _knockOffArea == null) {
            return false;
        }
        return true;
    }

    public Location getStartPointTeam1(Integer pNR) {
        if (_team1StartLocs.containsKey(pNR)) {
            return _team1StartLocs.get(pNR);
        }
        return null;
    }

    public Location getStartPointTeam2(Integer pNR) {
        if (_team2StartLocs.containsKey(pNR)) {
            return _team2StartLocs.get(pNR);
        }
        return null;
    }

    public Map<Integer, Location> getTeam1StartLocs() {
        return _team1StartLocs;
    }

    public Map<Integer, Location> getTeam2StartLocs() {
        return _team2StartLocs;
    }

    public Location getTeam1Location1() {
        return _team1Location1;
    }

    public Location getTeam1Location2() {
        return _team1Location2;
    }

    public Location getTeam1Location3() {
        return _team1Location3;
    }

    public Location getTeam1KnockedOffLocation() {
        return _team1KnockedOffLocation;
    }

    public Location getTeam2Location1() {
        return _team2Location1;
    }

    public Location getTeam2Location2() {
        return _team2Location2;
    }

    public Location getTeam2Location3() {
        return _team2Location3;
    }

    public Location getTeam2KnockedOffLocation() {
        return _team2KnockedOffLocation;
    }

    public String getTeam1Field1() {
        return _team1Field1;
    }

    public String getTeam1Field2() {
        return _team1Field2;
    }

    public String getTeam1Field3() {
        return _team1Field3;
    }

    public String getTeam2Field1() {
        return _team2Field1;
    }

    public String getTeam2Field2() {
        return _team2Field2;
    }

    public String getTeam2Field3() {
        return _team2Field3;
    }

    public String getKnockOffArea() {
        return _knockOffArea;
    }

    public void setFieldName(String name) {
        _fieldName = name;
    }

    public void setStartPointTeam1(Integer pNR, Location location) {
        _team1StartLocs.put(pNR, location);
    }

    public void setStartPointTeam2(Integer pNR, Location location) {
        _team2StartLocs.put(pNR, location);
    }

    public void setTeam1Location1(Location team1Location1) {
        _team1Location1 = team1Location1;
    }

    public void setTeam1Location2(Location team1Location2) {
        _team1Location2 = team1Location2;
    }

    public void setTeam1Location3(Location team1Location3) {
        _team1Location3 = team1Location3;
    }

    public void setTeam1KnockedOffLocation(Location team1KnockedOffLocation) {
        _team1KnockedOffLocation = team1KnockedOffLocation;
    }

    public void setTeam2Location1(Location team2Location1) {
        _team2Location1 = team2Location1;
    }

    public void setTeam2Location2(Location team2Location2) {
        _team2Location2 = team2Location2;
    }

    public void setTeam2Location3(Location team2Location3) {
        _team2Location3 = team2Location3;
    }

    public void setTeam2KnockedOffLocation(Location team2KnockedOffLocation) {
        _team2KnockedOffLocation = team2KnockedOffLocation;
    }

    public void setTeam1Field1(String team1Field1) {
        _team1Field1 = team1Field1;
    }

    public void setTeam1Field2(String team1Field2) {
        _team1Field2 = team1Field2;
    }

    public void setTeam1Field3(String team1Field3) {
        _team1Field3 = team1Field3;
    }

    public void setTeam2Field1(String team2Field1) {
        _team2Field1 = team2Field1;
    }

    public void setTeam2Field2(String team2Field2) {
        _team2Field2 = team2Field2;
    }

    public void setTeam2Field3(String team2Field3) {
        _team2Field3 = team2Field3;
    }

    public void setKnockOffArea(String knockOffArea) {
        _knockOffArea = knockOffArea;
    }
}
