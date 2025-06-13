package com.plummy.cyhunters.LocationFinder;

import com.plummy.cyhunters.Enums.GameDimension;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BiomeSearchResult;

import java.util.List;
import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.config;

public class NetherLocationFinder extends AbstractLocationFinder {
    private static final List<Structure> searchStructures = List.of(
            Structure.BASTION_REMNANT,
            Structure.FORTRESS
    );

    private static final List<Biome> searchBiomes = List.of(
            Biome.CRIMSON_FOREST,
            Biome.WARPED_FOREST
    );

    private static final List<Material> permittedMaterials = List.of(
            Material.LAVA,
            Material.MAGMA_BLOCK
    );

    @Override
    public GameDimension getDimension() {
        return GameDimension.NETHER;
    }

    @Override
    public Location findLocation() {
        World world = Bukkit.getWorlds().stream().filter(w -> w.getEnvironment() == World.Environment.NETHER).findFirst().orElse(null);

        if (world == null) {
            return null;
        }

        Location structureLocation;

        int minDistance = config().getInt("parameters.spawn.min-offset-distance");
        int maxDistance = config().getInt("parameters.spawn.max-offset-distance");

        for (int i = 0; i < 5; i++) {
            structureLocation = findStructureLocation(world, searchStructures);

            if (structureLocation == null) {
                continue;
            }

            int x = structureLocation.getBlockX();
            int z = structureLocation.getBlockZ();

            Biome biome = structureLocation.getBlock().getBiome();

            if (biome != Biome.NETHER_WASTES) {
                continue;
            }

            if (!verifyBiomeAvailability(structureLocation, maxDistance)) {
                continue;
            }

            for (int j = 0; j < 10; j++) {
                double distance = random.nextDouble(minDistance, maxDistance);
                double angle = random.nextDouble(2 * Math.PI);

                Block block = world.getHighestBlockAt((int) (x + distance * Math.cos(angle)), (int) (z + distance * Math.sin(angle)));

                if (!verifyLocationInRadius(block.getLocation())) {
                    continue;
                }

                return block.getLocation().add(0.5, 1, 0.5);
            }
        }

        return null;
    }

    private static boolean verifyLocationInRadius(Location location) {
        int distance = config().getInt("parameters.spawn.hunter-spawn-distance") + 1;
        int maxHeightDifference = config().getInt("parameters.spawn.max-height-difference");

        int minY = location.getBlockY();
        int maxY = location.getBlockY();

        for (int x = -distance; x <= distance; x++) {
            for (int z = -distance; z <= distance; z++) {
                Block block = getNetherHighestBlockAt(location.getWorld(), location.getBlockX() + x, location.getBlockZ() + z);

                if (block == null) {
                    continue;
                }

                if (permittedMaterials.contains(block.getType())) {
                    continue;
                }

                minY = Math.min(minY, block.getY());
                maxY = Math.max(maxY, block.getY());
            }
        }

        return maxY - minY <= maxHeightDifference;
    }

    private static boolean verifyBiomeAvailability(Location location, int maxOffsetDistance) {
        for (Biome biome : searchBiomes) {
            BiomeSearchResult searchResult = Objects.requireNonNull(location.getWorld()).locateNearestBiome(location, maxOffsetDistance, biome);

            if (searchResult != null) {
                return true;
            }
        }

        return false;
    }

    private static Block getNetherHighestBlockAt(World world, int x, int z) {
        boolean foundAir = false;

        for (int y = 127; y >= 0; y--) {
            Block block = world.getBlockAt(x, y, z);

            if (block.getType() == Material.AIR) {
                foundAir = true;
            } else if (foundAir) {
                return block;
            }
        }

        return null;
    }
}
