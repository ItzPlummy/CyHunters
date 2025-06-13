package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameState;
import com.plummy.cyhunters.Iterfaces.*;
import com.plummy.cyhunters.Player.Speedrunner;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

import static com.plummy.cyhunters.CyHunters.*;
import static com.plummy.cyhunters.CyHunters.getMainGame;

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
    public IKitCreator getKitCreator() {
        return kitCreator;
    }

    @Override
    public boolean hasStarted() {
        return state != GameState.NOT_STARTED;
    }

    @Override
    public boolean locating() {
        return state == GameState.LOCATING;
    }

    @Override
    public boolean preparing() {
        return state == GameState.PREPARE;
    }

    @Override
    public boolean prepared() {
        return hasStarted() && !locating() && !preparing();
    }

    @Override
    public boolean handicap() {
        return state == GameState.HANDICAP;
    }

    @Override
    public boolean debut() {
        return state == GameState.DEBUT;
    }

    @Override
    public boolean hunt() {
        return state == GameState.HUNTING;
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
    public void start(Player startPlayer, Player speedrunner) {
        if (hasStarted()) {
            return;
        }

        setup(speedrunner);

        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
            Location location = setLocatingStage(startPlayer);

            if (location == null) {
                onLocationNotFound();
                return;
            }

            Bukkit.getScheduler().runTask(getInstance(), () -> {
                long prepareTime = 10L;
                Long handicapTime = config().getLong("parameters.game.seconds-to-debut") * getPlayerManager().getHunters().size();
                Long debutTime = config().getLong("parameters.game.seconds-to-compass");

                setPreparingStage(location);

                Bukkit.getScheduler().runTaskLater(getInstance(), () -> setHandicapStage(handicapTime), prepareTime * 20);

                getMainGame().getScheduler().addRunnable(() -> setDebutStage(debutTime), handicapTime, false);
                getMainGame().getScheduler().addRunnable(this::setHuntingStage, handicapTime + debutTime, false);
            });
        });
    }

    public void stop(Player stopPlayer, GameEndingReason reason) {
        if (!hasStarted()) {
            return;
        }

        setStoppingStage(stopPlayer, reason);
    }

    public Location setLocatingStage(Player startPlayer) {
        setState(GameState.LOCATING);

        sendLocating(startPlayer.getName());

        return getLocationFinder().findLocation();
    }

    public void setPreparingStage(Location location) {
        setState(GameState.PREPARE);

        getPlayerManager().ready(location);
        sync();

        World world = Objects.requireNonNull(location.getWorld());

        world.setSpawnLocation(location);
        world.setTime(0);
        world.setClearWeatherDuration(world.getWeatherDuration());
        world.setGameRule(GameRule.KEEP_INVENTORY, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.DO_INSOMNIA, false);

        setWorldBorder(world);

        Bukkit.getScheduler().runTaskLater(getInstance(), this::displayIntro, 0L);
        Bukkit.getScheduler().runTaskLater(getInstance(), this::displayDimension, 40L);
        Bukkit.getScheduler().runTaskLater(getInstance(), this::displayStyle, 80L);
        Bukkit.getScheduler().runTaskLater(getInstance(), this::displayKit, 120L);
        Bukkit.getScheduler().runTaskLater(getInstance(), this::displaySpeedrunner, 160L);
    }

    public void setHandicapStage(Long handicapTime) {
        setState(GameState.HANDICAP);

        getScheduler().start();
        kitCreator.createSpeedrunnerKit(getPlayerManager().getSpeedrunner().getPlayer());

        sendHandicap(handicapTime);
        displayStart();
    }

    public void setDebutStage(Long debutTime) {
        setState(GameState.DEBUT);

        for (IHunter hunter : getPlayerManager().getHunters()) {
            kitCreator.createHunterKit(hunter.getPlayer());
        }

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.BLOCK_END_PORTAL_SPAWN, 1f, 0.75f);
        }

        sendDebut();
    }

    public void setHuntingStage() {
        setState(GameState.HUNTING);

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.BLOCK_END_PORTAL_SPAWN, 1f, 0.75f);
        }

        getPlayerManager().getHunters().forEach(IHunter::giveCompass);

        sendHunting();
    }

    public void setStoppingStage(Player stopPlayer, GameEndingReason reason) {
        setState(GameState.NOT_STARTED);

        displayStop(stopPlayer == null ? null : stopPlayer.getName(), reason);

        reset();
    }

    protected void setState(GameState state) {
        this.state = state;
    }

    protected void setup(Player speedrunner) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        getPlayerManager().setPlayers(players, speedrunner);
        getCameraManager().setupCameras();
    }

    protected void reset() {
        getPlayerManager().resetPlayers();
        getScheduler().stop();
        getCameraManager().resetCameras();
        sync();
    }

    protected void onLocationNotFound() {
        setState(GameState.NOT_STARTED);

        sendLocationNotFound();
        reset();
    }

    protected void setWorldBorder(World world) {
        WorldBorder border = world.getWorldBorder();

        border.setCenter(0, 0);
        border.setSize(29999984L);
    }

    protected void displayIntro() {
        displayTitle(getPlayerManager().getOnlinePlayers(), "§b§lCy§d§lHunters", "§3Let the fun §5Begin§3!", 0.5F);
    }

    protected void displayDimension() {
        displayTitle(getPlayerManager().getOnlinePlayers(), "§c" + getLocationFinder().getDimension().getName(), "§4Dimension", 0.63F);
    }

    protected void displayStyle() {
        displayTitle(getPlayerManager().getOnlinePlayers(), "§e" + getStyle().getName(), "§6Game Style", 0.749F);
    }

    protected void displayKit() {
        displayTitle(getPlayerManager().getOnlinePlayers(), "§a" + getKitCreator().getType().getName(), "§2Selected Kit", 0.841F);
    }

    protected void displaySpeedrunner() {
        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            if (player instanceof Speedrunner) {
                player.getPlayer().sendTitle("§b§lYou", "§3Are a §5Speedrunner", 0, 40, 0);
            } else {
                player.getPlayer().sendTitle("§b§l" + getPlayerManager().getSpeedrunner().getName(), "§3Is a §5Speedrunner", 0, 40, 0);
            }

            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.944f, 1f);
        }
    }

    protected void displayStart() {
        displayTitle(getPlayerManager().getOnlinePlayers(), "§b§lLets §d§lGo!", "", 60, 1F);

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
        }
    }

    protected void displayStop(String playerName, GameEndingReason reason) {
        String title = "";
        String subtitle = "";

        switch (reason) {
            case SPEEDRUNNER_WINS -> {
                title = "§c§lGame Over";
                subtitle = "§c" + playerName + " won!";
            }
            case HUNTER_WINS -> {
                title = "§c§lGame Over";
                subtitle = "§cHunters won!";
            }
            case COMMAND -> {
                title = "§c§lGame Stopped";
                subtitle = "§cBy " + playerName;
            }
        }

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().sendTitle(title, subtitle, 40, 40, 60);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
        }
    }

    protected void displayTitle(List<IPlayer> players, String title, String subtitle, float pitch) {
        displayTitle(players, title, subtitle, 0, pitch);
    }

    protected void displayTitle(List<IPlayer> players, String title, String subtitle, int fadeOut, float pitch) {
        for (IPlayer player : players) {
            player.getPlayer().sendTitle(title, subtitle, 0, 40, fadeOut);
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, pitch, 1f);
        }
    }

    protected void send(String message) {
        for (IPlayer player : playerManager.getOnlinePlayers()) {
            player.getPlayer().sendMessage(message);
        }
    }

    protected void sendLocating(String playerName) {
        send("§a§lCyHunters has been started by " + playerName + "!");
        send("§aSearching for a suitable location...");
    }

    protected void sendHandicap(Long handicapTime) {
        send("§e§lHandicap stage has been started!");
        send("§eSpeedrunner has " + handicapTime + " seconds to ready up, before");
        send("§ehunters will start to chase him!");
    }

    protected void sendDebut() {
        send("§c§lHunters are free!");
    }

    protected void sendHunting() {
        send("§cHunters now got compasses to track down the speedrunner!");
    }

    protected void sendLocationNotFound() {
        send("§cUnable to find a suitable location to start the game. Please try again.");
    }
}
