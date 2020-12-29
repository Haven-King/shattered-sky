package dev.hephaestus.shatteredsky.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Test extends PersistentProjectileEntity {
    protected Test(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public void tick() {

    }
}
