package com.projectkorra.probending.objects;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class PBGear {

    private ItemStack helmet, chestplate, leggings, boots;

    public PBGear(Color color) {
        this.helmet = getGear(Material.LEATHER_HELMET, color);
        this.chestplate = getGear(Material.LEATHER_CHESTPLATE, color);
        this.leggings = getGear(Material.LEATHER_LEGGINGS, color);
        this.boots = getGear(Material.LEATHER_BOOTS, color);
    }

    public PBGear(Color helmet, Color chestplate, Color leggins, Color boots) {
        this.helmet = getGear(Material.LEATHER_HELMET, helmet);
        this.chestplate = getGear(Material.LEATHER_CHESTPLATE, chestplate);
        this.leggings = getGear(Material.LEATHER_LEGGINGS, leggins);
        this.boots = getGear(Material.LEATHER_BOOTS, boots);
    }

    public void setHelmetColor(Color color) {
        helmet = getGear(Material.LEATHER_HELMET, color);
    }

    public void setChestplateColor(Color color) {
        chestplate = getGear(Material.LEATHER_CHESTPLATE, color);
    }

    public void setLeggingsColor(Color color) {
        leggings = getGear(Material.LEATHER_LEGGINGS, color);
    }

    public void setBootsColor(Color color) {
        boots = getGear(Material.LEATHER_BOOTS, color);
    }

    private ItemStack getGear(Material material, Color color) {
        ItemStack item;
        LeatherArmorMeta meta;
        if (!(material.equals(Material.LEATHER_BOOTS) || material.equals(Material.LEATHER_LEGGINGS) || material.equals(Material.LEATHER_CHESTPLATE) || material.equals(Material.LEATHER_HELMET))) {
            return null; //error
        }
        item = new ItemStack(material);
        meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
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
