package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.scoreboard.PBScoreboard;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.objects.ProbendingField;
import com.projectkorra.probending.storage.DBProbendingPlayer;
import com.projectkorra.probending.storage.FFProbendingField;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProbendingHandler implements Listener {

    private final JavaPlugin plugin;

    protected Map<UUID, PBPlayer> players;

    protected List<ProbendingField> availableFields;
    protected Set<Game> games;

    private FFProbendingField _fieldStorage;
    private DBProbendingPlayer _playerStorage;

    public ProbendingHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
        this.availableFields = new ArrayList<>();
        this.games = new HashSet<>();
        _fieldStorage = new FFProbendingField(plugin);
        _playerStorage = new DBProbendingPlayer(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        loadFields();
    }

    public boolean addField(ProbendingField field) {
        if (!availableFields.contains(field)) {
            _fieldStorage.insertField(field);
            availableFields.add(field);
        }
        return true;
    }

    public void removeField(ProbendingField field) {
        if (availableFields.contains(field)) {
            availableFields.remove(field);
            _fieldStorage.deleteField(field);
        }
    }

    public ProbendingField getField(Player player, String fieldNumber) {
        for (ProbendingField f : availableFields) {
            if (f.getFieldName().equals(fieldNumber)) {
                return f;
            }
        }
        for (Game game : games) {
            if (game.getField().getFieldName().equals(fieldNumber)) {
                PBMessenger.sendMessage(player, ChatColor.RED + "Field is in use, and cannot be edited!", true);
                return null;
            }
        }
        PBMessenger.sendMessage(player, ChatColor.RED + "Field could not be found!", true);
        return null;
    }

    public Map<ProbendingField, Boolean> getAllFields() {
        Map<ProbendingField, Boolean> fields = new HashMap<>();
        for (ProbendingField f : availableFields) {
            fields.put(f, true);
        }
        for (Game game : games) {
            fields.put(game.getField(), false);
        }
        return fields;
    }

    private void loadFields() {
        availableFields = _fieldStorage.loadFields();
    }

    public void getOfflinePBPlayer(UUID uuid, final Callback<PBPlayer> callback) {
        _playerStorage.loadPBPlayerAsync(uuid, new Callback<PBPlayer>() {
            public void run(PBPlayer player) {
                callback.run(player);
            }
        });
    }

    public void updatePBPlayer(PBPlayer player) {
        _playerStorage.updatePBPlayerAsync(player);
    }

    public void getPlayerInfo(Player player, Player infoPlayer) {
        if (infoPlayer == null) {
            PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPLAYER);
            return;
        }
        if (players.containsKey(infoPlayer.getUniqueId())) {
            PBScoreboard.showInformation(player, players.get(infoPlayer.getUniqueId()), plugin);
        } else {
            PBMessenger.sendMessage(player, PBMessenger.PBMessage.ERROR);
        }
    }

    public PBPlayer getPBPlayer(UUID uuid) {
        return players.containsKey(uuid) ? players.get(uuid) : null;
    }
    
    @EventHandler
    private void playerLogin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        _playerStorage.loadPBPlayerAsync(player.getUniqueId(), new Callback<PBPlayer>() {
            public void run(PBPlayer pbPlayer) {
                players.put(player.getUniqueId(), pbPlayer);
            }
        });
        Probending.get().getTeamManager().updatePlayerMapsForLogin(player);
        Probending.get().getInviteManager().handleJoin(player);
    }

    @EventHandler
    private void playerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!players.containsKey(player.getUniqueId())) {
            return;
        }
        players.remove(player.getUniqueId());
        Probending.get().getTeamManager().updatePlayerMapsForLogout(player);
        Probending.get().getInviteManager().handleQuit(player);
    }
}
