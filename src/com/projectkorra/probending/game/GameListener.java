/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Ivo
 */
public class GameListener implements Listener {

    private Game game;

    public GameListener(Game game) {
        this.game = game;
    }

    @EventHandler
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
        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            game.playerMove(player, from, to);
        }
    }
    
    @EventHandler
    public void PlayerAnimationEvent(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (!game.canPlayerMove(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void DamgeEvent(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (game.isPlayerAProbender(player)) {
                event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (game.isPlayerAProbender(player)) {
                event.setCancelled(true);
            }
        }
    }
}
