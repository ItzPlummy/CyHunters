package com.plummy.cyhunters.Assets.Interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IPlayerManager {
    List<IGamePlayer> getPlayers();

    IGamePlayer getPlayer(UUID uuid);

    boolean hasPlayer(UUID uuid);

    int size();

    void joinPlayer(Player player, boolean hasStarted);

    void leavePlayer(UUID uuid, boolean hasStarted);

    void syncPlayers(boolean hasStarted);

    void ready(Location location);
}
