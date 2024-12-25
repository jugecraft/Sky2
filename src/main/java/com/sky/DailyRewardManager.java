package com.sky;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DailyRewardManager {
    private SkyPlugin plugin;
    private Map<UUID, Long> lastClaimed;

    public DailyRewardManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.lastClaimed = new HashMap<>();
    }

    public boolean canClaimReward(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        if (!lastClaimed.containsKey(playerUUID)) {
            return true;
        }
        long lastClaimedTime = lastClaimed.get(playerUUID);
        return (currentTime - lastClaimedTime) >= 24 * 60 * 60 * 1000; // 24 horas en milisegundos
    }

    public void claimReward(Player player) {
        if (canClaimReward(player)) {
            lastClaimed.put(player.getUniqueId(), System.currentTimeMillis());
            plugin.getRewardManager().addPoints(player, 100); // Ejemplo: 100 puntos diarios
            player.sendMessage("¡Has reclamado tu recompensa diaria de 100 puntos!");
        } else {
            player.sendMessage("¡Ya has reclamado tu recompensa diaria! Vuelve mañana.");
        }
    }
}