package com.plummy.cyhunters.Iterfaces;

public interface IScheduler {
    void start();

    void stop();

    void addRunnable(Runnable runnable, Long delay, boolean isAsync);
}
