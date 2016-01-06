package com.projectkorra.probending.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

public class Arena {
	public static ConcurrentHashMap<String, Arena> arenas = new ConcurrentHashMap<String, Arena>();
	
	private String name;

	private World world;
	private Location spectatorSpawn;
	private Location teamOneSpawn;
	private Location teamTwoSpawn;
	
	private String divider;
	private String teamOneZoneOne;
	private String teamOneZoneTwo;
	private String teamOneZoneThree;
	
	private String teamTwoZoneOne;
	private String teamTwoZoneTwo;
	private String teamTwoZoneThree;
	
	private Color teamOneColor;
	private Color teamTwoColor;
	
	public Arena(String name, World world, Location spectatorSpawn, Location teamOneSpawn, Location teamTwoSpawn, String divider, String teamOneZoneOne, String teamOneZoneTwo, String teamOneZoneThree, String teamTwoZoneOne, String teamTwoZoneTwo, String teamTwoZoneThree, Color teamOneColor, Color teamTwoColor) {
		this.name = name;
		this.world = world;
		this.spectatorSpawn = spectatorSpawn;
		this.teamOneSpawn = teamOneSpawn;
		this.teamTwoSpawn = teamTwoSpawn;
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
	
	public Location getSpectatorSpawn() {
		return this.spectatorSpawn;
	}
	
	public Location getTeamOneSpawn() {
		return this.teamOneSpawn;
	}
	
	public Location getTeamTwoSpawn() {
		return this.teamTwoSpawn;
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
	
	

}
