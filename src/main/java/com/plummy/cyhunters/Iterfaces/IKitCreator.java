package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.KitType;
import org.bukkit.entity.Player;

public interface IKitCreator {
    KitType getType();
    void createSpeedrunnerKit(Player player);
    void createHunterKit(Player player);
}
