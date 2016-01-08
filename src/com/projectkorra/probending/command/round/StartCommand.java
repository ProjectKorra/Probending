package com.projectkorra.probending.command.round;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Arena;
import com.projectkorra.probending.objects.Round;
import com.projectkorra.probending.objects.Team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StartCommand extends PBCommand {

	public StartCommand() {
		super ("round-start", "/pb round start <Team 1> <Team 2> <Arena>", "Starts round.", new String[] {"start"}, true, Commands.roundaliases);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasRoundPermission(sender) || !correctLength(sender, args.size(), 4, 4)) {
			return;
		}

		Team team1 = PBMethods.getTeam(args.get(1));
		Team team2 = PBMethods.getTeam(args.get(2));
		Arena arena = PBMethods.getArena(args.get(3));

		if (team1 == null || team2 == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.TeamDoesNotExist);
			return;
		}

		if (team1 == team2) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "The same team cannot play against itself.");
			return;
		}

		if (arena == null) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "That team does not exist.");
			return;
		}

		int minSize = Probending.plugin.getConfig().getInt("TeamSettings.MinTeamSize");

		if (team1.getOnlinePlayers().size() < minSize || team2.getOnlinePlayers().size() < minSize) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.InvalidTeamSize);
			return;
		}

		if (PBMethods.isRoundAtArena(arena)) {
			sender.sendMessage(PBMethods.Prefix + ChatColor.RED + "There is already an ongoing round at " + args);
		}

		Round round = new Round(team1, team2, team1.getOnlinePlayers(), team2.getOnlinePlayers(), arena);
		for (Player player: team1.getOnlinePlayers()) {
			Color teamColor = arena.getTeamOneColor();
			Commands.pbChat.add(player);
			player.teleport(arena.getTeamOneSpawn());
			Commands.tmpArmor.put(player, player.getInventory().getArmorContents()); // Backs up their armor.
			ItemStack armor1 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_HELMET), teamColor);
			ItemStack armor2 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), teamColor);
			ItemStack armor3 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), teamColor);
			ItemStack armor4 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), teamColor);
			player.getInventory().setHelmet(armor1);
			player.getInventory().setChestplate(armor2);
			player.getInventory().setLeggings(armor3);
			player.getInventory().setBoots(armor4);
			round.setAllowedZone(player, arena.getTeamOneZoneOne());
		}
		for (Player player: team2.getOnlinePlayers()) {
			Color teamColor = arena.getTeamTwoColor();
			Commands.pbChat.add(player);
			player.teleport(arena.getTeamTwoSpawn());
			Commands.tmpArmor.put(player, player.getInventory().getArmorContents()); // Backs up their armor.
			ItemStack armor1 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_HELMET), teamColor);
			ItemStack armor2 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_CHESTPLATE), teamColor);
			ItemStack armor3 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_LEGGINGS), teamColor);
			ItemStack armor4 = PBMethods.createColorArmor(new ItemStack(Material.LEATHER_BOOTS), teamColor);
			player.getInventory().setHelmet(armor1);
			player.getInventory().setChestplate(armor2);
			player.getInventory().setLeggings(armor3);
			player.getInventory().setBoots(armor4);
			round.setAllowedZone(player, arena.getTeamTwoZoneOne());
		}
		for (Player player: Bukkit.getOnlinePlayers()) {
			if (!round.getRoundPlayers().contains(player)) {
				if (PBMethods.RegionsAtLocation(player.getLocation()) != null && PBMethods.RegionsAtLocation(player.getLocation()).contains(arena.getField())) {
					player.teleport(arena.getSpectatorSpawn());
					player.sendMessage(PBMethods.Prefix + ChatColor.RED + "A round is starting at this arena shortly. You may not be on the field.");
				}
			}

		}
	}
}