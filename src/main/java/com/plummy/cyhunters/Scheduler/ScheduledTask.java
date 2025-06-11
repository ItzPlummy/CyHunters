package com.plummy.cyhunters.Scheduler;

import com.plummy.cyhunters.Iterfaces.IScheduledTask;
import org.bukkit.Bukkit;

import static com.plummy.cyhunters.CyHunters.getInstance;

public record ScheduledTask(Runnable runnable, Long delay, boolean isAsync) implements IScheduledTask {
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
