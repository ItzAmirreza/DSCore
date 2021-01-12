package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.ArenaManager.SpawningSystem;
import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;

public class FinishedWaveEvent implements Listener {

    SpawningSystem spawningSystem = new SpawningSystem();
    @EventHandler
    public void onWave(onFinishedWave e) {
        String arenaName = e.getArena().name;
        if (e.getArena().currentPhase.current_wave == e.getArena().currentPhase.wavescount) {

            Collection<Phase> phaseList = e.getArena().phases.values();
            ArrayList<Phase> newList = new ArrayList<>(phaseList);
            int currentindex = newList.indexOf(e.getArena().currentPhase);
            int wholelength = newList.size();

            if (currentindex + 1 == wholelength) {
                shortTermStorages.arenaHashMap.get(arenaName).finishArena();
            } else {
                Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
                Phase nextPhase = newList.get(currentindex + 1);
                nextPhase.state = PhaseState.INGAME;
                shortTermStorages.arenaHashMap.get(arenaName).currentPhase.state = PhaseState.PASSED;
                shortTermStorages.arenaHashMap.get(arenaName).currentPhase = nextPhase;
                shortTermStorages.arenaHashMap.get(arenaName).phases.get(thatArena.currentPhase.name).state = PhaseState.PASSED;
                shortTermStorages.arenaHashMap.get(arenaName).announceToAllPlayers("&aPhase completed, now you are allowed to enter the next level.");
            }

        } else {

            int currentwave = shortTermStorages.arenaHashMap.get(arenaName).currentPhase.current_wave;
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            shortTermStorages.arenaHashMap.get(arenaName).currentPhase.current_wave = currentwave + 1;
            shortTermStorages.arenaHashMap.get(arenaName).phases.get(thatArena.currentPhase.name).current_wave = currentwave + 1;
            spawningSystem.spawn(arenaName);
            e.getArena().announceToAllPlayers("&eNext wave started!");

        }

    }
}
