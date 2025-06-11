package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Player.Spectator;
import com.plummy.cyhunters.Player.Speedrunner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.plummy.cyhunters.CyHunters.getInstance;
import static com.plummy.cyhunters.CyHunters.getMainGame;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        getMainGame().joinPlayer(e.getPlayer());
        getMainGame().sync();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        getMainGame().leavePlayer(e.getPlayer().getUniqueId());
        getMainGame().sync();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        getMainGame().leavePlayer(e.getPlayer().getUniqueId());
        getMainGame().sync();
    }

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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!getMainGame().preparing()) {
            return;
        }

        if (getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof Spectator) {
            return;
        }

        if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getEntity().getUniqueId()) instanceof Speedrunner)) {
            return;
        }

        getMainGame().getScheduler().addRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                getMainGame().stop(GameEndingReason.HUNTER_WINS, null);
            }
        }, 1L, false);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof IHunter player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(getInstance(), player::setSpectating, 1L);

        getMainGame().getScheduler().addRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                player.setPlaying();
            }
        }, 10L, false);
    }
}
