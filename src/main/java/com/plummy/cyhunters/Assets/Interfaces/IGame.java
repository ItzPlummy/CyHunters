package com.plummy.cyhunters.Assets.Interfaces;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IGame {
    IGameBoard getGameBoard();

    ILocationFinder getLocationFinder();

    ICameraManager getCameraManager();

    boolean hasStarted();

    boolean preparing();

    List<IGamePlayer> getAlivePlayers();

    IGamePlayer getPlayer(UUID uuid);

    boolean hasPlayer(UUID uuid);

    int size();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();

    void start(IGamePlayer startPlayer);

    void send(String message);

    void title(String title, String subtitle, int in, int hold, int out);

    void sound(Sound sound, float pitch);
}
