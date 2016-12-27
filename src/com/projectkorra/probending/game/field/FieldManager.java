/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectkorra.probending.game.field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.projectkorra.probending.enums.WinningType;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.objects.ProbendingField;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 *
 * @author Ivo
 */
public class FieldManager {

    private Game game;
    private ProbendingField field;

    private Map<UUID, Set<ProtectedRegion>> oldPlayerRegions = new HashMap<>();

    private Map<UUID, Integer> playerLocation = new HashMap<>();
    private Map<UUID, Integer> playerFaults = new HashMap<>();

    public FieldManager(Game game, ProbendingField field) {
        this.game = game;
        this.field = field;
    }

    public void playerMove(Player player, Location from, Location to) {
        Plugin wgp = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wgp == null) {
            return;
        }
        WorldGuardPlugin wg = (WorldGuardPlugin) wgp;
        Set<ProtectedRegion> toRegions = wg.getRegionContainer().get(player.getWorld()).getApplicableRegions(to).getRegions();
        Set<ProtectedRegion> newRegions = toRegions;
        if (!oldPlayerRegions.containsKey(player.getUniqueId())) {
            oldPlayerRegions.put(player.getUniqueId(), new HashSet<ProtectedRegion>());
        }
        for (ProtectedRegion rg : oldPlayerRegions.get(player.getUniqueId())) {
            if (newRegions.contains(rg)) {
                newRegions.remove(rg);
            }
        }
        if (!game.isSuddenDeath()) {
            if (newRegions.isEmpty()) {
                return;
            }
            oldPlayerRegions.get(player.getUniqueId()).clear();
            oldPlayerRegions.get(player.getUniqueId()).addAll(toRegions);
            for (ProtectedRegion rg : newRegions) {
                if (playerLocation.containsKey(player.getUniqueId())) {
                    String regionName = rg.getId();
                    if (field.getTeam1Field1().equalsIgnoreCase(regionName) || field.getTeam1Field2().equalsIgnoreCase(regionName)
                            || field.getTeam1Field3().equalsIgnoreCase(regionName) || field.getTeam2Field1().equalsIgnoreCase(regionName)
                            || field.getTeam2Field2().equalsIgnoreCase(regionName) || field.getTeam2Field3().equalsIgnoreCase(regionName)
                            || field.getKnockOffArea().equalsIgnoreCase(regionName)) {
                        gamePlaces(player, regionName);
                    }
                }
            }
        } else {
            if (oldPlayerRegions.get(player.getUniqueId()).isEmpty()) {
                oldPlayerRegions.get(player.getUniqueId()).addAll(toRegions);
            }
            boolean isDMPlayer = false;
            for (ProtectedRegion rg : oldPlayerRegions.get(player.getUniqueId())) {
                String regionName = rg.getId();
                if (field.getDeathMatchArea().equalsIgnoreCase(regionName)) {
                    isDMPlayer = true;
                    break;
                }
            }
            boolean leftDMArea = true;
            for (ProtectedRegion rg : newRegions) {
                String regionName = rg.getId();
                if (field.getDeathMatchArea().equalsIgnoreCase(regionName)) {
                    leftDMArea = false;
                    break;
                }
            }
            if (!isDMPlayer && leftDMArea) {
                //Nothing happened, was never a DM player and did not step in!
            } else if (isDMPlayer && leftDMArea) {
                //A DM player left the area!
                gameDMPlaces(player, true);
            } else if (!isDMPlayer && !leftDMArea) {
                //A not DM player entered the area!!!
                gameDMPlaces(player, false);
            } else if (isDMPlayer && !leftDMArea) {
                //Nothing happened, a DM player stayed in his area!
            }
        }
    }

    public void reset() {
        int i = 1;
        oldPlayerRegions.clear();
        if (!game.isSuddenDeath()) {
            boolean differentSpawnPoints = false;
            if (field.getStartPointTeam1(game.getTeam1Players().size()) != null) {
                differentSpawnPoints = true;
            }
            for (Player p : game.getTeam1Players()) {
                playerLocation.put(p.getUniqueId(), 4);
                playerFaults.put(p.getUniqueId(), 0);
                Location loc = field.getStartPointTeam1(i);
                if (loc != null) {
                    p.teleport(loc);
                }
                if (differentSpawnPoints) {
                    i++;
                }
            }
            i = 1;
            differentSpawnPoints = false;
            if (field.getStartPointTeam2(game.getTeam2Players().size()) != null) {
                differentSpawnPoints = true;
            }
            for (Player p : game.getTeam2Players()) {
                playerLocation.put(p.getUniqueId(), 3);
                playerFaults.put(p.getUniqueId(), 0);
                Location loc = field.getStartPointTeam2(i);
                if (loc != null) {
                    p.teleport(loc);
                }
                if (differentSpawnPoints) {
                    i++;
                }
            }
        } else {
            //TODO: pick the player that should do the deathmatch, rather than picking a random one!
            int dmPlayer = 0;
            int random = new Random().nextInt(game.getTeam1Players().size());
            for (Player p : game.getTeam1Players()) {
                if (dmPlayer == random) {
                    Location loc = field.getTeam1DMLocation();
                    if (loc != null) {
                        p.teleport(loc);
                    }
                } else {
                    Location loc = field.getTeam1Location2();
                    if (loc != null) {
                        p.teleport(loc);
                    }
                }
                dmPlayer++;
            }
            dmPlayer = 0;
            random = new Random().nextInt(game.getTeam2Players().size());
            for (Player p : game.getTeam2Players()) {
                if (dmPlayer == random) {
                    Location loc = field.getTeam2DMLocation();
                    if (loc != null) {
                        p.teleport(loc);
                    }
                } else {
                    Location loc = field.getTeam2Location2();
                    if (loc != null) {
                        p.teleport(loc);
                    }
                }
                dmPlayer++;
            }
        }
    }

    public WinningType getWinningTeam() {
        int team2loc = 0;
        int team1loc = 0;
        for (Player p : game.getTeam1Players()) {
            if (playerLocation.get(p.getUniqueId()) > 0) {
                int i = playerLocation.get(p.getUniqueId());
                if (i == 0) {
                    i = 7;
                }
                team1loc = team1loc + (7 - i);
            }
        }
        for (Player p : game.getTeam2Players()) {
            if (playerLocation.get(p.getUniqueId()) > 0) {
                team2loc = team2loc + playerLocation.get(p.getUniqueId());
            }
        }
        if (team1loc > team2loc) {
            return WinningType.TEAM1;
        } else if (team1loc < team2loc) {
            return WinningType.TEAM2;
        } else {
            return WinningType.DRAW;
        }
    }

    private void gameDMPlaces(Player player, boolean dmPlayer) {
        String teamName = "N/A";
        if (game.getTeam1Players().contains(player)) {
            teamName = "team1";
        } else if (game.getTeam2Players().contains(player)) {
            teamName = "team2";
        } else {
            return;
        }
        if (dmPlayer) {
            if (teamName.equalsIgnoreCase("team1")) {
                game.endRound(WinningType.TEAM2);
            } else {
                game.endRound(WinningType.TEAM1);
            }
        } else {
            if (teamName.equalsIgnoreCase("team1")) {
                Location loc = field.getTeam1Location2();
                if (loc != null) {
                    player.teleport(loc);
                }
            } else {
                Location loc = field.getTeam2Location2();
                if (loc != null) {
                    player.teleport(loc);
                }
            }
        }
    }

    private void gamePlaces(Player player, String toPlaceStr) {
        gamePlaces(player, fromRegionToNumber(toPlaceStr));
    }

    private void gamePlaces(Player player, int toPlace) {
        Set<Player> enemyTeam;
        int fDir = 0;
        String teamName = "N/A";
        if (game.getTeam1Players().contains(player)) {
            fDir = -1;
            teamName = "team1";
            enemyTeam = game.getTeam2Players();
        } else if (game.getTeam2Players().contains(player)) {
            fDir = 1;
            teamName = "team2";
            enemyTeam = game.getTeam1Players();
        } else {
            return;
        }
        int fromPlace = playerLocation.get(player.getUniqueId());

        if (!game.isSuddenDeath()) {
            //SHOULD NOT BE CALLED IN DEATHMATCH USE gameDMPlaces
            if (fromPlace >= 1 && fromPlace <= 6) {
                if (toPlace >= 1 && toPlace <= 6) {
                    if (toPlace == fromPlace - fDir) {//Hit line behind!
                        setPlayerLocation(player, toPlace);
                        broadcast(ChatColor.GOLD + "Player: " + ChatColor.BLUE + player.getDisplayName() + ChatColor.GOLD + " moved 1 place back!");
                    } else if (toPlace == fromPlace + fDir) {//Hit line infront!
                        frontLineHit(player, fromPlace - fDir, fromPlace);
                    } else if (fromPlace - toPlace == 0) {//Player gets teleported!
                    } else {//This player is hacking ?_?
                    }
                    if ((fromPlace - extremeLocation(teamName)) * fDir > 0) { //The team can move forward!
                        teleportTeamForward(enemyTeam, fromPlace);
                    }
                } else if (toPlace == 0) {//over rand
                    playerLocation.put(player.getUniqueId(), 0);
                    setPlayerLocation(player, toPlace);
                }
            } else {
                if (toPlace == 0) {
                    if (game.getTeam1Players().contains(player)) {
                        setPlayerLocation(player, getRegionWarp(toPlace, "team1"));
                    } else if (game.getTeam2Players().contains(player)) {
                        setPlayerLocation(player, getRegionWarp(toPlace, "team2"));
                    }
                    player.sendMessage(ChatColor.RED + "Please be patient till the game has ended! (Or use /pb leave, this might have consequences!)");
                }
            }
            if (extremeLocation(teamName) == 0) {
                if (teamName.equalsIgnoreCase("team1")) {
                    game.endRound(WinningType.TEAM2);
                } else if (teamName.equalsIgnoreCase("team2")) {
                    game.endRound(WinningType.TEAM1);
                }
            }
        }
    }

    //Hit line infront of you!
    private void frontLineHit(Player player, int iplace, int fromPlace) {
        playerFaults.put(player.getUniqueId(), (playerFaults.get(player.getUniqueId()) + 1));
        if (playerFaults.get(player.getUniqueId()) < 3) {
            setPlayerLocation(player, fromPlace);
            broadcast(ChatColor.GOLD + "Player: " + ChatColor.BLUE + player.getDisplayName() + ChatColor.GOLD
                    + " has now " + ChatColor.DARK_RED + playerFaults.get(player.getUniqueId()) + ChatColor.GOLD + " faults!");
        } else {
            broadcast(ChatColor.GOLD + "Player: " + ChatColor.BLUE + player.getDisplayName() + ChatColor.GOLD
                    + " now has " + ChatColor.DARK_RED + playerFaults.get(player.getUniqueId()) + ChatColor.GOLD + " faults and will be teleported 1 place back!");
            setPlayerLocation(player, iplace);
            playerFaults.put(player.getUniqueId(), 0);
        }
    }

    private void setPlayerLocation(Player player, int iplace) {
        if (iplace == 7) {
            iplace = 0;
        }
        playerLocation.put(player.getUniqueId(), iplace);
        if (game.getTeam1Players().contains(player)) {
            setPlayerLocation(player, getRegionWarp(iplace, "team1"));
        } else if (game.getTeam2Players().contains(player)) {
            setPlayerLocation(player, getRegionWarp(iplace, "team2"));
        }
    }

    private void setPlayerLocation(Player player, Location place) {
        player.teleport(place);
    }

    private void teleportTeamForward(Set<Player> team, int iplace) {
        for (Player p : team) {
            if (playerLocation.get(p.getUniqueId()) == 0) {
                p.sendMessage(ChatColor.DARK_GRAY + "Your team was moved forward, "
                        + "but you have already been shot out of the arena, good luck next time!");
            } else {
                setPlayerLocation(p, iplace);
            }
        }
    }

    private int extremeLocation(String teamName) {
        int extremeLoc = 0;
        Set<Player> team = null;
        if (teamName == "team1") {
            team = game.getTeam1Players();
        } else if (teamName == "team2") {
            team = game.getTeam2Players();
        }
        if (team != null) {
            for (Player p : team) {
                int newLoc = playerLocation.get(p.getUniqueId());
                if (newLoc <= 0) {
                    continue;
                }
                if ("team2".equals(teamName)) {
                    if (newLoc > extremeLoc) {
                        extremeLoc = newLoc;
                    }
                } else if ("team1".equals(teamName)) {
                    if ((7 - newLoc) > extremeLoc) {
                        extremeLoc = newLoc;
                    }
                }
            }
        }
        return extremeLoc;
    }

    private int fromRegionToNumber(String strPlace) {
        if (strPlace.equalsIgnoreCase(field.getKnockOffArea())) {
            return 0;
        } else if (strPlace.equalsIgnoreCase(field.getTeam2Field3())) {
            return 1;
        } else if (strPlace.equalsIgnoreCase(field.getTeam2Field2())) {
            return 2;
        } else if (strPlace.equalsIgnoreCase(field.getTeam2Field1())) {
            return 3;
        } else if (strPlace.equalsIgnoreCase(field.getTeam1Field1())) {
            return 4;
        } else if (strPlace.equalsIgnoreCase(field.getTeam1Field2())) {
            return 5;
        } else if (strPlace.equalsIgnoreCase(field.getTeam1Field3())) {
            return 6;
        } else {
            return -1;
        }
    }

    private Location getRegionWarp(int intPlace, String teamName) {
        Location l = null;
        switch (intPlace) {
            case 0:
                if (teamName.equals("team1")) {
                    l = field.getTeam1KnockedOffLocation().clone();
                } else if (teamName.equals("team2")) {
                    l = field.getTeam2KnockedOffLocation().clone();
                }
                break;
            case 1:
                l = field.getTeam2Location3().clone();
                if (teamName.equals("team1")) {
                    l.setDirection(field.getTeam2Location3().clone().getDirection().multiply(-1).setY(0));
                }
                break;
            case 2:
                l = field.getTeam2Location2().clone();
                if (teamName.equals("team1")) {
                    l.setDirection(field.getTeam2Location2().clone().getDirection().multiply(-1).setY(0));
                }
                break;
            case 3:
                l = field.getTeam2Location1().clone();
                if (teamName.equals("team1")) {
                    l.setDirection(field.getTeam2Location1().clone().getDirection().multiply(-1).setY(0));
                }
                break;
            case 4:
                l = field.getTeam1Location1().clone();
                if (teamName.equals("team2")) {
                    l.setDirection(field.getTeam1Location1().clone().getDirection().multiply(-1).setY(0));
                }
                break;
            case 5:
                l = field.getTeam1Location2().clone();
                if (teamName.equals("team2")) {
                    l.setDirection(field.getTeam1Location2().clone().getDirection().multiply(-1).setY(0));
                }
                break;
            case 6:
                l = field.getTeam1Location3().clone();
                if (teamName.equals("team2")) {
                    l.setDirection(field.getTeam1Location3().clone().getDirection().multiply(-1).setY(0));
                }
                break;
        }
        return l;
    }

    private void broadcast(String message) {
        Set<Player> players = new HashSet<>();
        players.addAll(game.getTeam1Players());
        players.addAll(game.getTeam2Players());
        for (Player p : players) {
            p.sendMessage(message);
        }
    }
}
