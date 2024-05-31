package ro.kmagic.mcgolevels.data.objects;

import org.bukkit.Bukkit;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.events.PlayerDataUpdateEvent;

public class LevelsAPI {

    private MCGOLevels instance = MCGOLevels.getInstance();

    public void addExp(String name, int exp) {
        PlayerData data = getPlayer(name);
        if(data == null) return;

        data.addExp(exp);
        instance.getMySQL().addPlayer(data);

        Bukkit.getPluginManager().callEvent(new PlayerDataUpdateEvent(data));
    }

    public PlayerData getPlayer(String name) {
        return instance.getMySQL().getPlayer(name);
    }

    public void setExp(String name, int exp) {
        PlayerData data = getPlayer(name);
        if(data == null) return;

        data.setExp(exp);
        instance.getMySQL().addPlayer(data);

        Bukkit.getPluginManager().callEvent(new PlayerDataUpdateEvent(data));
    }

    public void removeExp(String name, int exp) {
        PlayerData data = getPlayer(name);
        if(data == null) return;

        data.removeExp(exp);
        instance.getMySQL().addPlayer(data);

        Bukkit.getPluginManager().callEvent(new PlayerDataUpdateEvent(data));
    }

    public void setLevel(String name, int lvl) {
        PlayerData data = getPlayer(name);
        if(data == null) return;

        data.setLevel(lvl);
        instance.getMySQL().addPlayer(data);

        Bukkit.getPluginManager().callEvent(new PlayerDataUpdateEvent(data));
    }

}
