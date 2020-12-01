package me.prismskey.rpgcore.rpgcore.arenas;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import me.prismskey.rpgcore.rpgcore.Rpgcore;
//import me.prismskey.rpgcore.rpgcore.party.Party;
import me.prismskey.rpgcore.rpgcore.party.DungeonParty;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DungeonJoinCommand extends BukkitCommand {

    public DungeonJoinCommand(String name) {
        super(name);
        this.description = "A command for joining a dungeon";
        this.usageMessage = "implement usage";
        this.setPermission("rpgcore.dungeon");
        this.setAliases(new ArrayList<String>());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if(args.length < 1) {
            sender.sendMessage("Incorrect usage.");
            return true;
        }
        String subcommand = args[0];

        if(subcommand.equalsIgnoreCase("list")) {
            for(Arena arena: Rpgcore.instance.getArenas()) {
                sender.sendMessage(arena.getName() + ": " + arena.getStatus());
            }
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }
        Player player = (Player) sender;


        if(subcommand.equalsIgnoreCase("join")) {
            if(args.length < 2) {
                player.sendMessage("please specify which dungeon you want to join.");
            }
            String dungeonName = args[1];
            Arena arena = Rpgcore.instance.getArenaByName(dungeonName);
            if(arena == null) {
                player.sendMessage("Could not find an arena with that name.");
                return true;
            }
            if(arena.getParty() != null) {
                player.sendMessage("That arena is currently in use. Please try again later.");
                return true;
            }

            if(!PartyAPI.isHosting(player)) {
                player.sendMessage("Only the party host can join a dungeon.");
                return true;
            }

            for(Arena a: Rpgcore.instance.getArenas()) {
                if(a.getParty() == null) {
                    continue;
                }
                if(a.getParty().mcmmoParty.hasMember(player.getUniqueId())) {
                    player.sendMessage("Your party is already registered for a dungeon.");
                    return true;
                }
            }

            Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
            if(mcmmoParty == null) {
                player.sendMessage("You must be hosting a party to join a dungeon.");
            }
            DungeonParty party = new DungeonParty(mcmmoParty);

            if(party.allPlayersOnline()) {
                arena.setParty(party);
                party.setArena(arena);
                arena.start();
            } else {
                player.sendMessage("At least one party member is offline. All members must be online to join a dungeon.");
                return true;
            }
        } else if(subcommand.equalsIgnoreCase("rejoin")) {
            Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
            if(mcmmoParty == null) {
                player.sendMessage("You are not in a party.");
                return true;
            }

            Arena targetArena = null;
            for(Arena arena: Rpgcore.instance.getArenas()) {
                DungeonParty dungeonParty = arena.getParty();
                if(dungeonParty != null) {
                    if(dungeonParty.mcmmoParty.hasMember(player.getName())) {
                        targetArena = arena;
                        break;
                    }
                }
            }
            if(targetArena == null) {
                player.sendMessage("Your party is not registered for a dungeon.");
                return true;
            }

            if(!Rpgcore.instance.isWithinDungeon(player.getLocation())) {
                targetArena.teleportPlayerToSpawn(player);
            }
        } else if(subcommand.equalsIgnoreCase("leave")) {
            Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
            if(mcmmoParty == null) {
                player.sendMessage("You are not in a dungeon");
                return true;
            }

            Arena targetArena = null;
            for(Arena arena: Rpgcore.instance.getArenas()) {
                DungeonParty dungeonParty = arena.getParty();
                if(dungeonParty != null) {
                    if(dungeonParty.mcmmoParty.hasMember(player.getName())) {
                        targetArena = arena;
                        break;
                    }
                }
            }

            if(targetArena == null) {
                player.sendMessage("You are not in a dungeon");
                return true;
            }
            if(!PartyAPI.isHosting(player)) {
                player.sendMessage("Only the party host can do that. To leave the dungeon use /spawn");
                return true;
            }

            for(Player p: mcmmoParty.getOnlineMembers()) {
                if(Rpgcore.instance.isWithinDungeon(p.getLocation())) {
                    p.teleport(Rpgcore.instance.getSpawn());
                    p.sendMessage("Since the host ended the raid you have been kicked from the dungeon.");
                }
            }

            targetArena.reset();
        }

        return true;
    }
}
