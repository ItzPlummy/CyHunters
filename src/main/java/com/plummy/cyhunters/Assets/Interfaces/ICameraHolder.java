package com.plummy.cyhunters.Assets.Interfaces;

import org.bukkit.Location;

import java.util.UUID;

public interface ICameraHolder {
    void attachCamera(ICamera camera);
    void detachCamera(UUID uuid);
    void updateCameras();
}
