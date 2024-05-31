package ro.kmagic.mcgolevels.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.commands.handler.CommandInterface;
import ro.kmagic.mcgolevels.gui.MainGUI;
import ro.kmagic.mcgolevels.utils.Utils;

public class MenuCommand implements CommandInterface {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("poziomy.use")) {
            if (args.length >= 1) {
                Player player = Bukkit.getPlayerExact(args[0]);

                if (player == null) {
                    sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("invalid-player")));
                    return;
                }

                new MainGUI(player, 1).getGui().open(player);
            } else {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("not-a-console-command")));
                    return;
                }

                new MainGUI((Player) sender, 1).getGui().open((Player) sender);
            }
        } else {
            sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("no-permission")));
        }
    }
}
