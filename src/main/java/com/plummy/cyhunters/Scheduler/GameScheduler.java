package com.plummy.cyhunters.Scheduler;

import com.plummy.cyhunters.Iterfaces.IScheduledTask;
import com.plummy.cyhunters.Iterfaces.IScheduler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScheduler implements IScheduler {
    private final ScheduledExecutorService scheduler;
    private final List<IScheduledTask> tasks;

    private boolean isRunning;
    private Long tick;

    public GameScheduler() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.tasks = new ArrayList<>();

        this.isRunning = false;
        this.tick = 0L;
    }

    @Override
    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        tick = 0L;

        scheduler.scheduleWithFixedDelay(new BukkitRunnable() {
            @Override
            public void run() {
                tick += 1;

                for (IScheduledTask task : tasks) {
                    if (!task.canRun(tick)) {
                        continue;
                    }

                    task.run();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        tick = 0L;

        scheduler.shutdown();
        tasks.clear();
    }

    @Override
    public void addRunnable(Runnable runnable, Long delay, boolean isAsync) {
        tasks.add(new ScheduledTask(runnable, tick + delay, isAsync));
    }

    @Override
    public void addStopRunnable(Runnable runnable, Long delay, boolean isAsync) {
        addRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                stop();
            }
        }, delay, isAsync);
    }
}
