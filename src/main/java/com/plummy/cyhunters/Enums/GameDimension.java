package com.plummy.cyhunters.Enums;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public enum GameDimension {
    OVERWORLD("Overworld"),
    NETHER("Nether");

    private final String name;

    GameDimension(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static GameDimension getFromConfig() {
        String dimension = Objects.requireNonNull(getInstance().getConfig().getString("settings.style"));

        if (dimension.equals("nether")) {
            return NETHER;
        } else {
            return OVERWORLD;
        }
    }
}
