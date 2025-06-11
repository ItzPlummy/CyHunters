package com.plummy.cyhunters.Camera;

import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Iterfaces.IPlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class Camera implements ICamera {
    private final IHunter player;
    private final List<UUID> targets;
    private int targetIndex;

    private boolean isAttached;
    private float yaw;

    public Camera(IHunter player) {
        this.player = player;
        this.targets = new ArrayList<>();
        this.targetIndex = 0;

        this.isAttached = false;
        this.yaw = 30;
    }

    @Override
    public UUID getUUID() {
        return player.getPlayer().getUniqueId();
    }

    @Override
    public UUID getTarget() {
        return targets.get(targetIndex);
    }

    @Override
    public IPlayer getTargetPlayer() {
        return getMainGame().getPlayerManager().getPlayer(getTarget());
    }

    @Override
    public void setTargets(List<UUID> targets) {
        this.targets.clear();
        this.targets.addAll(targets);
    }

    @Override
    public boolean isAttached() {
        return isAttached;
    }

    @Override
    public void attach() {
        detach();

        int prevTargetIndex = targetIndex;
        boolean foundTarget = false;

        do {
            targetIndex = (targetIndex + 1) % targets.size();

            IPlayer target = getTargetPlayer();

            if (!target.isOnline()) {
                continue;
            }
            if (!(target instanceof IHunter hunter)) {
                continue;
            }
            if (hunter.isSpectating()) {
                continue;
            }

            foundTarget = true;
            break;
        } while (targetIndex != prevTargetIndex);

        if (targetIndex == prevTargetIndex && !foundTarget) {
            return;
        }

        isAttached = true;
        getMainGame().getCameraManager().updateCameras(getTarget());
    }

    @Override
    public void detach() {
        isAttached = false;
    }

    @Override
    public void updateLocation() {
        if (!player.isOnline() || !player.isSpectating() || !isAttached()) {
            return;
        }

        float piYaw = yaw * (float) Math.PI / 180;
        float piPitch = 5 * (float) Math.PI / 180;

        Vector offset = new Vector(Math.sin(piYaw) * Math.cos(piPitch), Math.sin(piPitch), -Math.cos(piYaw) * Math.cos(piPitch));
        Location calculatedLocation = getTargetPlayer().getPlayer().getLocation().add(0, 0.5, 0).add(offset.multiply(2));

        calculatedLocation.setYaw(yaw);
        calculatedLocation.setPitch(5);

        player.getPlayer().teleport(calculatedLocation);
    }

    @Override
    public void rotate(float degrees) {
        yaw += degrees;
    }
}
