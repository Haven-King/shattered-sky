package dev.hephaestus.shatteredsky.util;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class SyncedChunkData implements ChunkData {
	private boolean dirty = false;

	protected void markDirty() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public CompoundTag sync(ServerWorld world, Chunk chunk) {
		this.dirty = false;
		return this.serialize(world, chunk, new CompoundTag());
	}

	public static void receive(PacketContext context, PacketByteBuf buf) {
		ChunkPos pos = new ChunkPos(buf.readLong());
		Identifier id = buf.readIdentifier();
		CompoundTag tag = buf.readCompoundTag();

		context.getTaskQueue().execute(() -> {
			World world = context.getPlayer().world;
			Chunk chunk = world.getChunk(pos.x, pos.z);
			ChunkDataRegistry.create(id, chunk).deserialize(world, chunk, tag);
		});
	}
}
