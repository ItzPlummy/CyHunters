package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameEndingReason;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    IPlayerManager getPlayerManager();

    IScheduler getScheduler();

    IGameBoard getGameBoard();

    ILocationFinder getLocationFinder();

    ICameraManager getCameraManager();

    boolean hasStarted();

    boolean preparing();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();

    void start(Player startPlayer);

    void stop(GameEndingReason reason, Player player);
}
