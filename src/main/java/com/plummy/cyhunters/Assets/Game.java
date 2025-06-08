package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Enums.GameState;

public class Game implements IGame {
    private GameState state;

    public Game() {
        state = GameState.NOT_STARTED;
    }

    public boolean hasStarted() {
        return state != GameState.NOT_STARTED;
    }
}
