package ro.kmagic.mcgolevels;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ro.kmagic.mcgolevels.commands.*;
import ro.kmagic.mcgolevels.commands.handler.CommandHandler;
import ro.kmagic.mcgolevels.data.files.ColorsFile;
import ro.kmagic.mcgolevels.data.files.LevelsFile;
import ro.kmagic.mcgolevels.data.files.RewardsFile;
import ro.kmagic.mcgolevels.data.mysql.MySQL;
import ro.kmagic.mcgolevels.data.objects.LevelsAPI;
import ro.kmagic.mcgolevels.listeners.DataUpdateListener;
import ro.kmagic.mcgolevels.listeners.PlayerListener;
import ro.kmagic.mcgolevels.utils.Placeholders;
import ro.kmagic.mcgolevels.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public final class MCGOLevels extends JavaPlugin {

    private static MCGOLevels instance;
    private static LevelsAPI api;

    private MySQL mySQL;

    private ColorsFile colorsFile;
    private LevelsFile levelsFile;
    private RewardsFile rewardsFile;

    private List<ConfigurationSection> rewards;

    @Override
    public void onEnable() {
        instance = this;
        api = new LevelsAPI();

        loadData();
        loadCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadData() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        mySQL = new MySQL();

        colorsFile = new ColorsFile();
        levelsFile = new LevelsFile();
        rewardsFile = new RewardsFile();

        rewards = new LinkedList<>();

        for(String s : getRewardsFile().getConfigurationSection("Rewards").getKeys(false)) {
            rewards.add(getRewardsFile().getConfigurationSection("Rewards").getConfigurationSection(s));
        }

        Utils.info("Successfully loaded data");
    }

    private void loadCommands() {
        // main commands
        CommandHandler commandHandler = new CommandHandler();

        commandHandler.register("menu", new MenuCommand());
        commandHandler.register("help", new HelpCommand());
        commandHandler.register("dodajexp", new AddExpCommand());
        commandHandler.register("ustawexp", new SetExpCommand());
        commandHandler.register("usunexp", new RemoveExpCommand());
        commandHandler.register("ustawpoziom", new SetLevelCommand());

        getCommand("poziomy").setExecutor(commandHandler);
        getCommand("poziomy").setTabCompleter(commandHandler);

        // stats command
        CommandHandler commandHandler2 = new CommandHandler();
        commandHandler2.register("stats", new StatsCommand());
        getCommand("poziom").setExecutor(commandHandler);

        Utils.info("Successfully loaded commands");

        new Placeholders().register();

        Utils.info("Successfully hooked into PlaceholderAPI");

        Bukkit.getPluginManager().registerEvents(new DataUpdateListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        Utils.info("Successfully registered listeners");
    }

    public static MCGOLevels getInstance() {
        return instance;
    }

    public static LevelsAPI getAPI() {
        return api;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public FileConfiguration getColors() {
        return colorsFile.getData();
    }

    public FileConfiguration getLevels() {
        return levelsFile.getData();
    }

    public FileConfiguration getRewardsFile() {
        return rewardsFile.getData();
    }

    public List<ConfigurationSection> getRewards() {
        return rewards;
    }
}
