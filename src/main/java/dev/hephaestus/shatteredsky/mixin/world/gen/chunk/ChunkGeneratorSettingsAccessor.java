package dev.hephaestus.shatteredsky.mixin.world.gen.chunk;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGeneratorSettings.class)
public interface ChunkGeneratorSettingsAccessor {
	@Accessor("mobGenerationDisabled")
	boolean isMobGenerationDisabled();
}
