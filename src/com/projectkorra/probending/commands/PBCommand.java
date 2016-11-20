package com.projectkorra.probending.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.projectkorra.probending.managers.PBFieldCreationManager;
import com.projectkorra.probending.managers.ProbendingHandler;

public abstract class PBCommand implements PBSubCommand{
	
	public static Map<String, PBCommand> commands = new HashMap<>();
	
	private String[] aliases;
	private String description;
	private String name;
	private String properUse;
	
	public PBCommand(String name, String description, String properUse, String[] aliases) {
		this.name = name;
		this.description = description;
		this.properUse = properUse;
		this.aliases = aliases;
		
		commands.put(name.toLowerCase(), this);
	}

	@Override
	public String[] getAliases() {
		return aliases;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProperUse() {
		return properUse;
	}

	@Override
	public void help(CommandSender arg0, boolean arg1) {
	}

	@Override
	public abstract void execute(CommandSender sender, List<String> args);

	public static Map<String, PBCommand> getCommands() {
		return commands;
	}
}
