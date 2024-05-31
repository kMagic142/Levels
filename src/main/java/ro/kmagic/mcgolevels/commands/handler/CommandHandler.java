package ro.kmagic.mcgolevels.commands.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private static final HashMap<String, CommandInterface> commands = new HashMap<>();


    public void register(String name, CommandInterface cmd) {
        commands.put(name, cmd);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 0 && commandLabel.equals("poziomy")) {
            commands.get("menu").onCommand(sender, args);
            return true;
        }

        if(args.length == 0 && commandLabel.equals("poziom")) {
            commands.get("stats").onCommand(sender, args);
            return true;
        }

        if(args.length == 1 && commandLabel.equals("poziom")) {
            commands.get("stats").onCommand(sender, args);
            return true;
        }

        if(commands.containsKey(args[0])) {
            if(args.length > 2) {
                commands.get(args[0]).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            } else {
                commands.get(args[0]).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            }

            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getLabel().equals("poziom")) {
            if(args.length == 2) {
                return Collections.emptyList();
            }

            return null;
        }

        final List<String> completions = new ArrayList<>();

        if (args[0].equalsIgnoreCase(commands.keySet().stream().filter(c -> c.equals(args[0])).findFirst().orElse("none"))) {
            switch (args[0]) {
                case "help":
                case "menu": {
                    return null;
                }
                default: {
                    if(args.length == 2) {
                        return null;
                    }
                    if (args.length == 3) {
                        return Arrays.asList("1", "2", "3", "4", "5");
                    }
                    return Collections.emptyList();
                }
            }
        }

        StringUtil.copyPartialMatches(args[0], commands.keySet(), completions);

        Collections.sort(completions);
        return completions;
    }
}
