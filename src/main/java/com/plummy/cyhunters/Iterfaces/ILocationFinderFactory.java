package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameDimension;

public interface ILocationFinderFactory {
    ILocationFinder createLocationFinder(GameDimension dimension);
}
