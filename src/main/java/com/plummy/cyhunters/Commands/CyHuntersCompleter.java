package com.plummy.cyhunters.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CyHuntersCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of(
                    "start",
                    "stop",
                    "settings",
                    "help"
            );
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("settings")) {
            return List.of(
                    "dimension",
                    "style",
                    "kit"
            );
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("settings")) {
            return switch (args[1]) {
                case "dimension" -> List.of(
                        "overworld",
                        "nether"
                );
                case "style" -> List.of(
                        "normal",
                        "blitz"
                );
                case "kit" -> List.of(
                        "empty",
                        "basic",
                        "bow",
                        "shears",
                        "op",
                        "mace"
                );
                default -> null;
            };
        }

        return null;
    }
}
