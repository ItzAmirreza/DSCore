package me.prismskey.rpgcore.rpgcore.arenas;

import org.bukkit.Bukkit;

public class ServerCommandExecutor extends ArenaCommand {

    public String serverCommandString;

    public ServerCommandExecutor(String serverCommandString) {
        this.serverCommandString = serverCommandString;
    }

    @Override
    public void runCommand() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), serverCommandString);
    }


}
