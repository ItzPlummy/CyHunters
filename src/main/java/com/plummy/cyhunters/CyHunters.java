package com.plummy.cyhunters;

import com.plummy.cyhunters.Assets.*;
import com.plummy.cyhunters.Assets.Interfaces.IGame;
import com.plummy.cyhunters.Commands.CyHuntersCommand;
import com.plummy.cyhunters.Commands.CyHuntersCompleter;
import com.plummy.cyhunters.Listeners.CameraListener;
import com.plummy.cyhunters.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class CyHunters extends JavaPlugin {
    private static CyHunters instance;
    private static IGame mainGame;
    private static Logger logger;

    {
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        instance = this;

        logger.info("Enabling CyHunters...");

        logger.info("Creating and syncing game...");
        mainGame = new Game(
                new PlayerManager(),
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

        logger.info("CyHunters Enabled!");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            mainGame.getGameBoard().updateBoard();
        }, 0L, 10L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            mainGame.getCameraManager().rotateAllCameras();
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        logger().info("Disabling CyHunters...");

        logger().info("CyHunters Disabled!");
    }

    public static CyHunters getInstance() {
        return instance;
    }

    public static IGame getMainGame() {
        return mainGame;
    }

    public static Logger logger() {
        return logger;
    }
}
