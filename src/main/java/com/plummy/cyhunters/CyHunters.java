package com.plummy.cyhunters;

import com.plummy.cyhunters.Camera.CameraManager;
import com.plummy.cyhunters.Commands.CyHuntersCommand;
import com.plummy.cyhunters.Commands.CyHuntersCompleter;
import com.plummy.cyhunters.Game.Game;
import com.plummy.cyhunters.Game.GameBoard;
import com.plummy.cyhunters.Game.ItemManager;
import com.plummy.cyhunters.Game.LocationFinder;
import com.plummy.cyhunters.Iterfaces.IGame;
import com.plummy.cyhunters.Listeners.CameraListener;
import com.plummy.cyhunters.Listeners.PlayerListener;
import com.plummy.cyhunters.Player.PlayerManager;
import com.plummy.cyhunters.Scheduler.GameScheduler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class CyHunters extends JavaPlugin {
    private static final NamespacedKey namespacedKey = Objects.requireNonNull(NamespacedKey.fromString("cyhunters"));

    private static CyHunters instance;
    private static IGame mainGame;
    private static Logger logger;

    {
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        instance = this;

        logger.info("Enabling Plugin...");

        if (!getDataFolder().exists()) {
            logger().info("Creating plugin folder...");
            if (getDataFolder().mkdir()) {
               logger().info("Plugin folder created successfully!");
            } else {
                logger.warning("Plugin folder was not created, which may cause issues.");
            }
        }
        saveDefaultConfig();
        reloadConfig();

        logger.info("Creating and syncing game...");
        mainGame = new Game(
                new PlayerManager(),
                new GameScheduler(),
                new GameBoard(),
                new LocationFinder(),
                new CameraManager()
        );
        mainGame.sync();
        logger.info("Game synced!");

        logger.info("Registering commands...");
        Objects.requireNonNull(getCommand("cyhunters")).setExecutor(new CyHuntersCommand());
        Objects.requireNonNull(getCommand("cyhunters")).setTabCompleter(new CyHuntersCompleter());
        logger.info("Commands registered!");

        logger.info("Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), instance);
        Bukkit.getPluginManager().registerEvents(new CameraListener(), instance);
        logger.info("Listeners registered!");

        logger.info("Plugin Enabled!");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            mainGame.getGameBoard().updateBoard();
        }, 0L, 10L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            mainGame.getCameraManager().rotateCameras();
            ItemManager.updateCompasses();
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        logger().info("Disabling Plugin...");

        logger().info("Disabling Schedulers...");
        Bukkit.getScheduler().cancelTasks(instance);
        getMainGame().getScheduler().stop();
        logger().info("Schedulers Disabled!");

        logger().info("Plugin Disabled!");
    }

    public static CyHunters getInstance() {
        return instance;
    }

    public static IGame getMainGame() {
        return mainGame;
    }

    public static NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public static Logger logger() {
        return logger;
    }
}
