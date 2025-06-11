package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Iterfaces.IHunter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static com.plummy.cyhunters.CyHunters.getMainGame;
import static com.plummy.cyhunters.CyHunters.getNamespacedKey;

public class ItemManager {
    public static ItemStack createCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

        assert compassMeta != null;

        compassMeta.setDisplayName("§c§lHunter's Compass");
        compassMeta.setLore(List.of("§4Points to speedrunner's location"));
        compassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        compassMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        compassMeta.getPersistentDataContainer().set(getNamespacedKey(), PersistentDataType.STRING, "compass");

        compass.setItemMeta(compassMeta);
        return compass;
    }

    public static void updateCompasses() {
        if (!getMainGame().hasStarted()) {
            return;
        }

        for (IHunter hunter : getMainGame().getPlayerManager().getHunters()) {
            ItemStack item = hunter.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() != Material.COMPASS) {
                return;
            }

            CompassMeta compassMeta = (CompassMeta) item.getItemMeta();
            assert compassMeta != null;

            String pdc = compassMeta.getPersistentDataContainer().get(getNamespacedKey(), PersistentDataType.STRING);
            if (pdc == null || !pdc.equals("compass")) {
                return;
            }

            hunter.getPlayer().setCompassTarget(getMainGame().getPlayerManager().getSpeedrunner().getPlayer().getLocation());
        }
    }
}