package com.projectkorra.probending.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.projectkorra.probending.objects.ProbendingField;
import com.projectkorra.probending.libraries.flatfile.FileManager;
import java.util.ArrayList;

public class FFProbendingField extends FileManager {

    public FFProbendingField(JavaPlugin plugin) {
        super(plugin, "", "fields");
    }

    private Location stringToLoc(String str) {
        try {
            String[] args = str.split("@");
            World world = Bukkit.getServer().getWorld(args[0]);
            Double x = Double.parseDouble(args[1]);
            Double y = Double.parseDouble(args[2]);
            Double z = Double.parseDouble(args[3]);
            Float pitch = Float.parseFloat(args[4]);
            Float yaw = Float.parseFloat(args[5]);
            Location loc = new Location(world, x, y, z);
            loc.setPitch(pitch);
            loc.setYaw(yaw);

            return loc;
        } catch (Exception ex) {
            return null;
        }
    }

    private String locToString(Location location, boolean playerSpawn) {
        Location l;
        if (playerSpawn) {
            l = location.getBlock().getLocation().add(0.5, 0, 0.5);
        } else {
            l = location.getBlock().getLocation();
        }
        return l.getWorld().getName() + "@" + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + l.getPitch() + "@" + l.getYaw();
    }

    public List<ProbendingField> loadFields() {
        List<ProbendingField> fields = Lists.newArrayList();

        ConfigurationSection fieldList = getConfigurationSection("fields");
        if (fieldList == null) {
            return new ArrayList<>();
        }
        for (String fieldKey : fieldList.getKeys(false)) {
            String name = fieldKey;
            ConfigurationSection fieldSection = fieldList.getConfigurationSection(fieldKey);
            String team1RG1 = fieldSection.getString("Team1RG1");
            String team1RG2 = fieldSection.getString("Team1RG2");
            String team1RG3 = fieldSection.getString("Team1RG3");
            String team2RG1 = fieldSection.getString("Team2RG1");
            String team2RG2 = fieldSection.getString("Team2RG2");
            String team2RG3 = fieldSection.getString("Team2RG3");
            String knockOff = fieldSection.getString("KnockOff");
            String deathMatch = fieldSection.getString("DeathMatch");
            Location team1Loc1 = stringToLoc(fieldSection.getString("Team1LC1"));
            Location team1Loc2 = stringToLoc(fieldSection.getString("Team1LC2"));
            Location team1Loc3 = stringToLoc(fieldSection.getString("Team1LC3"));
            Location team1KnockOff = stringToLoc(fieldSection.getString("Team1LC4"));
            Location team2Loc1 = stringToLoc(fieldSection.getString("Team2LC1"));
            Location team2Loc2 = stringToLoc(fieldSection.getString("Team2LC2"));
            Location team2Loc3 = stringToLoc(fieldSection.getString("Team2LC3"));
            Location team2KnockOff = stringToLoc(fieldSection.getString("Team2LC4"));
            Location team1DMLoc = stringToLoc(fieldSection.getString("Team1DMLC"));
            Location team2DMLoc = stringToLoc(fieldSection.getString("Team2DMLC"));
            Map<Integer, Location> team1Spawns = new HashMap<>();
            Map<Integer, Location> team2Spawns = new HashMap<>();
            List<String> unparsedSpawns = fieldSection.getStringList("spawns");
            for (String unparsed : unparsedSpawns) {
                String[] parts = unparsed.split("\\|");
                Integer team = Integer.parseInt(parts[0]);
                Integer spawn = Integer.parseInt(parts[1]);
                Location location = stringToLoc(parts[2]);
                if (team == 1) {
                    team1Spawns.put(spawn, location);
                } else if (team == 2) {
                    team2Spawns.put(spawn, location);
                }
            }
            fields.add(new ProbendingField(name, team1Spawns, team2Spawns, team1Loc1, team1Loc2, team1Loc3,
                    team1KnockOff, team2Loc1, team2Loc2, team2Loc3, team2KnockOff, team1DMLoc, team2DMLoc, team1RG1, team1RG2, team1RG3,
                    team2RG1, team2RG2, team2RG3, knockOff, deathMatch));
        }

        return fields;
    }

    public void insertField(ProbendingField field) {
        String name = field.getFieldName();
        set("fields." + name + ".Team1RG1", field.getTeam1Field1(), false);
        set("fields." + name + ".Team1RG2", field.getTeam1Field2(), false);
        set("fields." + name + ".Team1RG3", field.getTeam1Field3(), false);
        set("fields." + name + ".Team2RG1", field.getTeam2Field1(), false);
        set("fields." + name + ".Team2RG2", field.getTeam2Field2(), false);
        set("fields." + name + ".Team2RG3", field.getTeam2Field3(), false);
        set("fields." + name + ".KnockOff", field.getKnockOffArea(), false);
        set("fields." + name + ".DeathMatch", field.getDeathMathArea(), false);
        set("fields." + name + ".Team1LC1", locToString(field.getTeam1Location1(), true), false);
        set("fields." + name + ".Team1LC2", locToString(field.getTeam1Location2(), true), false);
        set("fields." + name + ".Team1LC3", locToString(field.getTeam1Location3(), true), false);
        set("fields." + name + ".Team1LC4", locToString(field.getTeam1KnockedOffLocation(), true), false);
        set("fields." + name + ".Team2LC1", locToString(field.getTeam2Location1(), true), false);
        set("fields." + name + ".Team2LC2", locToString(field.getTeam2Location2(), true), false);
        set("fields." + name + ".Team2LC3", locToString(field.getTeam2Location3(), true), false);
        set("fields." + name + ".Team2LC4", locToString(field.getTeam2KnockedOffLocation(), true), false);
        set("fields." + name + ".Team1DMLC", locToString(field.getTeam1DMLocation(), true), false);
        set("fields." + name + ".Team2DMLC", locToString(field.getTeam2DMLocation(), true), false);
        List<String> spawns = Lists.newArrayList();
        for (int i = 1; i < 4; i++) {
            Location t1 = field.getTeam1StartLocs().get(i);
            Location t2 = field.getTeam2StartLocs().get(i);
            spawns.add("1" + "|" + i + "|" + locToString(t1, true));
            spawns.add("2" + "|" + i + "|" + locToString(t2, true));
        }
        set("fields." + name + ".spawns", spawns, false);

        save();
    }

    public void deleteField(ProbendingField field) {
        set("fields.name", null, true);
    }
}
