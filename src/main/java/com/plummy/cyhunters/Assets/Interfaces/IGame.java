package com.plummy.cyhunters.Assets.Interfaces;

import com.plummy.cyhunters.Assets.GameBoard;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IGame {
    GameBoard getGameBoard();

    boolean hasStarted();

    boolean hasPlayer(UUID uuid);

    int getPlayerCount();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void sync();
}
