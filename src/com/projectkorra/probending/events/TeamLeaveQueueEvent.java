package com.projectkorra.probending.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.probending.objects.PBTeam;

public class TeamLeaveQueueEvent extends Event implements Cancellable{
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancel;
	private PBTeam team;
	
	public TeamLeaveQueueEvent(PBTeam team) {
		this.team = team;
	}
	
	public PBTeam getTeam() {
		return team;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
