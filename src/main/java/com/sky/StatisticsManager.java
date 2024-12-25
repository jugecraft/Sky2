package com.sky;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class StatisticsManager {
    private SkyPlugin plugin;
    private Map<UUID, PlayerStatistics> playerStatisticsMap;
    private File dataFile;
    private FileConfiguration dataConfig;

    public StatisticsManager(SkyPlugin plugin) {
        this.plugin = plugin;
        this.playerStatisticsMap = new HashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "statistics.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadStatistics();
    }

    public int getPlayerLevel(Player player) {
        PlayerStatistics stats = playerStatisticsMap.getOrDefault(player.getUniqueId(), new PlayerStatistics());
        return stats.getGamesWon(); // Ejemplo simple de nivel basado en juegos ganados
    }

    public void incrementGamesWon(Player player) {
        PlayerStatistics stats = playerStatisticsMap.getOrDefault(player.getUniqueId(), new PlayerStatistics());
        stats.incrementGamesWon();
        playerStatisticsMap.put(player.getUniqueId(), stats);
        saveStatistics(player);
    }

    public void incrementKills(Player player) {
        PlayerStatistics stats = playerStatisticsMap.getOrDefault(player.getUniqueId(), new PlayerStatistics());
        stats.incrementKills();
        playerStatisticsMap.put(player.getUniqueId(), stats);
        saveStatistics(player);
    }

    public PlayerStatistics getStatistics(Player player) {
        return playerStatisticsMap.getOrDefault(player.getUniqueId(), new PlayerStatistics());
    }

    private void saveStatistics(Player player) {
        PlayerStatistics stats = playerStatisticsMap.get(player.getUniqueId());
        dataConfig.set(player.getUniqueId().toString() + ".gamesWon", stats.getGamesWon());
        dataConfig.set(player.getUniqueId().toString() + ".kills", stats.getKills());
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStatistics() {
        for (String key : dataConfig.getKeys(false)) {
            PlayerStatistics stats = new PlayerStatistics();
            stats.setGamesWon(dataConfig.getInt(key + ".gamesWon"));
            stats.setKills(dataConfig.getInt(key + ".kills"));
            playerStatisticsMap.put(UUID.fromString(key), stats);
        }
    }

    public Map<UUID, PlayerStatistics> getTopPlayers(int limit) {
        return playerStatisticsMap.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().getGamesWon(), entry1.getValue().getGamesWon()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}