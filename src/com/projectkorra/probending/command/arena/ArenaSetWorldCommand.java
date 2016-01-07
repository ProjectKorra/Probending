package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

public class ArenaSetWorldCommand extends PBCommand {

	public ArenaSetWorldCommand() {
		super ("setworld", "/pb arena setworld [Arena] [World]", "Sets the world an arena is located in.", new String[] {"setworld", "sw"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender arg0, List<String> arg1) {
		arg0.sendMessage("test");
	}
}
