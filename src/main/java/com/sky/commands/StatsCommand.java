package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.PlayerStatistics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public StatsCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                PlayerStatistics stats = plugin.getStatisticsManager().getStatistics(player);
                player.sendMessage("Tus estadísticas: ");
                player.sendMessage("Juegos Ganados: " + stats.getGamesWon());
                player.sendMessage("Eliminaciones: " + stats.getKills());
            } else if (args.length == 1) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target != null) {
                    PlayerStatistics stats = plugin.getStatisticsManager().getStatistics(target);
                    player.sendMessage("Estadísticas de " + target.getName() + ": ");
                    player.sendMessage("Juegos Ganados: " + stats.getGamesWon());
                    player.sendMessage("Eliminaciones: " + stats.getKills());
                } else {
                    player.sendMessage("Jugador no encontrado.");
                }
            } else {
                player.sendMessage("Uso: /stats [jugador]");
            }
        }
        return true;
    }
}