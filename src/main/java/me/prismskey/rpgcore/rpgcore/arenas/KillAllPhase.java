package me.prismskey.rpgcore.rpgcore.arenas;

public class KillAllPhase extends ArenaPhase {
    private int killsRemaining;
    private int totalKillsForPhase;

    public void setTotalKillsForPhase(int n) {
        totalKillsForPhase = n;
    }

    public int getTotalKillsForPhase() {
        return totalKillsForPhase;
    }

    public int getKillsRemaining() {
        return killsRemaining;
    }

    public void setKillsRemaining(int kills) {
        killsRemaining = kills;
    }

    @Override
    public boolean isComplete() {
        return killsRemaining <= 0;
    }
}
