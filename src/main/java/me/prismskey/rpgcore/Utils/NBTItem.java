package me.prismskey.rpgcore.Utils;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItem {

    private final ItemStack item;
    private final net.minecraft.server.v1_16_R3.ItemStack stack;

    public NBTItem(ItemStack item) {
        this.item = item;
        this.stack = CraftItemStack.asNMSCopy(item);
    }

    public NBTTagCompound getTag() {
        if(this.stack.getTag() == null) {
            this.stack.setTag(new NBTTagCompound());
        }
        return this.stack.getTag();
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

    public ItemStack getItem() {
        return CraftItemStack.asCraftMirror(this.stack);
    }
}
