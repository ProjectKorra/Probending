package com.projectkorra.probending.enums;

import net.md_5.bungee.api.ChatColor;

public enum TeamColor {
	
	BLACK(0, 0, 0, ChatColor.BLACK),
	WHITE(255, 255, 255, ChatColor.WHITE),
	
	RED(255, 0, 0, ChatColor.RED),
	DARK_RED(173, 0, 0, ChatColor.DARK_RED),
	
	GREEN(0, 255, 0, ChatColor.GREEN),
	DARK_GREEN(0, 173, 0, ChatColor.DARK_GREEN),
	
	BLUE(0, 0, 255, ChatColor.BLUE),
	DARK_BLUE(0, 0, 173, ChatColor.DARK_BLUE),
	
	YELLOW(255, 255, 0, ChatColor.YELLOW),
	DARK_YELLOW(173, 173, 0, ChatColor.GOLD),
	
	ORANGE(255, 157, 0, ChatColor.GOLD),
	DARK_ORANGE(222, 137, 0, ChatColor.GOLD),
	
	PURPLE(255, 0, 255, ChatColor.LIGHT_PURPLE),
	DARK_PURPLE(173, 0, 173, ChatColor.DARK_PURPLE),
	
	GRAY(173, 173, 173, ChatColor.GRAY),
	DARK_GRAY(105, 105, 105, ChatColor.DARK_GRAY),
	
	AQUA(0, 255, 255, ChatColor.AQUA),
	DARK_AQUA(0, 173, 173, ChatColor.DARK_AQUA);
	
	private int R;
	private int G;
	private int B;
	private ChatColor closest;

	private TeamColor(int R, int G, int B, ChatColor closest) {
		this.R = R;
		this.G = G;
		this.B = B;
		this.closest = closest;
	}
	
	public Integer[] getRGBColor() {
		return new Integer[] {R, G, B};
	}
	
	public int getRValue() {
		return R;
	}
	
	public int getGValue() {
		return G;
	}
	
	public int getBValue() {
		return B;
	}
	
	public ChatColor getClosest() {
		return closest;
	}
	
	public static TeamColor parseColor(String color) {
		return TeamColor.valueOf(color.toUpperCase());
	}
}
