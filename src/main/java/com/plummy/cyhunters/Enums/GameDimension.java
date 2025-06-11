package com.plummy.cyhunters.Enums;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public enum GameDimension {
    OVERWORLD,
    NETHER;

    public static GameDimension getFromConfig() {
        String dimension = Objects.requireNonNull(getInstance().getConfig().getString("settings.style"));

        if (dimension.equals("nether")) {
            return NETHER;
        } else {
            return OVERWORLD;
        }
    }
}
