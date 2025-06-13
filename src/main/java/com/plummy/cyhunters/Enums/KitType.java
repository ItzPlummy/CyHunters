package com.plummy.cyhunters.Enums;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.config;

public enum KitType {
    EMPTY("Empty"),
    BASIC("Basic"),
    BOW("Bow"),
    SHEARS("Shears"),
    OP("OP"),
    MACE("Mace");

    private final String name;

    KitType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static KitType getFromConfig() {
        String kit = Objects.requireNonNull(config().getString("settings.kit"));

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
