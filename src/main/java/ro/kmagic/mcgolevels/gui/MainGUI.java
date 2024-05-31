package ro.kmagic.mcgolevels.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.PlayerData;
import ro.kmagic.mcgolevels.utils.Utils;

import java.util.List;

public class MainGUI {

    private final Gui gui;
    private int page;

    public MainGUI(Player player, int page) {
        MCGOLevels instance = MCGOLevels.getInstance();
        PlayerData data = instance.getMySQL().getPlayer(player.getName());
        ConfigurationSection menuSection = instance.getConfig().getConfigurationSection("GUI");

        this.page = page;

        gui = Gui.gui()
                .title(Component.text(Utils.color(menuSection.getString("GuiName"))))
                .rows(menuSection.getInt("Rows"))
                .disableAllInteractions()
                .create();

        gui.updateTitle(Utils.color(menuSection.getString("GuiName")).replace("{PAGE}", "" + page));

        for(String s : menuSection.getConfigurationSection("CustomItems").getKeys(false)) {
            ConfigurationSection customItems = menuSection.getConfigurationSection("CustomItems").getConfigurationSection(s);

            String material = customItems.getString("item");
            String name = customItems.getString("name");
            List<String> lore = customItems.getStringList("lore");
            int slot = customItems.getInt("slot");

            GuiItem item = ItemBuilder.from(Material.getMaterial(material))
                    .name(Component.text(Utils.color(name)))
                    .lore(Utils.formatComponentList(lore, player))
                    .asGuiItem(event -> {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for(String s1 : customItems.getStringList("commands")) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, s1));
                                }
                            }}.runTaskLater(MCGOLevels.getInstance(), 4);

                        gui.close(player);
                    });

            gui.setItem(slot, item);
        }

        for(ConfigurationSection reward : instance.getRewards()) {
            String name = reward.getString("Displayname");
            int level = reward.getInt("Level");
            List<String> lore = reward.getStringList("Lore");
            List<Integer> claimed = instance.getMySQL().getClaimed(player.getName());
            int slot = reward.getInt("Slot");
            int itempage = reward.getInt("Page");

            if(this.page != itempage) continue;

            for(int c : claimed) {
                if(c == level) {
                    String material = menuSection.getConfigurationSection("Claimed").getString("Item");
                    lore.addAll(menuSection.getConfigurationSection("Claimed").getStringList("Lore"));

                    GuiItem item = ItemBuilder.from(Material.getMaterial(material))
                            .name(Component.text(Utils.color(name)))
                            .lore(Utils.formatComponentList(lore, player))
                            .asGuiItem();

                    gui.setItem(slot, item);
                }
            }

            if(data.getLevel() < level && !claimed.contains(level)) {
                String material = menuSection.getConfigurationSection("Locked").getString("Item");
                lore.addAll(menuSection.getConfigurationSection("Locked").getStringList("Lore"));

                GuiItem item = ItemBuilder.from(Material.getMaterial(material))
                        .name(Component.text(Utils.color(name)))
                        .lore(Utils.formatComponentList(lore, player))
                        .asGuiItem();

                gui.setItem(slot, item);
                continue;
            }

            if(!claimed.contains(level)) {
                String material = menuSection.getConfigurationSection("Unlocked").getString("Item");
                lore.addAll(menuSection.getConfigurationSection("Unlocked").getStringList("Lore"));

                GuiItem item = ItemBuilder.from(Material.getMaterial(material))
                        .name(Component.text(Utils.color(name)))
                        .lore(Utils.formatComponentList(lore, player))
                        .asGuiItem(event -> {
                            int points = reward.getInt("add-points");
                            int exp = reward.getInt("add-exp");

                            if (points != -1) {
                                PlayerPoints.getInstance().getAPI().give(player.getUniqueId(), points);
                            }

                            if (exp != -1) {
                                data.addExp(exp);
                                instance.getMySQL().addPlayer(data);
                            }

                            for (String s : reward.getStringList("Commands")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, s));
                            }

                            player.sendMessage(Utils.color(reward.getString("message"), player));

                            instance.getMySQL().addClaimed(data, level);
                            gui.close(player);
                        });

                gui.setItem(slot, item);
            }

        }

        String previousname = menuSection.getConfigurationSection("Previous").getString("Name");
        String previousid = menuSection.getConfigurationSection("Previous").getString("Item");
        List<String> previouslore = menuSection.getConfigurationSection("Previous").getStringList("Lore");

        GuiItem previous = ItemBuilder.from(Material.valueOf(previousid))
                .name(Component.text(Utils.color(previousname)))
                .lore(Utils.formatComponentList(previouslore))
                .asGuiItem(event -> {
                    if(this.page > 1) {
                        new MainGUI(player, page-1).getGui().open(player);
                    }
                });

        String nextname = menuSection.getConfigurationSection("Next").getString("Name");
        String nextid = menuSection.getConfigurationSection("Next").getString("Item");
        List<String> nextlore = menuSection.getConfigurationSection("Next").getStringList("Lore");

        GuiItem next = ItemBuilder.from(Material.valueOf(nextid))
                .name(Component.text(Utils.color(nextname)))
                .lore(Utils.formatComponentList(nextlore))
                .asGuiItem(event -> {
                    if(getNextPageItems() >= 1) {
                        new MainGUI(player, page+1).getGui().open(player);
                    }
                });


        gui.setItem(6, 4, previous);
        gui.setItem(6, 6, next);

        for(String s : menuSection.getConfigurationSection("Fillers").getKeys(false)) {
            ConfigurationSection filler = menuSection.getConfigurationSection("Fillers").getConfigurationSection(s);

            if(s.equals("Filler")) {
                String material = filler.getString("item");
                String name = filler.getString("name");
                List<String> slots = filler.getStringList("slots");
                Integer materialdata = filler.getInt("data");

                for(String slot : slots) {
                    if(materialdata != null) {
                        gui.setItem(Integer.parseInt(slot), ItemBuilder.from(Material.getMaterial(material).getNewData(materialdata.byteValue()).toItemStack(1)).name(Component.text(Utils.color(name))).asGuiItem());
                    } else {
                        gui.setItem(Integer.parseInt(slot), ItemBuilder.from(Material.getMaterial(material)).name(Component.text(Utils.color(name))).asGuiItem());
                    }
                }
            }

            if(s.equals("FillerTop")) {
                String material = filler.getString("item");
                String name = filler.getString("name");
                Integer materialdata = filler.getInt("data");

                if(materialdata != null) {
                    gui.getFiller().fillTop(ItemBuilder.from(Material.getMaterial(material).getNewData(materialdata.byteValue()).toItemStack(1)).name(Component.text(Utils.color(name))).asGuiItem());
                } else {
                    gui.getFiller().fillTop(ItemBuilder.from(Material.getMaterial(material)).name(Component.text(Utils.color(name))).asGuiItem());
                }
            }

            if(s.equals("FillerBottom")) {
                String material = filler.getString("item");
                String name = filler.getString("name");
                Integer materialdata = filler.getInt("data");

                if(materialdata != null) {
                    gui.getFiller().fillBottom(ItemBuilder.from(Material.getMaterial(material).getNewData(materialdata.byteValue()).toItemStack(1)).name(Component.text(Utils.color(name))).asGuiItem());
                } else {
                    gui.getFiller().fillBottom(ItemBuilder.from(Material.getMaterial(material)).name(Component.text(Utils.color(name))).asGuiItem());
                }
            }
        }

    }

    public Gui getGui() {
        return gui;
    }

    public int getNextPageItems() {
        int index = 0;
        for(ConfigurationSection rewards : MCGOLevels.getInstance().getRewards()) {
            if(rewards.getInt("Page") == page+1) index++;
        }
        return index;
    }
}
