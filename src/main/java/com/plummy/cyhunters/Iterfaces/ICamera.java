package com.plummy.cyhunters.Iterfaces;

import org.bukkit.Location;

import java.util.UUID;

public interface ICamera {
    UUID getUniqueID();
    void setLocation(Location location);
    void updateLocation();
    void rotate(float degrees);
}
