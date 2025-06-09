package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.PlayerState;
import org.bukkit.entity.Player;

public class GamePlayer implements IGamePlayer {
    private Player player;
    private PlayerState state;

    public GamePlayer(Player player, PlayerState state) {
        this.player = player;
        this.state = state;
    }

    public boolean isSpectating() {
        return state == PlayerState.SPECTATING;
    }

    public boolean isOnline() {
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }
}
