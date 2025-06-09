package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.PlayerState;
import org.bukkit.entity.Player;

public class GamePlayer implements IGamePlayer {
    private Player player;
    private PlayerState state;

    public GamePlayer(Player player) {
        this.player = player;
        this.state = PlayerState.PLAYING;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSpectating() {
        return state == PlayerState.SPECTATING;
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }
}
