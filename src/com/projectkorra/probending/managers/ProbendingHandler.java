package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.game.field.ProbendingField;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.storage.DBProbendingPlayer;
import com.projectkorra.probending.storage.FFProbendingField;

public class ProbendingHandler
{
    private final JavaPlugin plugin;

    private Map<UUID, PBPlayer> players;
    
    private List<ProbendingField> availableFields;
    private Set<Game> games;

    private Set<PBPlayer> queuedUpPlayers;
    private List<PBPlayer> playerMode1P;
    private List<PBPlayer> playerMode3P;

    private long informDelay = 2000;
    private long curTime;
    
    private FFProbendingField _fieldStorage;
    private DBProbendingPlayer _playerStorage;

    public ProbendingHandler(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.curTime = System.currentTimeMillis();
        this.players = new HashMap<>();
        this.availableFields = new ArrayList<>();
        this.games = new HashSet<>();
        this.queuedUpPlayers = new HashSet<>();
        this.playerMode1P = new ArrayList<>();
        this.playerMode3P = new ArrayList<>();
        _fieldStorage = new FFProbendingField(plugin);
        _playerStorage = new DBProbendingPlayer(plugin);
        plugin.getServer().getPluginManager().registerEvents(new PBHandlerListener(this), plugin);
        loadFields();
    }

    public boolean addField(ProbendingField field)
    {
        if (!availableFields.contains(field))
        {
        	_fieldStorage.insertField(field);
            availableFields.add(field);
        }
        return true;
    }

    public boolean removeField(ProbendingField field)
    {
        if (availableFields.contains(field))
        {
            availableFields.remove(field);
            _fieldStorage.deleteField(field);
        }
        return true;
    }

    private void loadFields()
    {
        availableFields = _fieldStorage.loadFields();
    }
    
    public void getPointsEarned(UUID uuid, final Callback<Double> callback)
    {
    	_playerStorage.loadPBPlayerAsync(uuid, new Callback<PBPlayer>()
    	{
    		public void run(PBPlayer player)
    		{
    			callback.run(player.getPointsEarned());
    		}
    	});
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

    public void removePlayerFromQueue(Player player) {
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

    private void informPlayers()
    {
        if (curTime + informDelay < System.currentTimeMillis())
        {
            curTime = System.currentTimeMillis();
            for (PBPlayer pbPlayer : queuedUpPlayers)
            {
                if (Bukkit.getPlayer(pbPlayer.getUUID()) != null)
                {
                    Player p = Bukkit.getPlayer(pbPlayer.getUUID());
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
                team1.add(Bukkit.getPlayer(playerMode1P.get(0).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode1P.get(1).getUUID()));
            } else if (PlayersQueued3v3 >= 6) {
                team1.add(Bukkit.getPlayer(playerMode3P.get(0).getUUID()));
                team1.add(Bukkit.getPlayer(playerMode3P.get(1).getUUID()));
                team1.add(Bukkit.getPlayer(playerMode3P.get(2).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(3).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(4).getUUID()));
                team2.add(Bukkit.getPlayer(playerMode3P.get(5).getUUID()));
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

    protected void playerLogin(final Player player)
    {
    	_playerStorage.loadPBPlayerAsync(player.getUniqueId(), new Callback<PBPlayer>()
    	{
    		public void run(PBPlayer pbPlayer)
    		{
    			players.put(player.getUniqueId(), pbPlayer);
    		}
    	});
    }

    protected void playerLogout(Player player)
    {
        if (players.containsKey(player))
        {
            players.remove(player);
        }
    }
}