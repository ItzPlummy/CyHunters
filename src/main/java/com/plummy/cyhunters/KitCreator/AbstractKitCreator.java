package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Iterfaces.IKitCreator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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

    protected static ItemStack getEnchantedItem(ItemStack item, List<Enchantment> enchantments, List<Integer> levels) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        for (int i = 0; i < enchantments.size(); i++) {
            meta.addEnchant(enchantments.get(i), levels.get(i), true);
        }

        item.setItemMeta(meta);
        return item;
    }
}
