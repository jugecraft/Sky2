package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.PlayerStatistics;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaderboardCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public LeaderboardCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Top 5 Jugadores:");
            plugin.getStatisticsManager().getTopPlayers(5).forEach((uuid, stats) -> {
                OfflinePlayer topPlayer = plugin.getServer().getOfflinePlayer(uuid);
                if (topPlayer != null && topPlayer.getName() != null) {
                    player.sendMessage(topPlayer.getName() + " - Juegos Ganados: " + stats.getGamesWon() + " - Eliminaciones: " + stats.getKills());
                }
            });
        }
        return true;
    }
}
