package dev.hephaestus.shatteredsky.util;

import net.minecraft.world.chunk.Chunk;

public interface ChunkDataDefinition<T extends ChunkData> {
	T of(Chunk chunk);
}
