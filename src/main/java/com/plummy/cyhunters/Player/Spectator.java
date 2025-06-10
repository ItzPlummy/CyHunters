package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.ISpectator;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Spectator extends AbstractPlayer implements ISpectator {
    public Spectator(UUID uuid, Player player) {
        super(uuid, player, PlayerState.SPECTATING);
    }
}
