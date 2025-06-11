package com.plummy.cyhunters.Iterfaces;

public interface IScheduler {
    Long getTick();

    void start();

    void stop();

    void addRunnable(Runnable runnable, Long delay, boolean isAsync);
}
