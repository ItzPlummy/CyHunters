package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Iterfaces.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.plummy.cyhunters.CyHunters.config;
import static com.plummy.cyhunters.CyHunters.getInstance;

public class BlitzGame extends AbstractGame {
    public BlitzGame(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager, IKitCreator kitCreator) {
        super(playerManager, scheduler, gameBoard, locationFinder, cameraManager, kitCreator);
    }

    @Override
    public GameStyle getStyle() {
        return GameStyle.BLITZ;
    }

    @Override
    public void start(Player startPlayer) {
        if (hasStarted()) {
            return;
        }

        setup();

        Location location;
        CompletableFuture<Location> findLocationTask = CompletableFuture.supplyAsync(() -> setLocatingStage(startPlayer));
        findLocationTask.join();

        try {
            location = findLocationTask.get();
        } catch (InterruptedException | ExecutionException exception) {
            onLocationNotFound();
            return;
        }

        Long prepareTime = 200L;
        Long handicapTime = config().getLong("parameters.game.seconds-to-debut-blitz") * getPlayerManager().getHunters().size();
        Long debutTime = config().getLong("parameters.game.seconds-to-compass-blitz");

        setPreparingStage(location);
        setWorldBorder(location);

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> setHandicapStage(handicapTime), prepareTime);
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> setDebutStage(debutTime), prepareTime + handicapTime);
        Bukkit.getScheduler().runTaskLater(getInstance(), this::setHuntingStage, prepareTime + handicapTime + debutTime);
    }

    @Override
    public void setHandicapStage(Long handicapTime) {
        super.setHandicapStage(handicapTime);

        long secondsBeforeEnd = config().getLong("parameters.game.seconds-before-end");

        getScheduler().addRunnable(this::sendThreeMinuteWarning, secondsBeforeEnd - 180, true);
        getScheduler().addRunnable(this::sendOneMinuteWarning, secondsBeforeEnd - 60, true);

        getScheduler().addRunnable(() -> stop(getPlayerManager().getSpeedrunner().getPlayer(), GameEndingReason.SPEEDRUNNER_WINS), secondsBeforeEnd, false);
    }

    protected void setWorldBorder(Location location) {
        WorldBorder border = Objects.requireNonNull(location.getWorld()).getWorldBorder();

        border.setCenter(location.getBlockX() + 0.5, location.getBlockZ() + 0.5);
        border.setSize(config().getInt("parameters.game.border-size") + 1);
    }

    protected void sendThreeMinuteWarning() {
        send("§a3 minutes left until the Speedrunner wins!");

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1f, 1f);
        }
    }
    protected void sendOneMinuteWarning() {
        send("§a1 minute left until the Speedrunner wins!");

        for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1f, 1f);
        }
    }
}
