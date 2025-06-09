package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Interfaces.ILocationFinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.List;
import java.util.Random;

public class LocationFinder implements ILocationFinder {
    private final static int searchRadius = 25000;
    private final static int minDistance = 50;
    private final static int maxDistance = 200;

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

    public LocationFinder() {
        random = new Random();
    }

    public Location findLocation(World world) {
        Location structureLocation;

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

                if (permittedMaterials.contains(block.getType()) || block.getY() < 64) {
                    continue;
                }

                return block.getLocation().add(0.5, 1, 0.5);
            }
        }

        return null;
    }

    private Location findStructureLocation(World world) {
        Location randomLocation = new Location(world, random.nextInt(-searchRadius, searchRadius), 0, random.nextInt(-searchRadius, searchRadius));

        List<StructureSearchResult> searchResults = searchStructures.stream().map(structure -> world.locateNearestStructure(randomLocation, structure, 500, false)).toList();

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
