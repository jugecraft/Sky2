package com.sky;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerLevel {
    private SkyPlugin plugin;
    private Map<Player, Integer> playerLevels;
    private Map<Player, Integer> playerExperience;

    public PlayerLevel(SkyPlugin plugin) {
        this.plugin = plugin;
        this.playerLevels = new HashMap<>();
        this.playerExperience = new HashMap<>();
    }

    public int getLevel(Player player) {
        return playerLevels.getOrDefault(player, 1);
    }

    public int getExperience(Player player) {
        return playerExperience.getOrDefault(player, 0);
    }

    public void addExperience(Player player, int amount) {
        int currentExp = getExperience(player);
        int newExp = currentExp + amount;
        playerExperience.put(player, newExp);

        int currentLevel = getLevel(player);
        if (newExp >= getExperienceToNextLevel(currentLevel)) {
            playerLevels.put(player, currentLevel + 1);
            player.sendMessage("¡Has subido de nivel! Ahora eres nivel " + (currentLevel + 1));
        }
    }

    private int getExperienceToNextLevel(int level) {
        return level * 100; // Ejemplo: se necesitan 100 puntos de experiencia por nivel
    }
}