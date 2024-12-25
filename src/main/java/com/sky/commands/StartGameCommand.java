package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.arenas.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public StartGameCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                String arenaName = args[0];
                Arena arena = plugin.getArenaByName(arenaName);
                if (arena != null) {
                    plugin.getGameManager().startCountdown(plugin.getGameCountdown()); // Iniciar cuenta regresiva
                    sender.sendMessage("El juego comenzará pronto en la arena " + arenaName);
                } else {
                    sender.sendMessage("Arena no encontrada.");
                }
            } else {
                sender.sendMessage("Por favor, especifica el nombre de la arena.");
            }
        }
        return true;
    }
}
