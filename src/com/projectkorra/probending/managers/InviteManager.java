package com.projectkorra.probending.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMessenger;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.libraries.flatfile.FileManager;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;
import com.projectkorra.projectkorra.Element;

import net.md_5.bungee.api.ChatColor;

public class InviteManager {
	
	private Probending plugin;
	
	private Map<UUID, Invitation> invites = new HashMap<>();
	private FileManager storage;

	public InviteManager(Probending plugin) {
		this.plugin = plugin;
		storage = new FileManager(plugin, "", "invites");
		populateMap();
	}
	
	public void populateMap() {
		List<String> entries = storage.getStringList("invites");
		for (String entry : entries) {
			String[] split = entry.split(":");
			UUID uuid = UUID.fromString(split[0]);
			PBTeam team = plugin.getTeamManager().getTeamFromName(split[1]);
			TeamMemberRole role = TeamMemberRole.parseRole(split[2]);
			
			if (uuid == null) {
				continue;
			}
			
			if (team == null) {
				continue;
			}
			
			if (role == null || !role.isEnabled()) {
				continue;
			}
			
			Invitation invite = new Invitation(team.getTeamName(), role.toString());
			invites.put(uuid, invite);
		}
	}
	
	public void saveToFile() {
		List<String> newList = new ArrayList<>();
		for (UUID uuid : invites.keySet()) {
			Invitation invite = invites.get(uuid);
			String entry = uuid.toString() + ":" + invite.getName() + ":" + invite.getRole();
			newList.add(entry);
		}
		
		storage.getFile().set("invites", newList);
		storage.save();
	}
	
	public boolean hasInvitation(Player player) {
		return invites.containsKey(player.getUniqueId());
	}
	
	/**
	 * 
	 * @param player Player checked for an invitation
	 * @return team name of the invitation
	 */
	public Invitation getInvitation(Player player) {
		return hasInvitation(player) ? invites.get(player.getUniqueId()) : null;
	}
	
	public void removeInvitation(Player player) {
		if (hasInvitation(player)) {
			invites.remove(player.getUniqueId());
		}
	}
	
	public void sendInvitation(Player player, String name, String role) {
		Invitation invite = new Invitation(name, role);
		invites.put(player.getUniqueId(), invite);
		notifyPlayer(player);
	}
	
	public void notifyPlayer(Player player) {
		if (!hasInvitation(player)) {
			return;
		}
		
		Invitation invite = invites.get(player.getUniqueId());
		Element e = Element.fromString(invite.getRole());
		String message = ChatColor.GOLD + "You have an invitation to join " + ChatColor.GREEN + invite.getName() + ChatColor.GOLD + " as their " + e.getColor() + e.getName() + e.getType()
					   + ChatColor.GOLD + "\nUse /pb join " + ChatColor.GREEN + invite.getName() + ChatColor.GOLD + " to accept the invitation! Notice: You must leave your current team if you have one!";
		PBMessenger.sendMessage(player, ChatColor.translateAlternateColorCodes('&', message), true);
	}
	
	public class Invitation {
		
		private String name;
		private String role;
		
		public Invitation(String name, String role) {
			this.name = name;
			this.role = role;
		}
		
		public String getName() {
			return name;
		}
		
		public String getRole() {
			return role;
		}
	}
}
