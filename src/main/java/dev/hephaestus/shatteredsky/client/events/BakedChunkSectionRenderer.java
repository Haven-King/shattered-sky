package dev.hephaestus.shatteredsky.client.events;

import dev.hephaestus.shatteredsky.util.ArrayBackedConditionalEvent;
import dev.hephaestus.shatteredsky.util.ConditionalEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public interface BakedChunkSectionRenderer {
    ConditionalEvent<ChunkRenderConditionContext, BakedChunkSectionRenderer> EVENT =
            new ArrayBackedConditionalEvent<>(listeners -> {
                return (context) -> {
                    ChunkRenderConditionContext conditionContext = new ChunkRenderConditionContext(context.startPos, context.endPos);
                    for (Pair<Function<ChunkRenderConditionContext, Boolean>, BakedChunkSectionRenderer> pair : listeners) {
                        if (pair.getLeft().apply(conditionContext)) {
                            pair.getRight().bake(context);
                        }
                    }
                };
            }, null);

    void bake(ChunkRenderContext context);

    class ChunkRenderContext {
        public final ChunkRendererRegion chunkRendererRegion;
        public final BlockState state;
        public final BlockPos pos;
        public final Random random;
        public final BlockRenderManager blockRenderManager;
        public final TerrainRenderContext context;
        public final BlockPos endPos;
        public final BlockPos startPos;
        public final MatrixStack matrixStack;

        public ChunkRenderContext(ChunkRendererRegion chunkRendererRegion, BlockState state, BlockRenderManager blockRenderManager, BlockPos startPos, BlockPos endPos, BlockPos pos, Random random, TerrainRenderContext context, MatrixStack matrixStack) {
            this.chunkRendererRegion = chunkRendererRegion;
            this.state = state;
            this.blockRenderManager = blockRenderManager;
            this.startPos = startPos;
            this.endPos = endPos;
            this.pos = pos;
            this.random = random;
            this.context = context;
            this.matrixStack = matrixStack;
        }
    }

    class ChunkRenderConditionContext {
        public final BlockPos startPos;
        public final BlockPos endPos;

        public ChunkRenderConditionContext(BlockPos startPos, BlockPos endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }
    }
}
