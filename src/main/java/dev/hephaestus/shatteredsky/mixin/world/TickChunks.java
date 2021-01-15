package dev.hephaestus.shatteredsky.mixin.world;

import dev.hephaestus.shatteredsky.Networking;
import dev.hephaestus.shatteredsky.util.ChunkData;
import dev.hephaestus.shatteredsky.util.ChunkDataHolder;
import dev.hephaestus.shatteredsky.util.SyncedChunkData;
import dev.hephaestus.shatteredsky.util.TickableChunkData;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.Identifier;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Optional;

@Mixin(ServerChunkManager.class)
public class TickChunks {
	@Shadow @Final private ServerWorld world;

	@Shadow @Final public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "method_20801", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void tickChunk(long l, boolean b1, SpawnHelper.Info info, boolean b2, int i, ChunkHolder chunkHolder, CallbackInfo ci, Optional<?> o1, Optional<?> o2, WorldChunk chunk) {
		for (Map.Entry<Identifier, ChunkData> pair : ((ChunkDataHolder) chunk).getChunkData().entrySet()) {
			ChunkData data = pair.getValue();

			if (data instanceof TickableChunkData) {
				((TickableChunkData) data).tick(world);
			}

			if (data instanceof SyncedChunkData && ((SyncedChunkData) data).isDirty()) {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

				buf.writeLong(chunkHolder.getPos().toLong());
				buf.writeIdentifier(pair.getKey());
				buf.writeCompoundTag(((SyncedChunkData) data).sync(world, chunk));

				this.threadedAnvilChunkStorage.getPlayersWatchingChunk(chunkHolder.getPos(), false).forEach(player -> {
					ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Networking.SYNC_CHUNK_DATA, buf);
				});
			}
		}
	}
}
