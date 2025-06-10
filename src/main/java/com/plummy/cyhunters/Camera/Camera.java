package com.plummy.cyhunters.Camera;

import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.IHunter;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Camera implements ICamera {
    private final IHunter player;
    private Location location;

    private float yaw;

    public Camera(IHunter player) {
        this.player = player;
        this.location = null;

        this.yaw = 30;
    }

    @Override
    public UUID getUUID() {
        return player.getPlayer().getUniqueId();
    }

    @Override
    public void setLocation(Location location) {
        if (!player.isOnline() || !player.isSpectating()) {
            return;
        }

        this.location = location;

        updateLocation();
    }

    @Override
    public void updateLocation() {
        float piYaw = yaw * (float) Math.PI / 180;
        float piPitch = 5 * (float) Math.PI / 180;

        Vector offset = new Vector(Math.sin(piYaw) * Math.cos(piPitch), Math.sin(piPitch), -Math.cos(piYaw) * Math.cos(piPitch));
        Location calculatedLocation = this.location.clone().add(0, 0.5, 0).add(offset.multiply(2));

        calculatedLocation.setYaw(yaw);
        calculatedLocation.setPitch(5);

        player.getPlayer().teleport(calculatedLocation);
    }

    @Override
    public void rotate(float degrees) {
        if (!player.isOnline() || !player.isSpectating()) {
            return;
        }

        yaw += degrees;
        updateLocation();
    }
}
