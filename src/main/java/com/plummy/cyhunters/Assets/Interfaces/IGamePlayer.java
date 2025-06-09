package com.plummy.cyhunters.Assets.Interfaces;

import com.plummy.cyhunters.Assets.Enums.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IGamePlayer {
    Role getRole();

    void setRole(Role role);

    boolean isSpectating();

    boolean isOnline();

    boolean isAlive();

    boolean isDead();

    Player getPlayer();

    ICamera getCamera();

    ICameraHolder getCameraHolder();

    void setPlayer(Player player);

    void ready(Location location);

    void reset();

    void die();
}
