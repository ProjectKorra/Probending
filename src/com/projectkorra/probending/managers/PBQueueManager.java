/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.enums.GamePlayerMode;
import com.projectkorra.probending.enums.GameType;
import com.projectkorra.probending.enums.WinningType;
import com.projectkorra.probending.events.PlayerJoinQueueEvent;
import com.projectkorra.probending.events.PlayerLeaveQueueEvent;
import com.projectkorra.probending.events.TeamJoinQueueEvent;
import com.projectkorra.probending.events.TeamLeaveQueueEvent;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.TeamGame;
import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.objects.PBTeam;

/**
 *
 * @author Ivo
 */
public class PBQueueManager implements Listener {

    private final ProbendingHandler pHandler;
    private final JavaPlugin plugin;

    private Set<PBPlayer> queuedUpPlayers;
    private List<PBPlayer> playerMode1P;
    private List<PBPlayer> playerMode3P;
    private Map<PBTeam, List<PBPlayer>> playerModeTeam;

    private long informDelay = 2000;
    private long curTime;
    private long curTeamTime;

    public PBQueueManager(JavaPlugin plugin, ProbendingHandler pHandler) {
        this.plugin = plugin;
        this.pHandler = pHandler;
        this.curTime = System.currentTimeMillis();
        this.curTeamTime = System.currentTimeMillis();
        this.queuedUpPlayers = new HashSet<>();
        this.playerMode1P = new ArrayList<>();
        this.playerMode3P = new ArrayList<>();
        this.playerModeTeam = new HashMap<>();
    }

    public void quePlayer(Player player, GamePlayerMode gameMode) {
        if (!pHandler.players.containsKey(player.getUniqueId()) || gameMode == null) {
            return;
        }
        PBPlayer pbPlayer = pHandler.players.get(player.getUniqueId());
        if (pbPlayer == null) {
            player.sendMessage(ChatColor.DARK_RED + "Could not retrieve any information of you!");
            return;
        }
        if (!queuedUpPlayers.contains(pbPlayer)) {
            PlayerJoinQueueEvent event = new PlayerJoinQueueEvent(player, gameMode);
            Probending.get().getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                queuedUpPlayers.add(pbPlayer);
                if (gameMode.equals(GamePlayerMode.ANY)) {
                    playerMode1P.add(pbPlayer);
                    playerMode3P.add(pbPlayer);
                } else if (gameMode.equals(GamePlayerMode.SINGLE)) {
                    playerMode1P.add(pbPlayer);
                } else if (gameMode.equals(GamePlayerMode.TRIPLE)) {
                    playerMode3P.add(pbPlayer);
                }
                player.sendMessage(ChatColor.GREEN + "You queued up!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are already queued up!");
            return;
        }
        informPlayers(true);
        tryStartGame();
    }

    public void removePlayerFromQueue(Player player) {
        if (!pHandler.players.containsKey(player.getUniqueId())) {
            return;
        }
        PBPlayer pbPlayer = pHandler.players.get(player.getUniqueId());
        if (pbPlayer == null) {
            player.sendMessage(ChatColor.DARK_RED + "Could not retrieve any information of you!");
            return;
        }
        if (queuedUpPlayers.contains(pbPlayer)) {
            PlayerLeaveQueueEvent event = new PlayerLeaveQueueEvent(player);
            Probending.get().getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                queuedUpPlayers.remove(pbPlayer);
                if (playerMode1P.contains(pbPlayer)) {
                    playerMode1P.remove(pbPlayer);
                }
                if (playerMode3P.contains(pbPlayer)) {
                    playerMode3P.remove(pbPlayer);
                }
                player.sendMessage(ChatColor.RED + "You left the queue");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not queued up!");
            return;
        }
        informPlayers(true);
    }
    
    public void queTeam(PBTeam team, Player leader, Player one, Player two) {
    	PBPlayer leaderPB = pHandler.getPBPlayer(leader.getUniqueId());
    	PBPlayer onePB = pHandler.getPBPlayer(one.getUniqueId());
    	PBPlayer twoPB = pHandler.getPBPlayer(two.getUniqueId());
    	List<PBPlayer> participants = Arrays.asList(leaderPB, onePB, twoPB);
    	
    	if (!playerModeTeam.containsKey(team)) {
    		TeamJoinQueueEvent event = new TeamJoinQueueEvent(team, participants);
    		Probending.get().getServer().getPluginManager().callEvent(event);
    		if (!event.isCancelled()) {
    			playerModeTeam.put(team, participants);
    			leader.sendMessage(ChatColor.GREEN + "Entering team queue");
    			one.sendMessage(ChatColor.GREEN + "Entering team queue");
    			two.sendMessage(ChatColor.GREEN + "Entering team queue");
    		}
    	} else {
    		leader.sendMessage(ChatColor.RED + "Your team is already queued");
    	}
    	informTeams(true);
    	tryStartTeamGame();
    }
    
