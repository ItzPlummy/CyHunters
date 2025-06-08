package com.plummy.cyhunters;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CyHunters extends JavaPlugin {
    private static CyHunters instance;
    private static Logger logger;

    {
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        instance = this;

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

    public Logger logger() {
        return logger;
    }
}
