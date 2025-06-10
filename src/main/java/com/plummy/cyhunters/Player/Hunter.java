package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Camera.Camera;
import com.plummy.cyhunters.Camera.CameraSelector;
import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.ICamera;
import com.plummy.cyhunters.Iterfaces.ICameraSelector;
import com.plummy.cyhunters.Iterfaces.IHunter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static com.plummy.cyhunters.CyHunters.getInstance;
import static com.plummy.cyhunters.CyHunters.getMainGame;

public class Hunter extends AbstractPlayer implements IHunter {
    private final ICamera camera;
    private final ICameraSelector cameraSelector;

    public Hunter(UUID uuid, Player player) {
        super(uuid, player, PlayerState.PLAYING);

        camera = new Camera(this);
        cameraSelector = new CameraSelector(this.camera);
    }

    @Override
    public ICamera getCamera() {
        return camera;
    }

    @Override
    public ICameraSelector getCameraSelector() {
        return cameraSelector;
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
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> getPlayer().setGameMode(GameMode.SPECTATOR), 1L);

        getMainGame().getCameraManager().detachAllCameras(getPlayer().getUniqueId());
        getCameraSelector().attachCamera();
    }

    @Override
    public void setPlaying() {

    }

    @Override
    public void ready(Location location) {
        if (!isOnline()) {
            return;
        }

        reset();

        getPlayer().setRespawnLocation(location, true);
        getPlayer().teleport(location);

        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0, true, false, false));
    }
}
