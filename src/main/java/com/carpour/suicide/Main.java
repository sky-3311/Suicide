package com.carpour.suicide;

import com.carpour.suicide.Events.EntityDamageByEntity;
import com.carpour.suicide.Events.OnPlayerDeath;
import com.carpour.suicide.Utils.Metrics;
import com.carpour.suicide.Utils.UpdateChecker;
import com.carpour.suicide.commands.SuicideCommand;
import org.bukkit.ChatColor;
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

        getServer().getConsoleSender().sendMessage("[Suicide] "+ ChatColor.GREEN + "Plugin Enabled!");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Objects.requireNonNull(getCommand("suicide")).setExecutor(new SuicideCommand());
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);

        //bstats

        Metrics metrics = new Metrics(this, 11664);

        //Update Checker
        UpdateChecker updater = new UpdateChecker(this);
        updater.checkForUpdate();
    }

    public static Main getInstance() {
        return instance;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    @Override
    public void onDisable() {

        getServer().getConsoleSender().sendMessage("[Suicide] " + ChatColor.RED + "Plugin Disabled!");

    }
}