/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending;

import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.enums.GameMode;
import com.projectkorra.probending.managers.ProbendingHandler;
import org.bukkit.Bukkit;
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
                    if (!player.hasPermission("probending.command.join")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    pHandler.quePlayer(player, GameMode.ANY);
                    return;
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "queue":
                    if (!player.hasPermission("probending.command.join")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    if (args[1].equalsIgnoreCase("1")) {
                        pHandler.quePlayer(player, GameMode.SINGLE);
                    } else if (args[1].equalsIgnoreCase("3")) {
                        pHandler.quePlayer(player, GameMode.TRIPLE);
                    } else {
                        PBMessager.sendMessage(player, "Either try 1, or 3!", true);
                    }
                    return;
                case "info":
                    if (!player.hasPermission("probending.command.info")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    pHandler.getPlayerInfo(player, Bukkit.getPlayer(args[1]));
                    return;
            }
        }
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending queue {1/3}" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Queue up!", false);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending info [PLAYER]" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Statistics of player!", false);
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
                case "2":
                    PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
                    PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin setSpawn [FIELD] [team] [point]" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Set spawn!", false);
                    PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin setFieldSpawn [FIELD] [team] [field]", false);
                    PBMessager.sendMessage(player, ChatColor.DARK_RED + "                 - " + ChatColor.YELLOW + "Set field spawn!", false);
                    PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin setRegionName [FIELD] [team] [field] [NAME]", false);
                    PBMessager.sendMessage(player, ChatColor.DARK_RED + "                 - " + ChatColor.YELLOW + "Set region name!", false);
                    PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin setKnockOff [FIELD] [NAME]" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Set spawn!", false);
                    PBMessager.sendMessage(player, ChatColor.GREEN + "[FIELD]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "FieldNumber" + ChatColor.AQUA + " | "
                            + ChatColor.GREEN + "[team]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "1 or 2" + ChatColor.AQUA + " | ", false);
                    PBMessager.sendMessage(player, ChatColor.GREEN + "[field]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "FieldPart 1 to 3" + ChatColor.AQUA + " | "
                            + ChatColor.GREEN + "[NAME]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "RegionValue" + ChatColor.AQUA, false);
                    PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
                    return;
                case "createfield":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.createField(player);
                    return;
                case "list":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.getFields(player);
                    return;
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "info":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.getFieldInfo(player, args[1]);
                    return;
                case "delete":
                    if (!player.hasPermission("probending.command.arena.delete")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.deleteField(player, args[1]);
                    return;
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "setknockoff":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.setKnockOffName(player, args[1], args[2]);
                    return;
            }
        } else if (args.length == 4) {
            switch (args[0].toLowerCase()) {
                case "setspawn":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.setFieldSpawn(player, args[1], args[2], args[3]);
                    return;
                case "setfieldspawn":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.setFieldRegionSpawn(player, args[1], args[2], args[3]);
                    return;

            }
        } else if (args.length == 5) {
            switch (args[0].toLowerCase()) {
                case "setregionname":
                    if (!player.hasPermission("probending.command.arena.create")) {
                        PBMessager.sendMessage(player, PBMessager.PBMessage.NOPERMS);
                        return;
                    }
                    cHandler.setFieldRegionName(player, args[1], args[2], args[3], args[4]);
                    return;
            }
        }
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin createField" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Create a field!", false);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin list" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "List all fields!", false);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin info [FIELD]" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Get info about a field!", false);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin delete [FIELD]" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Delete a field!", false);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, PBMessager.PBMessage.BLANK);
        PBMessager.sendMessage(player, ChatColor.GOLD + "/probending admin 2" + ChatColor.DARK_RED + " - " + ChatColor.YELLOW + "Page 2!", false);
        PBMessager.sendMessage(player, PBMessager.PBMessage.DEFAULT_LINE);
    }
}
