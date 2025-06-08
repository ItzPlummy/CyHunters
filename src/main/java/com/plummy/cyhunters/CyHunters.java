package com.plummy.cyhunters;

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

    public CyHunters getInstance() {
        return instance;
    }

    public IGame getMainGame() {
        return mainGame;
    }

    public Logger logger() {
        return logger;
    }
}
