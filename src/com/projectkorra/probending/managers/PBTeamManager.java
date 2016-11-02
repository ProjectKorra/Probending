package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.storage.DBProbendingTeam;

public class PBTeamManager {
	
	private DBProbendingTeam teamStorage;
	private Map<Player, PBTeam> teamsByOnlinePlayer = new HashMap<>();
	private Map<OfflinePlayer, PBTeam> teamsByOfflinePlayer = new HashMap<>();
	private Map<Integer, PBTeam> teamsByID = new HashMap<>();
	private Map<String, PBTeam> teamsByName = new HashMap<>();

	public PBTeamManager(DBProbendingTeam teamStorage) {
		this.teamStorage = teamStorage;
		populateTeamMap();
	}
	
	public void populateTeamMap() {
		Map<Player, PBTeam> playerMap = new HashMap<>();
		Map<OfflinePlayer, PBTeam> oPlayerMap = new HashMap<>();
		Map<Integer, PBTeam> idMap = new HashMap<>();
		Map<String, PBTeam> nameMap = new HashMap<>();
		//Replace the ArrayList with loading teams
		for (PBTeam team : new ArrayList<PBTeam>()) {
			for (UUID uuid : team.getMembers().keySet()) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				if (player.isOnline()) {
					Player p = (Player) player;
					playerMap.put(p, team);
				} else {
					oPlayerMap.put(player, team);
				}
			}
			idMap.put(team.getID(), team);
			nameMap.put(team.getTeamName(), team);
		}
	}
	
	public void updatePlayerMapsForLogin(Player player) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
		if (teamsByOfflinePlayer.containsKey(oPlayer)) {
			teamsByOnlinePlayer.put(player, teamsByOfflinePlayer.get(oPlayer));
			teamsByOfflinePlayer.remove(oPlayer);
		}
	}
	
	public void updatePlayerMapsForLogout(Player player) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
		if (teamsByOnlinePlayer.containsKey(player)) {
			teamsByOfflinePlayer.put(oPlayer, teamsByOnlinePlayer.get(player));
			teamsByOnlinePlayer.remove(player);
		}
	}
	
	public void updateMapsForNewTeam(PBTeam team) {
		teamsByID.put(team.getID(), team);
		teamsByName.put(team.getTeamName(), team);
		teamsByOnlinePlayer.put(Bukkit.getPlayer(team.getLeader()), team);
	}
	
	public void updatePlayerMapForNewMember(PBTeam team, Player player) {
		if (team.getMembers().containsKey(player.getUniqueId())) {
			teamsByOnlinePlayer.put(player, team);
		}
	}
	
	public PBTeam getTeamFromPlayer(Player player) {
		return teamsByOnlinePlayer.containsKey(player) ? teamsByOnlinePlayer.get(player) : null;
	}
	
	public PBTeam getTeamFromOfflinePlayer(OfflinePlayer oPlayer) {
		return teamsByOfflinePlayer.containsKey(oPlayer) ? teamsByOfflinePlayer.get(oPlayer) : null;
	}
	
	public PBTeam getTeamFromID(int id) {
		return teamsByID.containsKey(id) ? teamsByID.get(id) : null;
	}
	
	public PBTeam getTeamFromName(String name) {
		return teamsByName.containsKey(name) ? teamsByName.get(name) : null;
	}
}
