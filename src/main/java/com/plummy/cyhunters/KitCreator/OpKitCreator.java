package com.plummy.cyhunters.KitCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpKitCreator extends AbstractKitCreator {
    @Override
    public void createSpeedrunnerKit(Player player) {
        setItem(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_SWORD, 1),
                List.of(Enchantment.SHARPNESS),
                List.of(5)
        ), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_PICKAXE, 1),
                List.of(Enchantment.EFFICIENCY),
                List.of(5)
        ), 1);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_AXE, 1),
                List.of(Enchantment.EFFICIENCY, Enchantment.SHARPNESS),
                List.of(5, 5)
        ), 2);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.BOW, 1),
                List.of(Enchantment.POWER, Enchantment.PUNCH, Enchantment.FLAME),
                List.of(5, 2, 1)
        ), 3);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 8), 4);
        setItem(player, new ItemStack(Material.SMOOTH_SANDSTONE, 64), 5);
        setItem(player, new ItemStack(Material.SMOOTH_SANDSTONE, 64), 6);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 16), 8);

        setOffHand(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        setItem(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1), 9);
        setItem(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1), 10);
        setItem(player, new ItemStack(Material.ARROW, 64), 27);
        setItem(player, new ItemStack(Material.ARROW, 64), 28);

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION, Enchantment.THORNS),
                List.of(3, 2)
        ), EquipmentSlot.CHEST);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_LEGGINGS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.LEGS);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.NETHERITE_BOOTS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        setItem(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_SWORD, 1),
                List.of(Enchantment.SHARPNESS),
                List.of(4)
        ), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_PICKAXE, 1),
                List.of(Enchantment.EFFICIENCY),
                List.of(3)
        ), 1);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_AXE, 1),
                List.of(Enchantment.EFFICIENCY, Enchantment.SHARPNESS),
                List.of(3, 4)
        ), 2);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.BOW, 1),
                List.of(Enchantment.POWER, Enchantment.PUNCH),
                List.of(4, 1)
        ), 3);
        setItem(player, new ItemStack(Material.GOLDEN_APPLE, 4), 4);
        setItem(player, new ItemStack(Material.SANDSTONE, 64), 5);
        setItem(player, new ItemStack(Material.SANDSTONE, 64), 6);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 16), 8);

        setOffHand(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        setItem(player, new ItemStack(Material.ARROW, 64), 27);

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_HELMET, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.HEAD);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                List.of(Enchantment.PROTECTION, Enchantment.THORNS),
                List.of(3, 2)
        ), EquipmentSlot.CHEST);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.LEGS);
        setArmor(player, getEnchantedItem(
                new ItemStack(Material.DIAMOND_BOOTS, 1),
                List.of(Enchantment.PROTECTION),
                List.of(3)
        ), EquipmentSlot.FEET);
    }
}
