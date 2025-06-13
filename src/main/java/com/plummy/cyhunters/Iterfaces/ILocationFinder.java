package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameDimension;
import org.bukkit.Location;
import org.bukkit.World;

public interface ILocationFinder {
    GameDimension getDimension();
    Location findLocation();
}
