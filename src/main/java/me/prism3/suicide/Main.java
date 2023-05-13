package me.prism3.suicide;

import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.suicide.commands.Suicide;
import me.prism3.suicide.events.EntityDamage;
import me.prism3.suicide.events.PlayerDeath;
import me.prism3.suicide.utils.Data;
import me.prism3.suicide.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.prism3.suicide.utils.Data.resourceID;

public class Main extends JavaPlugin {

    private static Main instance;
    private List<UUID> players;

    @Override
    public void onEnable() {

        instance = this;
        this.players = new ArrayList<>();

        this.saveDefaultConfig();

        this.initializeData(new Data());

        this.getCommand("suicide").setExecutor(new Suicide());

        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(), this);

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

    public void initializeData(final Data data) {

        data.initializeStrings();
        data.initializeStringPermissions();
        data.initializeLongs();
        data.initializeIntegers();
        data.initializeBooleans();
        data.initializeLists();
    }

    public static Main getInstance() {
        return instance;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }
}
