package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Iterfaces.IPlayer;
import com.plummy.cyhunters.Player.Hunter;
import com.plummy.cyhunters.Player.Spectator;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class PrepareListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        IPlayer gamePlayer = getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (getMainGame().preparing()) {
            if (gamePlayer instanceof Spectator) {
                return;
            }

            if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
                e.setCancelled(true);
            }
        } else if (getMainGame().handicap()) {
            if (!(gamePlayer instanceof Hunter)) {
                return;
            }

            if (e.getTo() == null || !(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        onAction(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent e) {
        onAction(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) {
            return;
        }

        onAction(player, e);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        onAction(player, e);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        onAction(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        onAction(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        onAction(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        onAction((Player) e.getWhoClicked(), e);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        onAction(e.getPlayer(), e);
    }

    private static void onAction(Player player, Cancellable event) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        IPlayer gamePlayer = getMainGame().getPlayerManager().getPlayer(player.getUniqueId());

        if (getMainGame().preparing()) {
            if (gamePlayer instanceof Spectator) {
                return;
            }

            event.setCancelled(true);
        } else if (getMainGame().handicap()) {
            if (!(gamePlayer instanceof Hunter)) {
                return;
            }

            event.setCancelled(true);
        }
    }
}
