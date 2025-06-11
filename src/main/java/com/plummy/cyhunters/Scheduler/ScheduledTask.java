package com.plummy.cyhunters.Scheduler;

import com.plummy.cyhunters.Iterfaces.IScheduledTask;
import org.bukkit.Bukkit;

import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getInstance;

public record ScheduledTask(UUID uuid, Runnable runnable, Long delay, boolean isAsync) implements IScheduledTask {
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean canRun(Long tick) {
        return tick >= delay;
    }

    @Override
    public void run() {
        if (isAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);
        } else {
            Bukkit.getScheduler().runTask(getInstance(), runnable);
        }
    }
}
