package com.plummy.cyhunters.KitCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShearsKitCreator extends AbstractKitCreator {
    @Override
    public void createSpeedrunnerKit(Player player) {
        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, getEnchantedItem(
                new ItemStack(Material.SHEARS, 1),
                List.of(Enchantment.UNBREAKING),
                List.of(5)
        ), 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 8), 2);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 4), 8);

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.IRON_BOOTS, 1),
                List.of(Enchantment.FEATHER_FALLING),
                List.of(4)
        ), EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, new ItemStack(Material.SHEARS, 1), 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 8), 2);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 4), 8);

        setArmor(player, getEnchantedItem(
                new ItemStack(Material.CHAINMAIL_BOOTS, 1),
                List.of(Enchantment.FEATHER_FALLING),
                List.of(4)
        ), EquipmentSlot.FEET);
    }
}
