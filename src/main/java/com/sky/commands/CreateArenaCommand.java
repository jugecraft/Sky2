package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArenaCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public CreateArenaCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String arenaName = args[0];
                Location spawn1 = player.getLocation();
                Location spawn2 = player.getLocation().clone().add(100, 0, 0); // Ejemplo
                Arena arena = new Arena(arenaName, spawn1, spawn2);
                plugin.getArenas().add(arena);
                player.sendMessage("Arena " + arenaName + " creada.");
            } else {
                player.sendMessage("Por favor, especifica el nombre de la arena.");
            }
        }
        return true;
    }
}