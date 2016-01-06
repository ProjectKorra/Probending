package com.projectkorra.probending.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

public class Arena {
	public static List<Arena> arenas = new ArrayList<>();
	
	private String name;
	private UUID id;
	
	private Location spectator;
	
	private Location teamOneZoneOne;
	private Location teamOneZoneTwo;
	private Location teamOneZoneThree;
	
	private Location teamTwoZoneOne;
	private Location teamTwoZoneTwo;
	private Location teamTwoZoneThree;
	
	public Arena(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
		
		arenas.add(this);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public void setSpectator(Location l) {
		this.spectator = l;
	}
	
	public Location getSpectator() {
		return this.spectator;
	}
	
	public void setTeamOneZoneOne(Location l) {
		this.teamOneZoneOne = l;
	}
	
	public Location getTeamOneZoneOne() {
		return this.teamOneZoneOne;
	}
	
	public void setTeamOneZoneTwo(Location l) {
		this.teamOneZoneTwo = l;
	}
	
	public Location getTeamOneZoneTwo() {
		return this.teamOneZoneTwo;
	}
	
	public void setTeamOneZoneThree(Location l) {
		this.teamOneZoneThree = l;
	}
	
	public Location getTeamOneZoneThree() {
		return this.teamOneZoneThree;
	}
	
	public void setTeamTwoZoneOne(Location l) {
		this.teamTwoZoneOne = l;
	}
	
	public Location getTeamTwoZoneOne() {
		return this.teamTwoZoneOne;
	}
	
	public void setTeamTwoZoneTwo(Location l) {
		this.teamTwoZoneTwo = l;
	}
	
	public Location getTeamTwoZoneTwo() {
		return this.teamTwoZoneTwo;
	}
	
	public void setTeamTwoZoneThree(Location l) {
		this.teamTwoZoneThree = l;
	}
	
	public Location getTeamTwoZoneThree() {
		return this.teamTwoZoneThree;
	}
	
	

}
