package ro.kmagic.mcgolevels.utils;

import com.google.common.base.Strings;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.mcgolevels.MCGOLevels;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Utils {

    public static void log(Level lvl, String msg) {
        MCGOLevels.getInstance().getLogger().log(lvl, msg);
    }

    public static void info(String msg) {
        log(Level.INFO, msg);
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String color(String msg, OfflinePlayer player) {
        return Utils.color(PlaceholderAPI.setPlaceholders(player, msg));
    }

    public static List<Component> formatComponentList(List<String> list) {
        List<Component> complist = new ArrayList<>();
        for(String str : list) {
            complist.add(Component.text(Utils.color(str)));
        }

        return complist;
    }

    public static List<Component> formatComponentList(List<String> list, Player player) {
        List<Component> complist = new ArrayList<>();
        for(String str : list) {
            complist.add(Component.text(Utils.color(str, player)));
        }

        return complist;
    }

    public static float getPercent(long current, long max) {
        return ((float) current / max);
    }

    public static String getProgressBar(long current, long max, char symbol, String completedColor, String notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (10 * percent);

        int countProgress = (10 - progressBars) < 0 ? Math.abs(10 - progressBars) + 4 : 10 - progressBars;

        return completedColor + Strings.repeat("" + symbol, progressBars)
                + notCompletedColor + Strings.repeat("" + symbol, countProgress);
    }

    public static String getSymbol(int lvl) {
        MCGOLevels instance = MCGOLevels.getInstance();
        FileConfiguration colors = instance.getColors();
        ConfigurationSection symbols = colors.getConfigurationSection("Symbols");

        String symbol = colors.getString("DefaultSymbol");

        for(String s : symbols.getKeys(false)) {
            int level1 = Integer.parseInt(s.split("-")[0]);
            int level2 = Integer.parseInt(s.split("-")[1]);

            if(lvl >= level1 && lvl <= level2) {
                symbol = symbols.getString(s);
            }
        }

        return symbol;
    }

    public static String getColor(int lvl) {
        MCGOLevels instance = MCGOLevels.getInstance();
        FileConfiguration colors = instance.getColors();
        ConfigurationSection symbols = colors.getConfigurationSection("Colors");

        String color = colors.getString("DefaultColor");

        for(String s : symbols.getKeys(false)) {
            int level1 = Integer.parseInt(s.split("-")[0]);
            int level2 = Integer.parseInt(s.split("-")[1]);

            if(lvl >= level1 && lvl <= level2) {
                color = symbols.getString(s);
            }
        }

        return color;
    }

}
