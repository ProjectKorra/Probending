package com.projectkorra.probending.events;

import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.probending.objects.PBPlayer;
import com.projectkorra.probending.objects.PBTeam;

public class TeamJoinQueueEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean cancel;
	private PBTeam team;
	private List<PBPlayer> players;

	public TeamJoinQueueEvent(PBTeam team, List<PBPlayer> players) {
		this.team = team;
		this.players = players;
	}

	public PBTeam getTeam() {
		return team;
	}

	public List<PBPlayer> getPlayers() {
		return players;
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
