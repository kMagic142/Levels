package ro.kmagic.mcgolevels.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.PlayerData;
import ro.kmagic.mcgolevels.data.objects.events.PlayerDataUpdateEvent;
import ro.kmagic.mcgolevels.utils.Utils;
import xyz.tozymc.spigot.api.title.Title;
import xyz.tozymc.spigot.api.title.TitleApi;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DataUpdateListener implements Listener {

    @EventHandler
    public void onDataUpdate(PlayerDataUpdateEvent event) {
        MCGOLevels instance = MCGOLevels.getInstance();
        PlayerData data = event.getData();
        long exp = data.getExp();

        ConfigurationSection levels = instance.getLevels().getConfigurationSection("Levels");
        Set<String> objectList = instance.getLevels().getConfigurationSection("Levels").getKeys(false);
        int nextExp = levels.getInt((String) objectList.toArray()[(int) data.getLevel()]);

        if(Integer.parseInt((String) objectList.toArray()[(int) data.getLevel()]) >= Integer.parseInt((String) objectList.toArray()[objectList.size()-1]))
            nextExp = 0;

        if(nextExp == 0) {
            return;
        }

        if(exp >= nextExp) {
            for(int i = 0; i < Arrays.copyOfRange(objectList.toArray(), (int) data.getLevel(), objectList.size()).length; i++) {
                if(data.getExp() >= levels.getInt((String) objectList.toArray()[(int) data.getLevel()])) {
                    data.setExp(data.getExp() - levels.getInt((String) objectList.toArray()[(int) data.getLevel()]));
                    data.setLevel(Integer.parseInt((String) objectList.toArray()[(int) data.getLevel()]));
                }
            }

            instance.getMySQL().addPlayer(data);

            Player player = Bukkit.getPlayerExact(data.getName());

            if(player != null) {
                Title title = new Title(Utils.color(instance.getConfig().getString("Level-up.Title"), player), Utils.color(instance.getConfig().getString("Level-up.Subtitle"), player), 20, 60, 20);
                TitleApi.sendTitle(player, title);
                TitleApi.sendActionbar(player, Utils.color(instance.getConfig().getString("Level-up.Actionbar"), player));

                List<String> levelupMessage = instance.getConfig().getStringList("Level-up.Message");

                for(String s : levelupMessage) {
                    if(s.contains("{CLICK}")) {
                        BaseComponent[] component =
                                new ComponentBuilder("")
                                        .append(Utils.color(instance.getConfig().getString("Level-up.Click"), player))
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poziomy"))
                                        .append(Utils.color(s.replace("{CLICK}", ""), player))
                                        .create();

                        player.spigot().sendMessage(component);
                        continue;
                    }

                    player.sendMessage(Utils.color(s, player));


                }
            }
        }
    }

}
