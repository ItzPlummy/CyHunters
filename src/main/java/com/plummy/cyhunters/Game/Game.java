package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameState;
import com.plummy.cyhunters.Iterfaces.*;
import com.plummy.cyhunters.Player.Speedrunner;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

import static com.plummy.cyhunters.CyHunters.getInstance;

public class Game implements IGame {
    private GameState state;

    private final IPlayerManager playerManager;
    private final IScheduler scheduler;
    private final IGameBoard gameBoard;
    private final ILocationFinder locationFinder;
    private final ICameraManager cameraManager;

    public Game(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager) {
        this.state = GameState.NOT_STARTED;

        this.playerManager = playerManager;
        this.scheduler = scheduler;
        this.gameBoard = gameBoard;
        this.locationFinder = locationFinder;
        this.cameraManager = cameraManager;
    }

    @Override
    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public IScheduler getScheduler() {
        return scheduler;
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
    public ICameraManager getCameraManager() {
        return cameraManager;
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
    public void joinPlayer(Player player) {
        playerManager.joinPlayer(player);
    }

    @Override
    public void leavePlayer(UUID uuid) {
        playerManager.leavePlayer(uuid);
    }

    @Override
    public void sync() {
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            if (hasStarted()) {
                playerManager.syncPlayers();
            }

            gameBoard.updateBoard();
        }, 1L);
    }

    @Override
    public void start(Player startPlayer) {
        if (hasStarted()) {
            return;
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);
        playerManager.setPlayers(players);
        cameraManager.setupCameras();

        state = GameState.LOCATING;
        send("§aGame has been started by " + startPlayer.getName() + ".");
        send("§aSearching for a suitable location...");

        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
            Location location = locationFinder.findLocation(Bukkit.getWorlds().get(0));

            Bukkit.getScheduler().runTask(getInstance(), () -> {
                if (location == null) {
                    send("§cUnable to find a suitable location to start the game. Please try again.");

                    playerManager.resetPlayers();
                    cameraManager.resetCameras();
                    sync();

                    return;
                }

                state = GameState.PREPARE;
                playerManager.ready(location);
                sync();

                Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.KEEP_INVENTORY, false);
                Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

                for (IPlayer player : playerManager.getOnlinePlayers()) {
                    player.getPlayer().sendTitle("§b§lCy§d§lHunters", "§3Let the fun §5Begin§3!", 0, 40, 0);
                    player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.843f, 1f);
                }

                Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
                    for (IPlayer player : playerManager.getOnlinePlayers()) {
                        if (player instanceof Speedrunner) {
                            player.getPlayer().sendTitle("§b§lYou", "§3Are a §5Speedrunner", 0, 40, 0);
                        } else {
                            player.getPlayer().sendTitle("§b§l" + playerManager.getSpeedrunner().getPlayer().getName(), "§3Is a §5Speedrunner", 0, 40, 0);

                        }

                        player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.843f, 1f);
                    }
                }, 40L);

                Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
                    state = GameState.HANDICAP;
                    scheduler.start();

                    for (IPlayer player : playerManager.getOnlinePlayers()) {
                        player.getPlayer().sendTitle("§b§lLets §d§lGo!", "", 0, 40, 60);
                        player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
                    }
                }, 80L);
            });
        });
    }

    public void stop(GameEndingReason reason, Player stopPlayer) {
        if (!hasStarted()) {
            return;
        }

        state = GameState.NOT_STARTED;

        String title = "";
        String subtitle = "";

        switch (reason) {
            case SPEEDRUNNER_WINS -> {
                title = "§c§lGame Over";
                subtitle = "§c" + stopPlayer.getName() + " won!";
            }
            case HUNTER_WINS -> {
                title = "§c§lGame Over";
                subtitle = "§cHunters won!";
            }
            case COMMAND -> {
                title = "§c§lGame Stopped";
                subtitle = "§cBy " + stopPlayer.getName();
            }
        }

        for (IPlayer player : playerManager.getOnlinePlayers()) {
            player.getPlayer().sendTitle(title, subtitle, 40, 40, 60);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
        }

        playerManager.resetPlayers();
        scheduler.stop();
        cameraManager.resetCameras();
        sync();
    }

    public void send(String message) {
        for (IPlayer player : playerManager.getOnlinePlayers()) {
            player.getPlayer().sendMessage(message);
        }
    }
}
