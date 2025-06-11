package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Camera.CameraManager;
import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Enums.KitType;
import com.plummy.cyhunters.Iterfaces.IGame;
import com.plummy.cyhunters.Iterfaces.IGameFactory;
import com.plummy.cyhunters.Iterfaces.IKitCreatorFactory;
import com.plummy.cyhunters.Iterfaces.ILocationFinderFactory;
import com.plummy.cyhunters.KitCreator.KitCreatorFactory;
import com.plummy.cyhunters.LocationFinder.LocationFinderFactory;
import com.plummy.cyhunters.Player.PlayerManager;
import com.plummy.cyhunters.Scheduler.GameScheduler;

public class GameFactory implements IGameFactory {
    private final ILocationFinderFactory locationFinderFactory;
    private final IKitCreatorFactory kitCreatorFactory;

    public GameFactory() {
        this.locationFinderFactory = new LocationFinderFactory();
        this.kitCreatorFactory = new KitCreatorFactory();
    }

    @Override
    public IGame createGame(GameDimension dimension, GameStyle style, KitType type) {
        return switch (style) {
            case NORMAL -> new NormalGame(
                    new PlayerManager(),
                    new GameScheduler(),
                    new GameBoard(),
                    locationFinderFactory.createLocationFinder(dimension),
                    new CameraManager(),
                    kitCreatorFactory.createKitCreator(type)
            );
            case BLITZ -> new BlitzGame(
                    new PlayerManager(),
                    new GameScheduler(),
                    new GameBoard(),
                    locationFinderFactory.createLocationFinder(dimension),
                    new CameraManager(),
                    kitCreatorFactory.createKitCreator(type)
            );
        };
    }
}