    public void removeTeamFromQueue(PBTeam team) {
    	if (playerModeTeam.containsKey(team)) {
    		TeamLeaveQueueEvent event = new TeamLeaveQueueEvent(team);
    		Probending.get().getServer().getPluginManager().callEvent(event);
    		if (!event.isCancelled()) {
    			playerModeTeam.remove(team);
    		}
    	}
    	informTeams(true);
    }

    public void gameEnded(Game game, WinningType winners) {
        if (pHandler.games.contains(game)) {
            pHandler.availableFields.add(game.getField());
            pHandler.games.remove(game);
        }
        if (game.getGamePlayerMode() == GamePlayerMode.TEAM) {
            TeamGame tGame = (TeamGame) game;
            PBTeam team1 = tGame.getTeam1();
            PBTeam team2 = tGame.getTeam2();
            PBTeam winningTeam = null;
            if (winners.equals(WinningType.TEAM1)) {
                team1.updateAfterGame(tGame, true);
                team2.updateAfterGame(tGame, false);
                winningTeam = team1;
            } else if (winners.equals(WinningType.TEAM2)) {
                team1.updateAfterGame(tGame, false);
                team2.updateAfterGame(tGame, true);
                winningTeam = team2;
            } else {
                team1.updateAfterGame(tGame, false);
                team2.updateAfterGame(tGame, false);
            }

            Set<Player> players = new HashSet<>();
            players.addAll(tGame.getTeam1Players());
            players.addAll(tGame.getTeam2Players());

            for (Player p : players) {
                if (p.isOnline()) {
                    if (this.pHandler.players.containsKey(p.getUniqueId())) {
                        PBPlayer pbPlayer = this.pHandler.players.get(p.getUniqueId());
                        p.teleport(p.getWorld().getSpawnLocation());
                        if (winningTeam == null) {
                            PBMessenger.sendMessage(p, "It's a draw!", true);
                        } else {
                            PBMessenger.sendMessage(p, ChatColor.GOLD + "Winning Team: " + winningTeam.getTeamName(), true);
                        }
                        pbPlayer.updateTeamStats(tGame, winningTeam != null ? winningTeam.getMembers().containsKey(pbPlayer.getUUID()) : false);
                    }
                }
            }
        } else {
            Set<Player> winningPlayers = null;
            if (winners.equals(WinningType.TEAM1)) {
                //TODO: get the correct team... Calculation now is team 1 = team 2 and team 2 = team 1
                winningPlayers = game.getTeam1Players();
            } else if (winners.equals(WinningType.TEAM2)) {
                winningPlayers = game.getTeam2Players();
            } else {
                winningPlayers = new HashSet<>();
            }
            //TODO: Create spawn....
            for (Player p : game.getTeam1Players()) {
                if (p.isOnline()) {
                    if (pHandler.players.containsKey(p.getUniqueId())) {
                        pHandler.players.get(p.getUniqueId()).updateIndividualStats(p, game, winningPlayers);
                    } else {
                        continue;
                    }
                    p.teleport(p.getWorld().getSpawnLocation());
                    if (winningPlayers.isEmpty()) {
                        PBMessenger.sendMessage(p, "It's a draw!", true);
                    } else {
                        PBMessenger.sendMessage(p, ChatColor.GOLD + "Winners:", true);
                        for (Player pl : winningPlayers) {
                            PBMessenger.sendMessage(p, ChatColor.GREEN + pl.getName(), true);
                        }
                    }
                }
            }
            for (Player p : game.getTeam2Players()) {
                if (p.isOnline()) {
                    if (pHandler.players.containsKey(p.getUniqueId())) {
                        pHandler.players.get(p.getUniqueId()).updateIndividualStats(p, game, winningPlayers);
                    } else {
                        continue;
                    }
                    p.teleport(p.getWorld().getSpawnLocation());
                    if (winningPlayers.isEmpty()) {
                        PBMessenger.sendMessage(p, "It's a draw!", true);
                    } else {
                        PBMessenger.sendMessage(p, ChatColor.GOLD + "Winners:", true);
                        for (Player pl : winningPlayers) {
                            PBMessenger.sendMessage(p, ChatColor.GREEN + pl.getName(), true);
                        }
                    }
                }
            }
        }
        tryStartGame();
    }

    private void informPlayers(boolean override) {
        if (curTime + informDelay < System.currentTimeMillis() || override) {
            curTime = System.currentTimeMillis();
            for (PBPlayer pbPlayer : queuedUpPlayers) {
                if (Bukkit.getPlayer(pbPlayer.getUUID()) != null) {
                    Player p = Bukkit.getPlayer(pbPlayer.getUUID());
                    p.sendMessage(ChatColor.GRAY + "===================");
                    p.sendMessage(ChatColor.YELLOW + "1v1: " + ChatColor.AQUA + playerMode1P.size() + ChatColor.GRAY + "/2");
                    p.sendMessage(ChatColor.YELLOW + "3v3: " + ChatColor.AQUA + playerMode3P.size() + ChatColor.GRAY + "/6");
                    p.sendMessage(ChatColor.GRAY + "===================");
                }
            }
        }
    }
    
