package com.plummy.cyhunters.Assets.Interfaces;

import org.bukkit.Location;

import java.util.UUID;

public interface ICamera {
    UUID getUniqueID();
    void setLocation(Location location);
    void updateLocation();
    void addYaw(float add);
}
