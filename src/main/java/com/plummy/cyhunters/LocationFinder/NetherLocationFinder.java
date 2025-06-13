package com.plummy.cyhunters.LocationFinder;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Iterfaces.ILocationFinder;
import org.bukkit.Location;
import org.bukkit.World;

public class NetherLocationFinder implements ILocationFinder {
    @Override
    public GameDimension getDimension() {
        return GameDimension.NETHER;
    }

    @Override
    public Location findLocation(World world) {
        return null;
    }
}
