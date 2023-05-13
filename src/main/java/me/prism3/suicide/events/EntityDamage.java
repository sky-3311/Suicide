package me.prism3.suicide.events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


/**
 * Listens for EntityDamageByEntityEvent and checks if the entity damager is a Firework with metadata "noDamage".
 * If so, the event is cancelled to prevent damage.
 */
public class EntityDamage implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void entityDamage(final EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Firework) {

            final Firework fw = (Firework) e.getDamager();

            if (fw.hasMetadata("noDamage")) e.setCancelled(true);
        }
    }
}
