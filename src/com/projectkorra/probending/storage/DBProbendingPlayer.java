package com.projectkorra.probending.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBPlayer;

public class DBProbendingPlayer extends DBInterpreter {
	
	private static final int INITIAL_RATING = 1000;
	private static final int SOLO_WIN_ID = 1;
	private static final int GROUP_WIN_ID = 2;
	private static final int TOTAL_GAMES_ID = 3;
	private static final int TEAM_WIN_ID = 4;
	private static final int TEAM_TOTAL_GAMES_ID = 5;
	private static final int RATING_ID = 6;
	
    public DBProbendingPlayer(JavaPlugin plugin) {
        super(plugin);

        runAsync(new Runnable() {
            public void run() {
                if (!DatabaseHandler.getDatabase().tableExists("pb_players")) {
                    String query = "CREATE TABLE pb_players (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(100) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX uuidIndex (uuid));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_players (id INTEGER PRIMARY KEY, uuid TEXT NOT NULL UNIQUE);";
                    }

                    DatabaseHandler.getDatabase().executeUpdate(query);
                }
                if (!DatabaseHandler.getDatabase().tableExists("pb_player_stats")) {
                    String query = "CREATE TABLE pb_player_stats (id INT NOT NULL AUTO_INCREMENT, playerId INT NOT NULL, stat INT NOT NULL, value INT NOT NULL, PRIMARY KEY (id), UNIQUE INDEX playerIndex (playerId, stat), INDEX valueIndex (value));";
                    if (!DatabaseHandler.isMySQL()) {
                        query = "CREATE TABLE pb_player_stats (id INTEGER PRIMARY KEY, playerId INTEGER NOT NULL, stat INTEGER NOT NULL, value INTEGER NOT NULL);CREATE UNIQUE INDEX playerIndex ON pb_player_stats (playerId, stat);CREATE INDEX valueIndex ON pb_player_stats (value);";
                    }

                    DatabaseHandler.getDatabase().executeUpdate(query);
                }
            }
        });
    }

    public void loadPBPlayer(final UUID uuid, final Callback<PBPlayer> callback) {
        DatabaseHandler.getDatabase().executeQuery("SELECT * FROM pb_players WHERE uuid=?;", new Callback<ResultSet>() {
            public void run(ResultSet rs) {
                try {
                    if (rs.next()) {
                        final int id = rs.getInt("id");
                        
                        DatabaseHandler.getDatabase().executeQuery("SELECT stat, value FROM pb_player_stats WHERE playerId=?;", new Callback<ResultSet>() {
							public void run(ResultSet rs) {
								int soloWin = 0;
								int groupWin = 0;
								int gameTotal = 0;
								int teamWin = 0;
								int teamTotal = 0;
								int rating = INITIAL_RATING;
								try {
									while (rs.next()) {
										int stat = rs.getInt("stat");
										int value = rs.getInt("value");
										if (stat == SOLO_WIN_ID) {
											soloWin = value;
										} else if (stat == GROUP_WIN_ID) {
											groupWin = value;
										} else if (stat == TOTAL_GAMES_ID) {
											gameTotal = value;
										} else if (stat == TEAM_WIN_ID) {
											teamWin = value;
										} else if (stat == TEAM_TOTAL_GAMES_ID) {
											teamTotal = value;
										} else if (stat == RATING_ID) {
											rating = value;
										}
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
								
								callback.run(new PBPlayer(id, uuid, soloWin, groupWin, gameTotal, teamWin, teamTotal, rating));
							}
						}, id);
                    } else {
                    	DatabaseHandler.getDatabase().executeUpdate("INSERT INTO pb_players (uuid) VALUES(?);", uuid.toString(), 0D);
                        DatabaseHandler.getDatabase().executeQuery("SELECT id FROM pb_players WHERE uuid=?;", new Callback<ResultSet>() {
                        	public void run(ResultSet rs2) {
                        		try {
									rs2.next();
									final int id = rs2.getInt(1);
									for (int stat = SOLO_WIN_ID; stat <= RATING_ID; stat++) {
										DatabaseHandler.getDatabase().executeUpdate("INSERT INTO pb_player_stats (playerId, stat, value) VALUES (?, ?, ?);", id, stat, stat == RATING_ID ? INITIAL_RATING : 0);
									}
									DatabaseHandler.getDatabase().executeQuery("SELECT stat, value FROM pb_player_stats WHERE playerId=?;", new Callback<ResultSet>() {
										public void run(ResultSet rs) {
											int soloWin = 0;
											int groupWin = 0;
											int gameTotal = 0;
											int teamWin = 0;
											int teamTotal = 0;
											int rating = INITIAL_RATING;
											try {
												while (rs.next()) {
													int stat = rs.getInt("stat");
													int value = rs.getInt("value");
													if (stat == SOLO_WIN_ID) {
														soloWin = value;
													} else if (stat == GROUP_WIN_ID) {
														groupWin = value;
													} else if (stat == TOTAL_GAMES_ID) {
														gameTotal = value;
													} else if (stat == TEAM_WIN_ID) {
														teamWin = value;
													} else if (stat == TEAM_TOTAL_GAMES_ID) {
														teamTotal = value;
													} else if (stat == RATING_ID) {
														rating = value;
													}
												}
											} catch (SQLException e) {
												e.printStackTrace();
											}
											
											callback.run(new PBPlayer(id, uuid, soloWin, groupWin, gameTotal, teamWin, teamTotal, rating));
										}
									}, id);
								} catch (SQLException e) {
									e.printStackTrace();
								}
                        	}
                        }, uuid.toString());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, uuid.toString());
    }

    public void loadPBPlayerAsync(final UUID uuid, final Callback<PBPlayer> callback) {
        runAsync(new Runnable() {
            public void run() {
                loadPBPlayer(uuid, new Callback<PBPlayer>() {
                    public void run(PBPlayer player) {
                        final PBPlayer pbPlayer = player;
                        runSync(new Runnable() {
                            public void run() {
                                callback.run(pbPlayer);
                            }
                        });
                    }
                });
            }
        });
    }

    public void updatePBPlayer(final PBPlayer player) {
    	final int[] values = new int[] {player.getID(), player.getIndividualWins(true), player.getIndividualWins(false), player.getGamesPlayed(), player.getTeamWins(), player.getTeamGamesPlayed(), player.getRating()};
        for (int i = SOLO_WIN_ID; i <= RATING_ID && i < values.length; i++) {
        	String query = "INSERT INTO pb_player_stats (playerId, stat, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=VALUES(value);";
        	if (!DatabaseHandler.isMySQL()) {
        		query = "INSERT OR REPLACE INTO pb_player_stats (playerId, stat, value) VALUES (?, ?, ?);";
        	}
        	DatabaseHandler.getDatabase().executeUpdate(query, values[0], i, values[i]);
        }
    }

    public void updatePBPlayerAsync(final PBPlayer player) {
        runAsync(new Runnable() {
            public void run() {
                updatePBPlayer(player);
            }
        });
    }
}
