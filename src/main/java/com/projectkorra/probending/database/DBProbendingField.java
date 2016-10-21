/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.database;

import com.projectkorra.probending.game.field.ProbendingField;
import com.projectkorra.probending.libraries.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 *
 * @author Ivo
 */
public class DBProbendingField {

    static {
        Database database = DatabaseHandler.getDababase();
        if (!DatabaseHandler.isMysql()) {
            if (!database.tableExists("Fields")) {
                database.modifyQuery("CREATE TABLE Fields ("
                        + "`FieldID` INTEGER(3) PRIMARY KEY,"
                        + "`Team1RG1` TEXT(32),"
                        + "`Team1RG2` TEXT(32),"
                        + "`Team1RG3` TEXT(32),"
                        + "`Team2RG1` TEXT(32),"
                        + "`Team2RG2` TEXT(32),"
                        + "`Team2RG3` TEXT(32),"
                        + "`KnockOff` TEXT(32),"
                        + "`Team1LC1` TEXT(128),"
                        + "`Team1LC2` TEXT(128),"
                        + "`Team1LC3` TEXT(128),"
                        + "`Team1LC4` TEXT(128),"
                        + "`Team2LC1` TEXT(128),"
                        + "`Team2LC2` TEXT(128),"
                        + "`Team2LC3` TEXT(128),"
                        + "`Team2LC4` TEXT(128))");
            }
            if (!database.tableExists("SpawnPoints")) {
                database.modifyQuery("CREATE TABLE SpawnPoints ("
                        + "`FieldID` INTEGER(3) PRIMARY KEY,"
                        + "`Team` INTEGER(1),"
                        + "`Spawn` INTEGER(2),"
                        + "`Location` TEXT(128))");
            }
        }
    }

    public static void insertField(ProbendingField field) {
        Database database = DatabaseHandler.getDababase();
        database.modifyQuery("INSERT INTO Fields VALUES ("
                + "'" + field.getFieldNumber() + "', "
                + "'" + field.getTeam1Field1() + "', "
                + "'" + field.getTeam1Field2() + "', "
                + "'" + field.getTeam1Field3() + "', "
                + "'" + field.getTeam2Field1() + "', "
                + "'" + field.getTeam2Field2() + "', "
                + "'" + field.getTeam2Field3() + "', "
                + "'" + field.getKockOffArea() + "', "
                + "'" + locToString(field.getTeam1Location1(), true) + "', "
                + "'" + locToString(field.getTeam1Location2(), true) + "', "
                + "'" + locToString(field.getTeam1Location3(), true) + "', "
                + "'" + locToString(field.getTeam1KnockedOffLocation(), true) + "', "
                + "'" + locToString(field.getTeam2Location1(), true) + "', "
                + "'" + locToString(field.getTeam2Location2(), true) + "', "
                + "'" + locToString(field.getTeam2Location3(), true) + "', "
                + "'" + locToString(field.getTeam2KnockedOffLocation(), true) + "')");
        for (Integer i : field.getTeam1StartLocs().keySet()) {
            database.modifyQuery("INSERT INTO SpawnPoints VALUES ("
                    + "'" + field.getFieldNumber() + "', "
                    + "1, "
                    + "'" + i + "', "
                    + "'" + locToString(field.getStartPointTeam1(i), true) + "')");
        }
        for (Integer i : field.getTeam2StartLocs().keySet()) {
            database.modifyQuery("INSERT INTO SpawnPoints VALUES ("
                    + "'" + field.getFieldNumber() + "', "
                    + "2, "
                    + "'" + i + "', "
                    + "'" + locToString(field.getStartPointTeam2(i), true) + "')");
        }
    }

    public static List<ProbendingField> getFields() {
        Database database = DatabaseHandler.getDababase();
        List<ProbendingField> fields = new ArrayList<>();
        ResultSet rs1 = database.readQuery("SELECT * FROM Fields;");
        try {
            while (rs1.next()) {
                Integer fieldNumber = rs1.getInt("FieldID");
                String team1RG1 = rs1.getString("Team1RG1");
                String team1RG2 = rs1.getString("Team1RG2");
                String team1RG3 = rs1.getString("Team1RG3");
                String team2RG1 = rs1.getString("Team2RG1");
                String team2RG2 = rs1.getString("Team2RG2");
                String team2RG3 = rs1.getString("Team2RG3");
                String knockOff = rs1.getString("KnockOff");
                Location team1Loc1 = stringToLoc(rs1.getString("Team1LC1"));
                Location team1Loc2 = stringToLoc(rs1.getString("Team1LC2"));
                Location team1Loc3 = stringToLoc(rs1.getString("Team1LC3"));
                Location team1KnockOff = stringToLoc(rs1.getString("Team1LC4"));
                Location team2Loc1 = stringToLoc(rs1.getString("Team2LC1"));
                Location team2Loc2 = stringToLoc(rs1.getString("Team2LC2"));
                Location team2Loc3 = stringToLoc(rs1.getString("Team2LC3"));
                Location team2KnockOff = stringToLoc(rs1.getString("Team2LC4"));
                Map<Integer, Location> team1Spawns = new HashMap<>();
                Map<Integer, Location> team2Spawns = new HashMap<>();
                ResultSet rs2 = database.readQuery("SELECT * FROM SpawnPoints WHERE FieldID = '" + fieldNumber + "';");
                while (rs2.next()) {
                    Integer team = rs2.getInt("Team");
                    Integer spawn = rs2.getInt("Spawn");
                    Location location = stringToLoc(rs2.getString("Location"));
                    if (team == 1) {
                        team1Spawns.put(spawn, location);
                    } else if (team == 2) {
                        team2Spawns.put(spawn, location);
                    }
                }
                fields.add(new ProbendingField(fieldNumber, team1Spawns, team2Spawns, team1Loc1, team1Loc2, team1Loc3,
                        team1KnockOff, team2Loc1, team2Loc2, team2Loc3, team2KnockOff, team1RG1, team1RG2, team1RG3,
                        team2RG1, team2RG2, team2RG3, knockOff));
            }
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.WARNING, ex.getMessage());
        }
        return fields;
    }

    private static Location stringToLoc(String str) {
        try {
            String[] args = str.split("\\|");
            String[] dir = args[4].split("~");
            World world = Bukkit.getServer().getWorld(args[0]);
            Double x = Double.parseDouble(args[1]);
            Double y = Double.parseDouble(args[2]);
            Double z = Double.parseDouble(args[3]);
            Vector v = new Vector(Double.parseDouble(dir[0]), Double.parseDouble(dir[1]), Double.parseDouble(dir[2]));
            return new Location(world, x, y, z).setDirection(v);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String locToString(Location location, boolean playerSpawn) {
        DecimalFormat df = new DecimalFormat("#.00");
        Location l;
        if (playerSpawn) {
            l = location.getBlock().getLocation().add(0.5, 0, 0.5);
        } else {
            l = location.getBlock().getLocation();
        }
        return location.getWorld().getName() + "|" + (l.getX()) + "|" + l.getY() + "|" + l.getZ() + "|"
                + df.format(location.getDirection().getX()) + "~" + df.format(location.getDirection().getY()) + "~" + df.format(location.getDirection().getZ());
    }
}
