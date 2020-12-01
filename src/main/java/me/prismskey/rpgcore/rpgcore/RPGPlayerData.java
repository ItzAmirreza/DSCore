package me.prismskey.rpgcore.rpgcore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RPGPlayerData {
    private UUID playerUUID;
    private int camelotsMightCooldownTime;
    private int strikeBoltCooldownTime;
    private int lifeDrainCooldown;
    private int deathsCallCooldown;
    private boolean pvpOn;



    public RPGPlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        camelotsMightCooldownTime = 0;
        strikeBoltCooldownTime = 0;
        lifeDrainCooldown = 0;
        deathsCallCooldown = 0;

        pvpOn = Rpgcore.instance.getPvpStatesConfiguration().getBoolean(playerUUID.toString());

    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setCamelotsMightCooldown(int time) {
        camelotsMightCooldownTime = time;
    }

    public void setStrikeBoltCooldownTime(int time) {
        strikeBoltCooldownTime = time;
    }

    public int getCamelotsMightCooldown() {
        return camelotsMightCooldownTime;
    }

    public int getStrikeBoltCooldown() {
        return strikeBoltCooldownTime;
    }

    public int getLifeDrainCooldown() { return lifeDrainCooldown;}

    public void setLifeDrainCooldown(int time) { lifeDrainCooldown = time;}

    public int getDeathsCallCoolDown() { return deathsCallCooldown;}

    public void setDeathsCallCooldown(int time) { deathsCallCooldown = time;}

    public void decrementCooldowns() {
        if(camelotsMightCooldownTime > 0) {
            camelotsMightCooldownTime--;
        }
        if(strikeBoltCooldownTime > 0) {
            strikeBoltCooldownTime--;
        }
        if(lifeDrainCooldown > 0) {
            lifeDrainCooldown--;
        }
        if(deathsCallCooldown > 0) {
            deathsCallCooldown--;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RPGPlayerData)) {
            return false;
        }
        RPGPlayerData other = (RPGPlayerData) obj;
        if(playerUUID.equals(other.getPlayerUUID())) {
            return true;
        }
        return false;
    }

    public void setPvpState(boolean state) {
        pvpOn = state;
    }

    public boolean getPvpState() {
        return pvpOn;
    }

    public boolean isPlayerOnline() {
        return Bukkit.getPlayer(playerUUID) != null;
    }

    public boolean allCooldownsOff() {
        return camelotsMightCooldownTime <= 0 && strikeBoltCooldownTime <= 0;
    }
}
