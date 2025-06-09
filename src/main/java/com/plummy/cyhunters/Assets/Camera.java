package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Interfaces.ICamera;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Camera implements ICamera {
    private final IGamePlayer player;
    private Location location = null;

    private float yaw;

    public Camera(IGamePlayer player) {
        this.player = player;
        this.yaw = 30;
    }

    @Override
    public UUID getUniqueID() {
        return player.getPlayer().getUniqueId();
    }

    @Override
    public void setLocation(Location location) {
        if (player.isLeft() || !player.isDead()) {
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
        if (player.isLeft() || !player.isDead()) {
            return;
        }

        yaw += degrees;
        updateLocation();
    }
}
