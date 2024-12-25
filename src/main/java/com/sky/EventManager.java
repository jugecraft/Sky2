package com.sky;

import org.bukkit.Bukkit;

public class EventManager {
    private SkyPlugin plugin;

    public EventManager(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    public void activateDoublePoints() {
        Bukkit.broadcastMessage("¡Evento Especial Activado: Doble Puntos por Eliminaciones!");
        plugin.getRewardManager().setDoublePoints(true);
    }

    public void deactivateDoublePoints() {
        Bukkit.broadcastMessage("¡Evento Especial Desactivado: Puntos Normales por Eliminaciones!");
        plugin.getRewardManager().setDoublePoints(false);
    }

    public void startLimitedTimeEvent() {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getEventManager().activateDoublePoints();
                Bukkit.getScheduler().runTaskLater(plugin, plugin.getEventManager()::deactivateDoublePoints, 20L * 60 * 10); // Desactivar después de 10 minutos
            }
        }, 0L, 20L * 60 * 60 * 24); // Evento cada 24 horas
    }
}
