package com.projectkorra.probending.objects;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.projectkorra.probending.enums.TeamColor;

public class PBGear {

	private ItemStack helmet, chestplate, leggings, boots;

	public PBGear(TeamColor color) {
		this.helmet = getGear(Material.LEATHER_HELMET, color);
		this.chestplate = getGear(Material.LEATHER_CHESTPLATE, color);
		this.leggings = getGear(Material.LEATHER_LEGGINGS, color);
		this.boots = getGear(Material.LEATHER_BOOTS, color);
	}

	public PBGear(TeamColor helmet, TeamColor chestplate, TeamColor leggins, TeamColor boots) {
		this.helmet = getGear(Material.LEATHER_HELMET, helmet);
		this.chestplate = getGear(Material.LEATHER_CHESTPLATE, chestplate);
		this.leggings = getGear(Material.LEATHER_LEGGINGS, leggins);
		this.boots = getGear(Material.LEATHER_BOOTS, boots);
	}

	public void setHelmetTeamColor(TeamColor color) {
		helmet = getGear(Material.LEATHER_HELMET, color);
	}

	public void setChestplateTeamColor(TeamColor color) {
		chestplate = getGear(Material.LEATHER_CHESTPLATE, color);
	}

	public void setLeggingsTeamColor(TeamColor color) {
		leggings = getGear(Material.LEATHER_LEGGINGS, color);
	}

	public void setBootsTeamColor(TeamColor color) {
		boots = getGear(Material.LEATHER_BOOTS, color);
	}

	private ItemStack getGear(Material material, TeamColor color) {
		ItemStack item;
		LeatherArmorMeta meta;
		if (!(material.equals(Material.LEATHER_BOOTS) || material.equals(Material.LEATHER_LEGGINGS) || material.equals(Material.LEATHER_CHESTPLATE) || material.equals(Material.LEATHER_HELMET))) {
			return null; //error
		}
		item = new ItemStack(material);
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(color.getRValue(), color.getGValue(), color.getBValue()));
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack Helmet() {
		return helmet;
	}

	public ItemStack Chestplate() {
		return chestplate;
	}

	public ItemStack Leggings() {
		return leggings;
	}

	public ItemStack Boots() {
		return boots;
	}
}
