package me.prismskey.rpgcore.Events;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.ArenaLoader;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnCommandPreProcess implements Listener {


    private ArenaLoader arenaloader = new ArenaLoader();



    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if(event.getMessage().equalsIgnoreCase("/party disband")) {
            Player player = event.getPlayer();
            for(Arena arena: shortTermStorages.arenas) {
                DungeonParty party = arena.getParty();
                if(party != null) {
                    if(party.mcmmoParty.getLeader().getUniqueId().equals(player.getUniqueId())) {

                        for(Player pl: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                            if(arenaloader.isWithinDungeon(pl.getLocation())) {
                                pl.teleport(shortTermStorages.spawn);
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
                for(Arena arena: shortTermStorages.arenas) {
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
