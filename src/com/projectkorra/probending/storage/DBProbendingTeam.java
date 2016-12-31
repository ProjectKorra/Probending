package com.projectkorra.probending.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.projectkorra.probending.enums.TeamColor;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.managers.InviteManager.Invitation;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;

public class DBProbendingTeam extends DBInterpreter {
	
	private static final int INITIAL_RATING = 1000;
	
    public DBProbendingTeam(JavaPlugin plugin) {
        super(plugin);

        runAsync(new Runnable() {
            public void run() {
                if (!DatabaseHandler.getDatabase().tableExists("pb_teams")) {
                    String query = "CREATE TABLE pb_teams (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100) NOT NULL, leader VARCHAR(100) NOT NULL, coloring VARCHAR(100) NOT NULL, wins INT NOT NULL, games INT NOT NULL, rating INT NOT NULL, PRIMARY KEY (id), UNIQUE INDEX nameIndex (name), UNIQUE INDEX leaderIndex (leader), INDEX winIndex (wins), INDEX gameIndex (games), INDEX ratingIndex (rating));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_teams (id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, leader TEXT NOT NULL UNIQUE, coloring TEXT NOT NULL, wins INTEGER NOT NULL, games INTEGER NOT NULL, rating INTEGER NOT NULL);CREATE INDEX winIndex ON pb_team_members (wins);CREATE INDEX gameIndex ON pb_team_members (games);CREATE INDEX ratingIndex ON pb_team_members (rating);";
                    }

                    DatabaseHandler.getDatabase().executeUpdate(query);
                }
                if (!DatabaseHandler.getDatabase().tableExists("pb_team_members")) {
                    String query = "CREATE TABLE pb_team_members (id INT NOT NULL AUTO_INCREMENT, teamId INT NOT NULL, uuid VARCHAR(100) NOT NULL, role VARCHAR(50) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX uuidIndex (uuid), INDEX teamIndex (teamId), INDEX roleIndex (role));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_team_members (id INTEGER PRIMARY KEY, teamId INTEGER NOT NULL, uuid TEXT NOT NULL UNIQUE, role TEXT NOT NULL);CREATE INDEX teamIndex ON pb_team_members (teamId);CREATE INDEX roleIndex ON pb_team_members (role);";
                    }

                    DatabaseHandler.getDatabase().executeUpdate(query);
                }
                if (!DatabaseHandler.getDatabase().tableExists("pb_team_invites")) {
                	String query = "CREATE TABLE pb_team_invites (id INT NOT NULL AUTO_INCREMENT, teamId INT NOT NULL, uuid VARCHAR(100) NOT NULL, role VARCHAR(50) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX inviteIndex (teamId, uuid));";
                	if (!DatabaseHandler.isMySQL()) {
                		query = "CREATE TABLE pb_team_invites (id INTEGER PRIMARY KEY, teamId INTEGER NOT NULL, uuid TEXT NOT NULL, role TEXT NOT NULL);CREATE UNIQUE INDEX inviteIndex ON pb_team_invites (teamId, uuid);";
                	}
                	
                	DatabaseHandler.getDatabase().executeUpdate(query);
                }
            }
        });
    }

    public void loadPBTeams(final Callback<List<PBTeam>> callback) {
        DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_teams;", new Callback<ResultSet>() {
            public void run(ResultSet rs) {
                final List<PBTeam> teams = Lists.newArrayList();
                try {
                    while (rs.next()) {
                        final int id = rs.getInt("id");
                        final String name = rs.getString("name");
                        final UUID leader = UUID.fromString(rs.getString("leader"));
                        final int wins = rs.getInt("wins");
                        final int gamesPlayed = rs.getInt("games");
                        final int rating = rs.getInt("rating");
                        final String colorText = rs.getString("coloring");
                        final TeamColor[] colors = new TeamColor[4];
                        int colorIndex = 0;
                        for (String color : colorText.split(","))
                        {
                        	colors[colorIndex] = TeamColor.parseColor(color);
                        	colorIndex++;
                        }
                        DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_team_members WHERE teamId=?;", new Callback<ResultSet>() {
                        	public void run(ResultSet memberSet)
                        	{
                        		final Map<UUID, TeamMemberRole> members = new HashMap<>();
                                try {
									while (memberSet.next()) {
									    UUID uuid = UUID.fromString(memberSet.getString("uuid"));
									    TeamMemberRole role = TeamMemberRole.parseRole(memberSet.getString("role"));
									    members.put(uuid, role);
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
                                PBTeam team = new PBTeam(id, name, leader, members, wins, gamesPlayed, rating);
                                team.setColors(colors[0], colors[1], colors[2], colors[3]);
                                teams.add(team);
                        	}
                        }, id);
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

    public void updatePBTeam(final PBTeam team, final Runnable after) {
    	String coloring = team.getColors()[0].toString();
    	for (int i = 1; i < 4; i++)
    	{
    		coloring += ("," + team.getColors()[i].toString());
    	}
        DatabaseHandler.getDatabase().executeUpdate("UPDATE pb_teams SET name=?, leader=?, coloring=?, wins=?, games=?, rating=? WHERE id=?;", team.getTeamName(), team.getLeader().toString(), coloring, team.getWins(), team.getGamesPlayed(), team.getRating(), team.getID());
        if (after != null)
        	after.run();
    }

    public void updatePBTeamAsync(final PBTeam team, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                updatePBTeam(team, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }

    public void joinTeam(final UUID uuid, final TeamMemberRole role, final PBTeam team, final Runnable after) {
        DatabaseHandler.getDatabase().executeUpdate("INSERT INTO pb_team_members (uuid, teamId, role) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE teamId=VALUES(teamId), role=VALUES(role);", uuid.toString(), team.getID(), role.toString());
        if (after != null)
        	after.run();
    }

    public void joinTeamAsync(final UUID uuid, final TeamMemberRole role, final PBTeam team, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                joinTeam(uuid, role, team, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }

    public void leaveTeam(final UUID uuid, final PBTeam team, final Runnable after) {
        DatabaseHandler.getDatabase().executeUpdate("DELETE FROM pb_team_members WHERE uuid=? AND teamId=?;", uuid.toString(), team.getID());
        if (after != null)
        	after.run();
    }

    public void leaveTeamAsync(final UUID uuid, final PBTeam team, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                leaveTeam(uuid, team, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }

    public void createTeam(final UUID creator, final String teamName, final TeamMemberRole creatorRole, final TeamColor[] colors, final Callback<PBTeam> callback) {
    	String coloring = colors[0].toString() + "," + colors[1].toString() + "," + colors[2].toString() + "," + colors[3].toString();
    	DatabaseHandler.getDatabase().executeUpdate("INSERT INTO pb_teams (name, leader, coloring, wins, games, rating) VALUES (?, ?, ?, 0, 0, ?);", teamName, creator.toString(), coloring, INITIAL_RATING);
        DatabaseHandler.getDatabase().executeQuery("SELECT id FROM pb_teams WHERE name=? AND leader=?;", new Callback<ResultSet>() {
            public void run(ResultSet rs) {
                try {
                    if (rs.next()) {
                        final int id = rs.getInt(1);
                        joinTeam(creator, creatorRole, new PBTeam(id, "", creator, new HashMap<UUID, TeamMemberRole>(), 0, 0, 0), new Runnable() {
                        	public void run() {
                        		Map<UUID, TeamMemberRole> members = new HashMap<>();
                                members.put(creator, creatorRole);
                                PBTeam team = new PBTeam(id, teamName, creator, members, 0, 0, INITIAL_RATING);
                                team.setColors(colors[0], colors[1], colors[2], colors[3]);
                                callback.run(team);
                        	}
                        });
                    } else {
                        callback.run(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, teamName, creator.toString());
    }

    public void createTeamAsync(final UUID creator, final String teamName, final TeamMemberRole creatorRole, final TeamColor[] colors, final Callback<PBTeam> callback) {
        runAsync(new Runnable() {
            public void run() {
                createTeam(creator, teamName, creatorRole, colors, new Callback<PBTeam>() {
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

    public void deleteTeam(final PBTeam team, final Runnable after) {
        DatabaseHandler.getDatabase().executeUpdate("DELETE FROM pb_teams WHERE id=?;", team.getID());
        DatabaseHandler.getDatabase().executeUpdate("DELETE FROM pb_team_members WHERE teamId=?;", team.getID());
        DatabaseHandler.getDatabase().executeUpdate("DELETE FROM pb_team_invites WHERE teamId=?;", team.getID());
        if (after != null)
        	after.run();
    }

    public void deleteTeamAsync(final PBTeam team, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                deleteTeam(team, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }
    
    public void getTeamIdByMember(final UUID uuid, final Callback<Integer> callback) {
    	DatabaseHandler.getDatabase().executeQuery("SELECT teamId FROM pb_team_members WHERE uuid=?;", new Callback<ResultSet>() {
    		public void run(ResultSet rs) {
    			try {
					if (rs.next())
					{
						callback.run(rs.getInt("teamId"));
					}
					else
					{
						callback.run(-1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					callback.run(-1);
				}
    		}
    	}, uuid.toString());
    }
    
    public void getTeamIdByMemberAsync(final UUID uuid, final Callback<Integer> callback) {
        runAsync(new Runnable() {
            public void run() {
                getTeamIdByMember(uuid, new Callback<Integer>() {
                    public void run(Integer id) {
                        final Integer teamId = id;
                        runSync(new Runnable() {
                            public void run() {
                                callback.run(teamId);
                            }
                        });
                    }
                });
            }
        });
    }
    
    public void getInvitationsByUUID(final UUID uuid, final Callback<List<Invitation>> callback) {
    	DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_team_invites WHERE uuid=?;", new Callback<ResultSet>() {
    		public void run(ResultSet rs) {
    			List<Invitation> invites = Lists.newArrayList();
    			try {
					while (rs.next()) {
						invites.add(new Invitation(rs.getInt("teamId"), rs.getString("role")));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
    			
    			callback.run(invites);
    		}
    	}, uuid.toString());
    }
    
    public void getInvitationsByUUIDAsync(final UUID uuid, final Callback<List<Invitation>> callback) {
        runAsync(new Runnable() {
            public void run() {
                getInvitationsByUUID(uuid, new Callback<List<Invitation>>() {
                    public void run(List<Invitation> list) {
                        final List<Invitation> inviteList = list;
                        runSync(new Runnable() {
                            public void run() {
                                callback.run(inviteList);
                            }
                        });
                    }
                });
            }
        });
    }
    
    public void addInvitation(final UUID uuid, final PBTeam team, final String role, Runnable after) {
    	DatabaseHandler.getDatabase().executeUpdate("INSERT INTO pb_team_invites (uuid, teamId, role) VALUES (?, ?, ?);", uuid.toString(), team.getID(), role);
    	if (after != null) {
    		after.run();
    	}
    }
    
    public void addInvitationAsync(final UUID uuid, final PBTeam team, final String role, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                addInvitation(uuid, team, role, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }
    
    public void removeInvitation(final UUID uuid, final PBTeam team, Runnable after) {
    	DatabaseHandler.getDatabase().executeUpdate("DELETE * FROM pb_team_invites WHERE uuid=? AND teamId=?;", uuid.toString(), team.getID());
    	if (after != null)
    		after.run();
    }
    
    public void removeInvitationAsync(final UUID uuid, final PBTeam team, final Runnable after) {
        runAsync(new Runnable() {
            public void run() {
                removeInvitation(uuid, team, after != null ? new Runnable() {
                	public void run() {
                		runSync(after);
                	}
                } : null);
            }
        });
    }
}