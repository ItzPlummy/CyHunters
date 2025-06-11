package com.plummy.cyhunters.Iterfaces;

public interface IScheduledTask {
    boolean canRun(Long tick);
    void run();
}
