package com.plummy.cyhunters.Scheduler;

import com.plummy.cyhunters.Iterfaces.IScheduledTask;
import com.plummy.cyhunters.Iterfaces.IScheduler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScheduler implements IScheduler {
    private final Map<UUID, IScheduledTask> tasks;
    private ScheduledExecutorService scheduler;

    private boolean isRunning;
    private Long tick;

    public GameScheduler() {
        this.tasks = new HashMap<>();
        this.scheduler = null;

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

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new BukkitRunnable() {
            @Override
            public void run() {
                tick += 1;

                ArrayList<UUID> toRemove = new ArrayList<>();

                for (IScheduledTask task : tasks.values()) {
                    if (!task.canRun(tick)) {
                        continue;
                    }

                    task.run();
                    toRemove.add(task.getUUID());
                }

                for (UUID uuid : toRemove) {
                    tasks.remove(uuid);
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

        scheduler.shutdownNow();
        tasks.clear();
    }

    @Override
    public void addRunnable(Runnable runnable, Long delay, boolean isAsync) {
        UUID uuid = UUID.randomUUID();
        tasks.put(uuid, new ScheduledTask(uuid, runnable, tick + delay, isAsync));
    }
}
