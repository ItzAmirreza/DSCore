package me.prismskey.rpgcore.DataManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.Bukkit;

import java.util.UUID;

public class RPGPlayerData {
    private UUID playerUUID;
    private int camelotsMightCooldownTime;
    private int strikeBoltCooldownTime;
    private int lifeDrainCooldown;
    private int deathsCallCooldown;
    private int startRainCooldown;
    private int masterSwordBeamCooldown;
    private KEYBLADE_MODE keybladeMode;
    private int lostvayneCharge;


    private int pvpToggleCoolDown;
    private boolean pvpOn;



    public RPGPlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        camelotsMightCooldownTime = 0;
        strikeBoltCooldownTime = 0;
        lifeDrainCooldown = 0;
        deathsCallCooldown = 0;
        pvpToggleCoolDown = 0;
        startRainCooldown = 0;
        masterSwordBeamCooldown = 0;
        keybladeMode = KEYBLADE_MODE.FIRE;

        pvpOn = shortTermStorages.pvpStatesConfiguration.getBoolean(playerUUID.toString());

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

        if(pvpToggleCoolDown > 0) {
            pvpToggleCoolDown--;
        }
        if(startRainCooldown > 0) {
            startRainCooldown--;
        }
        if(masterSwordBeamCooldown > 0) {
            masterSwordBeamCooldown--;
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

    public int getPvpToggleCoolDown() {
        return pvpToggleCoolDown;
    }

    public void resetPvpToggleCooldown() {
        pvpToggleCoolDown = 60 * 20;
    }

    public boolean isPlayerOnline() {
        return Bukkit.getPlayer(playerUUID) != null;
    }

    public boolean allCooldownsOff() {
        return camelotsMightCooldownTime <= 0 && strikeBoltCooldownTime <= 0 && lifeDrainCooldown <= 0 &&
                deathsCallCooldown <= 0 && pvpToggleCoolDown <= 0 && startRainCooldown <= 0 || masterSwordBeamCooldown <= 0;
    }

    public int getStarRainCooldown() {
        return startRainCooldown;
    }

    public void setStarRainCooldown(int cooldown) {
        startRainCooldown = cooldown;
    }

    public int getMasterSwordBeamCooldown() {
        return masterSwordBeamCooldown;
    }

    public void setMasterSwordBeamCooldown(int cooldown) {
        masterSwordBeamCooldown = cooldown;
    }

    public void switchKeyBladeMode() {
        if(keybladeMode == KEYBLADE_MODE.FIRE) {
            keybladeMode = KEYBLADE_MODE.ICE;
        } else if(keybladeMode == KEYBLADE_MODE.ICE) {
            keybladeMode = KEYBLADE_MODE.LIGHTNING;
        } else if(keybladeMode == KEYBLADE_MODE.LIGHTNING) {
            keybladeMode = KEYBLADE_MODE.FIRE;
        }
    }

    public KEYBLADE_MODE getKeybladeMode() {
        return keybladeMode;
    }

    public int getLostvayneCharge() {
        return lostvayneCharge;
    }

    public void chargeLostvayne() {
        if(lostvayneCharge < 20) {
            lostvayneCharge++;
        }
    }

    public void decrementLostvayneCharge() {
        lostvayneCharge--;
    }

    public void resetLostvayneCharge() {
        lostvayneCharge = 0;
    }
}
