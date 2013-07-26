package com.etriacraft.probending;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
	
	Probending plugin;
	
	public SignListener(Probending plugin) {
		this.plugin = plugin;
	}
	
	public static String InvalidSign;
	public static String TeamSignCreated;
	
	public static Set<String> colors = new HashSet<String>();
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.isCancelled()) return;
		if (e.getPlayer() == null) return;
		Player p = e.getPlayer();
		String line1 = e.getLine(0);
		String teamColor = e.getLine(1);
		if (line1.equalsIgnoreCase("[Probending]") && !p.hasPermission("probending.team.sign.create")) {
			p.sendMessage(Commands.Prefix + Commands.noPermission);
			e.setCancelled(true);
			e.getBlock().breakNaturally();
			return;
		}
		if (line1.equalsIgnoreCase("[Probending]") && p.hasPermission("probending.team.sign.create")) {
			if (teamColor == null) {
				p.sendMessage(Commands.Prefix + InvalidSign);
				e.setCancelled(true);
				e.getBlock().breakNaturally();
			}
			
			if (!colors.contains(teamColor)) {
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				p.sendMessage(Commands.Prefix + "§cValid Colors: §a" + colors.toString());
				return;
			}
			
			p.sendMessage(Commands.Prefix + TeamSignCreated.replace("%color", teamColor));
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		
		if (block.getState() instanceof Sign) {
			Sign s = (Sign) block.getState();
			
			String line1 = s.getLine(0);
			if (line1.equalsIgnoreCase("[Probending]") && !player.hasPermission("probending.team.sign.break")) {
				e.setCancelled(true);
				block.breakNaturally();
				player.sendMessage(Commands.Prefix + Commands.noPermission);
				return;
			}
		}
	}


}
