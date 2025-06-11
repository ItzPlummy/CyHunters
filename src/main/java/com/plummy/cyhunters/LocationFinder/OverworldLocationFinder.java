package com.plummy.cyhunters.LocationFinder;

import com.plummy.cyhunters.Iterfaces.ILocationFinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.List;
import java.util.Random;

import static com.plummy.cyhunters.CyHunters.getInstance;

public class OverworldLocationFinder implements ILocationFinder {
    private static final List<Structure> searchStructures = List.of(
            Structure.VILLAGE_PLAINS,
            Structure.VILLAGE_DESERT,
            Structure.VILLAGE_SAVANNA,
            Structure.SHIPWRECK_BEACHED,
            Structure.DESERT_PYRAMID
    );

    private static final List<Material> permittedMaterials = List.of(
            Material.WATER,
            Material.LAVA
    );

    private final Random random;

    public OverworldLocationFinder() {
        random = new Random();
    }

    public Location findLocation(World world) {
        Location structureLocation;

        int minDistance = getInstance().getConfig().getInt("parameters.spawn.min-offset-distance");
        int maxDistance = getInstance().getConfig().getInt("parameters.spawn.max-offset-distance");
        int minHeight = getInstance().getConfig().getInt("parameters.spawn.min-height");

        for (int i = 0; i < 5; i++) {
            structureLocation = findStructureLocation(world);

            if (structureLocation == null) {
                continue;
            }

            int x = structureLocation.getBlockX();
            int z = structureLocation.getBlockZ();

            for (int j = 0; j < 10; j++) {
                double distance = random.nextDouble(minDistance, maxDistance);
                double angle = random.nextDouble(2 * Math.PI);

                Block block = world.getHighestBlockAt((int) (x + distance * Math.cos(angle)), (int) (z + distance * Math.sin(angle)));

                if (permittedMaterials.contains(block.getType()) || block.getY() < minHeight) {
                    continue;
                }

                return block.getLocation().add(0.5, 1, 0.5);
            }
        }

        return null;
    }

    private Location findStructureLocation(World world) {
        int searchRadius = getInstance().getConfig().getInt("parameters.spawn.location-search-radius");
        int structureOffset = getInstance().getConfig().getInt("parameters.spawn.structure-offset");

        Location randomLocation = new Location(world, random.nextInt(-searchRadius, searchRadius), 0, random.nextInt(-searchRadius, searchRadius));

        List<StructureSearchResult> searchResults = searchStructures.stream().map(structure -> world.locateNearestStructure(randomLocation, structure, structureOffset, false)).toList();

        double nearestDistance = Double.MAX_VALUE;
        StructureSearchResult nearestResult = null;

        for (StructureSearchResult searchResult : searchResults) {
            if (searchResult == null) {
                continue;
            }

            double distance = searchResult.getLocation().distance(randomLocation);

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestResult = searchResult;
            }
        }

        if (nearestResult == null) {
            return null;
        }

        return nearestResult.getLocation();
    }
}
