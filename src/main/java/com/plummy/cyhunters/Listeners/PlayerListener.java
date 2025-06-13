package com.plummy.cyhunters.Listeners;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Game.NormalGame;
import com.plummy.cyhunters.Iterfaces.IHunter;
import com.plummy.cyhunters.Iterfaces.ISpeedrunner;
import com.plummy.cyhunters.Player.Hunter;
import com.plummy.cyhunters.Player.Speedrunner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.*;

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

        if (getMainGame().prepared()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }
        if (getMainGame().getPlayerManager().getPlayer(e.getEntity().getUniqueId()) instanceof ISpeedrunner) {
            Bukkit.getScheduler().runTaskLater(getInstance(), () -> getMainGame().stop(null, GameEndingReason.HUNTER_WINS), 20L);
            return;
        }

        if (getMainGame().getPlayerManager().getPlayer(e.getEntity().getUniqueId()) instanceof Hunter) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!getMainGame().hasStarted()) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) instanceof IHunter player)) {
            return;
        }

        if (Objects.requireNonNull(e.getRespawnLocation().getWorld()).getEnvironment() == World.Environment.NORMAL && getMainGame().getLocationFinder().getDimension() == GameDimension.NETHER) {
            World world = Bukkit.getWorlds().stream().filter(w -> w.getEnvironment() == World.Environment.NETHER).findFirst().orElse(null);

            if (world != null) {
                e.setRespawnLocation(world.getSpawnLocation());
            }
        }

        Bukkit.getScheduler().runTaskLater(getInstance(), player::setSpectating, 1L);

        int respawnDelay = config().getInt("parameters.game.respawn-delay");

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

        if (!(getMainGame() instanceof NormalGame)) {
            return;
        }

        Player player = e.getEntity().getKiller();

        if (player == null) {
            return;
        }

        if (!(getMainGame().getPlayerManager().getPlayer(player.getUniqueId()) instanceof ISpeedrunner)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> getMainGame().stop(player, GameEndingReason.SPEEDRUNNER_WINS), 20L);
    }
}
