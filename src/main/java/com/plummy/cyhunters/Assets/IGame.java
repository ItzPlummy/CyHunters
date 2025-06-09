package com.plummy.cyhunters.Assets;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    boolean hasStarted();

    IGamePlayer newPlayer(Player player);

    void removePlayer(UUID uuid);
}
