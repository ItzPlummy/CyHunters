package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.PlayerState;
import com.plummy.cyhunters.Assets.Enums.Role;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class GamePlayer implements IGamePlayer {
    private Player player;
    private Role role;
    private PlayerState state;

    public GamePlayer(Player player, PlayerState state) {
        this.player = player;
        this.state = state;
        this.role = Role.UNDEFINED;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isSpectating() {
        return state == PlayerState.SPECTATING;
    }

    @Override
    public boolean isOnline() {
        return player != null;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void updatePlayer(Player player) {
        this.player = player;
    }

    @Override
    public void ready(Location location, Role role) {
        if (!isOnline() || isSpectating()) {
            return;
        }

        this.role = role;

        reset();
        player.setRespawnLocation(location);

        getPlayer().teleport(location);
    }

    @Override
    public void reset() {
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
