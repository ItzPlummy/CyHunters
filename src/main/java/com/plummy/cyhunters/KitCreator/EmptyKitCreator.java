package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Enums.KitType;
import org.bukkit.entity.Player;

public class EmptyKitCreator extends AbstractKitCreator {
    @Override
    public KitType getType() {
        return KitType.EMPTY;
    }

    @Override
    public void createSpeedrunnerKit(Player player) {}

    @Override
    public void createHunterKit(Player player) {}
}
