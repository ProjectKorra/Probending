package com.projectkorra.probending.command;

import java.util.List;

import org.bukkit.command.CommandSender;

public class ArenaCommand extends PBCommand {

	public ArenaCommand() {
		super("arena", "/probending arena", "Displays help for all Arena commands.", new String[] {"arena", "a"}, true);
	}
	
	@Override
	public void execute(CommandSender sender, List<String> args) {
		//TODO
	}
}
