/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.managers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Ivo
 */
public class PBHandlerListener implements Listener {

    private final ProbendingHandler pHandler;

    public PBHandlerListener(ProbendingHandler pHandler) {
        this.pHandler = pHandler;
    }

    @EventHandler
    public void playerLogin(PlayerJoinEvent event) {
        pHandler.playerLogin(event.getPlayer());
    }
    
    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        pHandler.playerLogout(event.getPlayer());
    }
}
