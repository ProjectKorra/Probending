package com.projectkorra.probending.objects;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import com.projectkorra.probending.storage.DBConnection;

public class Arena {
	public static ConcurrentHashMap<String, Arena> arenas = new ConcurrentHashMap<String, Arena>();
	
	private String name;

	private World world;
	private Location spectatorSpawn;
	private Location teamOneSpawn;
	private Location teamTwoSpawn;
	
	private String field;
	private String divider;
	private String teamOneZoneOne;
	private String teamOneZoneTwo;
	private String teamOneZoneThree;
	
	private String teamTwoZoneOne;
	private String teamTwoZoneTwo;
	private String teamTwoZoneThree;
	
	private Color teamOneColor;
	private Color teamTwoColor;
	
	public Arena(String name, World world, Location spectatorSpawn, Location teamOneSpawn, Location teamTwoSpawn, String field, String divider, String teamOneZoneOne, String teamOneZoneTwo, String teamOneZoneThree, String teamTwoZoneOne, String teamTwoZoneTwo, String teamTwoZoneThree, Color teamOneColor, Color teamTwoColor) {
		this.name = name;
		this.world = world;
		this.spectatorSpawn = spectatorSpawn;
		this.teamOneSpawn = teamOneSpawn;
		this.teamTwoSpawn = teamTwoSpawn;
		this.field = field;
		this.divider = divider;
		this.teamOneZoneOne = teamOneZoneOne;
		this.teamOneZoneTwo = teamOneZoneTwo;
		this.teamOneZoneThree = teamOneZoneThree;
		this.teamTwoZoneOne = teamTwoZoneOne;
		this.teamTwoZoneTwo = teamTwoZoneTwo;
		this.teamTwoZoneThree = teamTwoZoneThree;
		this.teamOneColor = teamOneColor;
		this.teamTwoColor = teamTwoColor;
		
		arenas.put(name, this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void setWorld(World world) {
		this.world = world;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET world = '" + world.getName() + "' WHERE name = '" + this.name + "'");
	}
	
	public void setSpectatorSpawn(Location loc) {
		this.spectatorSpawn = loc;
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET spectatorX = " + x + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET spectatorY = " + y + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET spectatorZ = " + z  +" WHERE name = '" + this.name + "'");
	}
	
	public void setTeamOneSpawn(Location loc) {
		this.teamOneSpawn = loc;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneX = " + loc.getBlockX() + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneY = " + loc.getBlockY() + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneZ = " + loc.getBlockZ() + " WHERE name = '" + this.name + "'");
	}
	
	public void setTeamTwoSpawn(Location loc) {
		this.teamTwoSpawn = loc;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoX = " + loc.getBlockX() + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoY = " + loc.getBlockY() + " WHERE name = '" + this.name + "'");
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoZ = " + loc.getBlockZ() + " WHERE name = '" + this.name + "'");
	}
	
	public Location getSpectatorSpawn() {
		return this.spectatorSpawn;
	}
	
	public Location getTeamOneSpawn() {
		return this.teamOneSpawn;
	}
	
	public Location getTeamTwoSpawn() {
		return this.teamTwoSpawn;
	}
	
	public void setField(String zone) {
		this.field = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET field = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setDivider(String zone) {
		this.divider = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET divider = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamOneZoneOne(String zone) {
		this.teamOneZoneOne = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneZoneOne = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamOneZoneTwo(String zone) {
		this.teamOneZoneTwo = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneZoneTwo = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamOneZoneThree(String zone) {
		this.teamOneZoneThree = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamOneZoneThree = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamTwoZoneOne(String zone) {
		this.teamTwoZoneOne = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoZoneOne = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamTwoZoneTwo(String zone) {
		this.teamTwoZoneTwo = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoZoneTwo = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public void setTeamTwoZoneThree(String zone) {
		this.teamTwoZoneThree = zone;
		DBConnection.sql.modifyQuery("UPDATE probending_arenas SET teamTwoZoneThree = '" + zone + "' WHERE name = '" + this.name + "'");
	}
	
	public String getField() {
		return this.field;
	}
	
	public String getTeamOneZoneOne() {
		return this.teamOneZoneOne;
	}
	
	public String getTeamOneZoneTwo() {
		return this.teamOneZoneTwo;
	}
	
	public String getTeamOneZoneThree() {
		return this.teamOneZoneThree;
	}
	
	public String getTeamTwoZoneOne() {
		return this.teamTwoZoneOne;
	}
	
	public String getTeamTwoZoneTwo() {
		return this.teamTwoZoneTwo;
	}
	
	public String getTeamTwoZoneThree() {
		return this.teamTwoZoneThree;
	}
	
	public String getDivider() {
		return this.divider;
	}
	
	public Color getTeamOneColor() {
		return this.teamOneColor;
	}
	
	public Color getTeamTwoColor() {
		return this.teamTwoColor;
	}
	
	public void delete() {
		DBConnection.sql.modifyQuery("DELETE FROM probending_arenas WHERE name = '" + this.name + "'");
		arenas.remove(this.name);
	}
	
	

}
