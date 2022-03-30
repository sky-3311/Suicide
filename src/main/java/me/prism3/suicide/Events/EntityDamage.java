package me.prism3.suicide.Events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e){

        if (e.getDamager() instanceof Firework) {

            Firework fw = (Firework) e.getDamager();

            if (fw.hasMetadata("noDamage")) e.setCancelled(true);

        }
    }
}
