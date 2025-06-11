package com.plummy.cyhunters.Iterfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IPlayerManager {
    IPlayer getPlayer(UUID uuid);

    boolean hasPlayer(UUID uuid);

    List<IPlayer> getPlayers();

    List<IPlayer> getOnlinePlayers();

    List<IPlayer> getActivePlayers();

    List<IHunter> getHunters();

    List<ISpectator> getSpectators();

    ISpeedrunner getSpeedrunner();

    int size();

    void setPlayers(List<Player> players);

    void resetPlayers();

    void joinPlayer(Player player);

    void leavePlayer(UUID uuid);

    void syncPlayers();

    void ready(Location location);
}
