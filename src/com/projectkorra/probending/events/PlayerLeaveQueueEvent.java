package com.projectkorra.probending.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveQueueEvent extends Event implements Cancellable {

	public static final HandlerList HANDLERS = new HandlerList();

	private Player player;
	private boolean cancel;

	public PlayerLeaveQueueEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHanderList() {
		return HANDLERS;
	}

}
