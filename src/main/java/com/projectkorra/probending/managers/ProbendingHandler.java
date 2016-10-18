/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.managers;

import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.field.ProbendingField;
import com.projectkorra.probending.objects.PBPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Ivo
 */
public class ProbendingHandler {

    private final JavaPlugin plugin;

    private Map<UUID, PBPlayer> players;
    
    private List<ProbendingField> availableFields;
    private Set<Game> games;

    private Set<PBPlayer> queuedUpPlayers;
    private List<PBPlayer> playerMode1P;
    private List<PBPlayer> playerMode3P;

    private long informDelay = 2000;
    private long curTime;

    public ProbendingHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.curTime = System.currentTimeMillis();
        this.players = new HashMap<>();
        this.availableFields = new ArrayList<>();
        this.games = new HashSet<>();
        this.queuedUpPlayers = new HashSet<>();
        this.playerMode1P = new ArrayList<>();
        this.playerMode3P = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(new PBHandlerListener(this), plugin);
        loadFields();
    }

    public boolean addField(ProbendingField field) {
        if (!availableFields.contains(field)) {
            availableFields.add(field);
        }
        return true;
    }

    public boolean removeField(ProbendingField field) {
        if (availableFields.contains(field)) {
            availableFields.remove(field);
        }
        return true;
    }

    private void loadFields() {
        //DATABASE
    }

    public void quePlayer(Player player, GameMode gameMode) {
        if (!players.containsKey(player.getUniqueId()) || gameMode == null) {
            return;
        }
        PBPlayer pbPlayer = players.get(player.getUniqueId());
        if (pbPlayer == null) {
            player.sendMessage(ChatColor.DARK_RED + "Could not retrieve any information of you!");
            return;
        }
        if (!queuedUpPlayers.contains(pbPlayer)) {
            queuedUpPlayers.add(pbPlayer);
            if (gameMode.equals(GameMode.ANY)) {
                playerMode1P.add(pbPlayer);
                playerMode3P.add(pbPlayer);
            } else if (gameMode.equals(GameMode.SINGLE)) {
                playerMode1P.add(pbPlayer);
            } else if (gameMode.equals(GameMode.TRIPLE)) {
                playerMode3P.add(pbPlayer);
            }
            player.sendMessage(ChatColor.GREEN + "You queued up!");
        } else {
            player.sendMessage(ChatColor.RED + "You are already queued up!");
            return;
        }
        informPlayers();
        tryStartGame();
    }

    public void removePlayerFromQue(Player player) {
        if (!players.containsKey(player)) {
            return;
        }
        PBPlayer pbPlayer = players.get(player.getUniqueId());
        if (pbPlayer == null) {
            player.sendMessage(ChatColor.DARK_RED + "Could not retrieve any information of you!");
            return;
        }
        if (queuedUpPlayers.contains(pbPlayer)) {
            queuedUpPlayers.remove(pbPlayer);
            if (playerMode1P.contains(pbPlayer)) {
                playerMode1P.remove(pbPlayer);
            }
            if (playerMode3P.contains(pbPlayer)) {
                playerMode3P.remove(pbPlayer);
            }
            player.sendMessage(ChatColor.RED + "You left the queue");
        }
        informPlayers();
    }

    private void informPlayers() {
        if (curTime + informDelay < System.currentTimeMillis()) {
            curTime = System.currentTimeMillis();
            for (PBPlayer pbPlayer : queuedUpPlayers) {
                if (pbPlayer.getPlayer().isOnline()) {
                    Player p = (Player) pbPlayer.getPlayer();
                    p.sendMessage(ChatColor.GRAY + "===================");
                    p.sendMessage(ChatColor.YELLOW + "1v1: " + ChatColor.AQUA + playerMode1P.size() + ChatColor.GRAY + "/2");
                    p.sendMessage(ChatColor.YELLOW + "3v3: " + ChatColor.AQUA + playerMode3P.size() + ChatColor.GRAY + "/6");
                    p.sendMessage(ChatColor.GRAY + "===================");
                }
            }
        }
    }

    private void tryStartGame() {
        int playersQueued = queuedUpPlayers.size();
        if (playersQueued >= 2) {
            Game game;
            if (availableFields.isEmpty()) {
                return;
            }
            int PlayersQueued1v1 = playerMode1P.size();
            int PlayersQueued3v3 = playerMode3P.size();
            Set<Player> team1 = new HashSet<>();
            Set<Player> team2 = new HashSet<>();
            if (PlayersQueued3v3 < 6 && PlayersQueued1v1 > 1) {
                team1.add(playerMode1P.get(0).getPlayer().getPlayer());
                team2.add(playerMode1P.get(1).getPlayer().getPlayer());
            } else if (PlayersQueued3v3 >= 6) {
                team1.add(playerMode3P.get(0).getPlayer().getPlayer());
                team1.add(playerMode3P.get(1).getPlayer().getPlayer());
                team1.add(playerMode3P.get(2).getPlayer().getPlayer());
                team2.add(playerMode3P.get(3).getPlayer().getPlayer());
                team2.add(playerMode3P.get(4).getPlayer().getPlayer());
                team2.add(playerMode3P.get(5).getPlayer().getPlayer());
            }
            game = new Game(plugin, this, Game.GameType.DEFAULT, availableFields.get(0), team1, team2);
            games.add(game);
            availableFields.remove(0);
            for (Player p : team1) {
                PBPlayer pbPlayer = players.get(p.getUniqueId());
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
                PBPlayer pbPlayer = players.get(p.getUniqueId());
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

    public void gameEnded(Game game, Integer team1Score, Integer team2Score) {
        if (games.contains(game)) {
            availableFields.add(game.Field());
            games.remove(game);
        }
        tryStartGame();
    }

    protected void playerLogin(Player player) {
        players.put(player.getUniqueId(), new PBPlayer(player.getUniqueId(), null, 0.0));
    }

    protected void playerLogout(Player player) {
        if (players.containsKey(player)) {
            players.remove(player);
        }
    }
}
