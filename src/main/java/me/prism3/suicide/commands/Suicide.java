package me.prism3.suicide.commands;

import me.prism3.suicide.Main;
import me.prism3.suicide.utils.Data;
import me.prism3.suicide.utils.enums.FireworkType;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

import static me.prism3.suicide.utils.Data.*;


/**
 * The SuicideCommand class implements the CommandExecutor interface to handle the /suicide command.
 * When a player executes this command, it kills them and performs additional actions, such as broadcasting
 * a message or spawning a firework.
 */
public class Suicide implements CommandExecutor {

    private final Main main = Main.getInstance();
    private final Set<UUID> cooldowns;

    /**
     * Constructor for the SuicideCommand class and initialize the Set of UUIDs.
     */
    public Suicide() {
        this.cooldowns = new HashSet<>();
    }

    /**
     * Called when a player executes the /suicide command.
     * Kills the player and performs additional actions, such as broadcasting a message or spawning a firework.
     *
     * @param sender The command sender.
     * @param cmd The command being executed.
     * @param label The alias used to execute the command.
     * @param args The arguments passed with the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (!sender.hasPermission(suicideCommand))
            return false;

        // Handle /suicide reload command
        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
            return this.reloadConfig(sender);

        // Handle invalid syntax
        else if (args.length != 0)
            return this.invalidSyntax(sender);

        // Only allow players to execute this command
        if (!(sender instanceof Player))
            return this.onlyInGame();

        final Player player = (Player) sender;

        // Check if the player is in a disabled world
        if (disabledWorlds.contains(player.getWorld().getName()))
            return this.disabledWorld(sender);

        // Check if the player is on cooldown and doesn't have permission to bypass it
        if (isCoolDown && !player.hasPermission(suicideBypass) && this.cooldowns.contains(player.getUniqueId()))
            return this.onCooldown(player);

        // Add the player to the cooldown set
        this.cooldowns.add(player.getUniqueId());

        // Kill the player
        player.setHealth(0.0);

        // Broadcast a message
        if (!isBroadCast)
            this.broadcast(player);

        // Suicide Message to the Player
        if (!isMessage)
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', suicideMessage));

        // Spawn a firework at the player's death location
        if (!isFirework)
            this.spawnFirework(player.getLocation());

        if (!isCoords)
            this.displayCoord(player);

        if (!isSound)
            this.playSound(player);

        return true;
    }

    /**
     * Spawns a firework at the specified location with the desired parameters.
     *
     * @param location The location to spawn the firework.
     */
    private void spawnFirework(final Location location) {

        final Firework firework = location.getWorld().spawn(location, Firework.class);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        final FireworkEffect.Builder builder = FireworkEffect.builder();

        builder.with(FireworkEffect.Type.valueOf(Arrays.stream(FireworkType.values())
                .filter(e -> e.name().equalsIgnoreCase(fireworkType))
                .findFirst()
                .orElse(FireworkType.BALL_LARGE).name()));

        builder.withColor(Color.fromRGB(fireworkColorRed, fireworkColorGreen, fireworkColorBlue));
        builder.withFade(Color.fromRGB(fireworkFadeColorRed, fireworkFadeColorGreen, fireworkFadeColorBlue));

        if (isFireworkTrail)
            builder.withTrail();

        if (isFireworkFlicker)
            builder.withFlicker();

        fireworkMeta.addEffect(builder.build());
        fireworkMeta.setPower(fireworkPower);
        firework.setFireworkMeta(fireworkMeta);
    }

    /**
     * Sends a random broadcast message to all players with the specified format string, replacing the placeholder
     * with the player's name.
     *
     * @param player The player to use for the placeholder.
     */
    private void broadcast(final Player player) {

        // Create a new Random object to generate a random index for the list of messages.
        final Random random = new Random();

        // Get a random message from the list of messages in the plugin's configuration file, and replace any color codes with their corresponding values.
        final String message = ChatColor.translateAlternateColorCodes('&', broadCastMessages.get(random.nextInt(broadCastMessages.size()))).replace("%player%", player.getName());

        // Send the formatted message to all players on the server.
        Bukkit.broadcastMessage(message);
    }

    /**
     * Reloads the plugin's configuration file, and sends a message to the specified sender indicating success or failure.
     *
     * @param sender The sender who triggered the reload command.
     * @return true if the sender has permission to execute the command, false otherwise.
     */
    private boolean reloadConfig(final CommandSender sender) {

        // Check if the sender has permission to execute the reload command. If not, send a message indicating that they do not have permission.
        if (!sender.hasPermission(suicideReload)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
            return false;
        }

        // Reload the plugin's configuration file.
        this.main.reloadConfig();
        Data.initializer();

        // Send a message to the sender indicating that the configuration file was successfully reloaded.
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
        return true;
    }

    /**
     * Sends a message to the specified sender indicating that the syntax of their command was invalid.
     *
     * @param sender The sender who entered an invalid command.
     * @return true.
     */
    private boolean invalidSyntax(final CommandSender sender) {

        // Send a message to the sender indicating that their command syntax was invalid.
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessage));
        return false;
    }

    /**
     * Sends a message indicating that the command can only be used in-game.
     *
     * @return true.
     */
    private boolean onlyInGame() {

        // Send a message to the sender indicating that the command can only be used in-game.
        this.main.getLogger().severe("This command can only be run in-game!");

        return true;
    }

    /**
     * Sends a message to the specified sender indicating that the command is disabled in the current world.
     *
     * @param sender The sender who entered a command that is disabled in the current world.
     * @return true.
     */
    private boolean disabledWorld(final CommandSender sender) {

        // Send a message to the player notifying that he can't execute the suicide command.
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', disabledWorldMessage));
        return true;
    }

    /**
     * Sends a message to the player indicating that he still in cooldown before executing the suicide command again.
     *
     * @param player The player who entered the command.
     * @return true.
     */
    private boolean onCooldown(final Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', coolDownMessage));
        return true;
    }

    /**
     * Method to display the death coordination's of his location.
     *
     * @param player that the message will be sent too.
     */
    private void displayCoord(final Player player) {

        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&fYou suicided at: &cX: " + x + " Y: " + y + " Z: " + z));
    }

    /**
     * Method to play a sound on player's death.
     *
     * @param player that the sound will be played for.
     */
    private void playSound(final Player player) {

        final Location loc = player.getLocation();

        try {

            player.playSound(loc, Sound.valueOf(playedSound), (soundVolume), (soundPitch));

        } catch (final IllegalArgumentException e) {

            this.main.getLogger().severe(
                    "The sound you're trying to play isn't registered in this Minecraft Version: " + playedSound);
        }
    }
}
