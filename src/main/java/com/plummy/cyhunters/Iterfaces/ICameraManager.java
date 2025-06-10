package com.plummy.cyhunters.Iterfaces;

import java.util.List;
import java.util.UUID;

public interface ICameraManager {
    void setupPlayers(List<IHunter> players);
    void resetPlayers();
    void addPlayer(IHunter player);
    void attachCamera(UUID uuid, ICamera camera);
    void detachCamera(UUID uuid, UUID cameraUUID);
    void detachAllCameras(UUID uuid);
    void updateCameras(UUID uuid);
    void rotateAllCameras();
}
