package com.projectkorra.probending.commands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.enums.TeamColor;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.managers.InviteManager.Invitation;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;
import com.projectkorra.projectkorra.BendingPlayer;

import net.md_5.bungee.api.ChatColor;

public class TeamCommand extends PBCommand{

	public TeamCommand() {
		super("team", "Access and use the team commands!", "/probending team", new String[] {"team", "t"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command!");
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			List<String> help = Arrays.asList("&6/probending team create [name] [role] [color1] {color2} {color3} {color4} &eCreate a team and choose your role, and select the team colors!",
					"&6/probending team invite [player] [role] &eInvite a player to your team for a certain role!",
					"&6/probending team join [team] &eJoin a team. You can only join one that has invited you!",
					"&6/probending team leave &eLeave your current team!",
					"&6/probending team kick [player] &eKick a player from your team! &cLeader only!",
					"&6/probending team setname [name] &eChange the name of your team!",
					"&6/probending team setcolor [#(0-3)] [color] &eChange the color of a gear piece! /pb colors for color list. &cLeader only!",
					"&6/probending team setmemberrole [player] [role] &eChange the role of a member! &cLeader only!");
			
			for (String s : help) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			}
			return;
		}
		
		if (!player.hasPermission("probending.command.team." + args[0].toLowerCase())) {
			player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
			return;
		}
		
		switch (args[0].toLowerCase()) {
			case "create":
				if (args.length <= 3 || args.length >= 9) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team create [name] [role] [color1] {color2} {color3} {color4}");
					return;
				}
				TeamColor[] colors = new TeamColor[] {null, null, null, null};
				for (int i = 4; i <= 7; i++) {
					if (args.length >= i) {
						colors[(i-4)] = (TeamColor.parseColor(args[i-1]) == null ? TeamColor.WHITE : TeamColor.parseColor(args[i-1]));
					}
				}
				
				for (int i = 1; i < 4; i++) {
					if (colors[i] == null) {
						colors[i] = colors[0];
					}
				}
				
				createTeam(player, args[1], args[2], colors[0], colors[1], colors[2], colors[3]);
				return;
			case "invite":
				if (args.length != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team invite [player] [element]");
					return;
				}
				invitePlayer(player, args[1], args[2]);
				return;
			case "join":
				if (args.length != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team join [teamName]");
					return;
				}
				attemptJoinTeam(player, args[1]);
				return;
			case "leave":
				if (args.length != 1) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team leave");
					return;
				}
				attemptLeaveTeam(player);
				return;
			case "kick":
				if (args.length != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team kick [player]");
					return;
				}
				attemptKickPlayer(player, args[1]);
				return;
			case "setname":
				if (args.length != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setname [name]");
					return;
				}
				changeTeamName(player, args[1]);
				return;
			case "setcolor":
				if (args.length != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setcolor [#] [color]");
					return;
				}
				changeTeamColor(player, args[1], args[2]);
				return;
			case "setmemberrole":
				if (args.length != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setmemberrole [player] [element]");
					return;
				}
				changeMemberRole(player, args[1], args[2]);
				return;
			case "info":
				if (args.length != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team info [team]");
					return;
				}
				attemptTeamLookup(player, args[1]);
				return;
			case "disband":
				if (args.length != 1) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team disband");
					return;
				}
				attemptTeamDisband(player);
				return;
		}
	}

	public void createTeam(Player leader, String name, String element, TeamColor helmet, TeamColor chest, TeamColor leggings, TeamColor boots) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(leader);
		if (team != null) {
			leader.sendMessage(ChatColor.RED + "You already have a team!");
			return;
		}
		
		team = Probending.get().getTeamManager().getTeamFromName(name);
		if (team != null) {
			leader.sendMessage(ChatColor.RED + "Team Name taken, please select another!");
			return;
		}
		
		TeamMemberRole role = TeamMemberRole.parseRole(element);
		if (role == null || !role.isEnabled()) {
			leader.sendMessage(ChatColor.RED + "Unknown team role, try one of these:");
			for (TeamMemberRole role2 : TeamMemberRole.getEnabledRoles()) {
				leader.sendMessage("- " + role2.getDisplay());
			}
			return;
		}
		
		leader.sendMessage(ChatColor.GREEN + "Finalizing team creation, do not log off!");
		final UUID leaderUUID = leader.getUniqueId();
		Probending.get().getTeamManager().createNewTeam(leader, name, role, new TeamColor[] {helmet, chest, leggings, boots}, new Callback<Boolean>() {
			public void run(Boolean success) {
				if (Bukkit.getPlayer(leaderUUID) == null) {
					return;
				}
				if (success) {
					Bukkit.getPlayer(leaderUUID).sendMessage(ChatColor.GREEN + "Team successfully created!");
				} else {
					Bukkit.getPlayer(leaderUUID).sendMessage(ChatColor.RED + "Unexpected error creating team! It will not be saved!");
				}
			}
		});
	}
	
	public void attemptTeamDisband(Player player) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(player);
		if (team == null) {
			player.sendMessage(ChatColor.RED + "You do not belong to a team!");
			return;
		}
		
		if (team.getLeader() != player.getUniqueId()) {
			player.sendMessage(ChatColor.RED + "You are not the leader of your team!");
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "Finalizing team disband, do not log off!");
		team.disband();
	}
	
	public void invitePlayer(Player leader, String targetName, String element) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(leader);
		if (team == null) {
			leader.sendMessage(ChatColor.RED + "You do not have a team!");
			return;
		}
		if (team.getLeader().toString().equals(leader.getUniqueId().toString())) {
			leader.sendMessage(ChatColor.RED + "You are not the team leader!");
			return;
		}
		
		Player target = Bukkit.getPlayer(targetName);
		if (target == null) {
			leader.sendMessage(ChatColor.RED + "Player not found!");
			return;
		}
		
		if (leader == target) {
			leader.sendMessage(ChatColor.RED + "You cannot invite yourself!");
			return;
		}
		
		if (team.getMembers().containsKey(target.getUniqueId())) {
			leader.sendMessage(ChatColor.RED + "Player is already on your team!");
			return;
		}
		
		TeamMemberRole role = TeamMemberRole.parseRole(element);
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(target);
		
		if (!role.isEnabled()) {
			leader.sendMessage(ChatColor.RED + "That role is disabled!");
			return;
		}
		
		if (!bPlayer.hasElement(role.getElement())) {
			leader.sendMessage(ChatColor.RED + "Player does not have requested element!");
			return;
		}
		
		if (team.getMembers().values().contains(role)) {
			leader.sendMessage(ChatColor.RED + "The role requested is already taken!");
			return;
		}
		
		leader.sendMessage(ChatColor.GREEN + "Invitation sent!");
		Probending.get().getInviteManager().sendInvitation(target, team.getID(), role.toString());
	}
	
	public void attemptJoinTeam(Player joinee, final String teamName) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(joinee);
		if (team != null) {
			joinee.sendMessage(ChatColor.RED + "You must leave your current team before joining another." + ChatColor.GOLD + "/pb team leave");
			return;
		}
		
		team = Probending.get().getTeamManager().getTeamFromName(teamName);
		if (team == null) {
			joinee.sendMessage(ChatColor.RED + "No team with that name was found!");
			Probending.get().getInviteManager().notifyPlayer(joinee, true);
			return;
		}
		
		if (!Probending.get().getInviteManager().hasInvitations(joinee)) {
			joinee.sendMessage(ChatColor.RED + "You have no invitations!");
			return;
		}
		
		Invitation found = null;
		List<Invitation> invites = Probending.get().getInviteManager().getInvitations(joinee);
		for (Invitation invite : invites) {
			if (invite.getTeamID() == team.getID()) {
				found = invite;
				break;
			}
		}
		
		if (found == null) {
			joinee.sendMessage(ChatColor.RED + "You do not have an invitation to join that team!");
			return;
		}
		
		TeamMemberRole role = TeamMemberRole.parseRole(found.getRole());
		if (role == null) {
			joinee.sendMessage(ChatColor.RED + "The requested team role is not known!");
			return;
		}
		
		if (team.getMembers().values().contains(role)) {
			joinee.sendMessage(ChatColor.RED + "That team role has already been filled. Voiding your invitation.");
			Probending.get().getInviteManager().removeInvitation(joinee, found);
			return;
		}
		
		joinee.sendMessage(ChatColor.GREEN + "Finalizing team join, do not log off!");
		final UUID joineeUUID = joinee.getUniqueId();
		team.addPlayer(joinee, role, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				if (Bukkit.getPlayer(joineeUUID) == null) {
					return;
				}
				
				if (success) {
					Bukkit.getPlayer(joineeUUID).sendMessage(ChatColor.GREEN + "Successfully joined " + teamName);
				} else {
					Bukkit.getPlayer(joineeUUID).sendMessage(ChatColor.RED + "Unexpected error joining team! It will not be saved!");
				}
			}
			
		});
	}
	
	public void attemptLeaveTeam(Player leaver) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(leaver);
		if (team == null) {
			leaver.sendMessage(ChatColor.RED + "You do not belong to any team!");
			return;
		}
		
		leaver.sendMessage(ChatColor.GREEN + "Finalizing team leave, do not log off!");
		final PBTeam TEAM = team;
		final UUID uuid = leaver.getUniqueId();
		team.removePlayer(leaver, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				
				if (success) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully left " + TEAM.getTeamName());
				} else {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Unexpected error leaving team! It will not be saved!");
				}
			}
			
		});
	}
	
	public void attemptKickPlayer(Player kicker, final String targetName) {
		Player target = Bukkit.getPlayer(targetName);
		if (target == null) {
			kicker.sendMessage(ChatColor.RED + "Player not found!");
			return;
		}
		
		if (kicker == target) {
			kicker.sendMessage(ChatColor.RED + "You cannot kick yourself!");
			return;
		}
		
		final PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(kicker);
		if (team.getLeader() != kicker.getUniqueId()) {
			kicker.sendMessage(ChatColor.RED + "Only the leader can kick people!");
			return;
		}
		
		if (!team.getMembers().containsKey(target.getUniqueId())) {
			kicker.sendMessage(ChatColor.RED + "Player is not on your team!");
			return;
		}
		
		kicker.sendMessage(ChatColor.GREEN + "Finalizing player kick, do not log off!");
		final UUID uuid = kicker.getUniqueId();
		team.kickPlayer(target, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				
				if (success) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully kicked " + targetName);
				} else {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Unexpected error kicking player! It will not be saved!");
				}
			}
			
		});
	}
	
	public void changeTeamName(Player player, String name) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(player);
		if (team == null) {
			player.sendMessage(ChatColor.RED + "You do not lead any team!");
			return;
		}
		
		if (team.getLeader() != player.getUniqueId()) {
			player.sendMessage(ChatColor.RED + "You are not the leader of the team!");
			return;
		}
		
		PBTeam check = Probending.get().getTeamManager().getTeamFromName(name);
		if (check != null) {
			player.sendMessage(ChatColor.RED + "That name is already taken");
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "Finalizing changes! Do not log off!");
		final UUID uuid = player.getUniqueId();
		final String NAME = name;
		team.setName(name, new Runnable() {
			public void run() {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully changed team name to " + NAME);
			}
			
		});
	}
	
	public void changeTeamColor(Player player, String s, String colorName) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(player);
		if (team == null) {
			player.sendMessage(ChatColor.RED + "You do not lead any team!");
			return;
		}
		
		if (team.getLeader() != player.getUniqueId()) {
			player.sendMessage(ChatColor.RED + "You are not the leader of the team!");
			return;
		}
		
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			player.sendMessage(ChatColor.RED + "You need a number for the second argument, from 0 to 3.\n0 - helmet\n1 - chestplate\n2 - leggings\n3 - boots");
			return;
		}
		
		if (i < 0 || i > 3) {
			player.sendMessage("The number has to be between (including) 0 and 3.\n0 - helmet\n1 - chestplate\n2 - leggings\n3 - boots");
			return;
		}
		
		TeamColor color = TeamColor.parseColor(colorName);
		if (color == null) {
			player.sendMessage(ChatColor.RED + "That is not an acceptable color. Try: ");
			for (TeamColor display : TeamColor.values()) {
				player.sendMessage(display.getClosest() + "- " + display.toString().toLowerCase());
			}
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "Finalizing changes! Do not log off!");
		final int ii = i;
		final UUID uuid = player.getUniqueId();
		team.changeColor(i, color, new Runnable() {
			public void run() {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully changed color of position " + ii);
			}
			
		});
	}
	
	public void changeMemberRole(Player player, String targetName, String roleName) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromPlayer(player);
		if (team == null) {
			player.sendMessage(ChatColor.RED + "You do not lead any team!");
			return;
		}
		
		if (team.getLeader() != player.getUniqueId()) {
			player.sendMessage(ChatColor.RED + "You are not the leader of the team!");
			return;
		}
		
		Player target = Bukkit.getPlayer(targetName);
		if (target == null) {
			player.sendMessage(ChatColor.RED + "Player is not online, try again when they are!");
			return;
		}
		
		if (!team.getMembers().containsKey(target.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "That player is not part of your team!");
			return;
		}
		
		TeamMemberRole role = TeamMemberRole.parseRole(roleName);
		if (role == null) {
			player.sendMessage(ChatColor.RED + "The requested role could not be found!");
			return;
		}
		
		if (team.getMembers().get(target.getUniqueId()) == role) {
			player.sendMessage(ChatColor.RED + "The player already has that role!");
			return;
		}
		
		if (team.getMembers().values().contains(role)) {
			player.sendMessage(ChatColor.RED + "Someone already occupies that role!");
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "Finalizing changes! Do not log off!");
		if (player != target) {
			target.sendMessage(ChatColor.RED + "Your role is being switched, do not log off!");
		}
		
		final UUID targetUUID = target.getUniqueId();
		final UUID senderUUID = player.getUniqueId();
		team.setMemberRole(targetUUID, role, new Runnable() {
			public void run() {
				Player sender = Bukkit.getPlayer(senderUUID);
				Player target = Bukkit.getPlayer(targetUUID);
				
				if (sender != null) {
					sender.sendMessage(ChatColor.GREEN + "Successfully changed " + target.getName() + "'s role!");
				}
				
				if (target != null && target != sender) {
					sender.sendMessage(ChatColor.GREEN + sender.getName() + " successfully changed your role!");
				}
			}
		});
	}
	
	public void attemptTeamLookup(Player player, String name) {
		PBTeam team = Probending.get().getTeamManager().getTeamFromName(name);
		if (team == null) {
			player.sendMessage(ChatColor.RED + "Team not found!");
			return;
		}
		
		player.sendMessage(team.getColors()[1].getClosest() + team.getTeamName());
		String colors = "Colors: " + team.getColors()[0].toString();
		for (int i = 1; i < 4; i++) {
			colors += (", " + team.getColors()[i].toString());
		}
		player.sendMessage(colors);
		player.sendMessage("Rating: " + team.getRating());
		player.sendMessage("Wins: " + team.getWins());
		player.sendMessage("Games Played: " + team.getGamesPlayed());
		player.sendMessage("Members: ");
		for (UUID uuid : team.getMembers().keySet()) {
			OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(uuid);
			player.sendMessage("- " + oPlayer.getName() + ChatColor.AQUA + " (" + ChatColor.RESET + uuid.toString() + ChatColor.AQUA + ")");
		}
	}
}
