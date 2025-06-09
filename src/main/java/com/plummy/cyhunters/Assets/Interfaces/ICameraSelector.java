package com.plummy.cyhunters.Assets.Interfaces;

import com.plummy.cyhunters.Assets.Enums.Role;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public interface ICameraSelector {
    void setTargets(List<UUID> targets);

    void attachCamera();

    void detachCamera();
}
