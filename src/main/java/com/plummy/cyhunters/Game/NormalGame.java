package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Iterfaces.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.config;
import static com.plummy.cyhunters.CyHunters.getInstance;

public class NormalGame extends AbstractGame {
    public NormalGame(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager, IKitCreator kitCreator) {
        super(playerManager, scheduler, gameBoard, locationFinder, cameraManager, kitCreator);
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
                setWorldBorder(Objects.requireNonNull(location.getWorld()));

                displayIntroMessage();
                Bukkit.getScheduler().runTaskLater(getInstance(), this::displaySpeedrunnerMessage, 40L);

                Long secondsToDebut = config().getLong("parameters.game.seconds-to-debut") * getPlayerManager().getHunters().size();
                Long secondsToCompass = config().getLong("parameters.game.seconds-to-compass");

                Bukkit.getScheduler().runTaskLater(getInstance(), () -> startHandicap(secondsToDebut, secondsToCompass), 80L);
            });
        });
    }

    private void setWorldBorder(World world) {
        WorldBorder border = world.getWorldBorder();

        border.setCenter(0, 0);
        border.setSize(29999984L);
    }
}
