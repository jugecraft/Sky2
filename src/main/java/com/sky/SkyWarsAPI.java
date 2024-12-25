package com.sky;

import org.bukkit.entity.Player;

public class SkyWarsAPI {
    private SkyPlugin plugin;

    public SkyWarsAPI(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    public void joinPlayerToGame(Player player) {
        // Lógica para unir al jugador a una partida
    }

    public PlayerStatistics getPlayerStatistics(Player player) {
        return plugin.getStatisticsManager().getStatistics(player);
    }

    // Otros métodos de la API
}
