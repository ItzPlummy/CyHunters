package com.plummy.cyhunters.Iterfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IPlayerManager {
    IPlayer getPlayer(UUID uuid);

    boolean hasPlayer(UUID uuid);

    List<IPlayer> getPlayers();

    List<IHunter> getHunters();

    ISpeedrunner getSpeedrunner();

    int size();

    void setPlayers(List<Player> players);

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void syncPlayers(boolean hasStarted);

    void ready(Location location);
}
