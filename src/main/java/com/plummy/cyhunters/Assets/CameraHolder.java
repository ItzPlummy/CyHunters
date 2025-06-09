package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Interfaces.ICamera;
import com.plummy.cyhunters.Assets.Interfaces.ICameraHolder;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraHolder implements ICameraHolder {
    private final IGamePlayer player;
    private final Map<UUID, ICamera> cameras;

    public CameraHolder(IGamePlayer player) {
        this.player = player;
        this.cameras = new HashMap<>();
    }

    public void attachCamera(ICamera camera) {
        cameras.put(camera.getUniqueID(), camera);
    }

    public void detachCamera(UUID uuid) {
        cameras.remove(uuid);
    }

    public void updateCameras() {
        for (ICamera camera : cameras.values()) {
            camera.setLocation(player.getPlayer().getLocation().clone());
        }
    }
}
