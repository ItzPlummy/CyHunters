package com.plummy.cyhunters.Iterfaces;

import java.util.UUID;

public interface IScheduledTask {
    UUID getUUID();
    boolean canRun(Long tick);
    void run();
}
