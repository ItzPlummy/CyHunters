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

public abstract class AbstractGame implements IGame {
    private GameState state;

    private final IPlayerManager playerManager;
    private final IScheduler scheduler;
    private final IGameBoard gameBoard;
    private final ILocationFinder locationFinder;
    private final ICameraManager cameraManager;
    private final IKitCreator kitCreator;

    public AbstractGame(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager, IKitCreator kitCreator) {
        this.state = GameState.NOT_STARTED;

        this.playerManager = playerManager;
        this.scheduler = scheduler;
        this.gameBoard = gameBoard;
        this.locationFinder = locationFinder;
        this.cameraManager = cameraManager;
        this.kitCreator = kitCreator;
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
    public boolean handicap() {
        return state == GameState.HANDICAP;
    }

    @Override
    public boolean debuted() {
        return state == GameState.STARTED;
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
    public void stop(GameEndingReason reason, Player stopPlayer) {
        if (!hasStarted()) {
            return;
        }

        setState(GameState.NOT_STARTED);

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

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().sendTitle(title, subtitle, 40, 40, 60);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
        }

        getPlayerManager().resetPlayers();
        getScheduler().stop();
        getCameraManager().resetCameras();
        sync();
    }

    protected GameState getState() {
        return state;
    }

    protected void setState(GameState state) {
        this.state = state;
    }

    protected void startLocating(String playerName) {
        setState(GameState.LOCATING);
        send("§a§lCyHunters has been started by " + playerName + "!");
        send("§aSearching for a suitable location...");
    }

    protected void startPreparing(Location location) {
        setState(GameState.PREPARE);
        getPlayerManager().ready(location);
        sync();

        Objects.requireNonNull(location.getWorld()).setTime(0);
        Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.KEEP_INVENTORY, false);
        Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        Objects.requireNonNull(location.getWorld()).setGameRule(GameRule.DO_INSOMNIA, false);
    }

    protected void startHandicap(Long secondsToDebut, Long secondsToCompass) {
        setState(GameState.HANDICAP);
        getScheduler().start();

        kitCreator.createSpeedrunnerKit(getPlayerManager().getSpeedrunner().getPlayer());

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().sendTitle("§b§lLets §d§lGo!", "", 0, 40, 60);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
        }

        send("§e§lHandicap stage has been started!");
        send("§eSpeedrunner has " + secondsToDebut + " seconds to ready up, before");
        send("§ehunters will start to chase him!");

        getScheduler().addRunnable(this::startDebut, secondsToDebut, false);
        getScheduler().addRunnable(this::completeStart, secondsToCompass, false);
    }

    protected void startDebut() {
        setState(GameState.DEBUT);

        for (IHunter hunter : getPlayerManager().getHunters()) {
            kitCreator.createHunterKit(hunter.getPlayer());
        }

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.BLOCK_END_PORTAL_SPAWN, 1f, 0.75f);
        }

        send("§c§lHunters are free!");
    }

    protected void completeStart() {
        setState(GameState.STARTED);

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.BLOCK_END_PORTAL_SPAWN, 1f, 0.75f);
        }

        getPlayerManager().getHunters().forEach(IHunter::giveCompass);

        send("§cHunters now got compasses to track down the speedrunner!");
    }

    protected void setupPlayersAndCameras() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        getPlayerManager().setPlayers(players);
        getCameraManager().setupCameras();
    }

    protected void processNullLocation() {
        send("§cUnable to find a suitable location to start the game. Please try again.");

        getPlayerManager().resetPlayers();
        getCameraManager().resetCameras();
        sync();
    }

    protected void displayIntroMessage() {
        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().sendTitle("§b§lCy§d§lHunters", "§3Let the fun §5Begin§3!", 0, 40, 0);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.843f, 1f);
        }
    }

    protected void displaySpeedrunnerMessage() {
        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            if (player instanceof Speedrunner) {
                player.getPlayer().sendTitle("§b§lYou", "§3Are a §5Speedrunner", 0, 40, 0);
            } else {
                player.getPlayer().sendTitle("§b§l" + getPlayerManager().getSpeedrunner().getPlayer().getName(), "§3Is a §5Speedrunner", 0, 40, 0);
            }

            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.843f, 1f);
        }
    }

    protected void send(String message) {
        for (IPlayer player : playerManager.getOnlinePlayers()) {
            player.getPlayer().sendMessage(message);
        }
    }
}
