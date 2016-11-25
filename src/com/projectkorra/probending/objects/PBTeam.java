package com.projectkorra.probending.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.enums.TeamColor;
import com.projectkorra.probending.game.Game;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.projectkorra.Element;

import net.md_5.bungee.api.ChatColor;

public class PBTeam {

    private int _id;
    private String _name;
    private UUID _leader;
    private Map<UUID, TeamMemberRole> _members;
    private TeamColor[] colors;
    
    private int _wins, _gamesPlayed, _rating;

    public PBTeam(int id, String name, UUID leader, Map<UUID, TeamMemberRole> members, int wins, int gamesPlayed, int rating) {
        _id = id;
        _name = name;
        _leader = leader;
        _members = members;
        _wins = wins;
        _gamesPlayed = gamesPlayed;
        _rating = rating;
        colors = new TeamColor[] {TeamColor.WHITE, TeamColor.WHITE, TeamColor.WHITE, TeamColor.WHITE};
    }
    
    public TeamColor[] getColors() {
    	return colors;
    }
    
    public void setColors(TeamColor helmet, TeamColor chest, TeamColor leggings, TeamColor boots) {
    	colors[0] = helmet;
    	colors[1] = chest;
    	colors[2] = leggings;
    	colors[3] = boots;
    }
    
    public void changeColor(int spot, TeamColor color, Callback<Boolean> successCallback) {
    	//DB update
    	colors[spot] = color;
    }
    
    public ChatColor getDisplayColor() {
    	return colors[1].getClosest();
    }

    public int getID() {
        return _id;
    }

    public String getTeamName() {
        return _name;
    }
    
    public void setName(String name, Callback<Boolean> successCallback) {
    	//DB update needed here
    	_name = name;
    }

    public UUID getLeader() {
        return _leader;
    }

    public Map<UUID, TeamMemberRole> getMembers() {
        return _members;
    }
    
    public void setMemberRole(UUID member, TeamMemberRole role, Callback<Boolean> successCallback) {
    	//DB update
    	_members.put(member, role);
    }
    
    public int getGamesPlayed() {
    	return _gamesPlayed;
    }
    
    public int getWins() {
    	return _wins;
    }
    
    public int getRating() {
    	return _rating;
    }
    
    /**
     * Currently does nothing, added for future use.
     * @param ending 1:lose|2:win|3:draw
     * @param input some score that will be put into an algorithm
     */
    public void adjustRating(int ending, long input) {
    	//Put some rating algorithm here
    	//Probending.get().getTeamManager().updateTeam(this);
    }
    
    public void updateAfterGame(Game game, boolean win) {
    	_gamesPlayed += 1;
    	if (win) {
    		_wins += 1;
    	}
    	Probending.get().getTeamManager().updateTeam(this, null);
    }
    
    public void addPlayer(Player player, TeamMemberRole role, Callback<Boolean> successCallback) {
    	if (Probending.get().getTeamManager().handleJoinTeam(player, this, role, successCallback)) {
    		_members.put(player.getUniqueId(), role);
    	}
    }
    
    public void removePlayer(Player player, Callback<Boolean> successCallback) {
    	if (Probending.get().getTeamManager().handleLeaveTeam(player, this, successCallback)) {
    		_members.remove(player.getUniqueId());
    	}
    }
    
    public void kickPlayer(Player player, Callback<Boolean> successCallback) {
    	if (Probending.get().getTeamManager().handleKickPlayer(player, this, successCallback)) {
    		_members.remove(player.getUniqueId());
    	}
    }

    public static enum TeamMemberRole {

        WATER(Element.WATER.getColor() + "Waterbender", Element.WATER, true),
        AIR(Element.AIR.getColor() + "Airbender", Element.AIR, false),
        FIRE(Element.FIRE.getColor() + "Firebender", Element.FIRE, true),
        EARTH(Element.EARTH.getColor() + "Earthbender", Element.EARTH, true),
        CHI(Element.CHI.getColor() + "Chiblocker", Element.CHI, false);

        private String _display;
        private boolean _enabled;
        private Element _element;

        private TeamMemberRole(String display, Element element, boolean enabled) {
            _display = display;
            _enabled = enabled;
            _element = element;
        }

        public String getDisplay() {
            return _display;
        }

        public boolean isEnabled() {
            return _enabled;
        }
        
        public Element getElement() {
        	return _element;
        }

        public static TeamMemberRole parseRole(String role) {
            for (TeamMemberRole test : TeamMemberRole.values()) {
                if (test.toString().equalsIgnoreCase(role)) {
                    return test;
                }
            }

            return null;
        }
        
        public static TeamMemberRole[] getEnabledRoles() {
        	List<TeamMemberRole> enabled = new ArrayList<>();
        	for (TeamMemberRole role : values()) {
        		if (role.isEnabled()) {
        			enabled.add(role);
        		}
        	}
        	return enabled.toArray(new TeamMemberRole[enabled.size()]);
        }
    }
}
