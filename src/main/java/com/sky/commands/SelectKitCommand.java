package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.kits.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectKitCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public SelectKitCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String kitName = args[0];
                Kit kit = plugin.getKitByName(kitName);
                if (kit != null) {
                    plugin.setPlayerKit(player, kit);
                    player.sendMessage("Has seleccionado el kit " + kitName);
                } else {
                    player.sendMessage("Kit no encontrado.");
                }
            } else {
                player.sendMessage("Por favor, especifica el nombre del kit.");
            }
        }
        return true;
    }
}
