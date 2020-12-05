package me.prismskey.rpgcore.Utils;

import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class APIUsages {

    public static boolean hasMobNBT(Entity e, String tag) {
        net.minecraft.server.v1_16_R3.Entity entity = ((CraftEntity)e).getHandle();

        net.minecraft.server.v1_16_R3.NBTTagCompound compound = new net.minecraft.server.v1_16_R3.NBTTagCompound();
        entity.d(compound);
        NBTTagList list = compound.getList("Tags",8);
        return list.toString().contains(tag);
    }

}
