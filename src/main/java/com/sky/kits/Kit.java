package com.sky.kits;

import org.bukkit.inventory.ItemStack;

public class Kit {
    private String name;
    private ItemStack[] items;

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }
}