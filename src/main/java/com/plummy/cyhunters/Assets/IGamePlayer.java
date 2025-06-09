package com.plummy.cyhunters.Assets;

import org.bukkit.entity.Player;

public interface IGamePlayer {
    boolean isSpectating();

    boolean isOnline();

    Player getPlayer();

    void updatePlayer(Player player);
}
