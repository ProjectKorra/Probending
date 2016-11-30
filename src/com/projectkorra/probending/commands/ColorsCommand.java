package com.projectkorra.probending.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.projectkorra.probending.enums.TeamColor;

import net.md_5.bungee.api.ChatColor;

public class ColorsCommand extends PBCommand {

	public ColorsCommand() {
		super("colors", "Lists the colors for teams and gear! Selecting a color will show more about it", "/probending colors {color}", new String[] { "colors", "color", "c", "colours", "colour" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (args.size() == 0) {
			sender.sendMessage("--- Colors ---");
			for (TeamColor color : TeamColor.values()) {
				sender.sendMessage(color.getClosest() + "- " + color.toString().toLowerCase());
			}
		} else if (args.size() == 1) {
			TeamColor color = TeamColor.parseColor(args.get(0));
			if (color == null) {
				sender.sendMessage(ChatColor.RED + "Color not found!");
				for (TeamColor color1 : TeamColor.values()) {
					sender.sendMessage(color1.getClosest() + "- " + color1.toString().toLowerCase());
				}
				return;
			}

			sender.sendMessage(colorInformation(color));
		} else {
			sender.sendMessage(ChatColor.RED + "Incorrect arguments. Try: " + getProperUse());
			return;
		}
	}

	public String colorInformation(TeamColor color) {
		StringBuilder sb = new StringBuilder(color.getClosest() + "--- " + color.toString().toLowerCase() + " ---");
		sb.append(ChatColor.WHITE + "\n- RGB: " + ChatColor.RED + color.getRValue() + ChatColor.RESET + ", " + ChatColor.GREEN + color.getGValue() + ChatColor.RESET + ", " + ChatColor.BLUE + color.getBValue());
		sb.append(color.getClosest() + "\n- Closest chat color: " + color.getClosest().name());
		return sb.toString();
	}
}
