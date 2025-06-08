package com.plummy.cyhunters;

import com.plummy.cyhunters.Assets.Game;
import com.plummy.cyhunters.Assets.IGame;
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
        mainGame = new Game();

        logger().info("Enabling CyHunters...");

        logger().info("CyHunters Enabled!");
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
