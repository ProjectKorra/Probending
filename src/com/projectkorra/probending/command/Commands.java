package com.projectkorra.probending.command;

import com.projectkorra.probending.Probending;
import com.projectkorra.probending.command.arena.ArenaCreateCommand;
import com.projectkorra.probending.command.arena.ArenaDeleteCommand;
import com.projectkorra.probending.command.arena.ArenaListCommand;
import com.projectkorra.probending.command.arena.ArenaSetSpawnCommand;
import com.projectkorra.probending.command.arena.ArenaSetWorldCommand;
import com.projectkorra.probending.command.arena.ArenaSetZoneCommand;
import com.projectkorra.probending.command.round.StartCommand;
import com.projectkorra.probending.command.round.StopCommand;
import com.projectkorra.probending.command.team.AddLossCommand;
import com.projectkorra.probending.command.team.AddWinCommand;
import com.projectkorra.probending.command.team.CreateCommand;
import com.projectkorra.probending.command.team.DisbandCommand;
import com.projectkorra.probending.command.team.InfoCommand;
import com.projectkorra.probending.command.team.InviteCommand;
import com.projectkorra.probending.command.team.JoinCommand;
import com.projectkorra.probending.command.team.KickCommand;
import com.projectkorra.probending.command.team.ListCommand;
import com.projectkorra.probending.command.team.QuitCommand;
import com.projectkorra.probending.command.team.RenameCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Commands {

	private Probending plugin;
	
	// Integers
	public static int startingNumber;
	public static int currentNumber;
	public static int clockTask;
	// Booleans
	public static Boolean arenainuse;

	public static Set<Player> pbChat = new HashSet<Player>();
	public static HashMap<String, LinkedList<String>> teamInvites = new HashMap<String, LinkedList<String>>();
	public static HashMap<Player, ItemStack[]> tmpArmor = new HashMap<Player, ItemStack[]>();
	
	public static String[] arenaaliases = {"arena", "arenas", "a"};
	public static String[] teamaliases = {"team", "teams", "t"};
	public static String[] roundaliases = {"round", "r"};
	
	public Commands(Probending plugin) {
		this.plugin = plugin;
		init();
	}

	private void init() {
		PluginCommand pbcmd = plugin.getCommand("probending");

		//Base Commands
		new HelpCommand();
		new TeamCommand();
		new ArenaCommand();
		new RoundCommand();
		new ChatCommand();
		new ReloadCommand();
	
		//Arena Commands
		new ArenaDeleteCommand();
		new ArenaSetWorldCommand();
		new ArenaSetSpawnCommand();
		new ArenaSetZoneCommand();
		new ArenaListCommand();
		new ArenaCreateCommand();
		
		//Team Commands
		new AddWinCommand();
		new AddLossCommand();
		new CreateCommand();
		new QuitCommand();
		new DisbandCommand();
		new KickCommand();
		new JoinCommand();
		new InviteCommand();
		new ListCommand();
		new InfoCommand();
		new RenameCommand();

		//Round Commands
		new StartCommand();
		new StopCommand();

		/**
		 * Set of all of the Classes which extend Command
		 */

		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				for (int i = 0; i < args.length; i++) {
					args[i] = args[i];
				}

				if (args.length == 0) {
					new HelpCommand().execute(s, Arrays.asList(args));
					return true;
				}

				List<String> sendingArgs = Arrays.asList(args).subList(1, args.length);
				for (PBCommand command : PBCommand.instances.values()) {
					if (command.isChild() && args.length > 1) {
						if (Arrays.asList(command.getParentAliases()).contains(args[0].toLowerCase())
								&& Arrays.asList(command.getAliases()).contains(args[1].toLowerCase())) {
							command.execute(s, sendingArgs);
							return true;
						}
					}
					if (command.isParent() && args.length > 1) {
						if (!command.isNumeric(args[1]))
							continue;
					}

					if (Arrays.asList(command.getAliases()).contains(args[0].toLowerCase())) {
						command.execute(s, sendingArgs);
						return true;
					}
				}
				return true;
			}
		};
		pbcmd.setExecutor(exe);
	}
}
