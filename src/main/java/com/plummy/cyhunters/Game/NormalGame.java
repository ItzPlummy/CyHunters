package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Iterfaces.*;

public class NormalGame extends AbstractGame {
    public NormalGame(IPlayerManager playerManager, IScheduler scheduler, IGameBoard gameBoard, ILocationFinder locationFinder, ICameraManager cameraManager, IKitCreator kitCreator) {
        super(playerManager, scheduler, gameBoard, locationFinder, cameraManager, kitCreator);
    }

    @Override
    public GameStyle getStyle() {
        return GameStyle.NORMAL;
    }
}
