package me.prismskey.rpgcore.Events;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onMeAndKeyJoiningCauseWeAreCursed(PlayerJoinEvent event) {
        List<String> meAndKeys = Arrays.asList("Dead_Light", "Keys9");
        if (event.getPlayer().getName().equalsIgnoreCase("Dead_Light") && event.getPlayer().getName().equalsIgnoreCase("Keys9")) {

            event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.valueOf(20));
            event.getPlayer().setMaxHealth(20);
            event.getPlayer().setHealth(20);
            event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Double.valueOf(1));

        }
    }
}
