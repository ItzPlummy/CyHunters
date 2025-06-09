package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.PlayerState;
import com.plummy.cyhunters.Assets.Enums.Role;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import com.plummy.cyhunters.Assets.Interfaces.IPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager implements IPlayerManager {
    private final Map<UUID, IGamePlayer> players;
    private IGamePlayer speedrunner = null;

    public PlayerManager() {
        players = new HashMap<>();
    }

    @Override
    public IGamePlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    @Override
    public boolean hasPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public List<IGamePlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    @Override
    public List<IGamePlayer> getActivePlayers() {
        return getPlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectating()).toList();
    }

    @Override
    public List<IGamePlayer> getAlivePlayers() {
        return getPlayers().stream().filter(gamePlayer -> !gamePlayer.isDead()).toList();
    }

    @Override
    public IGamePlayer getSpeedrunner() {
        return speedrunner;
    }

    @Override
    public List<IGamePlayer> getHunters() {
        return getPlayers().stream().filter(gamePlayer -> gamePlayer.getRole() == Role.HUNTER).toList();
    }

    @Override
    public int size() {
        return getPlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectating()).toList().size();
    }

    @Override
    public void joinPlayer(Player player, boolean hasStarted) {
        UUID uuid = player.getUniqueId();

        if (hasPlayer(uuid)) {
            IGamePlayer gamePlayer = players.get(uuid);

            gamePlayer.setPlayer(player);
            return;
        }

        IGamePlayer gamePlayer = new GamePlayer(player, hasStarted ? PlayerState.SPECTATING : PlayerState.PLAYING);
        players.put(uuid, gamePlayer);
    }

    @Override
    public void leavePlayer(UUID uuid, boolean hasStarted) {
        if (!hasPlayer(uuid)) {
            return;
        }

        IGamePlayer gamePlayer = players.get(uuid);

        if (!hasStarted || gamePlayer.isSpectating()) {
            players.remove(uuid);
            return;
        }

        gamePlayer.setPlayer(null);
    }

    @Override
    public void syncPlayers(boolean hasStarted) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<UUID> onlineUUIDs = onlinePlayers.stream().map(Player::getUniqueId).toList();

        for (IGamePlayer gamePlayer : getPlayers()) {
            if (onlineUUIDs.contains(gamePlayer.getPlayer().getUniqueId())) {
                continue;
            }

            this.leavePlayer(gamePlayer.getPlayer().getUniqueId(), hasStarted);
        }

        for (Player onlinePlayer : onlinePlayers) {
            if (hasPlayer(onlinePlayer.getUniqueId())) {
                continue;
            }

            this.joinPlayer(onlinePlayer, hasStarted);
        }
    }

    @Override
    public void distributeRoles() {
        List<IGamePlayer> players = getActivePlayers();

        this.speedrunner = players.get((int) (Math.random() * size()));

        for (IGamePlayer gamePlayer : players) {
            gamePlayer.setRole(Role.HUNTER);
        }

        speedrunner.setRole(Role.SPEEDRUNNER);
    }

    @Override
    public void ready(Location location) {
        double distance = 3;
        double angle = 0;

        for (IGamePlayer gamePlayer : getHunters()) {
            double x = location.getX() + distance * Math.cos(angle);
            double z = location.getZ() + distance * Math.sin(angle);

            gamePlayer.ready(Objects.requireNonNull(location.getWorld()).getHighestBlockAt((int) x, (int) z).getLocation().add(0.5, 1, 0.5));
            angle += 2 * Math.PI / (size() - 1);
        }

        getSpeedrunner().ready(location);
    }
}
