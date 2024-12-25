package com.sky;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {
    private SkyPlugin plugin;
    private Map<String, Integer> shopItems;

    public ShopManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.shopItems = new HashMap<>();
        initializeShopItems();
    }

    private void initializeShopItems() {
        // Añadir objetos a la tienda y sus precios
        shopItems.put("DIAMOND_SWORD", 100);
        shopItems.put("IRON_HELMET", 50);
        shopItems.put("GOLDEN_APPLE", 30);
    }

    public boolean purchaseItem(Player player, String itemName) {
        if (!shopItems.containsKey(itemName)) {
            player.sendMessage("El objeto no existe en la tienda.");
            return false;
        }
        int price = shopItems.get(itemName);
        if (plugin.getEconomyManager()) {
            Material material = Material.valueOf(itemName);
            ItemStack item = new ItemStack(material);
            player.getInventory().addItem(item);
            player.sendMessage("Has comprado " + itemName + " por " + price + " monedas.");
            return true;
        } else {
            player.sendMessage("No tienes suficientes monedas para comprar " + itemName + ".");
            return false;
        }
    }
}