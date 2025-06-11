package com.plummy.cyhunters.Enums;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public enum GameStyle {
    NORMAL("Normal"),
    BLITZ("Blitz");

    private final String name;

    GameStyle(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static GameStyle getFromConfig() {
        String style = Objects.requireNonNull(getInstance().getConfig().getString("settings.style"));

        if (style.equals("blitz")) {
            return BLITZ;
        } else {
            return NORMAL;
        }
    }
}
