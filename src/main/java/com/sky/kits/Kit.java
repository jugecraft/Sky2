package com.sky.kits;

import com.sky.powers.Power;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {
    private String name;
    private ItemStack[] items;
    private List<Power> powers;

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
        this.powers = powers;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public List<Power> getPowers() {
        return powers;
    }

    public void activatePowers(Player player) {
        for (Power power : powers) {
            power.activate(player);
        }
    }
}