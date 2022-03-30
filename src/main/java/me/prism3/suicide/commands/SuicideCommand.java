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

import static me.prism3.suicide.Utils.Data.*;

public class SuicideCommand implements CommandExecutor {

    private final Main main = Main.getInstance();
    private final HashMap<UUID, Long> coolDown = new HashMap<>();
    private int i = 0;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length != 0 && !(args[0].equalsIgnoreCase("Reload"))) {

            sender.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));

            return false;

        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(suicideReload)) {

                this.main.reloadConfig();
                sender.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.Reload")).replaceAll("&", "§"));

                return true;

            } else {

                sender.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.No-Permission")).replaceAll("&", "§"));
                return false;

            }
        } else if (args.length > 1 && args[0].equalsIgnoreCase("Reload")) {

            sender.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));
            return false;

        } else if (args.length == 0) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (disabledWorlds.contains(player.getWorld().getName())) {

                    player.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.Disabled")).replaceAll("&", "§"));
                    return false;

                }

                if (!isCoolDown && !player.hasPermission(suicideBypass)) {

                    if (coolDown.containsKey(player.getUniqueId())) {

                        long secondsLeft = ((coolDown.get(player.getUniqueId()) / 1000) + coolDownTime) - (System.currentTimeMillis() / 1000);

                        if (coolDownTime <= 0) return false;

                        if (secondsLeft > 0) {

                            player.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("Messages.On-Cooldown")).replaceAll("&", "§").replaceAll("%time%", String.valueOf(secondsLeft)));

                        } else {
                            coolDown.remove(player.getUniqueId());
                            player.setHealth(0.0);
                            coolDown.put(player.getUniqueId(), System.currentTimeMillis());
                        }

                        return false;

                    }
                }

                this.main.getPlayers().add(player.getUniqueId());

                player.setHealth(0.0);
                coolDown.put(player.getUniqueId(), System.currentTimeMillis());

                // BroadCast
                if (!isBroadCast) {

                    if (isBroadCastRandom){

                        Collections.shuffle(broadCast);

                        Bukkit.broadcastMessage(broadCast.get(0).replace("%player%", player.getName()).replace("&", "§"));

                    } else {

                        Bukkit.broadcastMessage(broadCast.get(i).replace("%player%", player.getName()).replace("&", "§"));
                        i++;
                    }
                }

                // Suicide Message to the Player
                if (!isMessage) {

                    player.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Message-Sent-on-Suicide")).replace("&", "§"));

                }

                // Firework on Player Death
                if (!isFirework) {

                    Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL_LARGE).withFlicker().build());
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                    firework.setMetadata("noDamage", new FixedMetadataValue(this.main, true));

                }

                // Sends the death coords to the player
                if (!isCoords) {

                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    player.sendMessage(ChatColor.GREEN + "You suicided at: " + ChatColor.RED + "X: " + x + " Y: " + y + " Z: " + z);

                }

                // Sound Feature
                if (!isSound) {

                    Location loc = player.getLocation();
                    player.playSound(loc, Sound.valueOf(playedSound), (soundVolume), (soundPitch));

                }

            } else {

                this.main.getLogger().severe("This command can only be run in-game!");

            }
        } return true;
    }
}