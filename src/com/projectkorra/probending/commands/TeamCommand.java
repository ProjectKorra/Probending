package com.projectkorra.probending.commands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.libraries.database.Callback;
import com.projectkorra.probending.managers.FieldCreationManager;
import com.projectkorra.probending.managers.ProbendingHandler;
import com.projectkorra.probending.objects.PBTeam;
import com.projectkorra.probending.objects.PBTeam.TeamMemberRole;
import com.projectkorra.projectkorra.BendingPlayer;

import net.md_5.bungee.api.ChatColor;

public class TeamCommand extends PBCommand{

	public TeamCommand() {
		super("team", "Access and use the team commands!", "/pb team", new String[] {"team", "t"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args, ProbendingHandler pHandler, FieldCreationManager cManager) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command!");
		}
		
		Player player = (Player) sender;
		
		if (args.size() == 0) {
			List<String> help = Arrays.asList("&6/probending team create {name} {role} &eCreate a team and choose your role!",
					"&6/probending team invite {player} {role} &eInvite a player to your team for a certain role!");
			
			for (String s : help) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			}
			return;
		}
		
		switch (args.get(0).toLowerCase()) {
			case "create":
				if (args.size() != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: " + getProperUse());
					return;
				}
				createTeam(player, args.get(1), args.get(2)/*, Integer.parseInt(args.get(3)), Integer.parseInt(args.get(4)), Integer.parseInt(args.get(5))*/);
				return;
			case "invite":
				if (args.size() != 3) {
					player.sendMessage(ChatColor.RED + "Incorrect arguments. Try: " + getProperUse());
					return;
				}
				invitePlayer(player, args.get(1), args.get(2));
				return;
		}
	}

	public void createTeam(Player leader, String name, String element/*, int r, int g, int b*/) {
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
			leader.sendMessage(ChatColor.RED + "Unknown team role, try one of these:\n- Earth\n- Fire\n-Water");
			return;
		}
		
		final UUID leaderUUID = leader.getUniqueId();
		Probending.get().getTeamManager().createNewTeam(leader, name, role, new Callback<Boolean>() {
			public void run(Boolean success) {
				if (Bukkit.getPlayer(leaderUUID) == null) {
					return;
				}
				if (success) {
					Bukkit.getPlayer(leaderUUID).sendMessage(ChatColor.GREEN + "Team successfully created!");
				} else {
					Bukkit.getPlayer(leaderUUID).sendMessage(ChatColor.RED + "Error creating team!");
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
		
		leader.sendMessage(ChatColor.GREEN + "Sending them an invite!");
		Probending.get().getInviteManager().sendInvitation(target, team.getTeamName(), role.toString());
	}
}
