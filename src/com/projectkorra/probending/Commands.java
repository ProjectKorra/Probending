/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending;

import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.GameMode;
import com.projectkorra.probending.managers.ProbendingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Ivo
 */
public class Commands {

    private final FieldCreationManager cHandler;
    private final ProbendingHandler pHandler;

    public Commands(ProbendingHandler pHandler, FieldCreationManager cHandler) {
        this.pHandler = pHandler;
        this.cHandler = cHandler;
    }

    public void mainCommands(Player player, String[] args) {
        if (args.length >= 1) {
            String[] newArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                newArgs[i - 1] = args[i];
            }
            switch (args[0].toLowerCase()) {
                case "admin":
                    adminCommands(player, newArgs);
                    return;
            }
        }
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "queue":
                    pHandler.quePlayer(player, GameMode.ANY);
                    return;
            }
        }
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending queue" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Queue up!", false);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Admin commands!", false);
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
    }

    private void adminCommands(Player player, String[] args) {
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "createfield":
                    //Check player permissions!
                    cHandler.createField(player);
                    return;
            }
        }
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin createField" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Create a field!", false);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
    }
}
