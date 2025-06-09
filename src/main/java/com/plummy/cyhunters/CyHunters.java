package com.plummy.cyhunters;

import com.plummy.cyhunters.Assets.Game;
import com.plummy.cyhunters.Assets.IGame;
import com.plummy.cyhunters.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        mainGame = new Game();
        mainGame.sync();
        logger.info("Game synced!");

        logger.info("Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        logger.info("Listeners registered!");

        logger.info("CyHunters Enabled!");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            mainGame.getGameBoard().updateBoard();
        }, 0L, 10L);
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
