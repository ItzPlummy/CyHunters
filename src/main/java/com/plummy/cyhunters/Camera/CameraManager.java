package com.plummy.cyhunters.Camera;

import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.ICameraManager;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Iterfaces.IPlayer;

import java.util.*;

public class CameraManager implements ICameraManager {
    private final Map<UUID, List<ICamera>> cameras;
    private final Map<UUID, IHunter> players;

    public CameraManager() {
        this.cameras = new HashMap<>();
        this.players = new HashMap<>();
    }

    @Override
    public void setupPlayers(List<IHunter> players) {
        for (IHunter player : players) {
            addPlayer(player);
            detachAllCameras(player.getPlayer().getUniqueId());

            List<UUID> targets = new ArrayList<>();

            for (IPlayer target : players) {
                targets.add(target.getPlayer().getUniqueId());
            }

            player.getCameraSelector().setTargets(targets);
        }
    }

    @Override
    public void addPlayer(IHunter player) {
        cameras.putIfAbsent(player.getPlayer().getUniqueId(), new ArrayList<>());
        players.putIfAbsent(player.getPlayer().getUniqueId(), player);
    }

    @Override
    public void attachCamera(UUID uuid, ICamera camera) {
        cameras.get(uuid).add(camera);
    }

    @Override
    public void detachCamera(UUID uuid, UUID cameraUUID) {
        cameras.get(uuid).removeIf(camera -> camera.getUUID().equals(cameraUUID));
    }

    @Override
    public void detachAllCameras(UUID uuid) {
        cameras.get(uuid).clear();
    }

    @Override
    public void updateCameras(UUID uuid) {
        List<ICamera> cameraList = cameras.get(uuid);

        if (cameraList == null) {
            return;
        }

        for (ICamera camera : cameras.get(uuid)) {
            camera.setLocation(players.get(uuid).getPlayer().getLocation().clone());
        }
    }

    @Override
    public void rotateAllCameras() {
        for (UUID uuid : cameras.keySet()) {
            for (ICamera camera : cameras.get(uuid)) {
                camera.rotate(1);
            }
        }
    }
}
