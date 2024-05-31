package ro.kmagic.mcgolevels.commands;

import org.bukkit.command.CommandSender;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.commands.handler.CommandInterface;
import ro.kmagic.mcgolevels.utils.Utils;

public class HelpCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("poziomy.use")) {
            for(String s : MCGOLevels.getInstance().getConfig().getStringList("help-message")) {
                sender.sendMessage(Utils.color(s));
            }
        } else {
            sender.sendMessage(Utils.color(MCGOLevels.getInstance().getConfig().getString("no-permission")));
        }
    }
}
