package dev.hephaestus.shatteredsky.mixin.client.render.chunk;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.client.events.BakedChunkSectionRenderer;
import dev.hephaestus.shatteredsky.data.SkyFalls;
import dev.hephaestus.shatteredsky.util.ChunkData;
import dev.hephaestus.shatteredsky.util.ChunkDataRegistry;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessChunkRendererRegion;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Mixin(targets = "net/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask")
public abstract class ChunkBuilderMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getRenderType()Lnet/minecraft/block/BlockRenderType;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderAtmosphere(float cameraX, float cameraY, float cameraZ, ChunkBuilder.ChunkData data, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<Set<BlockEntity>> cir, int i, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, Set set, ChunkRendererRegion chunkRendererRegion, MatrixStack matrixStack, Random random, BlockRenderManager blockRenderManager, Iterator var15, BlockPos blockPos3, BlockState blockState) {
		int y = blockPos3.getY();

		BakedChunkSectionRenderer.ChunkRenderContext chunkRenderContext = new BakedChunkSectionRenderer.ChunkRenderContext(
				chunkRendererRegion,
				blockState,
				blockRenderManager,
				blockPos,
				blockPos2,
				blockPos3,
				random,
				((AccessChunkRendererRegion) chunkRendererRegion).fabric_getRenderer(),
				matrixStack
		);

		matrixStack.push();
		matrixStack.translate((blockPos3.getX() & 15), (blockPos3.getY() & 15), (blockPos3.getZ() & 15));

		BakedChunkSectionRenderer.EVENT.invoker().bake(chunkRenderContext);

		matrixStack.pop();
	}
}
