package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Iterfaces.IGame;
import com.plummy.cyhunters.Iterfaces.IGameFactory;

import java.util.Objects;

import static com.plummy.cyhunters.CyHunters.getInstance;

public class GameFactory implements IGameFactory {
    public GameFactory() {}

    public IGame createGame() {
        GameStyle style = GameStyle.get(Objects.requireNonNull(getInstance().getConfig().getString("settings.style")));

        return style == GameStyle.BLITZ ? new BlitzGame() : new NormalGame();
    }
}
