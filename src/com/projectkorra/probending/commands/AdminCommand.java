package com.projectkorra.probending.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.probending.PBMessenger;

public class AdminCommand extends PBCommand {

	public AdminCommand() {
		super("admin", "Shows the various admin commands!", "/probending admin", new String[] { "admin", "a" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!(sender instanceof Player)) {
			return;
		}

		Player player = (Player) sender;
		if (args.size() == 1) {
			switch (args.get(0).toLowerCase()) {
				case "2":
					PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin setSpawn [FIELD] [team] [point] " + ChatColor.YELLOW + "Set spawn!", false);
					PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin setFieldSpawn [FIELD] [team] [field]", false);
					PBMessenger.sendMessage(player, ChatColor.DARK_RED + "                 - " + ChatColor.YELLOW + "Set field spawn!", false);
					PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin setRegionName [FIELD] [team] [field] [NAME]", false);
					PBMessenger.sendMessage(player, ChatColor.DARK_RED + "                 - " + ChatColor.YELLOW + "Set region name!", false);
					PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin setKnockOff [FIELD] [NAME] " + ChatColor.YELLOW + "Set knockoff name!", false);
					PBMessenger.sendMessage(player, ChatColor.GREEN + "[FIELD]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "FieldNumber" + ChatColor.AQUA + " | " + ChatColor.GREEN + "[team]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "1 or 2" + ChatColor.AQUA + " | ", false);
					PBMessenger.sendMessage(player, ChatColor.GREEN + "[field]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "FieldPart 1 to 3" + ChatColor.AQUA + " | " + ChatColor.GREEN + "[NAME]" + ChatColor.WHITE + " = " + ChatColor.YELLOW + "RegionValue" + ChatColor.AQUA, false);
					return;
				case "createfield":
					if (!player.hasPermission("probending.command.arena.create")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().createField(player);
					return;
				case "list":
					if (!player.hasPermission("probending.command.arena.list")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().getFields(player);
					return;
			}
		} else if (args.size() == 2) {
			switch (args.get(0).toLowerCase()) {
				case "info":
					if (!player.hasPermission("probending.command.arena.info")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().getFieldInfo(player, args.get(1));
					return;
				case "delete":
					if (!player.hasPermission("probending.command.arena.delete")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().deleteField(player, args.get(1));
					return;
			}
		} else if (args.size() == 3) {
			switch (args.get(0).toLowerCase()) {
				case "setknockoff":
					if (!player.hasPermission("probending.command.arena.setknockoff")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().setKnockOffName(player, args.get(1), args.get(2));
					return;
			}
		} else if (args.size() == 4) {
			switch (args.get(0).toLowerCase()) {
				case "setspawn":
					if (!player.hasPermission("probending.command.arena.setspawn")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().setFieldSpawn(player, args.get(1), args.get(2), args.get(3));
					return;
				case "setfieldspawn":
					if (!player.hasPermission("probending.command.arena.setfieldspawn")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().setFieldRegionSpawn(player, args.get(1), args.get(2), args.get(3));
					return;

			}
		} else if (args.size() == 5) {
			switch (args.get(0).toLowerCase()) {
				case "setregionname":
					if (!player.hasPermission("probending.command.arena.setregionname")) {
						PBMessenger.sendMessage(player, PBMessenger.PBMessage.NOPERMS);
						return;
					}
					Commands.getFieldCreationManager().setFieldRegionName(player, args.get(1), args.get(2), args.get(3), args.get(4));
					return;
			}
		}
		PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin createField " + ChatColor.YELLOW + "Create a field!", false);
		PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin list " + ChatColor.YELLOW + "List all fields!", false);
		PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin info [FIELD] " + ChatColor.YELLOW + "Get info about a field!", false);
		PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin delete [FIELD] " + ChatColor.YELLOW + "Delete a field!", false);
		PBMessenger.sendMessage(player, ChatColor.GOLD + "/probending admin 2 " + ChatColor.YELLOW + "Page 2 of admin commands!", false);
	}
}
