package com.plummy.cyhunters.Assets.Interfaces;

import com.plummy.cyhunters.Assets.Enums.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IGamePlayer {
    Role getRole();

    void setRole(Role role);

    boolean isSpectating();

    boolean isOnline();

    boolean isAlive();

    boolean isDead();

    Player getPlayer();

    ICamera getCamera();

    void setPlayer(Player player);

    void ready(Location location);

    void reset();

    void die();

    void setSpectateTargets(List<UUID> newTargets);

    void switchSpectateTarget();
}
