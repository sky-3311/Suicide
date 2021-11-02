package com.carpour.suicide.Events;

import com.carpour.suicide.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    public void onDeath(PlayerDeathEvent event){

        Player player = event.getEntity();

        if(Main.getInstance().getPlayers().contains(player.getUniqueId()) && !main.getConfig().getBoolean("Broadcast")){

            Main.getInstance().getPlayers().remove(player.getUniqueId());
            event.setDeathMessage(null);

        }
    }
}
