package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.projectkorra.probending.enums.TeamColor;
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
	//private Map<TeamColor[], PBTeam> teamsByColor = new HashMap<>();

	public PBTeamManager(DBProbendingTeam teamStorage) {
		this.teamStorage = teamStorage;
		populateTeamMap();
	}
	
	public void updateTeam(PBTeam team, Runnable after) {
		this.teamStorage.updatePBTeamAsync(team, after);
	}
	
	public void handleTeamRename(PBTeam team, String newName) {
		teamsByName.remove(team.getTeamName().toUpperCase());
		teamsByName.put(newName.toUpperCase(), team);
	}
	
	public void populateTeamMap() {
		teamStorage.loadPBTeamsAsync(new Callback<List<PBTeam>>() {
			public void run(List<PBTeam> teams) {
				for (PBTeam team : teams) {
					teamsByID.put(team.getID(), team);
					teamsByName.put(team.getTeamName().toUpperCase(), team);
					//teamsByColor.put(team.getColors(), team);
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
		return teamsByName.containsKey(name.toUpperCase()) ? teamsByName.get(name.toUpperCase()) : null;
	}
	
	/*public PBTeam getTeamFromColors(TeamColor[] colors) {
		return teamsByColor.containsKey(colors) ? teamsByColor.get(colors) : null;
	}*/
	
	public void createNewTeam(Player leader, String name, TeamMemberRole leaderRole, TeamColor[] colors, final Callback<Boolean> successCallback) {
		if (teamsByOnlinePlayer.containsKey(leader)) {
			successCallback.run(false);
			return;
		} else if (teamsByName.containsKey(name.toUpperCase())) {
			successCallback.run(false);
			return;
		} else {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(leader);
			if (!bPlayer.hasElement(leaderRole.getElement())) {
				successCallback.run(false);
				return;
			}
			
			if (!leaderRole.isEnabled()) {
				successCallback.run(false);
				return;
			}
			
			final UUID leaderUUID = leader.getUniqueId();
			this.teamStorage.createTeamAsync(leaderUUID, name, leaderRole, colors, new Callback<PBTeam>() {
				public void run(PBTeam team) {
					teamsByID.put(team.getID(), team);
					teamsByName.put(team.getTeamName().toUpperCase(), team);
					if (Bukkit.getPlayer(leaderUUID) != null)
					{
						teamsByOnlinePlayer.put(Bukkit.getPlayer(leaderUUID), team);
					}
					
					successCallback.run(true);
				}
			});
		}
	}
	
	public boolean disbandTeam(final PBTeam team, final Callback<Boolean> successCallback) {
		if (teamsByID.containsValue(team)) {
			this.teamStorage.deleteTeam(team, new Runnable() {

				@Override
				public void run() {
					for (UUID uuid : team.getMembers().keySet()) {
						if (Bukkit.getPlayer(uuid) == null) {
							continue;
						} else {
							teamsByOnlinePlayer.remove(Bukkit.getPlayer(uuid));
						}
					}
					teamsByID.remove(team.getID());
					teamsByName.remove(team.getTeamName().toUpperCase());
					successCallback.run(true);
				}
			});
			return true;
		}
		successCallback.run(false);
		return false;
	}
	
	public boolean handleJoinTeam(Player player, final PBTeam team, TeamMemberRole role, final Callback<Boolean> successCallback) {
		if (teamsByOnlinePlayer.containsKey(player)) {
			successCallback.run(false);
			return false;
		} else {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
			if (!bPlayer.hasElement(role.getElement())) {
				successCallback.run(false);
				return false;
			}
			
			if (!role.isEnabled()) {
				successCallback.run(false);
				return false;
			}
			
			final UUID uuid = player.getUniqueId();
			this.teamStorage.joinTeam(uuid, role, team, new Runnable() {
				public void run() {
					if (Bukkit.getPlayer(uuid) == null) {
						successCallback.run(false);
					} else {
						teamsByOnlinePlayer.put(Bukkit.getPlayer(uuid), team);
						successCallback.run(true);
					}
					return;
				}
			});
			return true;
		}
	}

	public boolean handleLeaveTeam(Player player, PBTeam team, final Callback<Boolean> successCallback) {
		if (!teamsByOnlinePlayer.containsKey(player)) {
			successCallback.run(false);
			return false;
		} else {
			final UUID uuid = player.getUniqueId();
			this.teamStorage.leaveTeam(uuid, team, new Runnable() {
				public void run() {
					if (Bukkit.getPlayer(uuid) == null) {
						successCallback.run(false);
					} else {
						teamsByOnlinePlayer.remove(Bukkit.getPlayer(uuid));
						successCallback.run(true);
					}
				}
			});
			return true;
		}
	}
	
	public boolean handleKickPlayer(Player player, PBTeam team, final Callback<Boolean> successCallback) {
		if (!teamsByOnlinePlayer.containsKey(player)) {
			successCallback.run(false);
			final UUID uuid = player.getUniqueId();
			this.teamStorage.leaveTeam(uuid, team, new Runnable() {
				public void run() {
					if (Bukkit.getPlayer(uuid) == null) {
						successCallback.run(false);
					} else {
						teamsByOnlinePlayer.remove(Bukkit.getPlayer(uuid));
						successCallback.run(true);
					}
				}
			});
			return true;
		}
		return false;
	}
	
	public List<PBTeam> getTeamList() {
		List<PBTeam> list = new ArrayList<>();
		
		for (PBTeam team : teamsByID.values()) {
			if (list.contains(team)) {
				continue;
			}
			list.add(team);
		}
		
		return list;
	}
}
