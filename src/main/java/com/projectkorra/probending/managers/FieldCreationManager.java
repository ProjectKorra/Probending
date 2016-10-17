/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.managers;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Ivo
 */
public class FieldCreationManager {

    private final JavaPlugin plugin;
    private final ProbendingHandler pHandler;

    public FieldCreationManager(JavaPlugin plugin, ProbendingHandler pHandler) {
        this.plugin = plugin;
        this.pHandler = pHandler;
    }
}
