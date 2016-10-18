/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.managers;

import com.projectkorra.probending.game.field.ProbendingField;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Ivo
 */
public class FieldCreationManager implements Listener {

    private final JavaPlugin plugin;
    private final ProbendingHandler pHandler;
   
    private Player creator;
    private ProbendingField field;

    private Integer bigStep;
    private Integer smallStep;

    public FieldCreationManager(JavaPlugin plugin, ProbendingHandler pHandler) {
        this.plugin = plugin;
        this.pHandler = pHandler;
    }

    public void createField(Player player) {
        if (creator != null) {
            return;
        }
        bigStep = 0;
        smallStep = 1;
        creator = player;
        field = new ProbendingField();
        createFieldStep(player, null);
    }

    private void createFieldStep(Player player, String arg) {
        if (creator == null) {
            createField(player);
            return;
        }
        switch (bigStep) {
            case 0:
                if (arg == null) {
                    sendMessage(player, "Are you sure you want to create an arena?");
                    sendMessage(player, "When you create an arena, you cannot talk!");
                    sendMessage(player, "Options: [yes, no]");
                } else {
                    if (arg.equalsIgnoreCase("yes")) {
                        bigStep++;
                        sendMessage(player, "Next few steps will be the basic probending fields!");
                        createFieldStep(player, null);
                    } else if (arg.equalsIgnoreCase("no")) {
                        stopCreating(player);
                    } else {
                        sendMessage(player, "Options: [yes, no]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 1:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "Stand on the location of team " + ChatColor.RED + "1" + ChatColor.YELLOW
                            + " spawn of player " + smallStep + ", and type 'here'");
                } else {
                    if (arg.equalsIgnoreCase("here")) {
                        if (smallStep > 3) {
                            bigStep++;
                            smallStep = 1;
                            createFieldStep(player, null);
                            return;
                        }
                        field.setStartPointTeam1(smallStep, player.getLocation());
                        smallStep++;
                    } else {
                        sendMessage(player, "Options: [here]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 2:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "Stand on the location of team " + ChatColor.RED + "2" + ChatColor.YELLOW
                            + " spawn of player " + smallStep + ", and type 'here'");
                } else {
                    if (arg.equalsIgnoreCase("here")) {
                        if (smallStep > 3) {
                            bigStep++;
                            smallStep = 1;
                            createFieldStep(player, null);
                            return;
                        }
                        field.setStartPointTeam2(smallStep, player.getLocation());
                        smallStep++;
                    } else {
                        sendMessage(player, "Options: [here]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 3:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "Stand on the location of team " + ChatColor.RED + "2" + ChatColor.YELLOW
                            + " spawn of player " + smallStep + ", and type 'here'");
                } else {
                    if (arg.equalsIgnoreCase("here")) {
                        if (smallStep > 3) {
                            bigStep++;
                            smallStep = 1;
                            createFieldStep(player, null);
                            return;
                        }
                        field.setStartPointTeam2(smallStep, player.getLocation());
                        smallStep++;
                        createFieldStep(player, null);
                    } else {
                        sendMessage(player, "Options: [here]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 4:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "Now tell me, where is the teleport location " + smallStep + "/region for team "
                            + ChatColor.RED + "1");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off, "
                            + "region 4 = the location where they should be teleported then they fall off!");
                } else {
                    if (arg.equalsIgnoreCase("here")) {
                        switch (smallStep) {
                            case 1:
                                smallStep++;
                                field.setTeam1Location1(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 2:
                                smallStep++;
                                field.setTeam1Location2(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 3:
                                smallStep++;
                                field.setTeam1Location3(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 4:
                                smallStep = 1;
                                bigStep++;
                                field.setTeam1KnockedOffLocation(player.getLocation());
                                createFieldStep(player, null);
                                break;
                        }
                    } else {
                        sendMessage(player, "Options: [here]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 5:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "Now tell me, where is the teleport location " + smallStep + "/region for team "
                            + ChatColor.RED + "2");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off, "
                            + "region 4 = the location where they should be teleported then they fall off!");
                } else {
                    if (arg.equalsIgnoreCase("here")) {
                        switch (smallStep) {
                            case 1:
                                smallStep++;
                                field.setTeam2Location1(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 2:
                                smallStep++;
                                field.setTeam2Location2(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 3:
                                smallStep++;
                                field.setTeam2Location3(player.getLocation());
                                createFieldStep(player, null);
                                break;
                            case 4:
                                smallStep = 1;
                                bigStep++;
                                field.setTeam2KnockedOffLocation(player.getLocation());
                                createFieldStep(player, null);
                                break;
                        }
                    } else {
                        sendMessage(player, "Options: [here]");
                        createFieldStep(player, null);
                    }
                }
                break;
            case 6:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 1, region 1. [WG region, example: 'PB1Team1RG1']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam1Field1(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 7:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 1, region 2. [WG region, example: 'PB1Team1RG2']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam1Field2(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 8:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 1, region 3. [WG region, example: 'PB1Team1RG3']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam1Field3(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 9:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 2, region 1. [WG region, example: 'PB1Team2RG1']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam2Field1(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 10:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 2, region 2. [WG region, example: 'PB1Team2RG2']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam2Field2(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 11:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of team 2, region 3. [WG region, example: 'PB1Team2RG3']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off");
                } else {
                    field.setTeam2Field3(arg);
                    bigStep++;
                    createFieldStep(player, null);
                }
                break;
            case 12:
                if (arg == null) {
                    sendMessage(player, ChatColor.YELLOW + "What's the name of the knocked off region. [WG region, example: 'PB1KnockOff']");
                    sendMessage(player, ChatColor.YELLOW + "region 1 = The nearest to the middle, region 3 = the last part before they fall off, "
                            + "region 4 = the location where they should be teleported then they fall off!");
                } else {
                    field.setKockOffArea(arg);
                    finishCreating(player);
                }
                break;
        }
    }

    private void stopCreating(Player player) {
        sendMessage(player, "See you next time!");
        creator = null;
    }

    private void finishCreating(Player player) {
        sendMessage(player, "Awesome, thank you so much for creating the probending arena!");
        this.pHandler.addField(field);
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "FieldManager" + ChatColor.GOLD + "]" + ChatColor.WHITE + " " + message);
    }

    @EventHandler
    public void playerTalk(AsyncPlayerChatEvent event) {
        if (creator.equals(event.getPlayer())) {
            createFieldStep(event.getPlayer(), event.getMessage());
        }
    }
}
