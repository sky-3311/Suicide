package me.prism3.suicide;

import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.suicide.Events.EntityDamage;
import me.prism3.suicide.Events.PlayerDeath;
import me.prism3.suicide.Utils.Data;
import me.prism3.suicide.Utils.Metrics;
import me.prism3.suicide.commands.SuicideCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.prism3.suicide.Utils.Data.resourceID;

public class Main extends JavaPlugin{

    private static Main instance;
    private List<UUID> players;

    @Override
    public void onEnable() {

        instance = this;
        this.players = new ArrayList<>();

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        this.initializeData(new Data());

        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new SuicideCommand());

        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(), this);

        // bStats
        new Metrics(this, 11664);

        // Update Checker
        UpdateChecker.init(this, resourceID)
                .checkEveryXHours(2)
                .setChangelogLink(resourceID)
                .setNotifyOpsOnJoin(true)
                .checkNow();

        this.getLogger().info("Plugin Enabled!");
    }

    @Override
    public void onDisable() {

        this.getLogger().info("Plugin Disabled!");

    }

    private void initializeData(Data data){

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