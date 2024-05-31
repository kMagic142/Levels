package ro.kmagic.mcgolevels.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.commands.handler.CommandInterface;
import ro.kmagic.mcgolevels.data.objects.PlayerData;
import ro.kmagic.mcgolevels.data.objects.events.PlayerDataUpdateEvent;
import ro.kmagic.mcgolevels.utils.Utils;

public class RemoveExpCommand implements CommandInterface {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("poziomy.ustawexp")) {
            if(args.length < 2) {
                sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("not-enough-arguments")));
                return;
            }

            PlayerData playerData = MCGOLevels.getInstance().getMySQL().getPlayer(args[0]);

            if(playerData != null) {
                playerData.removeExp(Integer.parseInt(args[1]));
                MCGOLevels.getInstance().getMySQL().addPlayer(playerData);

                Bukkit.getPluginManager().callEvent(new PlayerDataUpdateEvent(playerData));

                sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("remove-exp")
                        .replace("{PLAYER}", args[0])
                        .replace("{EXP}", args[1])));
            } else {
                sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("invalid-player")));
                return;
            }

            if(Bukkit.getPlayer(args[0]) != null) {
                Bukkit.getPlayer(args[0]).sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("remove-exp-notify")
                        .replace("{EXP}", args[1])));
            }
        } else {
            sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("no-permission")));
        }
    }
}
