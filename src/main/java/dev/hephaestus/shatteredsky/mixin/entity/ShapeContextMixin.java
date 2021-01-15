package dev.hephaestus.shatteredsky.mixin.entity;

import dev.hephaestus.shatteredsky.util.ShapeContextFluidyThing;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(EntityShapeContext.class)
public class ShapeContextMixin implements ShapeContextFluidyThing {
    @Shadow @Final private Predicate<Fluid> field_24425;

    @Override
    public boolean test(FluidState state, Fluid fluid) {
        return this.field_24425.test(fluid) && !state.getFluid().matchesType(fluid);
    }
}
