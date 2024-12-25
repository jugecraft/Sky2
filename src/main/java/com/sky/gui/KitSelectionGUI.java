package com.sky.gui;

import com.sky.SkyPlugin;
import com.sky.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class KitSelectionGUI implements Listener {
    private SkyPlugin plugin;

    public KitSelectionGUI(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    public void openKitSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Selecciona tu Kit");

        int slot = 0;
        for (Kit kit : plugin.getKits()) {
            ItemStack item = new ItemStack(Material.CHEST);
            item.getItemMeta().setDisplayName(kit.getName());
            gui.setItem(slot++, item);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Selecciona tu Kit")) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String kitName = clickedItem.getItemMeta().getDisplayName();
        Kit kit = plugin.getKitByName(kitName);
        if (kit != null) {
            plugin.setPlayerKit(player, kit);
            player.sendMessage("Has seleccionado el kit " + kitName);
            player.closeInventory();
        }
    }
}