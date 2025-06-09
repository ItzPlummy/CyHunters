package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.GameState;
import org.bukkit.entity.Player;

import java.util.*;

public class Game implements IGame {
    private GameState state;

    private final Map<UUID, IGamePlayer> players;

    public Game() {
        state = GameState.NOT_STARTED;

        players = new HashMap<>();
    }

    @Override
    public boolean hasStarted() {
        return state != GameState.NOT_STARTED;
    }

    @Override
    public IGamePlayer newPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        if (players.containsKey(uuid)) {
            IGamePlayer gamePlayer = players.get(uuid);

            gamePlayer.updatePlayer(player);
            return gamePlayer;
        }

        IGamePlayer gamePlayer = new GamePlayer(player);
        players.put(uuid, gamePlayer);
        return gamePlayer;
    }

    @Override
    public void removePlayer(UUID uuid) {
        if (!(players.containsKey(uuid))) {
            return;
        }

        players.remove(uuid);
    }
}
