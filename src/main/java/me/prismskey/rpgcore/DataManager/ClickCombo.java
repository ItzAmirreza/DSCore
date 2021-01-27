package me.prismskey.rpgcore.DataManager;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ClickCombo {

    private char[] combo = new char[3];
    private static final int TICKS_TILL_EXPIRY = 40;
    private int currentTicks;

    public ClickCombo() {
        resetCombo();
    }

    public String getCombo() {
        return "[ " + combo[0] + " " + combo[1] + " " + combo[2] + " ]";
    }

    public void resetCombo() {
        combo[0] = '-';
        combo[1] = '-';
        combo[2] = '-';
        currentTicks = 0;
    }

    public void addClick(CLICKTYPE type, Player player) {
        for(int i = 0; i < combo.length; i++) {
            if(combo[i] == '-') {
                combo[i] = convertClickType(type);
                currentTicks = 0;
                if(i == 0) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                } else if(i == 1) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.2f);
                } else if(i == 2) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, .5f);
                }
                break;
            }
        }
    }

    private char convertClickType(CLICKTYPE type) {
        if(type == CLICKTYPE.LEFT) {
            return 'L';
        } else {
            return 'R';
        }
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public void checkTime() {
        if(currentTicks == TICKS_TILL_EXPIRY) {
            resetCombo();
        }
    }

    public void incrementTimer() {
        currentTicks++;
    }
}
