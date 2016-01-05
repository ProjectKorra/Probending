package com.projectkorra.probending.objects;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.projectkorra.probending.storage.DBConnection;

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
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Air = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.airbender = player;
	}
	
	public void setWaterbender(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Water = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.waterbender = player;
	}
	
	public void setEarthbender(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Earth = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.earthbender = player;
	}
	
	public void setOwner(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET owner = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.owner = player;
	}
	
	public void setFirebender(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Fire = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.firebender = player;
	}
	
	public void setChiblocker(UUID player) {
		DBConnection.sql.modifyQuery("UPDATE probending_teams SET Chi = '" + player.toString() + "' WHERE team = '" + this.name + "'");
		this.chiblocker = player;
	}
}
