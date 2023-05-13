package me.prism3.suicide.events;

import me.prism3.suicide.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.prism3.suicide.utils.Data.isBroadCast;


/**
 * Listens for EntityDamageByEntityEvent and checks if the entity damager is a Firework with metadata "noDamage".
 * If so, the event is cancelled to prevent damage.
 */
public class PlayerDeath implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) {

        final Player player = event.getEntity();

        if (Main.getInstance().getPlayers().contains(player.getUniqueId()) && !isBroadCast) {

            Main.getInstance().getPlayers().remove(player.getUniqueId());
            event.setDeathMessage(null);
        }
    }
}
