package com.sky;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import java.util.Random;

public class ChestManager {
    private SkyPlugin plugin;
    private Random random = new Random();

    public ChestManager(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    public void populateChest(Location location) {
        if (location.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) location.getBlock().getState();
            chest.getInventory().clear();

            // Ejemplo de ítems a añadir al cofre
            ItemStack[] items = new ItemStack[]{
                    new ItemStack(Material.STONE_SWORD),
                    new ItemStack(Material.APPLE, 5),
                    new ItemStack(Material.IRON_PICKAXE),
                    new ItemStack(Material.BOW),
                    new ItemStack(Material.ARROW, 32),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.GOLDEN_APPLE, 3)
            };

            // Añadir ítems al azar al cofre
            for (int i = 0; i < chest.getInventory().getSize(); i++) {
                if (random.nextBoolean()) {
                    chest.getInventory().setItem(i, items[random.nextInt(items.length)]);
                }
            }
        }
    }
}