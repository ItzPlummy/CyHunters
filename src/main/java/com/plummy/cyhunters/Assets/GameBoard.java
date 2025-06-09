package com.plummy.cyhunters.Assets;

import com.plummy.cyhunters.Assets.Interfaces.IGameBoard;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

import static com.plummy.cyhunters.CyHunters.getMainGame;
import static com.plummy.cyhunters.CyHunters.logger;

public class GameBoard implements IGameBoard {
    private static final String OBJECTIVE_NAME = "cyhunters";

    private Scoreboard scoreboard = null;
    private Objective objective = null;

    private List<String> board;

    public GameBoard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

        if (scoreboardManager == null) {
            logger().severe("Scoreboard Manager is null! Gameboard will not appear on the right side of the screen!");
            return;
        }

        scoreboard = scoreboardManager.getMainScoreboard();
        objective = scoreboard.getObjective(OBJECTIVE_NAME);

        if (objective == null) {
            objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, "§b§lCy§d§lHunters");
        }

        board = new ArrayList<>();
    }

    public void updateBoard() {
        if (scoreboard == null) {
            return;
        }

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        board.clear();

        if (getMainGame().hasStarted()) {
            setInGameBoard();
        } else {
            setPreGameBoard();
        }

        for (int index = 0; index < board.size(); index++) {
            objective.getScore(board.get(index)).setScore(index);
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void setPreGameBoard() {
        board.add("Players: " + getMainGame().size());
    }

    private void setInGameBoard() {

    }
}
