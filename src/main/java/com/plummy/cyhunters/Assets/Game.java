package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.GameState;
import com.plummy.cyhunters.Assets.Enums.PlayerState;
import com.plummy.cyhunters.Assets.Interfaces.IGame;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Game implements IGame {
    private GameState state;
    private final GameBoard gameBoard;

    private final Map<UUID, IGamePlayer> players;

    public Game() {
        state = GameState.NOT_STARTED;
        gameBoard = new GameBoard();

        players = new HashMap<>();
    }

    @Override
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public boolean hasStarted() {
        return state != GameState.NOT_STARTED;
    }

    @Override
    public boolean hasPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public int getPlayerCount() {
        return players.size();
    }

    @Override
    public void joinPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        if (hasPlayer(uuid)) {
            IGamePlayer gamePlayer = players.get(uuid);

            gamePlayer.updatePlayer(player);
            return;
        }

        IGamePlayer gamePlayer = new GamePlayer(player, hasStarted() ? PlayerState.SPECTATING : PlayerState.PLAYING);
        players.put(uuid, gamePlayer);
    }

    @Override
    public void leavePlayer(UUID uuid) {
        if (!hasPlayer(uuid)) {
            return;
        }

        IGamePlayer gamePlayer = players.get(uuid);

        if (!hasStarted() || gamePlayer.isSpectating()) {
            players.remove(uuid);
            return;
        }

        gamePlayer.updatePlayer(null);
    }

    @Override
    public void sync() {
        syncPlayers();
        syncGameboard();
    }

    private void syncPlayers() {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<UUID> onlineUUIDs = onlinePlayers.stream().map(Player::getUniqueId).toList();

        for (IGamePlayer gamePlayer : players.values()) {
            if (onlineUUIDs.contains(gamePlayer.getPlayer().getUniqueId())) {
                continue;
            }

            this.leavePlayer(gamePlayer.getPlayer().getUniqueId());
        }

        for (Player onlinePlayer : onlinePlayers) {
            if (hasPlayer(onlinePlayer.getUniqueId())) {
                continue;
            }

            this.joinPlayer(onlinePlayer);
        }
    }

    private void syncGameboard() {
        gameBoard.updateBoard();
    }
}
