package com.plummy.cyhunters.Commands;

import com.plummy.cyhunters.Enums.GameEndingReason;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.plummy.cyhunters.CyHunters.getMainGame;

public class CyHuntersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cError: Only players can execute command /cyhunters");

            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0]) {
            case "start" -> onGameStart(player);
            case "stop" -> onGameStop(player);
            case "help" -> sendHelp(player);
        }

        return true;
    }

    private void onGameStart(Player player) {
        if (getMainGame().hasStarted()) {
            player.sendMessage("§cError: Game has already been started");
            return;
        }

        getMainGame().start(player);
    }

    private void onGameStop(Player player) {
        if (!getMainGame().hasStarted()) {
            player.sendMessage("§cError: Game has not yet started");
            return;
        }

        getMainGame().stop(GameEndingReason.COMMAND, player);
    }

    private void sendHelp(Player player) {
        player.sendMessage("Help");
    }
}
