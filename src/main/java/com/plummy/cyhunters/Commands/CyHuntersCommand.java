package com.plummy.cyhunters.Commands;

import com.plummy.cyhunters.Enums.GameDimension;
import com.plummy.cyhunters.Enums.GameEndingReason;
import com.plummy.cyhunters.Enums.GameStyle;
import com.plummy.cyhunters.Enums.KitType;
import com.plummy.cyhunters.Iterfaces.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
            case "start" -> onGameStart(player, args.length > 1 ? args[1] : null);
            case "stop" -> onGameStop(player);
            case "settings" -> {
                if (args.length < 2) {
                    player.sendMessage("§cError: Invalid argument for settings command.");
                    player.sendMessage("§cUsage: /cyhunters settings <dimension | style | kit> <value>");
                    player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                    return true;
                }

                switch (args[1]) {
                    case "dimension" -> {
                        if (args.length < 3 || !List.of("overworld", "nether").contains(args[2])) {
                            player.sendMessage("§cError: Invalid argument for settings command.");
                            player.sendMessage("§cUsage: /cyhunters settings dimension <overworld | nether>");
                            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                            return true;
                        }
                    }
                    case "style" -> {
                        if (args.length < 3 || !List.of("normal", "blitz").contains(args[2])) {
                            player.sendMessage("§cError: Invalid argument for settings command.");
                            player.sendMessage("§cUsage: /cyhunters settings style <normal | blitz>");
                            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                            return true;
                        }
                    }
                    case "kit" -> {
                        if (args.length < 3 || !List.of("empty", "basic", "bow", "shears", "op", "mace").contains(args[2])) {
                            player.sendMessage("§cError: Invalid argument for settings command.");
                            player.sendMessage("§cUsage: /cyhunters settings kit <empty | basic | bow | shears | op | mace>");
                            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                            return true;
                        }
                    }
                    default -> {
                        player.sendMessage("§cError: Invalid argument for settings command.");
                        player.sendMessage("§cUsage: /cyhunters settings <dimension | style | kit> <value>");
                        player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                        return true;
                    }
                }

                onSettingChange(args[1], args[2]);
            }
            default -> sendHelp(player);
        }

        return true;
    }

    private void onGameStart(Player player, String speedrunnerName) {
        if (getMainGame().hasStarted()) {
            player.sendMessage("§cError: Game has already been started");
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            return;
        }

        Player speedrunner = speedrunnerName != null ? Bukkit.getPlayer(speedrunnerName) : null;

        getMainGame().start(player, speedrunner);
    }

    private void onGameStop(Player player) {
        if (!getMainGame().hasStarted()) {
            player.sendMessage("§cError: Game has not yet started");
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            return;
        }

        getMainGame().stop(player, GameEndingReason.COMMAND);
    }

    private void onSettingChange(String setting, String value) {
        config().set("settings." + setting, value);
        getInstance().saveConfig();
        getInstance().reloadConfig();

        setMainGame(getGameFactory().createGame(
                GameDimension.getFromConfig(),
                GameStyle.getFromConfig(),
                KitType.getFromConfig()
        ));

        for (Player player : Bukkit.getOnlinePlayers() ) {
            player.sendMessage("§aSet " + setting + " to " + value + "!");
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("Help");
    }
}
