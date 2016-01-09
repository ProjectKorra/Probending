package com.projectkorra.probending.command;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.projectkorra.command.SubCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract representation of a command executor. Implements
 * {@link SubCommand}.
 *
 */
public abstract class PBCommand implements SubCommand {
	
	/**
	 * The full name of the command.
	 */
	private final String name;
	/**
	 * The proper use of the command, in the form '/probending {@link PBCommand#name
	 * name} arg1 arg2 ... '
	 */
	private final String properUse;
	/**
	 * A description of what the command does.
	 */
	private final String description;
	/**
	 * String[] of all possible aliases of the command.
	 */
	private final String[] aliases;
	/**
	 * Name of the parent command.
	 */
	private final String[] parentAliases;
	/**
	 * If the command has a parent command, i.e. /probending <parent> <child>
	 */
	private final boolean hasParent;
	/**
	 * If the command is a parenmt command, i.e. /probending <parent> <child>
	 */
	private final boolean isParent;
	/**
	 * List of all command executors which extends PBCommand
	 */
	public static Map<String, PBCommand> instances = new HashMap<String, PBCommand>();

	public PBCommand(String name, String properUse, String description, String[] aliases) {
		this(name, properUse, description, aliases, false, null);
	}
	
	public PBCommand(String name, String properUse, String description, String[] aliases, boolean isParent) {
		this.name = name;
		this.properUse = properUse;
		this.description = description;
		this.aliases = aliases;
		this.isParent = isParent;
		this.hasParent = false;
		this.parentAliases = null;
		instances.put(name, this);
	}
	
	public PBCommand(String name, String properUse, String description, String[] aliases, boolean hasParent, String[] parentAliases) {
		this.name = name;
		this.properUse = properUse;
		this.description = description;
		this.aliases = aliases;
		this.isParent = false;
		this.hasParent = hasParent;
		this.parentAliases = parentAliases;
		instances.put(name, this);
	}

	public String getName() {
		return name;
	}

	public String getProperUse() {
		return properUse;
	}

	public String getDescription() {
		return description;
	}

	public String[] getAliases() {
		return aliases;
	}
	
	public boolean isParent() {
		return isParent;
	}
	
	public boolean isChild() {
		return hasParent;
	}
	
	public String[] getParentAliases() {
		return parentAliases;
	}

	public void help(CommandSender sender, boolean description) {
		sender.sendMessage(ChatColor.GOLD + "Proper Usage: " + ChatColor.DARK_AQUA + properUse);
		if (description) {
			sender.sendMessage(ChatColor.YELLOW + this.description);
		}
	}

	/**
	 * Checks if the {@link CommandSender} has permission to execute the
	 * command. The permission is in the format 'probending.command.
	 * {@link PBCommand#name name}'. If not, they are told so.
	 * 
	 * @param sender The CommandSender to check
	 * @return True if they have permission, false otherwise
	 */
	protected boolean hasPermission(CommandSender sender) {
		if (sender.hasPermission("probending.command." + name)) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			return false;
		}
	}
	
	/**
	 * Checks if the {@link CommandSender} has permission to execute the
	 * command. The permission is in the format 'probending.command.team.
	 * {@link PBCommand#name name}'. If not, they are told so.
	 * 
	 * @param sender The CommandSender to check
	 * @return True if they have permission, false otherwise
	 */
	protected boolean hasTeamPermission(CommandSender sender) {
		if (sender.hasPermission("probending.command.team." + name.replace("team-", ""))) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			return false;
		}
	}
	
	/**
	 * Checks if the {@link CommandSender} has permission to execute the
	 * command. The permission is in the format 'probending.command.arena.
	 * {@link PBCommand#name name}'. If not, they are told so.
	 * @param sender The CommandSender to check
	 * @return True if they have permission, false otherwise.
	 */
	protected boolean hasArenaPermission(CommandSender sender) {
		if (sender.hasPermission("probending.command.arena." + name.replace("arena-", ""))) {
			return true;
		} else {
			sender.sendMessage(PBMethods.Prefix + PBMethods.noPermission);
			return false;
		}
	}
	
	/**
	 * Checks if the {@link CommandSender} has permission to execute the
	 * command. The permission is in the format 'probending.command.round.
	 * {@link PBCommand#name name}'. If not, they are told so.
	 * 
	 * @param sender The CommandSender to check
	 * @return True if they have permission, false otherwise
	 */
	protected boolean hasRoundPermission(CommandSender sender) {
		if (sender.hasPermission("probending.command.round." + name.replace("round-", ""))) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			return false;
		}
	}

	/**
	 * Checks if the {@link CommandSender} has permission to execute the
	 * command. The permission is in the format 'bending.command.
	 * {@link PBCommand#name name}.extra'. If not, they are told so.
	 * 
	 * @param sender The CommandSender to check
	 * @param extra The additional node to check
	 * @return True if they have permission, false otherwise
	 */
	protected boolean hasPermission(CommandSender sender, String extra) {
		if (sender.hasPermission("probending.command." + name + "." + extra)) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			return false;
		}
	}

	/**
	 * Checks if the argument length is within certain parameters, and if not,
	 * informs the CommandSender of how to correctly use the command.
	 * 
	 * @param sender The CommandSender who issued the command
	 * @param size The length of the arguments list
	 * @param min The minimum acceptable number of arguments
	 * @param max The maximum acceptable number of arguments
	 * @return True if min < size < max, false otherwise
	 */
	protected boolean correctLength(CommandSender sender, int size, int min, int max) {
		if (size < min || size > max) {
			help(sender, false);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if the CommandSender is an instance of a Player. If not, it tells
	 * them they must be a Player to use the command.
	 * 
	 * @param sender The CommandSender to check
	 * @return True if sender instanceof Player, false otherwise
	 */
	protected boolean isPlayer(CommandSender sender) {
		if (sender instanceof Player) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
			return false;
		}
	}

	/**
	 * Returns a boolean if the string provided is numerical.
	 * @param id
	 * @return boolean
	 */
	protected boolean isNumeric(String id) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(id, pos);
		return id.length() == pos.getIndex();
	}
	
	/**
	 * Returns a list for of commands for a page.
	 * @param entries
	 * @param title
	 * @param page
	 * @param alphabetical
	 * @return
	 */
	protected List<String> getPage(List<String> entries, String title, int page, boolean alphabetical) {
		List<String> strings = new ArrayList<String>();
		if (alphabetical) {
			Collections.sort(entries);
		}
		
		if (page < 1) {
			page = 1;
		}
		if ((page * 8) - 8 >= entries.size()) {
			page = Math.round(entries.size() / 8) + 1;
			if (page < 1) {
				page = 1;
			}
		}
		strings.add(ChatColor.GOLD + "Probending " + ChatColor.DARK_GRAY + "- [" + ChatColor.GRAY + page + "/" + (int) Math.ceil((entries.size()+.0)/(8+.0)) + ChatColor.DARK_GRAY + "]");
		strings.add(title);
		if (entries.size() > ((page * 8) - 8)) {
			for (int i = ((page * 8) - 8); i < entries.size(); i++) {
				if (entries.get(i) != null) {
					strings.add(entries.get(i).toString());
				}
				if (i >= (page * 8)-1) {
					break;
				}
			}
		}
		
		return strings;
	}
}
