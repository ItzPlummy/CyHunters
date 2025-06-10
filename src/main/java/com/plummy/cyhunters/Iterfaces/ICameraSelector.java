package com.plummy.cyhunters.Iterfaces;

import java.util.List;
import java.util.UUID;

public interface ICameraSelector {
    void setTargets(List<UUID> targets);

    void attachCamera();

    void detachCamera();
}
