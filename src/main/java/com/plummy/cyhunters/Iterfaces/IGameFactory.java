package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Enums.KitType;

public interface IGameFactory {
    IGame createGame(GameDimension dimension, GameStyle style, KitType type);
}
