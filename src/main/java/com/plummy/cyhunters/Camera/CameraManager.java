package com.plummy.cyhunters.Camera;

import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.ICameraManager;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Iterfaces.IPlayer;

import java.util.*;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class CameraManager implements ICameraManager {
    private final List<ICamera> cameras;

    public CameraManager() {
        this.cameras = new ArrayList<>();
    }

    @Override
    public void setupCameras() {
        this.resetCameras();

        List<UUID> targets = new ArrayList<>(getMainGame().getPlayerManager().getActivePlayers().stream().map(IPlayer::getUUID).toList());

        for (IHunter hunter : getMainGame().getPlayerManager().getHunters()) {
            cameras.add(hunter.getCamera());
            hunter.getCamera().setTargets(targets);
        }
    }

    @Override
    public void resetCameras() {
        cameras.clear();
    }

    @Override
    public void detachCameras(UUID uuid) {
        for (ICamera camera : cameras.stream().filter(c -> c.getTarget().equals(uuid)).toList()) {
            camera.attach();
        }
    }

    @Override
    public void updateCameras(UUID uuid) {
        for (ICamera camera : cameras.stream().filter(c -> c.getTarget().equals(uuid)).toList()) {
            camera.updateLocation();
        }
    }

    @Override
    public void rotateCameras() {
        for (ICamera camera : cameras) {
            camera.rotate(1);
            camera.updateLocation();
        }
    }
}
