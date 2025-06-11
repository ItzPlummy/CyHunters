package com.plummy.cyhunters.Player;

import com.plummy.cyhunters.Enums.PlayerState;
import com.plummy.cyhunters.Iterfaces.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Iterator;
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
    public boolean isOnline() {
        return player != null;
    }

    protected PlayerState getState() {
        return state;
    }

    protected void setState(PlayerState state) {
        this.state = state;
    }

    protected void reset() {
        if (!isOnline()) {
            return;
        }

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

        Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();
        while (advancements.hasNext()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancements.next());
            for (String criteria : progress.getAwardedCriteria()) {
                progress.revokeCriteria(criteria);
            }
        }

        getPlayer().setRespawnLocation(null, true);
        player.setGameMode(GameMode.SURVIVAL);
    }
}
