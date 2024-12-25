package com.sky;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RewardManager {
    private SkyPlugin plugin;
    private Map<Player, Integer> playerPoints;
    private boolean doublePoints = false;

    public RewardManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.playerPoints = new HashMap<>();
    }

    public void addPoints(Player player, int points) {
        int currentPoints = playerPoints.getOrDefault(player, 0);
        int totalPoints = currentPoints + (doublePoints ? points * 2 : points);
        playerPoints.put(player, totalPoints);
        player.sendMessage("¡Has ganado " + (doublePoints ? points * 2 : points) + " puntos! Total: " + totalPoints);
    }

    public int getPoints(Player player) {
        return playerPoints.getOrDefault(player, 0);
    }

    public void resetPoints(Player player) {
        playerPoints.put(player, 0);
    }

    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }
}