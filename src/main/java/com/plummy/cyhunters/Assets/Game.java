package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.GameState;
import com.plummy.cyhunters.Assets.Interfaces.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Game implements IGame {
    private GameState state;

    private final IPlayerManager players;
    private final IGameBoard gameBoard;
    private final ILocationFinder locationFinder;

    public Game(IPlayerManager playerManager, IGameBoard gameBoard, ILocationFinder locationFinder) {
        this.state = GameState.NOT_STARTED;

        this.players = playerManager;
        this.gameBoard = gameBoard;
        this.locationFinder = locationFinder;
    }

    @Override
    public IGameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public ILocationFinder getLocationFinder() {
        return locationFinder;
    }

    @Override
    public boolean hasStarted() {
        return state != GameState.NOT_STARTED;
    }

    @Override
    public IGamePlayer getPlayer(UUID uuid) {
        return players.getPlayer(uuid);
    }

    @Override
    public boolean hasPlayer(UUID uuid) {
        return players.hasPlayer(uuid);
    }

    @Override
    public int size() {
        return players.size();
    }

    @Override
    public void joinPlayer(Player player) {
        players.joinPlayer(player, hasStarted());
    }

    @Override
    public void leavePlayer(UUID uuid) {
        players.leavePlayer(uuid, hasStarted());
    }

    @Override
    public void sync() {
        players.syncPlayers(hasStarted());
        gameBoard.updateBoard();
    }

    @Override
    public void start(IGamePlayer startPlayer) {
        if (hasStarted()) {
            return;
        }

        state = GameState.PREPARE;

        send("Game has been started by ");

        Location location = locationFinder.findLocation(Bukkit.getWorlds().get(0));

        if (location == null) {
            send("Unable to find a suitable location to start the game. Please try again.");
            return;
        }

        for (IGamePlayer gamePlayer : players.getPlayers()) {
            if (gamePlayer.isSpectating()) {
                continue;
            }

            gamePlayer.getPlayer().teleport(location);
        }
    }

    @Override
    public void send(String message) {
        for (IGamePlayer gamePlayer : players.getPlayers()) {
            if (gamePlayer.isSpectating()) {
                continue;
            }

            gamePlayer.getPlayer().sendMessage(message);
        }
    }
}
