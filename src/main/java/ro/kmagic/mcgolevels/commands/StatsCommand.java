package ro.kmagic.mcgolevels.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.commands.handler.CommandInterface;
import ro.kmagic.mcgolevels.data.objects.PlayerData;
import ro.kmagic.mcgolevels.utils.Utils;

import java.util.Set;

public class StatsCommand implements CommandInterface {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("poziom.use")) {
            if(args.length < 1) {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("not-a-console-command")));
                    return;
                }

                PlayerData playerData = MCGOLevels.getInstance().getMySQL().getPlayer(sender.getName());
                Set<String> objectList = MCGOLevels.getInstance().getLevels().getConfigurationSection("Levels").getKeys(false);

                if(playerData.getLevel() >= Integer.parseInt((String) objectList.toArray()[objectList.size()-1])) {
                    for (String s : MCGOLevels.getInstance().getConfig().getStringList("Stats.Self-max-level")) {
                        sender.sendMessage(Utils.color(s, (Player) sender));
                    }
                } else {
                    for (String s : MCGOLevels.getInstance().getConfig().getStringList("Stats.Self")) {
                        sender.sendMessage(Utils.color(s, (Player) sender));
                    }
                }
            } else {
                PlayerData playerData = MCGOLevels.getInstance().getMySQL().getPlayer(args[0]);
                Set<String> objectList = MCGOLevels.getInstance().getLevels().getConfigurationSection("Levels").getKeys(false);

                if(playerData == null) {
                    sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("invalid-player")));
                    return;
                }

                if(playerData.getLevel() >= Integer.parseInt((String) objectList.toArray()[objectList.size()-1])) {
                    for (String s : MCGOLevels.getInstance().getConfig().getStringList("Stats.Other-max-level")) {
                        sender.sendMessage(Utils.color(s, Bukkit.getOfflinePlayer(playerData.getUuid())));
                    }
                } else {
                    for (String s : MCGOLevels.getInstance().getConfig().getStringList("Stats.Other")) {
                        sender.sendMessage(Utils.color(s, Bukkit.getOfflinePlayer(playerData.getUuid())));
                    }
                }
            }
        } else {
            sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("no-permission")));
        }
    }
}
