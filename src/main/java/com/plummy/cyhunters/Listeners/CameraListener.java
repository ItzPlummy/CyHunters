package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class CameraListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        IGamePlayer gamePlayer = getMainGame().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isAlive()) {
            if (e.getTo() == null || (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ())) {
                return;
            }

            getMainGame().getCameraManager().updateCameras(e.getPlayer().getUniqueId());
        } else if (gamePlayer.isDead()) {
            gamePlayer.getCamera().updateLocation();
        }
    }

    @EventHandler
    public void onPlayerMoveSlot(PlayerItemHeldEvent e) {
        IGamePlayer gamePlayer = getMainGame().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isDead()) {
            gamePlayer.getCamera().addYaw(getRotation(e.getPreviousSlot(), e.getNewSlot()) * 15);
            gamePlayer.getCamera().updateLocation();
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) {
            return;
        }

        IGamePlayer gamePlayer = getMainGame().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer.isDead()) {
            gamePlayer.switchSpectateTarget();
        }
    }

    private static int getRotation(int previous, int next) {
        if (previous == 8 && next == 0) {
            return 1;
        }

        if (previous == 0 && next == 8) {
            return -1;
        }

        return previous < next ? 1 : -1;
    }
}
