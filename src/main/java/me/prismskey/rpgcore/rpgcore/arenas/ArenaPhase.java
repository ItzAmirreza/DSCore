package me.prismskey.rpgcore.rpgcore.arenas;

import java.util.ArrayList;

public abstract class ArenaPhase {
    protected ArrayList<ArenaCommand> commands;

    public ArenaPhase() {
        commands = new ArrayList<>();
    }

    public ArrayList<ArenaCommand> getCommands() {
        return commands;
    }

    public void addCommand(ArenaCommand command) {
        commands.add(command);
    }

    public abstract boolean isComplete();

    public void runCommands() {
        for(ArenaCommand cmd: commands) {
            cmd.runCommand();
        }
    }
}
