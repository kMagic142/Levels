package ro.kmagic.mcgolevels.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.PlayerData;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData data = MCGOLevels.getInstance().getMySQL().getPlayer(event.getPlayer().getName());

        if(data == null) {
            MCGOLevels.getInstance().getMySQL().addPlayer(new PlayerData(event.getPlayer().getName(), event.getPlayer().getUniqueId(), 0, 1));
        }
    }

}
