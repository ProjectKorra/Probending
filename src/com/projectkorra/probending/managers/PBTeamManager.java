package com.projectkorra.probending.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;
import com.projectkorra.probending.storage.DBProbendingTeam;
import com.projectkorra.projectkorra.BendingPlayer;

public class PBTeamManager {
	
	private DBProbendingTeam teamStorage;
	private Map<Player, PBTeam> teamsByOnlinePlayer = new HashMap<>();
	private Map<Integer, PBTeam> teamsByID = new HashMap<>();
	private Map<String, PBTeam> teamsByName = new HashMap<>();

	public PBTeamManager(DBProbendingTeam teamStorage) {
		this.teamStorage = teamStorage;
		populateTeamMap();
	}
	
	public void updateTeam(PBTeam team) {
		this.teamStorage.updatePBTeamAsync(team);
	}
	
	public void populateTeamMap() {
		teamStorage.loadPBTeamsAsync(new Callback<List<PBTeam>>() {
			public void run(List<PBTeam> teams) {
				for (PBTeam team : teams) {
					teamsByID.put(team.getID(), team);
					teamsByName.put(team.getTeamName(), team);
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (team.getMembers().containsKey(player.getUniqueId())) {
							teamsByOnlinePlayer.put(player, team);
						}
					}
				}
			}
		});
	}
	
	public void updatePlayerMapsForLogin(final Player player) {
		getTeamFromUUIDAsync(player.getUniqueId(), new Callback<PBTeam>() {
			public void run(PBTeam team) {
				if (team != null) {
					teamsByOnlinePlayer.put(player, team);
				}
			}
		});
	}
	
	public void updatePlayerMapsForLogout(Player player) {
		teamsByOnlinePlayer.remove(player);
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
	
	public void getTeamFromUUIDAsync(final UUID uuid, final Callback<PBTeam> callback) {
		this.teamStorage.getTeamIdByMemberAsync(uuid, new Callback<Integer>() {
			public void run(Integer teamId) {
				if (teamId == -1) {
					callback.run(null);
				} else {
					callback.run(teamsByID.get(teamId));
				}
			}
		});
	}
	
	public PBTeam getTeamFromID(int id) {
		return teamsByID.containsKey(id) ? teamsByID.get(id) : null;
	}
	
	public PBTeam getTeamFromName(String name) {
		return teamsByName.containsKey(name) ? teamsByName.get(name) : null;
	}
	
	public boolean createNewTeam(Player leader, String name, int id, TeamMemberRole leaderRole/*, Integer[] color*/) {
		if (teamsByID.containsKey(id)) {
			return false;
		} else if (teamsByOnlinePlayer.containsKey(leader)){
			return false;
		} else if (teamsByName.containsKey(name)) {
			return false;
		} else {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(leader);
			if (bPlayer.getElements().isEmpty()) {
				return false;
			}
			
			if (!leaderRole.isEnabled()) {
				return false;
			}
			
			Map<UUID, TeamMemberRole> map = new HashMap<>();
			map.put(leader.getUniqueId(), leaderRole);
			PBTeam team = new PBTeam(id, name, leader.getUniqueId(), map, 0, 0, 1000/*, color*/);
			teamsByName.put(team.getTeamName(), team);
			teamsByOnlinePlayer.put(leader, team);
			teamsByID.put(id, team);
			//TODO: Team stuff with database saving 'n such
		}
		return true;
	}
}
