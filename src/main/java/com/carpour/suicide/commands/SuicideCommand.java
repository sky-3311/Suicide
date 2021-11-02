package com.carpour.suicide.commands;

import com.carpour.suicide.Main;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class SuicideCommand implements CommandExecutor {

    private final Main main = Main.getInstance();

    private final HashMap<UUID, Long> coolDown = new HashMap<>();

    private final long coolDownTime = main.getConfig().getLong("Cooldown.Timer");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("suicide.command")) {

            sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.No-Permission")).replaceAll("&", "§"));

        }

        if(args.length != 0 && !args[0].equalsIgnoreCase("Reload"))
        {

            sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));

            return false;

        }

        else if (args.length == 1 && args[0].equalsIgnoreCase("Reload")) {

            if (sender.hasPermission("suicide.reload")) {

                main.reloadConfig();
                sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Reload-Message")).replaceAll("&", "§"));

            } else {

                sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.No-Permission")).replaceAll("&", "§"));

            }

            return true;

        }

        else if (args.length > 1 && args[0].equalsIgnoreCase("Reload")){

            sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));
            return false;

        }

        else if(sender instanceof Player && sender.hasPermission("suicide.command")) {

            Player player = (Player) sender;

            if (main.getConfig().getStringList("Disabled-Worlds").contains(player.getWorld().getName())) {

                player.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Disabled-Message")).replaceAll("&", "§"));
                return false;

            }

            if (!main.getConfig().getBoolean("Cooldown.Disable") && !player.hasPermission("suicide.bypass")) {

                if (coolDown.containsKey(player.getUniqueId())) {

                    long secondsleft = ((coolDown.get(player.getUniqueId()) / 1000) + coolDownTime) - (System.currentTimeMillis() / 1000);

                    if (coolDownTime <= 0) return false;

                    if (secondsleft > 0) {

                        player.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.On-Cooldown")).replaceAll("&", "§").replaceAll("%time%", String.valueOf(secondsleft)));

                    }else {
                        coolDown.remove(player.getUniqueId());
                        player.setHealth(0.0);
                        coolDown.put(player.getUniqueId(), System.currentTimeMillis());
                    }

                    return false;

                }
            }

            main.getPlayers().add(player.getUniqueId());

            player.setHealth(0.0);
            coolDown.put(player.getUniqueId(), System.currentTimeMillis());

            if(!main.getConfig().getBoolean("Broadcast")){

                Bukkit.broadcastMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Broadcast-Message")).replace("%player%", player.getName()).replaceAll("&", "§"));
            }

            if (!main.getConfig().getBoolean("Message")) {

                player.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Message-Sent-on-Suicide")).replaceAll("&", "§"));

            }

            if (!main.getConfig().getBoolean("Firework")) {

                Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL_LARGE).withFlicker().build());
                meta.setPower(1);
                firework.setFireworkMeta(meta);
                firework.setMetadata("noDamage", new FixedMetadataValue(main, true));

            }

            if (!main.getConfig().getBoolean("Coords")) {

                int x = player.getLocation().getBlockX();
                int y = player.getLocation().getBlockY();
                int z = player.getLocation().getBlockZ();
                player.sendMessage(ChatColor.GREEN + "You suicided at: " + ChatColor.RED + "X: " + x + " Y: " + y + " Z: " + z);

            }

            if (!main.getConfig().getBoolean("Sound.Disable")){

                Location loc = player.getLocation();
                player.playSound(loc, Sound.valueOf(main.getConfig().getString("Sound.SoundPlayed")), (main.getConfig().getInt("Sound.Volume")), (main.getConfig().getInt("Sound.Pitch")));

            }

        }

        else {

            getServer().getConsoleSender().sendMessage(ChatColor.RED + "This command can only be ran in-game!");

        }

        return true;
    }
}