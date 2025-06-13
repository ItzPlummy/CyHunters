package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameStyle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    IPlayerManager getPlayerManager();

    IScheduler getScheduler();

    IGameBoard getGameBoard();

    ILocationFinder getLocationFinder();

    ICameraManager getCameraManager();

    IKitCreator getKitCreator();

    GameStyle getStyle();

    boolean hasStarted();

    boolean locating();

    boolean preparing();

    boolean prepared();

    boolean handicap();

    boolean debut();

    boolean hunt();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();

    void start(Player startPlayer);

    void stop(Player stopPlayer, GameEndingReason reason);

    Location setLocatingStage(Player startPlayer);

    void setPreparingStage(Location location);

    void setHandicapStage(Long handicapTime);

    void setDebutStage(Long debutTime);

    void setHuntingStage();
}
