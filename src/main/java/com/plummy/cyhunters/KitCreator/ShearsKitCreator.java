package com.plummy.cyhunters.KitCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShearsKitCreator extends AbstractKitCreator {
    @Override
    public void createSpeedrunnerKit(Player player) {
        ItemStack shears = new ItemStack(Material.SHEARS, 1);
        ItemMeta shearsMeta = shears.getItemMeta();
        assert shearsMeta != null;
        shearsMeta.addEnchant(Enchantment.UNBREAKING, 5, true);
        shears.setItemMeta(shearsMeta);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        ItemMeta bootsMeta = boots.getItemMeta();
        assert bootsMeta != null;
        bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 4, true);
        boots.setItemMeta(bootsMeta);

        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, shears, 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 8), 2);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 4), 8);

        setArmor(player, boots, EquipmentSlot.FEET);
    }

    @Override
    public void createHunterKit(Player player) {
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        ItemMeta bootsMeta = boots.getItemMeta();
        assert bootsMeta != null;
        bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 4, true);
        boots.setItemMeta(bootsMeta);

        setItem(player, new ItemStack(Material.STONE_AXE, 1), 0);
        setItem(player, new ItemStack(Material.SHEARS, 1), 1);
        setItem(player, new ItemStack(Material.OAK_LOG, 8), 2);
        setItem(player, new ItemStack(Material.WATER_BUCKET, 1), 7);
        setItem(player, new ItemStack(Material.BREAD, 4), 8);

        setArmor(player, boots, EquipmentSlot.FEET);
    }
}
