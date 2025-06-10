package com.plummy.cyhunters.Camera;

import com.plummy.cyhunters.Enums.Role;
import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.ICameraSelector;
import com.plummy.cyhunters.Iterfaces.IGamePlayer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class CameraSelector implements ICameraSelector {
    private final ICamera camera;

    private List<UUID> targets;

    private int spectateTargetIndex = 0;

    public CameraSelector(ICamera camera) {
        this.camera = camera;
    }

    @Override
    public void setTargets(List<UUID> targets) {
        this.targets = targets;
        Collections.shuffle(this.targets);
    }

    @Override
    public void attachCamera() {
        int prevTargetIndex = spectateTargetIndex;

        do {
            spectateTargetIndex = (spectateTargetIndex + 1) % targets.size();

            IGamePlayer target = getMainGame().getPlayerManager().getPlayer(targets.get(spectateTargetIndex));

            if (target.isLeft()) {
                continue;
            }
            if (target.isSpectating()) {
                continue;
            }
            if (target.isDead()) {
                continue;
            }
            if (target.getRole() == Role.SPEEDRUNNER) {
                continue;
            }

            break;
        } while (spectateTargetIndex != prevTargetIndex);

        getMainGame().getCameraManager().detachCamera(targets.get(prevTargetIndex), camera.getUniqueID());

        if (spectateTargetIndex == prevTargetIndex) {
            return;
        }

        getMainGame().getCameraManager().attachCamera(targets.get(spectateTargetIndex), camera);
        getMainGame().getCameraManager().updateCameras(targets.get(spectateTargetIndex));
    }

    @Override
    public void detachCamera() {
        getMainGame().getCameraManager().detachCamera(targets.get(spectateTargetIndex), camera.getUniqueID());
    }
}
