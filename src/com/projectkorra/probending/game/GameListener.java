package com.projectkorra.probending.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.projectkorra.probending.Probending;
import com.projectkorra.projectkorra.event.AbilityStartEvent;

public class GameListener implements Listener {

    private Game game;

    public GameListener(Game game) {
        this.game = game;
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (!game.canPlayerMove(player)) {
            if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
                event.getPlayer().sendMessage(ChatColor.RED + "Do not move yet!");
                event.getPlayer().teleport(from);
                event.setCancelled(true);
                return;
            }
        }
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            game.playerMove(player, from, to);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void PlayerAnimationEvent(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (!game.canPlayerMove(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void DamageEvent(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (game.isPlayerInMatch(player)) {
                event.setDamage(0);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void FoodLevelChangeEvent(FoodLevelChangeEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (game.isPlayerInMatch(player)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void playerBendEvent(AbilityStartEvent event) {
    	Player p = event.getAbility().getPlayer();
    	if (game.isPlayerInMatch(p)) {
    		if (!isAbilityAllowed(event.getAbility().getName())) {
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	Player p = (Player) event.getWhoClicked();
    	if (game.isPlayerInMatch(p))
    	{
    		event.setCancelled(true);
    	}
    }
    
    public static boolean isAbilityAllowed(String ability) {
    	return Probending.get().getConfig().getStringList("RoundSettings.AllowedMoves").contains(ability);
    }
}
