package me.prismskey.rpgcore.Mobs;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityFallingBlock;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomFallingBlock extends EntityFallingBlock {

    public CustomFallingBlock(Location l) {
        super(((CraftWorld) l.getWorld()).getHandle(), l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, fromMaterial(Material.STONE));
    }

    @Override
    public void collide(Entity entity) {
        org.bukkit.entity.Entity ent = entity.getBukkitEntity();
        if(ent instanceof LivingEntity) {
            ent.setVelocity(this.getBukkitEntity().getVelocity());
            ((LivingEntity) ent).damage(5.0, this.getBukkitEntity());
            //ent.getWorld().strikeLightningEffect(ent.getLocation());
            ent.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, ((LivingEntity) ent).getEyeLocation(), 0);
        }
    }

    public static IBlockData fromMaterial(Material m) {
        net.minecraft.server.v1_16_R3.Block nmsBlock = CraftMagicNumbers.getBlock(m);
        return nmsBlock.getBlockData();
    }

    public FallingBlock spawn() {
        this.velocityChanged = true;
        this.hurtEntities = true;
        this.ticksLived = 1;
        this.dropItem = false;
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

        return (FallingBlock) this.getBukkitEntity();
    }
}
