package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IGamePlayer {
    ICamera getCamera();

    ICameraSelector getCameraSelector();

    Role getRole();

    void setRole(Role role);

    boolean isLeft();

    boolean isAlive();

    boolean isDead();

    boolean isSpectating();

    Player getPlayer();

    void setPlayer(Player player);

    void ready(Location location);

    void reset();

    void die();;
}
