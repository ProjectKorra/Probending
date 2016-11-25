package com.projectkorra.probending.commands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
		super("team", "Access and use the team commands!", "/pb team", new String[] {"team", "t"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command!");
		}
		
		Player player = (Player) sender;
		
		if (args.size() == 0) {
			List<String> help = Arrays.asList("&6/probending team create {name} {role} &eCreate a team and choose your role!",
					"&6/probending team invite {player} {role} &eInvite a player to your team for a certain role!",
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
		
		switch (args.get(0).toLowerCase()) {
			case "create":
				if (args.size() <= 3 || args.size() >= 8) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team create [color1] {color2} {color3} {color4}");
					return;
				}
				TeamColor[] colors = new TeamColor[] {};
				for (int i = 4; i <= 7; i++) {
					if (args.size() >= i) {
						colors[(i-4)] = (TeamColor.parseColor(args.get(i)) == null ? TeamColor.WHITE : TeamColor.parseColor(args.get(i)));
					}
				}
				
				for (int i = 1; i < 4; i++) {
					if (colors[i] == null) {
						colors[i] = colors[0];
					}
				}
				
				for (PBTeam team : Probending.get().getTeamManager().getTeamList()) {
					if (team.getColors() == colors) {
						player.sendMessage(ChatColor.RED + "Those colors are taken! Try a different combination!");
						return;
					}
				}
				
				createTeam(player, args.get(1), args.get(2), colors[0], colors[1], colors[2], colors[3]);
				return;
			case "invite":
				if (args.size() != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team invite [player] [element]");
					return;
				}
				invitePlayer(player, args.get(1), args.get(2));
				return;
			case "join":
				if (args.size() != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team join [teamName]");
					return;
				}
				attemptJoinTeam(player, args.get(1));
				return;
			case "leave":
				if (args.size() != 1) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team leave");
					return;
				}
				attemptLeaveTeam(player);
				return;
			case "kick":
				if (args.size() != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team kick [player]");
					return;
				}
				attemptKickPlayer(player, args.get(1));
				return;
			case "setname":
				if (args.size() != 2) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setname [name]");
					return;
				}
				changeTeamName(player, args.get(1));
				return;
			case "setcolor":
				if (args.size() != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setcolor [#] [color]");
					return;
				}
				changeTeamColor(player, args.get(1), args.get(2));
				return;
			case "setmemberrole":
				if (args.size() != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: /pb team setmemberrole [player] [element]");
					return;
				}
				changeMemberRole(player, args.get(1), args.get(2));
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
		Probending.get().getTeamManager().createNewTeam(leader, name, role, new Callback<Boolean>() {
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
		team.setName(name, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				
				if (success) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully changed team name to " + NAME);
				} else {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Unexpected error changing name! It will not be saved!");
				}
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
		
		TeamColor[] colors = team.getColors().clone();
		colors[i] = color;
		
		PBTeam check = Probending.get().getTeamManager().getTeamFromColors(colors);
		if (check != null) {
			player.sendMessage(ChatColor.RED + "That color combination is already taken!");
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "Finalzing changes! Do not log off!");
		final int ii = i;
		final UUID uuid = player.getUniqueId();
		team.changeColor(i, color, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				if (Bukkit.getPlayer(uuid) == null) {
					return;
				}
				
				if (success) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Successfully changed color of position " + ii);
				} else {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Unexpected error changing color. It will not be saved!");
				}
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
		team.setMemberRole(targetUUID, role, new Callback<Boolean>() {

			@Override
			public void run(Boolean success) {
				Player sender = Bukkit.getPlayer(senderUUID);
				Player target = Bukkit.getPlayer(targetUUID);
				
				if (sender != null) {
					if (success) {
						sender.sendMessage(ChatColor.GREEN + "Successfully changed " + target.getName() + "'s role!");
					} else {
						sender.sendMessage(ChatColor.RED + "Unexpected error changing role! It will not be saved!");
					}
				}
				
				if (target != null && target != sender) {
					if (success) {
						sender.sendMessage(ChatColor.GREEN + sender.getName() + " successfully changed your role!");
					} else {
						sender.sendMessage(ChatColor.RED + "");
					}
				}
			}
			
		});
	}
}
