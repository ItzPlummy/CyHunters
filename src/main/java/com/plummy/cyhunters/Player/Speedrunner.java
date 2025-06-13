package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.ISpeedrunner;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Speedrunner extends AbstractPlayer implements ISpeedrunner {
    private final String name;

    public Speedrunner(UUID uuid, Player player) {
        super(uuid, player, PlayerState.PLAYING);
        this.name = player.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void ready(Location location) {
        if (!isOnline()) {
            return;
        }

        reset();

        getPlayer().teleport(location);
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, true, false, false));
    }
}
