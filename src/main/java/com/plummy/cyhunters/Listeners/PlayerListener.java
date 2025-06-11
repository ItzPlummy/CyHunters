package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Iterfaces.ISpeedrunner;
import com.plummy.cyhunters.Player.Speedrunner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getEntity().getUniqueId()) instanceof Speedrunner)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> getMainGame().stop(GameEndingReason.HUNTER_WINS, null), 20L);
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

        int respawnDelay = getInstance().getConfig().getInt("parameters.game.respawn-delay");

        getMainGame().getScheduler().addRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                player.setPlaying();
            }
        }, (long) respawnDelay, false);

        for (long index = 1; index <= respawnDelay; index++) {
            long finalIndex = index;

            getMainGame().getScheduler().addRunnable(new BukkitRunnable() {
                @Override
                public void run() {
                    String text = finalIndex == respawnDelay ? "§f" : "§cRespawn in " + (respawnDelay - finalIndex);

                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
                }
            }, index, true);
        }
    }

    @EventHandler
    public void onPlayerKillDragon(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof EnderDragon)) {
            return;
        }

        if (!getMainGame().hasStarted()) {
            return;
        }

        Player player = e.getEntity().getKiller();

        if (player == null) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(player.getUniqueId()) instanceof ISpeedrunner)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> getMainGame().stop(GameEndingReason.SPEEDRUNNER_WINS, player), 20L);
    }
}
