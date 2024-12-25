package com.sky.commands;

import com.sky.ShopManager;
import com.sky.SkyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyCommand implements CommandExecutor {
    private SkyPlugin plugin;
    private ShopManager shopManager;

    public BuyCommand(SkyPlugin plugin, ShopManager shopManager) {
        this.plugin = plugin;
        this.shopManager = shopManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("¡Este comando solo puede ser ejecutado por jugadores!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("Por favor, especifica el nombre del objeto que deseas comprar.");
            return true;
        }
        Player player = (Player) sender;
        String itemName = args[0].toUpperCase();
        shopManager.purchaseItem(player, itemName);
        return true;
    }
}