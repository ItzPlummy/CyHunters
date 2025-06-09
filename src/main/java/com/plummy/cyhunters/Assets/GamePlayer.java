package com.plummy.cyhunters.Assets;

import com.google.j2objc.annotations.ObjectiveCName;
import com.plummy.cyhunters.Assets.Enums.PlayerState;
import com.plummy.cyhunters.Assets.Enums.Role;
import com.plummy.cyhunters.Assets.Interfaces.ICameraHolder;
import com.plummy.cyhunters.Assets.Interfaces.IGamePlayer;
import com.plummy.cyhunters.Assets.Interfaces.ICamera;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class GamePlayer implements IGamePlayer {
    private Player player;
    private Role role;
    private PlayerState state;

    private final ICamera camera;
    private final ICameraHolder cameraHolder;

    public GamePlayer(Player player, PlayerState state) {
        this.player = player;
        this.state = state;
        this.role = Role.UNDEFINED;

        this.camera = new Camera(this);
        this.cameraHolder = new CameraHolder(this);
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
    public boolean isAlive() {
        return state != PlayerState.DEAD && state != PlayerState.SPECTATING;
    }

    @Override
    public boolean isDead() {
        return state == PlayerState.DEAD;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public ICamera getCamera() {
        return camera;
    }

    @Override
    public ICameraHolder getCameraHolder() {
        return cameraHolder;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void ready(Location location) {
        if (!isOnline() || isSpectating()) {
            return;
        }

        reset();

        player.setRespawnLocation(location, true);
        getPlayer().teleport(location);

        if (role == Role.HUNTER) {
            Objects.requireNonNull(player.getPlayer()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0, true, false, false));
        }
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

    @Override
    public void die() {
        if (!isOnline() || isSpectating() || isDead()) {
            return;
        }

        state = PlayerState.DEAD;
        getPlayer().setGameMode(GameMode.SPECTATOR);

        ICameraHolder cameraHolder = getMainGame().getRandomCameraHolder();

        cameraHolder.attachCamera(camera);
        cameraHolder.updateCameras();
    }
}
