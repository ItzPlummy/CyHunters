package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.IPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.UUID;

public abstract class AbstractPlayer implements IPlayer {
    private final UUID uuid;
    private Player player;
    private PlayerState state;

    public AbstractPlayer(UUID uuid, Player player, PlayerState state) {
        this.uuid = uuid;
        this.player = player;
        this.state = state;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isLeft() {
        return player == null;
    }

    protected PlayerState getState() {
        return state;
    }

    protected void setState(PlayerState state) {
        this.state = state;
    }

    protected void reset() {
        Player player = getPlayer();

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setVelocity(new Vector());
        player.setFlying(false);
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setGameMode(GameMode.SURVIVAL);
    }
}
