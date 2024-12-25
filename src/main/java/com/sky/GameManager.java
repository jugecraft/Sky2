package com.sky;

import com.sky.arenas.Arena;
import com.sky.kits.Kit;
import com.sky.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        for (Team team : plugin.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.teleport(arena.getSpawn1());
                Kit kit = plugin.getPlayerKit(player);
                if (kit != null) {
                    player.getInventory().clear();
                    player.getInventory().addItem(kit.getItems());
                }
            }
        }
    }

    public void endGame() {
        this.gameActive = false;
        Bukkit.broadcastMessage("¡El juego de SkyWars ha terminado!");
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
            }
        }

        // Otorgar puntos al asesino
        Player killer = player.getKiller();
        if (killer != null) {
            plugin.getStatisticsManager().incrementKills(killer);
            plugin.getRewardManager().addPoints(killer, plugin.getKillReward());
            Bukkit.broadcastMessage(killer.getName() + " ha eliminado a " + player.getName() + "!");
        }
    }

    public void startCountdown(int seconds) {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int countdown = seconds;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage("El juego comienza en " + countdown + " segundos.");
                    countdown--;
                } else {
                    Bukkit.getScheduler().cancelTasks(plugin);
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