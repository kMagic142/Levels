package ro.kmagic.mcgolevels.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.PlayerData;

import java.util.Set;

public class Placeholders extends PlaceholderExpansion {

    private MCGOLevels instance;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) return false;

        instance = (MCGOLevels) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (instance == null) {
            return false;
        }

        return super.register();
    }

    @Override
    public String getRequiredPlugin() {
        return "MCGO-Levels";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "levels";
    }

    @Override
    public @NotNull String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        String[] args = identifier.split("_");

        if(args.length < 1) return null;

        if(args[0].equals("current")) {
            if(args.length > 1 && args[1].equals("exp")) {
                return String.valueOf(instance.getMySQL().getPlayer(player.getName()).getExp());
            }

            return String.valueOf(instance.getMySQL().getPlayer(player.getName()).getLevel());
        }

        if(args[0].equals("nextlevel")) {
            PlayerData data = instance.getMySQL().getPlayer(player.getName());
            String nextLevel = String.valueOf(data.getLevel()+1);

            Set<String> objectList = MCGOLevels.getInstance().getLevels().getConfigurationSection("Levels").getKeys(false);

            if(Integer.parseInt(nextLevel) >= Integer.parseInt((String) objectList.toArray()[objectList.size()-1]))
                nextLevel = null;

            return nextLevel;
        }

        if(args[0].equals("progres")) {
            if(args.length > 1 && args[1].equals("symbol")) {
                ConfigurationSection levels = instance.getLevels().getConfigurationSection("Levels");
                Set<String> objectList = instance.getLevels().getConfigurationSection("Levels").getKeys(false);
                PlayerData data = instance.getMySQL().getPlayer(player.getName());
                return Utils.getProgressBar(
                        data.getExp(),
                        levels.getInt((String) objectList.toArray()[(int) data.getLevel()]),
                        instance.getConfig().getString("symbol").charAt(0),
                        instance.getConfig().getString("color-completed"),
                        instance.getConfig().getString("color-empty")
                );
            }

            ConfigurationSection levels = instance.getLevels().getConfigurationSection("Levels");
            Set<String> objectList = instance.getLevels().getConfigurationSection("Levels").getKeys(false);
            PlayerData data = instance.getMySQL().getPlayer(player.getName());
            return String.valueOf((int) (Utils.getPercent(data.getExp(), levels.getInt((String) objectList.toArray()[(int) data.getLevel()])) * 100));
        }

        if(args[0].equals("need")) {
            if(args.length > 1 && args[1].equals("exp")) {
                ConfigurationSection levels = instance.getLevels().getConfigurationSection("Levels");
                Set<String> objectList = instance.getLevels().getConfigurationSection("Levels").getKeys(false);
                PlayerData data = instance.getMySQL().getPlayer(player.getName());

                int nextExp;

                if(objectList.toArray()[(int) data.getLevel()] != null) {
                    nextExp = levels.getInt((String) objectList.toArray()[(int) data.getLevel()]);

                    return String.valueOf(nextExp - data.getExp());
                }

                return "";
            }
        }

        if(args[0].equals("color")) {
            return Utils.getColor((int) instance.getMySQL().getPlayer(player.getName()).getLevel());
        }

        if(args[0].equals("symbol")) {
            return Utils.getSymbol((int) instance.getMySQL().getPlayer(player.getName()).getLevel());
        }

        return null;
    }

}



















