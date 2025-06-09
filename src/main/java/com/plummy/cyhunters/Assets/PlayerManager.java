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

    public PlayerManager() {
        players = new HashMap<>();
    }

    @Override
    public List<IGamePlayer> getPlayers() {
        return new ArrayList<>(players.values());
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
    public int size() {
        return players.size();
    }

    @Override
    public void joinPlayer(Player player, boolean hasStarted) {
        UUID uuid = player.getUniqueId();

        if (hasPlayer(uuid)) {
            IGamePlayer gamePlayer = players.get(uuid);

            gamePlayer.updatePlayer(player);
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

        gamePlayer.updatePlayer(null);
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

    public void ready(Location location) {
        int speedrunnerIndex = (int) (Math.random() * size());

        int index = 0;
        double distance = 3;
        double angle = 0;

        List<IGamePlayer> players = getPlayers();
        Collections.shuffle(players);

        for (IGamePlayer gamePlayer : players) {
            if (!gamePlayer.isOnline() || gamePlayer.isSpectating()) {
                continue;
            }

            if (index == speedrunnerIndex) {
                gamePlayer.ready(location, Role.SPEEDRUNNER);
            } else {
                double x = location.getX() + distance * Math.cos(angle);
                double z = location.getZ() + distance * Math.sin(angle);

                gamePlayer.ready(Objects.requireNonNull(location.getWorld()).getHighestBlockAt((int) x, (int) z).getLocation().add(0.5, 1, 0.5), Role.HUNTER);
                angle += 2 * Math.PI / (size() - 1);
            }

            index++;
        }
    }
}
