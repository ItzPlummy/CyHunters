package com.plummy.cyhunters.Assets.Interfaces;

import java.util.List;
import java.util.UUID;

public interface ICameraManager {
    void setupPlayers(List<IGamePlayer> players);
    void addPlayer(IGamePlayer player);
    void attachCamera(UUID uuid, ICamera camera);
    void detachCamera(UUID uuid, UUID cameraUUID);
    void detachAllCameras(UUID uuid);
    void updateCameras(UUID uuid);
}
