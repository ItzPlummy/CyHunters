package com.plummy.cyhunters.LocationFinder;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Iterfaces.ILocationFinder;
import com.plummy.cyhunters.Iterfaces.ILocationFinderFactory;

public class LocationFinderFactory implements ILocationFinderFactory {
    public LocationFinderFactory() {}

    @Override
    public ILocationFinder createLocationFinder(GameDimension dimension) {
        return switch (dimension) {
            case OVERWORLD -> new OverworldLocationFinder();
            case NETHER -> new NetherLocationFinder();
        };
    }
}
