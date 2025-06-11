package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Player.Hunter;
import com.plummy.cyhunters.Player.Spectator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class PrepareListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (getMainGame().preparing()) {
            if (getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof Spectator) {
                return;
            }

            if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
                e.setCancelled(true);
            }
        } else if (getMainGame().handicap()) {
            if (!(getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof Hunter)) {
                return;
            }

            if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
                e.setCancelled(true);
            }
        }
    }
}
