package com.sky;

import com.sky.arenas.Arena;
import com.sky.kits.Kit;
import com.sky.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class GameManager {
    private SkyPlugin plugin;
    private Arena arena;
    private boolean gameActive;

    public GameManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.gameActive = false;
    }

    public void startGame(Arena arena) {
        this.arena = arena;
        this.gameActive = true;
        Bukkit.broadcastMessage("¡El juego de SkyWars ha comenzado!");
        // Teletransportar jugadores a sus puntos de aparición y darles sus kits
        List<Location> bedrockLocations = arena.getBedrockLocations();
        int index = 0;
        for (Team team : plugin.getTeams()) {
            for (Player player : team.getPlayers()) {
                if (index < bedrockLocations.size()) {
                    Location spawnLoc = bedrockLocations.get(index);
                    player.teleport(spawnLoc);
                    index++;
                } else {
                    player.sendMessage("No hay suficientes puntos de aparición en esta arena.");
                    player.teleport(arena.getSpawn1()); // Teletransportar a una ubicación alternativa
                }
                Kit kit = plugin.getPlayerKit(player);
                if (kit != null) {
                    player.getInventory().clear();
                    player.getInventory().addItem(kit.getItems());
                    kit.activatePowers(player); // Activar poderes del kit
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            TitleManager.sendTitle(player, "¡El juego de SkyWars ha comenzado!", "", 10, 40, 10);
        }
    }

    public void endGame() {
        this.gameActive = false;
        Bukkit.broadcastMessage("¡El juego de SkyWars ha terminado!");
        for (Player player : Bukkit.getOnlinePlayers()) {
            TitleManager.sendTitle(player, "¡El juego de SkyWars ha terminado!", "", 10, 40, 10);
        }
        // Limpiar el estado del juego
    }

    public void handlePlayerDeath(Player player) {
        player.sendMessage("¡Has sido eliminado!");
        // Quitar al jugador de su equipo
        Team team = getTeamOfPlayer(player);
        if (team != null) {
            team.removePlayer(player);
            if (team.getPlayers().isEmpty()) {
                Bukkit.broadcastMessage("El equipo " + team.getName() + " ha sido eliminado!");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    TitleManager.sendTitle(onlinePlayer, "Equipo Eliminado", "El equipo " + team.getName() + " ha sido eliminado", 10, 20, 10);
                }
            }
        }
        // Verificar si queda algún equipo con jugadores
        if (checkForWinningTeam()) {
            endGame();
            Team winningTeam = getWinningTeam();
            if (winningTeam != null) {
                for (Player winner : winningTeam.getPlayers()) {
                    plugin.getStatisticsManager().incrementGamesWon(winner);
                    plugin.getRewardManager().addPoints(winner, plugin.getWinReward());
                }
                Bukkit.broadcastMessage("¡El equipo " + winningTeam.getName() + " ha ganado la partida!");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    TitleManager.sendTitle(onlinePlayer, "Partida Terminada", "¡El equipo " + winningTeam.getName() + " ha ganado la partida!", 10, 20, 10);
                }
            }
        }

        // Otorgar puntos al asesino
        Player killer = player.getKiller();
        if (killer != null) {
            plugin.getStatisticsManager().incrementKills(killer);
            plugin.getRewardManager().addPoints(killer, plugin.getKillReward());
            Bukkit.broadcastMessage(killer.getName() + " ha eliminado a " + player.getName() + "!");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                TitleManager.sendTitle(onlinePlayer, "¡Eliminación!", killer.getName() + " ha eliminado a " + player.getName(), 10, 20, 10);
            }
        }
    }

    public void startCountdown(int seconds) {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int countdown = seconds;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage("El juego comienza en " + countdown + " segundos.");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        TitleManager.sendTitle(player, "§e" + countdown, "", 10, 20, 10);
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                    }
                    countdown--;
                } else {
                    Bukkit.getScheduler().cancelTasks(plugin);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        TitleManager.sendTitle(player, "§a¡Comienza!", "", 10, 20, 10);
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
                    }
                    startGame(arena);
                }
            }
        }, 0L, 20L); // 20 ticks = 1 segundo
    }

    public void autoStartGame(int minPlayers) {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (plugin.getGameManager().isGameActive()) {
                    return; // Si ya hay una partida activa, no hacer nada
                }
                int playerCount = 0;
                for (Team team : plugin.getTeams()) {
                    playerCount += team.getPlayers().size();
                }
                if (playerCount >= minPlayers) {
                    Arena arena = plugin.getRandomArena(); // Seleccionar una arena aleatoria
                    plugin.getGameManager().startCountdown(plugin.getGameCountdown());
                }
            }
        }, 0L, 20L * 30); // Verificar cada 30 segundos
    }

    private boolean checkForWinningTeam() {
        int teamsWithPlayers = 0;
        for (Team team : plugin.getTeams()) {
            if (!team.getPlayers().isEmpty()) {
                teamsWithPlayers++;
            }
        }
        return teamsWithPlayers <= 1;
    }

    private Team getWinningTeam() {
        for (Team team : plugin.getTeams()) {
            if (!team.getPlayers().isEmpty()) {
                return team;
            }
        }
        return null;
    }

    private Team getTeamOfPlayer(Player player) {
        for (Team team : plugin.getTeams()) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public boolean isGameActive() {
        return gameActive;
    }
}