package me.prism3.suicide.events;

import me.prism3.suicide.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.prism3.suicide.utils.Data.isBroadCast;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event){

        final Player player = event.getEntity();

        if (Main.getInstance().getPlayers().contains(player.getUniqueId()) && !isBroadCast){

            Main.getInstance().getPlayers().remove(player.getUniqueId());
            event.setDeathMessage(null);

        }
    }
}
