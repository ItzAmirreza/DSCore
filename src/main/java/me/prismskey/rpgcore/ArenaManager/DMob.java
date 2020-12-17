package me.prismskey.rpgcore.ArenaManager;

import org.bukkit.entity.EntityType;

public class DMob {

    public String mob;
    public int percentage;
    public boolean isSpecial;
    public int level = 1;


    public DMob(String mob, int percentage, boolean isSpecial) {
        this.mob = mob;
        this.percentage = percentage;
        this.isSpecial = isSpecial;
    }


    public EntityType getEntityType() {
        return EntityType.valueOf(this.mob);
    }

    public String getSpecialitiyString() {
        if (this.isSpecial) {
            return "special";
        } else {
            return "vanilla";
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
