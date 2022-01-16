package com.carpour.suicide;

import com.carpour.suicide.Events.EntityDamage;
import com.carpour.suicide.Events.PlayerDeath;
import com.carpour.suicide.Utils.Metrics;
import com.carpour.suicide.commands.SuicideCommand;
import de.jeff_media.updatechecker.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin{

    private static Main instance;
    private List<UUID> players;

    @Override
    public void onEnable() {

        instance = this;
        players = new ArrayList<>();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Objects.requireNonNull(getCommand("suicide")).setExecutor(new SuicideCommand());

        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);

        //bstats
        new Metrics(this, 11664);

        //Update Checker
        int resource_ID = 93367;
        UpdateChecker.init(this, resource_ID)
                .checkEveryXHours(2)
                .setChangelogLink(resource_ID)
                .setNotifyOpsOnJoin(true)
                .checkNow();

        getLogger().info("Plugin Enabled!");
    }

    @Override
    public void onDisable() {

        getLogger().info("Plugin Disabled!");

    }

    public static Main getInstance() {
        return instance;
    }

    public List<UUID> getPlayers() {
        return players;
    }
}