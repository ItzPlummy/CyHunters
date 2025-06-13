package com.plummy.cyhunters.LocationFinder;

import com.plummy.cyhunters.Iterfaces.ILocationFinder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.List;
import java.util.Random;

import static com.plummy.cyhunters.CyHunters.config;

public abstract class AbstractLocationFinder implements ILocationFinder {
    protected final Random random;

    public AbstractLocationFinder() {
        this.random = new Random();
    }

    protected Location findStructureLocation(World world, List<Structure> searchStructures) {
        int searchRadius = config().getInt("parameters.spawn.location-search-radius");
        int structureOffset = config().getInt("parameters.spawn.structure-offset");

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
