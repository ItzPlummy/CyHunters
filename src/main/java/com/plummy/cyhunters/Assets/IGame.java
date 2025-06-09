package com.plummy.cyhunters.Assets;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    boolean hasStarted();

    boolean hasPlayer(UUID uuid);

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();
}
