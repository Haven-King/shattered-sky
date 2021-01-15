package dev.hephaestus.shatteredsky.util;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;

public interface ShapeContextFluidyThing {
    boolean test(FluidState state, Fluid fluid);
}
