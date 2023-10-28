package me.prism3.suicide;

import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.suicide.utils.Data;
import me.prism3.suicide.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.prism3.suicide.utils.Data.resourceID;


public class Main extends JavaPlugin {

    private List<UUID> players;

    @Override
    public void onEnable() {

        this.players = new ArrayList<>();

        this.saveDefaultConfig();

        Data.initializer();

        // bStats
        new Metrics(this, 11664);

        // Update Checker
        UpdateChecker.init(this, resourceID)
                .checkEveryXHours(4)
                .setChangelogLink(resourceID)
                .setNotifyOpsOnJoin(true)
                .checkNow();

        this.getLogger().info("Plugin Enabled!");
    }

    @Override
    public void onDisable() { this.getLogger().info("Plugin Disabled!"); }

    public static Main getInstance() { return JavaPlugin.getPlugin(Main.class); }

    public List<UUID> getPlayers() { return this.players; }
}
