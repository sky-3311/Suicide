package me.prism3.suicide.commands;

import me.prism3.suicide.Main;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

import static me.prism3.suicide.utils.Data.*;

public class SuicideCommand implements CommandExecutor {

    private final Main main = Main.getInstance();
    private final HashMap<UUID, Long> coolDown = new HashMap<>();
    private int i = 0;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length != 0 && !(args[0].equalsIgnoreCase("Reload"))) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.Invalid-Syntax"))));

            return false;

        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(suicideReload)) {

                this.main.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.Reload"))));

                return true;

            } else {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.No-Permission"))));
                return false;

            }
        } else if (args.length > 1 && args[0].equalsIgnoreCase("Reload")) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.Invalid-Syntax"))));
            return false;

        } else if (args.length == 0) {

            if (sender instanceof Player) {

                final Player player = (Player) sender;

                if (disabledWorlds.contains(player.getWorld().getName())) {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.Disabled"))));
                    return false;

                }

                if (!isCoolDown && !player.hasPermission(suicideBypass) && this.coolDown.containsKey(player.getUniqueId())) {

                    final long secondsLeft = ((this.coolDown.get(player.getUniqueId()) / 1000) + coolDownTime) - (System.currentTimeMillis() / 1000);

                    if (coolDownTime <= 0) return false;

                    if (secondsLeft > 0) {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.main.getConfig().getString("Messages.On-Cooldown")).replace("%time%", String.valueOf(secondsLeft))));

                    } else {
                        this.coolDown.remove(player.getUniqueId());
                        player.setHealth(0.0);
                        this.coolDown.put(player.getUniqueId(), System.currentTimeMillis());
                    }

                    return false;

                }

                this.main.getPlayers().add(player.getUniqueId());

                player.setHealth(0.0);
                this.coolDown.put(player.getUniqueId(), System.currentTimeMillis());

                // BroadCast
                if (!isBroadCast) {

                    if (isBroadCastRandom){

                        Collections.shuffle(broadCast);

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadCast.get(0).replace("%player%", player.getName())));

                    } else {

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadCast.get(this.i).replace("%player%", player.getName())));
                        this.i++;
                    }
                }

                // Suicide Message to the Player
                if (!isMessage) {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(main.getConfig().getString("Messages.Message-Sent-on-Suicide"))));

                }

                // Firework on Player Death
                if (!isFirework) {

                    final Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                    final FireworkMeta meta = firework.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL_LARGE).withFlicker().build());
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                    firework.setMetadata("noDamage", new FixedMetadataValue(this.main, true));

                }

                // Sends the death coords to the player
                if (!isCoords) {

                    final int x = player.getLocation().getBlockX();
                    final int y = player.getLocation().getBlockY();
                    final int z = player.getLocation().getBlockZ();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fYou suicided at: &cX: " + x + " Y: " + y + " Z: " + z));

                }

                // Sound Feature
                if (!isSound) {

                    final Location loc = player.getLocation();
                    player.playSound(loc, Sound.valueOf(playedSound), (soundVolume), (soundPitch));

                }

            } else {

                this.main.getLogger().severe("This command can only be run in-game!");

            }
        } return true;
    }
}