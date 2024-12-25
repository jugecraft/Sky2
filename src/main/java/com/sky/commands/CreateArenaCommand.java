package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateArenaCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public CreateArenaCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Uso: /createarena <nombre>");
                return false;
            }

            String arenaName = args[0];

            Location min = new Location(player.getWorld(), 0, 64, 0);  // Coordenadas de inicio de la arena
            Location max = new Location(player.getWorld(), 100, 70, 100);  // Coordenadas de fin de la arena

            Arena arena = new Arena(arenaName, min, max);
            plugin.getArenas().add(arena);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                        for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                                Location loc = new Location(player.getWorld(), x, y, z);
                                if (loc.getBlock().getType() == Material.BEDROCK) {
                                    arena.addBedrockLocation(loc);
                                    // Crear caja de cristal alrededor de la bedrock
                                    createGlassBox(loc);
                                }
                            }
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "¡Arena " + arenaName + " creada con éxito!");
                }
            }.runTaskAsynchronously(plugin);

            return true;
        } else {
            sender.sendMessage("¡Este comando solo puede ser ejecutado por jugadores!");
            return false;
        }
    }

    private void createGlassBox(Location loc) {
        Location corner1 = loc.clone().add(-1, 0, -1);
        Location corner2 = loc.clone().add(1, 2, 1);

        for (int x = corner1.getBlockX(); x <= corner2.getBlockX(); x++) {
            for (int y = corner1.getBlockY(); y <= corner2.getBlockY(); y++) {
                for (int z = corner1.getBlockZ(); z <= corner2.getBlockZ(); z++) {
                    Location glassLoc = new Location(loc.getWorld(), x, y, z);
                    if (!glassLoc.equals(loc)) {
                        glassLoc.getBlock().setType(Material.GLASS);
                    }
                }
            }
        }
    }
}