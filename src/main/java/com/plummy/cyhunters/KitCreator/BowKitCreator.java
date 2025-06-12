package com.plummy.cyhunters.KitCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BowKitCreator extends AbstractKitCreator {
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

        setArmor(player, new ItemStack(Material.IRON_HELMET), EquipmentSlot.HEAD);
        setArmor(player, new ItemStack(Material.IRON_CHESTPLATE), EquipmentSlot.CHEST);
        setArmor(player, new ItemStack(Material.IRON_BOOTS), EquipmentSlot.FEET);
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

        setArmor(player, new ItemStack(Material.LEATHER_CHESTPLATE), EquipmentSlot.CHEST);
        setArmor(player, new ItemStack(Material.LEATHER_BOOTS), EquipmentSlot.FEET);
    }
}
