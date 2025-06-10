package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.ISpeedrunner;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Speedrunner extends AbstractPlayer implements ISpeedrunner {
    public Speedrunner(UUID uuid, Player player) {
        super(uuid, player, PlayerState.PLAYING);
    }

    @Override
    public void ready(Location location) {
        if (isLeft()) {
            return;
        }

        reset();

        getPlayer().setRespawnLocation(location, true);
        getPlayer().teleport(location);
    }
}
