package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Iterfaces.IPlayer;
import com.plummy.cyhunters.Player.Spectator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

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
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        IPlayer gamePlayer = getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isSpectating()) {
            return;
        }

        gamePlayer.die();
    }
}
