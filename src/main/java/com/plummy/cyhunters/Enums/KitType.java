package com.plummy.cyhunters.Enums;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public enum KitType {
    EMPTY,
    BASIC,
    BOW,
    SHEARS,
    OP,
    MACE;

    public static KitType getFromConfig() {
        String kit = Objects.requireNonNull(getInstance().getConfig().getString("settings.kit"));

        return switch (kit) {
            case "basic" -> KitType.BASIC;
            case "bow" -> KitType.BOW;
            case "shears" -> KitType.SHEARS;
            case "op" -> KitType.OP;
            case "mace" -> KitType.MACE;
            default -> KitType.EMPTY;
        };
    }
}
