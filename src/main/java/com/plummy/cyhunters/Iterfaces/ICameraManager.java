package com.plummy.cyhunters.Iterfaces;

import java.util.UUID;

public interface ICameraManager {
    void setupCameras();
    void resetCameras();
    void detachCameras(UUID uuid);
    void updateCameras(UUID uuid);
    void rotateCameras();
}
