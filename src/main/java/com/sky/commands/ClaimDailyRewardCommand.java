package com.sky.commands;

import com.sky.DailyRewardManager;
import com.sky.SkyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimDailyRewardCommand implements CommandExecutor {
    private SkyPlugin plugin;
    private DailyRewardManager dailyRewardManager;

    public ClaimDailyRewardCommand(SkyPlugin plugin, DailyRewardManager dailyRewardManager) {
        this.plugin = plugin;
        this.dailyRewardManager = dailyRewardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            dailyRewardManager.claimReward(player);
            return true;
        } else {
            sender.sendMessage("¡Este comando solo puede ser ejecutado por jugadores!");
            return false;
        }
    }
}