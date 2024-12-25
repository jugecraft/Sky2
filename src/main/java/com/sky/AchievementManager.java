package com.sky;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AchievementManager {
    private SkyPlugin plugin;
    private Map<Player, Map<String, Boolean>> playerAchievements;

    public AchievementManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.playerAchievements = new HashMap<>();
    }

    public void grantAchievement(Player player, String achievement) {
        Map<String, Boolean> achievements = playerAchievements.getOrDefault(player, new HashMap<>());
        if (!achievements.getOrDefault(achievement, false)) {
            achievements.put(achievement, true);
            player.sendMessage("¡Has desbloqueado el logro: " + achievement + "!");
            playerAchievements.put(player, achievements);
            // Recompensar al jugador
            plugin.getRewardManager().addPoints(player, 100); // Ejemplo de recompensa
        }
    }

    public boolean hasAchievement(Player player, String achievement) {
        return playerAchievements.getOrDefault(player, new HashMap<>()).getOrDefault(achievement, false);
    }

    public Map<String, Boolean> getPlayerAchievements(Player player) {
        return playerAchievements.getOrDefault(player, new HashMap<>());
    }
}
