package com.projectkorra.probending.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.probending.enums.GamePlayerMode;

public class PlayerJoinQueueEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private Player player;
	private GamePlayerMode mode;
	private boolean cancel;

	public PlayerJoinQueueEvent(Player player, GamePlayerMode mode) {
		this.player = player;
		this.mode = mode;
	}

	public Player getPlayer() {
		return player;
	}

	public GamePlayerMode getGamePlayerMode() {
		return mode;
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

}
