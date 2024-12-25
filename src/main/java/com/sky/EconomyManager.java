package com.sky;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
    private SkyPlugin plugin;
    private Map<UUID, Integer> playerCoins;

    public EconomyManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.playerCoins = new HashMap<>();
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player.getUniqueId(), 0);
    }

    public void addCoins(Player player, int amount) {
        UUID playerUUID = player.getUniqueId();
        int currentCoins = getCoins(player);
        playerCoins.put(playerUUID, currentCoins + amount);
    }

    public boolean removeCoins(Player player, int amount) {
        UUID playerUUID = player.getUniqueId();
        int currentCoins = getCoins(player);
        if (currentCoins < amount) {
            return false; // No hay suficientes monedas
        }
        playerCoins.put(playerUUID, currentCoins - amount);
        return true;
    }
}
