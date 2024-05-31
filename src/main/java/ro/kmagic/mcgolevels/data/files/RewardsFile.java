package ro.kmagic.mcgolevels.data.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.kmagic.mcgolevels.MCGOLevels;

import java.io.File;
import java.io.IOException;

public class RewardsFile {

    private FileConfiguration fc;
    private File file;

    public RewardsFile() {
        getFile();
    }

    public File getFile() {
        if (file == null) {
            file = new File(MCGOLevels.getInstance().getDataFolder(), "rewards.yml");
            if (!file.exists()) MCGOLevels.getInstance().saveResource("rewards.yml", false);
        }
        return file;
    }

    public FileConfiguration getData() {
        if (fc == null) fc = YamlConfiguration.loadConfiguration(getFile());
        return fc;
    }

    public void saveData() {
        try {
            getData().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        fc = YamlConfiguration.loadConfiguration(getFile());
    }

}
