package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Player.Hunter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

        if (player.isSpectating()) {
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
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof Hunter player)) {
            return;
        }

        if (player.isSpectating()) {
            player.getCameraSelector().attachCamera();
        }
    }
}
