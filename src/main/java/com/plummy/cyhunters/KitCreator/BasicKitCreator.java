package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Enums.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BasicKitCreator extends AbstractKitCreator {
    @Override
    public KitType getType() {
        return KitType.BASIC;
    }

    @Override
    public void createSpeedrunnerKit(Player player) {
        setItem(player, new ItemStack(Material.IRON_PICKAXE, 1), 0);
        setItem(player, new ItemStack(Material.IRON_AXE, 1), 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 16), 2);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 8), 8);

        setOffHand(player, new ItemStack(Material.SHIELD, 1));

        setArmor(player, new ItemStack(Material.IRON_HELMET), EquipmentSlot.HEAD);
        setArmor(player, new ItemStack(Material.IRON_CHESTPLATE), EquipmentSlot.CHEST);
        setArmor(player, new ItemStack(Material.IRON_BOOTS), EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        setItem(player, new ItemStack(Material.STONE_PICKAXE, 1), 0);
        setItem(player, new ItemStack(Material.STONE_AXE, 1), 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 16), 2);

        setItem(player, new ItemStack(Material.BREAD, 8), 8);

        setArmor(player, new ItemStack(Material.LEATHER_CHESTPLATE), EquipmentSlot.CHEST);
        setArmor(player, new ItemStack(Material.LEATHER_BOOTS), EquipmentSlot.FEET);
    }
}
