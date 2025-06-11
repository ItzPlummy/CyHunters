package com.plummy.cyhunters.Commands;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Enums.KitType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.plummy.cyhunters.CyHunters.*;

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
            case "settings" -> {
                if (args.length < 3) {
                    player.sendMessage("§cError: Invalid arguments for command /cyhunters settings");
                    return true;
                }

                switch (args[1]) {
                    case "dimension" -> {
                        if (!List.of("overworld", "nether").contains(args[2])) {
                            player.sendMessage("§cError: Invalid arguments for command /cyhunters settings");
                            return true;
                        }
                    }
                    case "style" -> {
                        if (!List.of("normal", "blitz").contains(args[2])) {
                            player.sendMessage("§cError: Invalid arguments for command /cyhunters settings");
                            return true;
                        }
                    }
                    case "kit" -> {
                        if (!List.of("empty", "basic", "bow", "shears", "op", "mace").contains(args[2])) {
                            player.sendMessage("§cError: Invalid arguments for command /cyhunters settings");
                            return true;
                        }
                    }
                    default -> {
                        player.sendMessage("§cError: Invalid arguments for command /cyhunters settings");
                        return true;
                    }
                }

                onSettingChange(args[1], args[2]);
            }
            default -> sendHelp(player);
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

    private void onSettingChange(String setting, String value) {
        getInstance().getConfig().set("settings." + setting, value);
        getInstance().saveConfig();
        getInstance().reloadConfig();

        setMainGame(getGameFactory().createGame(
                GameDimension.getFromConfig(),
                GameStyle.getFromConfig(),
                KitType.getFromConfig()
        ));
    }

    private void sendHelp(Player player) {
        player.sendMessage("Help");
    }
}
