package com.sky;

import com.sky.arenas.Arena;
import com.sky.commands.*;
import com.sky.gui.KitSelectionGUI;
import com.sky.kits.Kit;
import com.sky.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SkyPlugin extends JavaPlugin {
    private List<Arena> arenas = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private List<Kit> kits = new ArrayList<>();
    private Map<Player, Kit> playerKits = new HashMap<>();
    private ChestManager chestManager;
    private GameManager gameManager;
    private RewardManager rewardManager;
    private StatisticsManager statisticsManager;
    private EventManager eventManager;
    private AchievementManager achievementManager;
    private SkyScoreboardManager scoreboardManager;
    private PlayerLevel playerLevel;  // Añadir PlayerLevel
    private DailyRewardManager dailyRewardManager;  // Añadir DailyRewardManager
    private EconomyManager economyManager;  // Añadir EconomyManager
    private ShopManager shopManager;  // Añadir ShopManager
    private Location lobbyLocation;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigurations();
        this.playerLevel = new PlayerLevel(this);  // Inicializar PlayerLevel
        this.dailyRewardManager = new DailyRewardManager(this);  // Inicializar DailyRewardManager
        this.economyManager = new EconomyManager(this);  // Inicializar EconomyManager
        this.shopManager = new ShopManager(this);  // Inicializar ShopManager
        getLogger().info("SkyWars ha sido habilitado");
        this.lobbyLocation = new Location(getServer().getWorld("world"), 0, 70, 0); // Definir la ubicación del lobby
        this.getCommand("join").setExecutor(new JoinCommand(this, lobbyLocation));
        this.getCommand("createarena").setExecutor(new CreateArenaCommand(this));
        this.getCommand("startgame").setExecutor(new StartGameCommand(this));
        this.getCommand("endgame").setExecutor(new EndGameCommand(this));
        this.getCommand("selectkit").setExecutor(new SelectKitCommand(this));
        this.getCommand("stats").setExecutor(new StatsCommand(this));
        this.getCommand("leaderboard").setExecutor(new LeaderboardCommand(this));
        this.getCommand("claimdailyreward").setExecutor(new ClaimDailyRewardCommand(this, dailyRewardManager));
        this.getCommand("buy").setExecutor(new BuyCommand(this, shopManager));
        this.chestManager = new ChestManager(this);
        this.gameManager = new GameManager(this);
        this.rewardManager = new RewardManager(this);
        this.statisticsManager = new StatisticsManager(this);
        this.achievementManager = new AchievementManager(this);
        this.eventManager = new EventManager(this);
        this.scoreboardManager = new SkyScoreboardManager(this);

        // Añadir ejemplo de arena
        arenas.add(new Arena("Arena1", new Location(getServer().getWorld("world"), 0, 64, 0), new Location(getServer().getWorld("world"), 100, 64, 100)));
        // Añadir ejemplos de kits
        kits.add(new Kit("Starter", new ItemStack[]{new ItemStack(Material.STONE_SWORD), new ItemStack(Material.APPLE, 5)}));
        kits.add(new Kit("Archer", new ItemStack[]{new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 32), new ItemStack(Material.LEATHER_HELMET)}));
        kits.add(new Kit("Warrior", new ItemStack[]{new ItemStack(Material.IRON_SWORD), new ItemStack(Material.IRON_CHESTPLATE)}));
        kits.add(new Kit("Miner", new ItemStack[]{new ItemStack(Material.IRON_PICKAXE), new ItemStack(Material.GOLDEN_APPLE, 3)}));

        // Registrar eventos del jugador
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        // Registrar eventos del GUI
        getServer().getPluginManager().registerEvents(new KitSelectionGUI(this), this);

        // Iniciar la verificación automática de partidas
        this.getGameManager().autoStartGame(getConfig().getInt("game.minPlayers", 4));

        // Activar eventos temporales
        eventManager.startLimitedTimeEvent();

        // Actualizar el scoreboard cada 5 segundos
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreboardManager.updateScoreboard(player);
                }
            }
        }, 0L, 100L); // 100 ticks = 5 segundos
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyWars ha sido deshabilitado");
    }

    private void loadConfigurations() {
        FileConfiguration config = getConfig();

        // Cargar opciones de configuración del juego
        int minPlayers = config.getInt("game.minPlayers");
        int countdown = config.getInt("game.countdown");
        int killReward = config.getInt("game.rewards.kill");
        int winReward = config.getInt("game.rewards.win");

        // Configurar ubicaciones del lobby
        lobbyLocation = new Location(
                Bukkit.getWorld(config.getString("lobby.location.world")),
                config.getDouble("lobby.location.x"),
                config.getDouble("lobby.location.y"),
                config.getDouble("lobby.location.z")
        );

        // Cargar configuraciones de arenas
        for (String arenaName : config.getConfigurationSection("arenas").getKeys(false)) {
            String worldName = config.getString("arenas." + arenaName + ".spawn1.world");
            double x1 = config.getDouble("arenas." + arenaName + ".spawn1.x");
            double y1 = config.getDouble("arenas." + arenaName + ".spawn1.y");
            double z1 = config.getDouble("arenas." + arenaName + ".spawn1.z");
            double x2 = config.getDouble("arenas." + arenaName + ".spawn2.x");
            double y2 = config.getDouble("arenas." + arenaName + ".spawn2.y");
            double z2 = config.getDouble("arenas." + arenaName + ".spawn2.z");

            Location spawn1 = new Location(Bukkit.getWorld(worldName), x1, y1, z1);
            Location spawn2 = new Location(Bukkit.getWorld(worldName), x2, y2, z2);

            arenas.add(new Arena(arenaName, spawn1, spawn2));
        }

        // Cargar configuraciones de kits
        for (String kitName : config.getConfigurationSection("kits").getKeys(false)) {
            List<ItemStack> items = new ArrayList<>();
            for (String item : config.getStringList("kits." + kitName)) {
                String[] parts = item.split(":");
                Material material = Material.valueOf(parts[0]);
                int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
                items.add(new ItemStack(material, amount));
            }
            kits.add(new Kit(kitName, items.toArray(new ItemStack[0])));
        }
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public Arena getArenaByName(String name) {
        for (Arena arena : arenas) {
            if (arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    public Arena getRandomArena() {
        Random rand = new Random();
        return arenas.get(rand.nextInt(arenas.size()));
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public List<Kit> getKits() {
        return kits;
    }

    public Kit getKitByName(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public void setPlayerKit(Player player, Kit kit) {
        playerKits.put(player, kit);
    }

    public Kit getPlayerKit(Player player) {
        return playerKits.get(player);
    }

    public ChestManager getChestManager() {
        return chestManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public SkyScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public PlayerLevel getPlayerLevel() {
        return playerLevel;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public int getKillReward() {
        return getConfig().getInt("game.rewards.kill");
    }

    public int getWinReward() {
        return getConfig().getInt("game.rewards.win");
    }

    public int getGameCountdown() {
        return getConfig().getInt("game.countdown");
    }

    public void sendWelcomeMessage(Player player) {
        player.sendMessage("¡Bienvenido a SkyWars, " + player.getName() + "!");
        player.sendMessage("Usa /join para unirte a un equipo y /selectkit para elegir tu kit.");
    }

    public boolean getEconomyManager() {
        return false;
    }
}