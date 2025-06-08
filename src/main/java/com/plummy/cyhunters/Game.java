package com.plummy.cyhunters;

public class Game implements IGame {
    private boolean isStarted;

    public Game() {
        isStarted = false;
    }

    public boolean hasStarted() {
        return isStarted;
    }
}
