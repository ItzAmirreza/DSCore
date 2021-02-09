package me.prismskey.rpgcore.Utils;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class NBTMobAPI {

    private final Entity entity;
    private final net.minecraft.server.v1_16_R3.Entity nmsEntity;

    public NBTMobAPI(Entity entity) {
        this.entity = entity;
        this.nmsEntity = ((CraftEntity)entity).getHandle();
    }

    public NBTTagCompound getTag() {
        NBTTagCompound tag = new NBTTagCompound();
        this.nmsEntity.save(tag);
        return tag;
    }

    public boolean hasKey(String path) {
        return this.getTag().hasKey(path);
    }

    public void setString(String path, String value) {
        this.getTag().setString(path, value);
    }

    public String getString(String path) {
        return this.getTag().getString(path);
    }

    public void setDouble(String path, double value) {
        this.getTag().setDouble(path, value);
    }

    public Double getDouble(String path) {
        return this.getTag().getDouble(path);
    }

    public void setInteger(String path, int value) {
        this.getTag().setInt(path, value);
    }

    public Integer getInteger(String path) {
        return this.getTag().getInt(path);
    }

    public void setBoolean(String path, boolean value) {
        this.getTag().setBoolean(path, value);
    }

    public Boolean getBoolean(String path) {
        return this.getTag().getBoolean(path);
    }

    public void setEntity() {
        this.nmsEntity.load(this.getTag());
    }
}
