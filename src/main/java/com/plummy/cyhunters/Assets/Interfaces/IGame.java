package com.plummy.cyhunters.Assets.Interfaces;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    IGameBoard getGameBoard();

    ILocationFinder getLocationFinder();

    boolean hasStarted();

    IGamePlayer getPlayer(UUID uuid);

    boolean hasPlayer(UUID uuid);

    int size();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();

    void start(IGamePlayer startPlayer);

    void send(String message);
}
