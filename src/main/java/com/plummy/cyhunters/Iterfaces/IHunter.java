package com.plummy.cyhunters.Iterfaces;

import org.bukkit.Location;

public interface IHunter extends IPlayer {
    ICamera getCamera();

    ICameraSelector getCameraSelector();

    boolean isSpectating();

    void setSpectating();

    void setPlaying();

    void ready(Location location);
}
