package com.sky.commands;

import com.sky.SkyPlugin;
import com.sky.teams.Team;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private SkyPlugin plugin;
    private Location lobbyLocation;

    public JoinCommand(SkyPlugin plugin, Location lobbyLocation) {
        this.plugin = plugin;
        this.lobbyLocation = lobbyLocation;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String teamName = args[0];
                Team team = plugin.getTeamByName(teamName);
                if (team == null) {
                    team = new Team(teamName);
                    plugin.addTeam(team);
                }
                team.addPlayer(player);
                player.teleport(lobbyLocation); // Teletransportar al lobby
                player.sendMessage("Te has unido al equipo " + team.getName() + " y has sido teletransportado al lobby.");
            } else {
                player.sendMessage("Por favor, especifica el nombre del equipo.");
            }
        }
        return true;
    }
}
