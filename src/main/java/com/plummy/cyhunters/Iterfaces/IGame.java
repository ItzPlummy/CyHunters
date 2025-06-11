package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameState;
import com.plummy.cyhunters.Enums.GameStyle;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    IPlayerManager getPlayerManager();

    IScheduler getScheduler();

    IGameBoard getGameBoard();

    ILocationFinder getLocationFinder();

    ICameraManager getCameraManager();

    GameStyle getStyle();

    boolean hasStarted();

    boolean preparing();

    boolean handicap();

    boolean debuted();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();

    void start(Player startPlayer);

    void stop(GameEndingReason reason, Player player);
}
