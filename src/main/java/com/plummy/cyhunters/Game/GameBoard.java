package com.plummy.cyhunters.Game;

import com.plummy.cyhunters.Iterfaces.IGameBoard;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.plummy.cyhunters.CyHunters.getMainGame;
import static com.plummy.cyhunters.CyHunters.logger;

public class GameBoard implements IGameBoard {
    private static final String OBJECTIVE_NAME = "cyhunters";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
            objective.getScore(board.get(index)).setScore(board.size() - index - 1);
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void setPreGameBoard() {
        board.add("§f---------------");
        board.add("§f");
        board.add("§dPlayers: §e" + Bukkit.getOnlinePlayers().size());
        board.add("§f§f");
        board.add("§f§f---------------");
    }

    private void setInGameBoard() {
        long hours = getMainGame().getScheduler().getTick() / 3600L;
        long minutes = getMainGame().getScheduler().getTick() / 60L;
        long seconds = getMainGame().getScheduler().getTick() % 60L;

        LocalTime localTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);

        board.add("§f--------------------");
        board.add("§f");
        board.add("§dPlayers: §e" + Bukkit.getOnlinePlayers().size());
        board.add("§f§f");
        board.add("§dTime in game: §b" + formatter.format(localTime));
        board.add("§f§f§f");
        board.add("§f§f--------------------");
    }
}
