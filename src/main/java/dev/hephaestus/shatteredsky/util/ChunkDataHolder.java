package dev.hephaestus.shatteredsky.util;

import net.minecraft.util.Identifier;

import java.util.Map;

public interface ChunkDataHolder {
	Map<Identifier, ChunkData> getChunkData();
	void put(Identifier id, ChunkData data);
	<T extends ChunkData> T getChunkData(Identifier id);
}
