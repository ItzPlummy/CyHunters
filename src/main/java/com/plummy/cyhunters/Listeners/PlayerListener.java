package com.plummy.cyhunters.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (getMainGame().hasStarted()) {
            return;
        }

        e.setCancelled(true);
    }
}
