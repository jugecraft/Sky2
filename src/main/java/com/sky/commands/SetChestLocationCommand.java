package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetChestLocationCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public SetChestLocationCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 1) {
                String arenaName = args[0];
                Arena arena = plugin.getArenaByName(arenaName);
                if (arena != null) {
                    Location location = player.getLocation();
                    arena.addChestLocation(location);
                    player.sendMessage("Ubicación de cofre añadida a la arena " + arenaName);
                } else {
                    player.sendMessage("Arena no encontrada.");
                }
            } else {
                player.sendMessage("Por favor, especifica el nombre de la arena.");
            }
        }
        return true;
    }
}