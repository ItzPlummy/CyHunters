package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Enums.KitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BowKitCreator extends AbstractKitCreator {
    @Override
    public KitType getType() {
        return KitType.BOW;
    }

    @Override
    public void createSpeedrunnerKit(Player player) {
        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.BOW, 1),
                List.of(Enchantment.POWER, Enchantment.PUNCH),
                List.of(2, 1)
        ), 1);
        setItem(player, new ItemStack(Material.ARROW, 64), 9);
        setItem(player, new ItemStack(Material.ARROW, 64), 10);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 4), 8);

        setOffHand(player, new ItemStack(Material.SHIELD, 1));

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(2)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION),
                List.of(2)
        ), EquipmentSlot.CHEST);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_BOOTS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(2)
        ), EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.BOW, 1),
                List.of(Enchantment.POWER),
                List.of(1)
        ), 1);
        setItem(player, new ItemStack(Material.ARROW, 32), 9);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 4), 8);

        setOffHand(player, new ItemStack(Material.SHIELD, 1));

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.CHAINMAIL_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(2)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION),
                List.of(2)
        ), EquipmentSlot.CHEST);
    }
}
