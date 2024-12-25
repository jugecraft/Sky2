package com.sky;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class SkyScoreboardManager {
    private SkyPlugin plugin;

    public SkyScoreboardManager(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("SkyWars", "dummy");
        objective.setDisplayName(ChatColor.AQUA + "SkyWars");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Jugador
        Team playerName = board.registerNewTeam("playerName");
        playerName.addPlayer(player);
        objective.getScore(ChatColor.GREEN + "Jugador: " + player.getName()).setScore(5);

        // Nivel
        Team level = board.registerNewTeam("level");
        level.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        level.setPrefix(ChatColor.BLUE + "Nivel: ");
        // Deberás implementar el método getLevel en tu StatisticsManager
        level.setSuffix(String.valueOf(plugin.getStatisticsManager().getPlayerLevel(player)));
        objective.getScore(ChatColor.BLUE + "Nivel: " + plugin.getStatisticsManager().getPlayerLevel(player)).setScore(4);

        // Monedas
        Team coins = board.registerNewTeam("coins");
        coins.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD.toString()));
        coins.setPrefix(ChatColor.GOLD + "Monedas: ");
        coins.setSuffix(String.valueOf(plugin.getRewardManager().getPoints(player)));
        objective.getScore(ChatColor.GOLD + "Monedas: " + plugin.getRewardManager().getPoints(player)).setScore(3);

        // IP del servidor
        Team serverIP = board.registerNewTeam("serverIP");
        serverIP.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        serverIP.setPrefix(ChatColor.RED + "IP: ");
        serverIP.setSuffix("play.miServidor.com");
        objective.getScore(ChatColor.RED + "IP: play.miServidor.com").setScore(1);

        // Decoración
        objective.getScore(ChatColor.BOLD.toString() + "====================").setScore(6);

        player.setScoreboard(board);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();

        if (board.getObjective("SkyWars") == null) {
            createScoreboard(player);
            return;
        }

        board.getTeam("level").setSuffix(String.valueOf(plugin.getStatisticsManager().getPlayerLevel(player)));
        board.getTeam("coins").setSuffix(String.valueOf(plugin.getRewardManager().getPoints(player)));
    }
}