package com.plummy.cyhunters.Enums;

public enum GameStyle {
    NORMAL,
    BLITZ;

    public static GameStyle get(String style) {
        if (style.equals("BLITZ")) {
            return BLITZ;
        }
        return NORMAL;
    }
}
