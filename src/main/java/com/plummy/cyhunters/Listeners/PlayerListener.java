package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getMainGame().joinPlayer(e.getPlayer());
        getMainGame().sync();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        getMainGame().leavePlayer(e.getPlayer().getUniqueId());
        getMainGame().sync();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
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

        if (getMainGame().getPlayer(e.getPlayer().getUniqueId()).isSpectating()) {
            return;
        }

        if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        IGamePlayer gamePlayer = getMainGame().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isSpectating()) {
            return;
        }

        gamePlayer.die();
    }
}
