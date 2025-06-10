package com.plummy.cyhunters.Iterfaces;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPlayer {
    UUID getUUID();

    Player getPlayer();

    void setPlayer(Player player);

    boolean isLeft();
}
