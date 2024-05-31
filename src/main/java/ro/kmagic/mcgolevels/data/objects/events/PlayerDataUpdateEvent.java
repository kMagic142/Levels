package ro.kmagic.mcgolevels.data.objects.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.kmagic.mcgolevels.data.objects.PlayerData;

public class PlayerDataUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerData data;

    public PlayerDataUpdateEvent(PlayerData data) {
        this.data = data;
    }

    public PlayerData getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
