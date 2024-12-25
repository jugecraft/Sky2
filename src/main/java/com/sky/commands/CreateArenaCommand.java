package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create " + arenaName + " normal -g VoidGenerator"); // Crear un mundo vacío con Multiverse-Core

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    World newWorld = Bukkit.getWorld(arenaName);
                    if (newWorld != null) {
                        Location spawn1 = new Location(newWorld, 0, 64, 0); // Punto de aparición en el nuevo mundo
                        Location spawn2 = new Location(newWorld, 100, 64, 100); // Otro punto de aparición de ejemplo

                        Arena arena = new Arena(arenaName, spawn1, spawn2);
                        plugin.getArenas().add(arena);
                        player.sendMessage("Arena " + arenaName + " y mundo creados.");
                    } else {
                        player.sendMessage("Error al crear el mundo para la arena.");
                    }
                }, 100L); // Esperar unos ticks para asegurarse de que el mundo se ha creado
            } else {
                player.sendMessage("Por favor, especifica el nombre de la arena.");
            }
        }
        return true;
    }
}
