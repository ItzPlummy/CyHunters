package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Camera.Camera;
import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Game.ItemManager;
import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.IHunter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class Hunter extends AbstractPlayer implements IHunter {
    private final ICamera camera;
    private Location spawnLocation;

    public Hunter(UUID uuid, Player player) {
        super(uuid, player, PlayerState.PLAYING);

        this.camera = new Camera(this);
        this.spawnLocation = null;
    }

    @Override
    public ICamera getCamera() {
        return camera;
    }

    @Override
    public boolean isSpectating() {
        return getState() == PlayerState.SPECTATING;
    }

    @Override
    public void setSpectating() {
        if (!isOnline() || isSpectating()) {
            return;
        }

        setState(PlayerState.SPECTATING);
        getPlayer().setGameMode(GameMode.SPECTATOR);
        spawnLocation = getPlayer().getLocation();

        getMainGame().getCameraManager().detachCameras(getPlayer().getUniqueId());
        getCamera().attach();

        getPlayer().sendTitle("§c§lYou Died", "§4Respawn in 30 seconds", 0, 40, 60);
        getPlayer().playSound(getPlayer(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 0.5f);
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0, true, false, false));
    }

    @Override
    public void setPlaying() {
        if (!isOnline() || !isSpectating()) {
            return;
        }

        setState(PlayerState.PLAYING);
        getPlayer().setGameMode(GameMode.SURVIVAL);

        if (spawnLocation != null) {
            getPlayer().teleport(spawnLocation);
            spawnLocation = null;
        }

        getCamera().detach();

        getPlayer().sendTitle("§a§lYou Respawned", "", 0, 40, 60);
        getPlayer().playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);

        if (getMainGame().debuted()) {
            getPlayer().getInventory().addItem(ItemManager.createCompass());
        }
    }

    @Override
    public void ready(Location location) {
        if (!isOnline()) {
            return;
        }

        reset();

        getPlayer().teleport(location);
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0, true, false, false));
    }

    @Override
    public void giveCompass() {
        if (!isOnline()) {
            return;
        }

        getPlayer().getInventory().addItem(ItemManager.createCompass());
    }
}
