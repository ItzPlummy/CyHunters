package com.plummy.cyhunters.Commands;

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
            sender.sendMessage("Nope");

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("CyHunters");

            return true;
        }

        switch (args[0]) {
            case "start" -> {
                getMainGame().start(getMainGame().getPlayer(player.getUniqueId()));
            }
        }

        return true;
    }
}
