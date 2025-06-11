package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameStyle;

public interface IGameFactory {
    IGame createGame(GameDimension dimension, GameStyle style);
}
