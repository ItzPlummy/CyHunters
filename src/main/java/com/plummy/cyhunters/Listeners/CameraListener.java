package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Iterfaces.IGamePlayer;
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

        IGamePlayer gamePlayer = getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isAlive()) {
            if (e.getTo() == null || (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ())) {
                return;
            }

            getMainGame().getCameraManager().updateCameras(e.getPlayer().getUniqueId());
        } else if (gamePlayer.isDead()) {
            e.setCancelled(true);
            gamePlayer.getCamera().updateLocation();
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

        IGamePlayer gamePlayer = getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isDead()) {
            gamePlayer.getCameraSelector().attachCamera();
        }
    }
}
