package dev.hephaestus.shatteredsky.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(RenderLayers.class)
public interface RenderLayersAccessor {
	@Accessor("FLUIDS")
	static Map<Fluid, RenderLayer> getFluids() {
		throw new UnsupportedOperationException();
	}
}
