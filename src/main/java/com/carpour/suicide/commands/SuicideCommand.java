package com.carpour.suicide.commands;

import com.carpour.suicide.Main;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import static org.bukkit.Bukkit.getServer;

public class SuicideCommand implements CommandExecutor, Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e){

        Player player = e.getEntity();

        if(Main.getInstance().getPlayers().contains(player.getUniqueId()) && (!(main.getConfig().getBoolean("Broadcast")))){

            Main.getInstance().getPlayers().remove(player.getUniqueId());
            e.setDeathMessage(null);

        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 1 && !args[0].equalsIgnoreCase("Reload"))
        {

            sender.sendMessage(main.getConfig().getString("Messages.Invalid-Syntax").replaceAll("&", "§"));

        }

        else if (args.length == 1 && args[0].equalsIgnoreCase("Reload")) {

            if (sender instanceof Player) {

                if (sender.hasPermission("suicide.reload")) {

                    main.reloadConfig();
                    sender.sendMessage(main.getConfig().getString("Messages.Reload-Message").replaceAll("&", "§"));

                }

                else {

                    sender.sendMessage(main.getConfig().getString("Messages.No-Permission").replaceAll("&", "§"));

                }

            } else {

                main.reloadConfig();
                getServer().getConsoleSender().sendMessage(main.getConfig().getString("Messages.Reload-Message").replaceAll("&", "§"));

            }

        } else if(sender instanceof Player && sender.hasPermission("suicide.command")) {

            Player player = (Player) sender;

            if (main.getConfig().getStringList("Disabled-Worlds").contains(player.getWorld().getName())) {

                player.sendMessage(main.getConfig().getString("Messages.Disabled-Message").replaceAll("&", "§"));
                return false;

            }

            main.getPlayers().add(player.getUniqueId());

            player.setHealth(0.0);

            if(!main.getConfig().getBoolean("Broadcast")){

                Bukkit.broadcastMessage(main.getConfig().getString("Messages.Broadcast-Message").replace("%player%", player.getName()).replaceAll("&", "§"));
            }

            if (!main.getConfig().getBoolean("Message")) {

                player.sendMessage(main.getConfig().getString("Messages.Message-Sent-on-Suicide").replaceAll("&", "§"));

            }

            if (!main.getConfig().getBoolean("Firework")) {

                Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL_LARGE).withFlicker().build());
                meta.setPower(1);
                firework.setFireworkMeta(meta);

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

        } else if(!sender.hasPermission("suicide.command")){

            sender.sendMessage(main.getConfig().getString("Messages.No-Permission").replaceAll("&", "§"));

        }

        else {

            getServer().getConsoleSender().sendMessage(ChatColor.RED + "This command can only be ran in-game!");

        }

        return false;
    }
}