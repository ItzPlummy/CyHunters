package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.Role;
import com.plummy.cyhunters.Assets.Interfaces.ICamera;
import com.plummy.cyhunters.Assets.Interfaces.ICameraManager;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;

import java.util.*;

public class CameraManager implements ICameraManager {
    private final Map<UUID, List<ICamera>> cameras;
    private final Map<UUID, IGamePlayer> players;

    public CameraManager() {
        this.cameras = new HashMap<>();
        this.players = new HashMap<>();
    }

    public void setupPlayers(List<IGamePlayer> players) {
        for (IGamePlayer player : players) {
            addPlayer(player);
            detachAllCameras(player.getPlayer().getUniqueId());
        }

        for (IGamePlayer player : players) {
            List<UUID> targetPlayers = new ArrayList<>();

            for (IGamePlayer targetPlayer : players) {
                if (targetPlayer.getRole() == Role.SPEEDRUNNER) {
                    continue;
                }

                if (targetPlayer.getPlayer().getUniqueId().equals(player.getPlayer().getUniqueId())) {
                    continue;
                }

                targetPlayers.add(targetPlayer.getPlayer().getUniqueId());
            }

            player.setSpectateTargets(targetPlayers);
        }
    }

    public void addPlayer(IGamePlayer player) {
        cameras.putIfAbsent(player.getPlayer().getUniqueId(), new ArrayList<>());
        players.putIfAbsent(player.getPlayer().getUniqueId(), player);
    }

    public void attachCamera(UUID uuid, ICamera camera) {
        cameras.get(uuid).add(camera);
    }

    public void detachCamera(UUID uuid, UUID cameraUUID) {
        cameras.get(uuid).removeIf(camera -> camera.getUniqueID().equals(cameraUUID));
    }

    public void detachAllCameras(UUID uuid) {
        cameras.get(uuid).clear();
    }

    public void updateCameras(UUID uuid) {
        for (ICamera camera : cameras.get(uuid)) {
            camera.setLocation(players.get(uuid).getPlayer().getLocation().clone());
        }
    }
}
