/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

/**
 *
 * @author Ivo
 */
public class PBMessenger {

    public static void sendMessage(Player player, String message, Boolean prefix) {
        if (prefix) {
            player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Probending" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " " + message);
        } else {
            player.sendMessage(ChatColor.GRAY + message);
        }
    }

    public static void sendMessage(Player player, PBMessage message) {
        if (message.hasPrefix()) {
            player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Probending" + ChatColor.DARK_RED + "] " + message.getMessage());
        } else {
            player.sendMessage(message.getMessage());
        }
    }

    public enum PBMessage {

        BLANK("", false),
        DEFAULT_LINE(ChatColor.GOLD + "--------------------------------------------------", false),
        ERROR(ChatColor.RED + "ERROR, something went wrong!", true),
        NOPLAYER(ChatColor.RED + "No such player!", true),
        NOPERMS(ChatColor.RED + "You do not have enough permissions!", true),
        WRONGFORMAT(ChatColor.RED + "Incorrect usage!", true);

        private Boolean prefix;
        private String message;

        private PBMessage(String message, Boolean prefix) {
            this.message = message;
            this.prefix = prefix;
        }

        public String getMessage() {
            return message;
        }

        public Boolean hasPrefix() {
            return prefix;
        }
    }
}
