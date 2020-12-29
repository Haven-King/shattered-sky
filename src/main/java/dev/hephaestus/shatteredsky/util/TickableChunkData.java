package dev.hephaestus.shatteredsky.util;

import net.minecraft.server.world.ServerWorld;

public interface TickableChunkData extends ChunkData {
	void tick(ServerWorld world);
}
