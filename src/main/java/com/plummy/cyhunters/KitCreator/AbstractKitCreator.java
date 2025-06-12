package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Iterfaces.IKitCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractKitCreator implements IKitCreator {
    protected static void setItem(Player player, ItemStack item, int slot) {
        if (player == null) {
            return;
        }

        player.getInventory().setItem(slot, item);
    }

    protected static void setOffHand(Player player, ItemStack item) {
        if (player == null) {
            return;
        }

        player.getInventory().setItemInOffHand(item);
    }

    protected static void setArmor(Player player, ItemStack item, EquipmentSlot slot) {
        if (player == null) {
            return;
        }

        player.getInventory().setItem(slot, item);
    }
}
