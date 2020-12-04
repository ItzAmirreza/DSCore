package me.prismskey.rpgcore.Events;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.PartySystem.DungeonParty;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnCommandPreProcess implements Listener {



    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if(event.getMessage().equalsIgnoreCase("/party disband")) {
            Player player = event.getPlayer();
            for(Arena arena: Rpgcore.instance.getArenas()) {
                DungeonParty party = arena.getParty();
                if(party != null) {
                    if(party.mcmmoParty.getLeader().getUniqueId().equals(player.getUniqueId())) {

                        for(Player pl: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                            if(Rpgcore.instance.isWithinDungeon(pl.getLocation())) {
                                pl.teleport(Rpgcore.instance.getSpawn());
                                pl.sendMessage("Since the host ended the party you have been kicked from the dungeon.");
                            }
                        }
                        arena.reset();
                    }
                }
            }
        }

        if(event.getMessage().equalsIgnoreCase("/party leave") ||
           event.getMessage().equalsIgnoreCase("/party quit")) {
            Player player = event.getPlayer();
            Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
            if(mcmmoParty == null) {
                return;
            }
            if(PartyAPI.getMembersMap(player).size() == 1) {
                for(Arena arena: Rpgcore.instance.getArenas()) {
                    DungeonParty party = arena.getParty();
                    if(party != null) {
                        if(mcmmoParty.getName().equals(party.mcmmoParty.getName())) {
                            arena.reset();
                        }
                    }
                }
            }
        }

        if(event.getMessage().toLowerCase().startsWith("/party create")) {
            Player player = event.getPlayer();
            Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
            if(mcmmoParty == null) {
                return;
            }
            player.sendMessage("You must leave your party first before you can create another one.");
            event.setCancelled(true);
        }
    }
}
