package com.sky;

import com.sky.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class MatchmakingManager {
    private SkyPlugin plugin;
    private List<Player> waitingPlayers;
    private boolean isMatchmakingEnabled;
    private int minPlayers;

    public MatchmakingManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.waitingPlayers = new ArrayList<>();
        this.isMatchmakingEnabled = true;
        this.minPlayers = plugin.getConfig().getInt("game.minPlayers");
        startMatchmaking();
    }

    public void addPlayerToQueue(Player player) {
        if (!waitingPlayers.contains(player)) {
            waitingPlayers.add(player);
            player.sendMessage("¡Te has unido a la cola de emparejamiento!");
        } else {
            player.sendMessage("Ya estás en la cola de emparejamiento.");
        }

        if (waitingPlayers.size() >= minPlayers) {
            startMatch();
        }
    }

    public void removePlayerFromQueue(Player player) {
        waitingPlayers.remove(player);
        player.sendMessage("Has salido de la cola de emparejamiento.");
    }

    private void startMatchmaking() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (isMatchmakingEnabled && waitingPlayers.size() >= minPlayers) {
                startMatch();
            }
        }, 0L, 100L); // Cada 5 segundos
    }

    private void startMatch() {
        List<Player> playersInMatch = new ArrayList<>(waitingPlayers.subList(0, minPlayers));
        waitingPlayers.removeAll(playersInMatch);
        plugin.getGameManager().startGame((Arena) playersInMatch);
    }
}