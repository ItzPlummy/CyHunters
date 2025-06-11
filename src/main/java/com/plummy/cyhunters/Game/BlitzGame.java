package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Camera.CameraManager;
import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Iterfaces.*;
import com.plummy.cyhunters.Player.PlayerManager;
import com.plummy.cyhunters.Scheduler.GameScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public class BlitzGame extends AbstractGame {
    public BlitzGame(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager) {
        super(playerManager, scheduler, gameBoard, locationFinder, cameraManager);
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

        setupPlayersAndCameras();
        startLocating(startPlayer.getName());

        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
            Location location = getLocationFinder().findLocation(startPlayer.getWorld());

            Bukkit.getScheduler().runTask(getInstance(), () -> {
                if (location == null) {
                    processNullLocation();
                    return;
                }

                startPreparing(location);
                setWorldBorder(location);

                displayIntroMessage();
                Bukkit.getScheduler().runTaskLater(getInstance(), this::displaySpeedrunnerMessage, 40L);

                Long secondsToDebut = getInstance().getConfig().getLong("parameters.game.seconds-to-debut-blitz") * getPlayerManager().getHunters().size();
                Long secondsToCompass = getInstance().getConfig().getLong("parameters.game.seconds-to-compass-blitz");

                Bukkit.getScheduler().runTaskLater(getInstance(), () -> startHandicap(secondsToDebut, secondsToCompass), 80L);
            });
        });
    }

    @Override
    protected void startHandicap(Long secondsToDebut, Long secondsToCompass) {
        super.startHandicap(secondsToDebut, secondsToCompass);

        long secondsBeforeEnd = getInstance().getConfig().getLong("parameters.game.seconds-before-end");

        getScheduler().addRunnable(() -> {
            send("§a3 minutes left until the Speedrunner wins!");

            for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
                player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1f, 1f);
            }
        }, secondsBeforeEnd - 180, true);

        getScheduler().addRunnable(() -> {
            send("§a1 minute left until the Speedrunner wins!");

            for (IPlayer player : getPlayerManager().getOnlinePlayers()) {
                player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1f, 1f);
            }
        }, secondsBeforeEnd - 60, true);

        getScheduler().addRunnable(() -> stop(GameEndingReason.SPEEDRUNNER_WINS, getPlayerManager().getSpeedrunner().getPlayer()), secondsBeforeEnd, false);
    }

    private void setWorldBorder(Location location) {
        WorldBorder border = Objects.requireNonNull(location.getWorld()).getWorldBorder();

        border.setCenter(location.getBlockX() + 0.5, location.getBlockZ() + 0.5);
        border.setSize(getInstance().getConfig().getInt("parameters.game.border-size") + 1);
    }
}
