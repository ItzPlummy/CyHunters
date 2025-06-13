package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.ISpectator;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Spectator extends AbstractPlayer implements ISpectator {
    public Spectator(UUID uuid, Player player) {
        super(uuid, player, PlayerState.SPECTATING);
    }

    public void applySpectator() {
        getPlayer().setGameMode(GameMode.SPECTATOR);
        getPlayer().sendMessage("§cGame has already started, you have become a spectator.");
    }
}
