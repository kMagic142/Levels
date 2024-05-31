package ro.kmagic.mcgolevels.data.objects;

import java.util.UUID;

public class PlayerData {

    private final String name;
    private final UUID uuid;
    private long exp;
    private long level;

    public PlayerData(String name, UUID uuid, long exp, long level) {
        this.name = name;
        this.uuid = uuid;
        this.exp = exp;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getExp() {
        return exp;
    }

    public long getLevel() {
        return level;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public void addExp(long add) {
        exp = exp+add;
    }

    public void removeExp(long subtract) {
        exp = exp-subtract;
    }
}
