package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Iterfaces.*;
import com.plummy.cyhunters.LocationFinder.NetherLocationFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

import static com.plummy.cyhunters.CyHunters.config;

public class PlayerManager implements IPlayerManager {
    private final Map<UUID, IPlayer> players;

    private UUID speedrunnerUUID;

    public PlayerManager() {
        players = new HashMap<>();

        speedrunnerUUID = null;
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    @Override
    public boolean hasPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return getPlayers().stream().filter(IPlayer::isOnline).toList();
    }

    @Override
    public List<IPlayer> getActivePlayers() {
        return getPlayers().stream().filter(player -> !(player instanceof Spectator)).toList();
    }

    @Override
    public List<IHunter> getHunters() {
        return getPlayers().stream().filter(player -> player instanceof IHunter).map(player -> (IHunter) player).toList();
    }

    @Override
    public List<ISpectator> getSpectators() {
        return getPlayers().stream().filter(player -> player instanceof ISpectator).map(player -> (ISpectator) player).toList();
    }

    @Override
    public ISpeedrunner getSpeedrunner() {
        return getPlayer(speedrunnerUUID) instanceof ISpeedrunner ? (ISpeedrunner) getPlayer(speedrunnerUUID) : null;
    }

    @Override
    public int size() {
        return getActivePlayers().size();
    }

    @Override
    public void setPlayers(List<Player> players, Player speedrunner) {
        resetPlayers();
        speedrunnerUUID = speedrunner != null ? speedrunner.getUniqueId() : players.get((int) (Math.random() * players.size())).getUniqueId();

        for (Player player : players) {
            if (player.getUniqueId().equals(speedrunnerUUID)) {
                addSpeedrunner(player);
            } else {
                addHunter(player);
            }
        }
    }

    @Override
    public void resetPlayers() {
        this.players.clear();
    }

    @Override
    public void joinPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        if (hasPlayer(uuid)) {
            getPlayer(uuid).setPlayer(player);
        } else {
            addSpectator(player);
            ((ISpectator) getPlayer(uuid)).applySpectator();
        }
    }

    @Override
    public void leavePlayer(UUID uuid) {
        if (!hasPlayer(uuid)) {
            return;
        }

        IPlayer player = getPlayer(uuid);

        if (player instanceof ISpectator) {
            removePlayer(uuid);
        } else {
            player.setPlayer(null);
        }
    }

    @Override
    public void syncPlayers() {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<UUID> onlineUUIDs = onlinePlayers.stream().map(Player::getUniqueId).toList();

        for (UUID uuid : players.keySet()) {
            if (onlineUUIDs.contains(uuid)) {
                continue;
            }

            this.leavePlayer(uuid);
        }

        for (Player onlinePlayer : onlinePlayers) {
            if (hasPlayer(onlinePlayer.getUniqueId())) {
                continue;
            }

            this.joinPlayer(onlinePlayer);
        }
    }

    @Override
    public void ready(Location location) {
        int distance = config().getInt("parameters.spawn.hunter-spawn-distance");
        double angle = 0;

        for (IHunter hunter : getHunters()) {
            double x = location.getX() + distance * Math.cos(angle);
            double z = location.getZ() + distance * Math.sin(angle);

            Block block;
            if (Objects.requireNonNull(location.getWorld()).getEnvironment() == World.Environment.NETHER) {
                block = NetherLocationFinder.getNetherHighestBlockAt(Objects.requireNonNull(location.getWorld()), (int) x, (int) z);
            } else {
                block = Objects.requireNonNull(location.getWorld()).getHighestBlockAt((int) x, (int) z);
            }

            assert block != null;
            hunter.ready(block.getLocation().add(0.5, 1, 0.5));

            angle += 2 * Math.PI / (size() - 1);
        }

        getSpeedrunner().ready(location);
    }

    private void addSpeedrunner(Player player) {
        this.players.put(player.getUniqueId(), new Speedrunner(player.getUniqueId(), player));
    }

    private void addHunter(Player player) {
        this.players.put(player.getUniqueId(), new Hunter(player.getUniqueId(), player));
    }

    private void addSpectator(Player player) {
        this.players.put(player.getUniqueId(), new Spectator(player.getUniqueId(), player));
    }

    private void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }
}
