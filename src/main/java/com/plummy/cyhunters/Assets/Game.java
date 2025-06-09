package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.GameState;
import com.plummy.cyhunters.Assets.Interfaces.*;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getInstance;

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
    public boolean preparing() {
        return state == GameState.PREPARE;
    }

    @Override
    public List<IGamePlayer> getAlivePlayers() {
        return players.getAlivePlayers();
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
    public ICameraHolder getRandomCameraHolder() {
        return players.getRandomCameraHolder();
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

        state = GameState.LOCATING;
        send("Game has been started by " + startPlayer.getPlayer().getName() + ". Searching for a suitable location");

        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
            Location location = locationFinder.findLocation(Bukkit.getWorlds().get(0));

            Bukkit.getScheduler().runTask(getInstance(), () -> {
                if (location == null) {
                    send("Unable to find a suitable location to start the game. Please try again.");
                    return;
                }

                players.distributeRoles();
                players.ready(location);

                Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.KEEP_INVENTORY, false);
                Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

                state = GameState.PREPARE;
                title("§b§lCy§d§lHunters", "§3Let the fun §5Begin§3!", 0, 40, 0);
                sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.843f);

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                title("§b§l" + players.getSpeedrunner().getPlayer().getName(), "§5Speedrunner", 0, 40, 0);
                                sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.949f);
                            }
                        },
                2000
                );

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                state = GameState.HANDICAP;

                                title("§a§lS T A R T", "", 0, 10, 30);
                                sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f);
                                sound(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f);
                            }
                        },
                4000
                );
            });
        });
    }

    @Override
    public void send(String message) {
        for (IGamePlayer gamePlayer : players.getActivePlayers()) {
            gamePlayer.getPlayer().sendMessage(message);
        }
    }

    @Override
    public void title(String title, String subtitle, int in, int hold, int out) {
        for (IGamePlayer gamePlayer : players.getActivePlayers()) {
            gamePlayer.getPlayer().sendTitle(title, subtitle, in, hold, out);
        }
    }

    @Override
    public void sound(Sound sound, float pitch) {
        for (IGamePlayer gamePlayer : players.getActivePlayers()) {
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), sound, 1f, pitch);
        }
    }
}
