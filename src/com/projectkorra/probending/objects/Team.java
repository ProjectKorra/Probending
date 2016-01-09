package com.projectkorra.probending.objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.storage.DBConnection;
import com.projectkorra.projectkorra.Element;

public class Team {

	public static ConcurrentHashMap<String, Team> teams = new ConcurrentHashMap<String, Team>();
	String name;
	UUID owner;
	UUID airbender;
	UUID waterbender;
	UUID earthbender;
	UUID firebender;
	UUID chiblocker;
	int wins;
	int losses;

	public Team(String name, UUID owner, UUID airbender, UUID waterbender, UUID earthbender, UUID firebender, UUID chiblocker, int wins, int losses) {
		this.name = name;
		this.owner = owner;
		this.airbender = airbender;
		this.waterbender = waterbender;
		this.earthbender = earthbender;
		this.firebender = firebender;
		this.chiblocker = chiblocker;
		this.wins = wins;
		this.losses = losses;
		teams.put(name, this);
	}

	public boolean isOwner(UUID uuid) {
		if (this.owner == uuid) return true;
		return false;
	}
	public String getName() {
		return name;
	}

	public UUID getOwner() {
		return owner;
	}

	public UUID getAirbender() {
		return airbender;
	}

	public UUID getWaterbender() {
		return waterbender;
	}

	public UUID getEarthbender() {
		return earthbender;
	}

	public UUID getFirebender() {
		return firebender;
	}

	public UUID getChiblocker() {
		return chiblocker;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}
	
	public Set<UUID> getPlayerUUIDs() {
		Set<UUID> playeruuids = new HashSet<UUID>();
		if (hasAirbender()) playeruuids.add(getAirbender());
		if (hasWaterbender()) playeruuids.add(getWaterbender());
		if (hasEarthbender()) playeruuids.add(getEarthbender());
		if (hasFirebender()) playeruuids.add(getFirebender());
		if (hasChiblocker()) playeruuids.add(getChiblocker());
		return playeruuids;
	}

	public Set<Player> getOnlinePlayers() {
		Set<Player> players = new HashSet<Player>();
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (PBMethods.players.containsKey(player.getUniqueId())) {
				if (PBMethods.getPlayerTeam(player.getUniqueId()).getName().equalsIgnoreCase(this.getName())) {
					players.add(player);
				}
			}
		}
		return players;
	}

	public void addWin() {
		this.wins = wins + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + this.wins + " WHERE team = '" + this.name + "'");
	}

	public void addLoss() {
		this.losses = losses + 1;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET losses = " + this.losses + " WHERE team = '" + this.name + "'");
	}

	public void setWins(int number) {
		this.wins = number;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + this.wins + " WHERE team = '" + this.name + "'");
	}

	public void setLosses(int number) {
		this.losses = number;
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET wins = " + this.losses + " WHERE team = '" + this.name + "'");
	}

	public void setAirbender(UUID player) {
		//String uuid = player != null ? "'" + player.toString() + "'" : "NULL";
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Air = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.airbender = player;
	}

	public void setWaterbender(UUID player) {
		//String uuid = player != null ? "'" + player.toString() + "'" : "NULL";
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Water = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.waterbender = player;
	}

	public void setEarthbender(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Earth = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.earthbender = player;
	}

	public void setOwner(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET owner = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.owner = player;
	}

	public void setFirebender(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Fire = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.firebender = player;
	}

	public void setChiblocker(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Chi = " + (player != null ? "'" + player.toString() + "'" : "NULL") + " WHERE team = '" + this.name + "'");
		this.chiblocker = player;
	}
	
	public void delete() {
		DBConnection.sql.modifyQuery("DELETE FROM probending_teams WHERE team = '" + this.getName() + "'");
		teams.remove(this.getName());
	}
	
	public boolean hasAirbender() {
		if (this.airbender != null) return true;
		return false;
	}
	
	public boolean hasWaterbender() {
		if (this.waterbender != null) return true;
		return false;
	}
	
	public boolean hasEarthbender() {
		if (this.earthbender != null) return true;
		return false;
	}
	
	public boolean hasFirebender() {
		if (this.firebender != null) return true;
		return false;
	}
	
	public boolean hasChiblocker() {
		if (this.chiblocker != null) return true;
		return false;
	}
	
	public void removePlayer(UUID uuid) {
		String element = null;
		if (getFirebender() == uuid) {
			element = "Fire";
			setFirebender(null);
		}
		if (getChiblocker() == uuid) {
			element = "Chi";
			setChiblocker(null);
		}
		if (getAirbender() == uuid) {
			element = "Air";
			setAirbender(null);
		}
		if (getWaterbender() == uuid) {
			element = "Water";
			setWaterbender(null);
		}
		if (getEarthbender() == uuid) {
			element = "Earth";
			setEarthbender(null);
		}
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = NULL WHERE uuid = '" + uuid.toString() + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET " + element + " = NULL WHERE team = '" + this.name + "'");
		PBMethods.players.put(uuid.toString(), null);
	}
	
	public Set<Element> getElements() {
		Set<Element> elements = new HashSet<Element>();
		if (getAirbender() != null) elements.add(Element.Air);
		if (getWaterbender() != null) elements.add(Element.Water);
		if (getEarthbender() != null) elements.add(Element.Earth);
		if (getFirebender() != null) elements.add(Element.Fire);
		if (getChiblocker() != null) elements.add(Element.Chi);
		return elements;
	}
	
	public void addPlayer(UUID player, String element) {
		DBConnection.sql.modifyQuery("UPDATE probending_players SET team = '" + this.name + "' WHERE uuid = '" + player.toString() + "'");
		if (element.equalsIgnoreCase("air")) this.setAirbender(player);
		if (element.equalsIgnoreCase("water")) this.setWaterbender(player);
		if (element.equalsIgnoreCase("earth")) this.setEarthbender(player);
		if (element.equalsIgnoreCase("fire")) this.setFirebender(player);
		if (element.equalsIgnoreCase("chi")) this.setChiblocker(player);
		PBMethods.players.put(player.toString(), this.name);
	}
	
	public int getSize() {
		int size = 0;
		if (hasAirbender()) size++;
		if (hasWaterbender()) size++;
		if (hasEarthbender()) size++;
		if (hasFirebender()) size++;
		if (hasChiblocker()) size++;
		return size;
	}
}
