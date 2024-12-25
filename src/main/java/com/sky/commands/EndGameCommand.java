package com.sky.commands;

import com.sky.SkyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndGameCommand implements CommandExecutor {
    private SkyPlugin plugin;

    public EndGameCommand(SkyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.getGameManager().endGame();
            sender.sendMessage("El juego ha terminado.");
        }
        return true;
    }
}
