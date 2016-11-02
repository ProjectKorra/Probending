package com.projectkorra.probending.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;

public class DBProbendingTeam extends DBInterpreter {

    public DBProbendingTeam(JavaPlugin plugin) {
        super(plugin);

        runAsync(new Runnable() {
            public void run() {
                if (!DatabaseHandler.getDatabase().tableExists("pb_teams")) {
                    String query = "CREATE TABLE pb_teams (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100) NOT NULL, leader VARCHAR(100) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX nameIndex (name), UNIQUE INDEX leaderIndex (leader));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_teams (id INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT, name TEXT NOT NULL UNIQUE, leader TEXT NOT NULL UNIQUE);";
                    }

                    DatabaseHandler.getDatabase().executeQuery(query);
                }
                if (!DatabaseHandler.getDatabase().tableExists("pb_team_members")) {
                    String query = "CREATE TABLE pb_team_members (id INT NOT NULL AUTO_INCREMENT, teamId INT NOT NULL, uuid VARCHAR(100) NOT NULL, role VARCHAR(50) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX uuidIndex (uuid), INDEX teamIndex (teamId), INDEX roleIndex (role));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_teams (id INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT, teamId INTEGER NOT NULL, uuid TEXT NOT NULL UNIQUE, role TEXT NOT NULL);CREATE INDEX teamIndex ON pb_players (teamId);CREATE INDEX roleIndex ON pb_players (role);";
                    }

                    DatabaseHandler.getDatabase().executeQuery(query);
                }
            }
        });
    }

    public void loadPBTeams(final Callback<List<PBTeam>> callback) {
        DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_teams;", new Callback<ResultSet>() {
            public void run(ResultSet rs) {
                List<PBTeam> teams = Lists.newArrayList();
                try {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        UUID leader = UUID.fromString(rs.getString("leader"));
                        Map<UUID, TeamMemberRole> members = new HashMap<>();
                        ResultSet memberSet = DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_team_members WHERE teamId=?;", id);
                        while (memberSet.next()) {
                            UUID uuid = UUID.fromString(memberSet.getString("uuid"));
                            TeamMemberRole role = TeamMemberRole.parseRole(memberSet.getString("role"));
                            members.put(uuid, role);
                        }
                        PBTeam team = new PBTeam(id, name, leader, members);
                        teams.add(team);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                callback.run(teams);
            }
        });
    }

    public void loadPBTeamsAsync(final Callback<List<PBTeam>> callback) {
        runAsync(new Runnable() {
            public void run() {
                loadPBTeams(new Callback<List<PBTeam>>() {
                    public void run(List<PBTeam> teams) {
                        final List<PBTeam> pbTeams = teams;
                        runSync(new Runnable() {
                            public void run() {
                                callback.run(pbTeams);
                            }
                        });
                    }
                });
            }
        });
    }

    public void updatePBTeam(final PBTeam team) {
        DatabaseHandler.getDatabase().executeQuery("UPDATE pb_teams SET name=?, leader=? WHERE id=?;", null, team.getTeamName(), team.getLeader().toString(), team.getID());
    }

    public void updatePBTeamAsync(final PBTeam team) {
        runAsync(new Runnable() {
            public void run() {
                updatePBTeam(team);
            }
        });
    }

    public void joinTeam(final UUID uuid, final TeamMemberRole role, final PBTeam team) {
        DatabaseHandler.getDatabase().executeQuery("INSERT INTO pb_team_members (uuid, teamId, role) VALUES (?, ?, ?) ON DUPLICATE KEY SET teamId=VALUES(teamId), role=VALUES(role);", null, uuid.toString(), team.getID(), role.toString());
    }

    public void joinTeamAsync(final UUID uuid, final TeamMemberRole role, final PBTeam team) {
        runAsync(new Runnable() {
            public void run() {
                joinTeam(uuid, role, team);
            }
        });
    }

    public void leaveTeam(final UUID uuid, final PBTeam team) {
        DatabaseHandler.getDatabase().executeQuery("DELETE FROM pb_team_members WHERE uuid=? AND teamId=?;", null, uuid.toString(), team.getID());
    }

    public void leaveTeamAsync(final UUID uuid, final PBTeam team) {
        runAsync(new Runnable() {
            public void run() {
                leaveTeam(uuid, team);
            }
        });
    }

    public void createTeam(final UUID creator, final String teamName, final TeamMemberRole creatorRole, final Callback<PBTeam> callback) {
        DatabaseHandler.getDatabase().executeQuery("INSERT INTO pb_teams (name, leader) VALUES (?, ?);", new Callback<ResultSet>() {
            public void run(ResultSet rs) {
                try {
                    if (rs.next()) {
                        final int id = rs.getInt(1);
                        joinTeam(creator, creatorRole, new PBTeam(id, "", creator, new HashMap<UUID, TeamMemberRole>()));
                        Map<UUID, TeamMemberRole> members = new HashMap<>();
                        members.put(creator, creatorRole);
                        callback.run(new PBTeam(id, teamName, creator, members));
                    } else {
                        callback.run(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, teamName, creator.toString());
    }

    public void createTeamAsync(final UUID creator, final String teamName, final TeamMemberRole creatorRole, final Callback<PBTeam> callback) {
        runAsync(new Runnable() {
            public void run() {
                createTeam(creator, teamName, creatorRole, new Callback<PBTeam>() {
                    public void run(PBTeam team) {
                        final PBTeam pbTeam = team;
                        runSync(new Runnable() {
                            public void run() {
                                callback.run(pbTeam);
                            }
                        });
                    }
                });
            }
        });
    }

    public void deleteTeam(PBTeam team) {
        DatabaseHandler.getDatabase().executeQuery("DELETE FROM pb_teams WHERE id=?;", null, team.getID());
        DatabaseHandler.getDatabase().executeQuery("DELETE FROM pb_team_members WHERE teamId=?;", null, team.getID());
    }

    public void deleteTeamAsync(final PBTeam team) {
        runAsync(new Runnable() {
            public void run() {
                deleteTeam(team);
            }
        });
    }
}
