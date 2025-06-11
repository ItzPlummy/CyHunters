package com.plummy.cyhunters.Iterfaces;

import java.util.List;
import java.util.UUID;

public interface ICamera {
    UUID getUUID();

    UUID getTarget();

    IPlayer getTargetPlayer();

    void setTargets(List<UUID> targets);

    boolean isAttached();

    void attach();

    void detach();

    void updateLocation();

    void rotate(float degrees);
}
