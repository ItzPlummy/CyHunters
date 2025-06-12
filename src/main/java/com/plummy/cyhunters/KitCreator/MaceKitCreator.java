package com.plummy.cyhunters.KitCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MaceKitCreator extends AbstractKitCreator {
    @Override
    public void createSpeedrunnerKit(Player player) {
        setItem(player, new ItemStack(Material.DIAMOND_SWORD, 1), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.MACE, 1),
                List.of(Enchantment.DENSITY, Enchantment.WIND_BURST),
                List.of(4, 1)
        ), 1);
        setItem(player, new ItemStack(Material.WIND_CHARGE, 64), 2);
        setItem(player, new ItemStack(Material.WIND_CHARGE, 64), 3);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 16), 4);
        setItem(player, new ItemStack(Material.SMOOTH_SANDSTONE, 64), 5);
        setItem(player, new ItemStack(Material.SMOOTH_SANDSTONE, 64), 6);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.COOKED_BEEF, 16), 8);

        setOffHand(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        setItem(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1), 9);
        setItem(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1), 10);

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.CHEST);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_BOOTS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        setItem(player, new ItemStack(Material.IRON_SWORD, 1), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.MACE, 1),
                List.of(Enchantment.DENSITY, Enchantment.WIND_BURST),
                List.of(2, 1)
        ), 1);
        setItem(player, new ItemStack(Material.WIND_CHARGE, 64), 2);
        setItem(player, new ItemStack(Material.WIND_CHARGE, 64), 3);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 4), 4);
        setItem(player, new ItemStack(Material.SANDSTONE, 64), 5);
        setItem(player, new ItemStack(Material.SANDSTONE, 64), 6);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.COOKED_BEEF, 16), 8);

        setOffHand(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1));

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.CHEST);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_BOOTS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(4)
        ), EquipmentSlot.FEET);
    }
}
