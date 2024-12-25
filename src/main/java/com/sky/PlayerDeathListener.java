package com.sky;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private SkyPlugin plugin;

    public PlayerDeathListener(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (plugin.getGameManager().isGameActive()) {
            plugin.getGameManager().handlePlayerDeath(event.getEntity());
        }
    }
}
