package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Player.Hunter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class CameraListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof Hunter player)) {
            return;
        }

        if (player.isSpectating() && player.getCamera().isAttached()) {
            e.setCancelled(true);
            player.getCamera().updateLocation();
        } else {
            if (e.getTo() == null || (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ())) {
                return;
            }

            getMainGame().getCameraManager().updateCameras(player.getUUID());
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        onClick(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerClickEntity(PlayerInteractEntityEvent e) {
        onClick(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerClickAtEntity(PlayerInteractAtEntityEvent e) {
        onClick(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerEnterSpectatorView(PlayerTeleportEvent e) {
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            return;
        }

        onClick(e.getPlayer(), e);
    }

    private static void onClick(Player player, Cancellable event) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(player.getUniqueId()) instanceof Hunter hunter)) {
            return;
        }

        if (hunter.isSpectating()) {
            event.setCancelled(true);
            hunter.getCamera().attach();
        }
    }
}
