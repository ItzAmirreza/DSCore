package me.prismskey.rpgcore.ArenaManager;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;

public class ForceEndCommand extends BukkitCommand {
    public ForceEndCommand(String name) {
        super(name);
        this.description = "Used to force end an Arena";
        this.usageMessage = "implement usage";
        this.setPermission("rpgcore.force_end");
        this.setAliases(new ArrayList<String>());
    }

    private ArenaLoader arenaloader = new ArenaLoader();

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!sender.hasPermission("force_end")) {
            sender.sendMessage("You lack the required permission node to execute this command.");
            return true;
        }
        if(args.length < 1) {
            sender.sendMessage("Incorrect usage");
            return true;
        }
        Arena arena = arenaloader.getArenaByName(args[0]);
        if(arena == null) {
            sender.sendMessage("Could not find an arena with that name.");
            return true;
        }
        if(arena.getParty() == null) {
            sender.sendMessage("Dungeon is already reset.");
            return true;
        }

        arena.reset();
        sender.sendMessage("Dungeon instance ended.");
        return true;
    }
}