    private void informTeams(boolean override) {
    	if (curTeamTime + informDelay < System.currentTimeMillis() || override) {
    		curTeamTime = System.currentTimeMillis();
    		for (List<PBPlayer> players : playerModeTeam.values()) {
    			for (PBPlayer pbPlayer : players) {
    				if (Bukkit.getPlayer(pbPlayer.getUUID()) != null) {
    					Player p = Bukkit.getPlayer(pbPlayer.getUUID());
                        p.sendMessage(ChatColor.GRAY + "===================");
                        p.sendMessage(ChatColor.YELLOW + "Teams: " + ChatColor.AQUA + playerModeTeam.size() + ChatColor.GRAY + "/2");
                        p.sendMessage(ChatColor.GRAY + "===================");
    				}
    			}
    		}
    	}
    }

    private void tryStartGame() {
        int playersQueued = queuedUpPlayers.size();
        if (playersQueued >= 2) {
            Game game;
            if (pHandler.availableFields.isEmpty()) {
                return;
            }
            int PlayersQueued1v1 = playerMode1P.size();
            int PlayersQueued3v3 = playerMode3P.size();
            Set<Player> team1 = new HashSet<>();
            Set<Player> team2 = new HashSet<>();
            GamePlayerMode mode = GamePlayerMode.ANY;
            if (PlayersQueued3v3 < 6 && PlayersQueued1v1 > 1) {
                team1.add(Bukkit.getPlayer(playerMode1P.get(0).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode1P.get(1).getUUID()));
                mode = GamePlayerMode.SINGLE;
            } else if (PlayersQueued3v3 >= 6) {
                team1.add(Bukkit.getPlayer(playerMode3P.get(0).getUUID()));
                team1.add(Bukkit.getPlayer(playerMode3P.get(1).getUUID()));
                team1.add(Bukkit.getPlayer(playerMode3P.get(2).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(3).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(4).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(5).getUUID()));
                mode = GamePlayerMode.TRIPLE;
            }
            game = new Game(plugin, this, GameType.DEFAULT, mode, pHandler.availableFields.get(0), team1, team2);
            pHandler.games.add(game);
            pHandler.availableFields.remove(0);
            for (Player p : team1) {
                PBPlayer pbPlayer = pHandler.players.get(p.getUniqueId());
                if (playerMode1P.contains(pbPlayer)) {
                    playerMode1P.remove(pbPlayer);
                }
                if (playerMode3P.contains(pbPlayer)) {
                    playerMode3P.remove(pbPlayer);
                }
                if (queuedUpPlayers.contains(pbPlayer)) {
                    queuedUpPlayers.remove(pbPlayer);
                }
            }
            for (Player p : team2) {
                PBPlayer pbPlayer = pHandler.players.get(p.getUniqueId());
                if (playerMode1P.contains(pbPlayer)) {
                    playerMode1P.remove(pbPlayer);
                }
                if (playerMode3P.contains(pbPlayer)) {
                    playerMode3P.remove(pbPlayer);
                }
                if (queuedUpPlayers.contains(pbPlayer)) {
                    queuedUpPlayers.remove(pbPlayer);
                }
            }
            game.startNewRound();
        }
    }
    
    public void tryStartTeamGame() {
    	if (playerModeTeam.size() >= 2) {
    		Game game;
    		Set<Player> team1 = new HashSet<>();
            Set<Player> team2 = new HashSet<>();
            PBTeam teamA = null;
            PBTeam teamB = null;
    		if (pHandler.availableFields.isEmpty()) {
                return;
            }
    		if (playerModeTeam.size() >= 2) {
    			for (PBTeam team : playerModeTeam.keySet()) {
    				if (team1.isEmpty()) {
    					for (PBPlayer pbPlayer : playerModeTeam.get(team)) {
    						team1.add(Bukkit.getPlayer(pbPlayer.getUUID()));
    					}
    					teamA = team;
    				} else if (team2.isEmpty()) {
    					for (PBPlayer pbPlayer : playerModeTeam.get(team)) {
    						team2.add(Bukkit.getPlayer(pbPlayer.getUUID()));
    					}
    					teamB = team;
    				} else {
    					break;
    				}
    			}
    		}
    		if (teamA == null) {
    			return;
    		}
    		
    		if (teamB == null) {
    			return;
    		}
    		game = new TeamGame(plugin, this, GameType.DEFAULT, GamePlayerMode.TEAM, pHandler.availableFields.get(0), team1, team2, teamA, teamB);
            pHandler.games.add(game);
            pHandler.availableFields.remove(0);
            playerModeTeam.remove(teamA);
            playerModeTeam.remove(teamB);
            game.startNewRound();
    	}
    }
    
    @EventHandler
    private void playerLogout(PlayerQuitEvent event) {
        removePlayerFromQueue(event.getPlayer());
    }
}
