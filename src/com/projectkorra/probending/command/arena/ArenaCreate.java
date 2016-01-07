package com.projectkorra.probending.command.arena;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;

public class ArenaCreate extends PBCommand {
	
	public ArenaCreate() {
		super ("arenacreate", "/pb arena create [Arena]", "Creates a new arena.", new String[] {"create", "c"}, true, Commands.arenaaliases);
	}

	@Override
	public void execute(CommandSender arg0, List<String> arg1) {
		arg0.sendMessage("test");
	}
}

