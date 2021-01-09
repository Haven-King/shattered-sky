package dev.hephaestus.shatteredsky.world.gen.surfacebuilder;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.block.SkyStoneBlock;
import dev.hephaestus.shatteredsky.block.WorldgenDummyBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class SkySurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
	public SkySurfaceBuilder() {
		super(TernarySurfaceConfig.CODEC);
	}

	@Override
	public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig config) {
		BlockState topMaterial = config.getTopMaterial();
		BlockState underMaterial = config.getUnderMaterial();
		BlockPos.Mutable mut = new BlockPos.Mutable();
		BlockPos.Mutable mut2 = new BlockPos.Mutable();

		int depth = -1;
		int maxDepth = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		int relativeX = x & 15;
		int relativeZ = z & 15;

		for(int y = height; y >= 0; --y) {
			mut.set(relativeX, y, relativeZ);
			mut2.set(relativeX, y - 1, relativeZ);

			BlockState currentState = chunk.getBlockState(mut);

			if (currentState.isAir()) {
				depth = -1;
			} else if (currentState.isOf(defaultBlock.getBlock())) {
				if (depth == -1) {
					depth = maxDepth;
					chunk.setBlockState(mut, topMaterial, false);
				} else if (depth > 0) {
					--depth;
					chunk.setBlockState(mut, underMaterial, false);
				}
			} else if (currentState.isOf(ShatteredSky.Blocks.WORLDGEN_DUMMY)) {
				if (depth == -1) {
					depth = maxDepth;
					chunk.setBlockState(mut, ShatteredSky.Blocks.WORLDGEN_DUMMY.getDefaultState().with(WorldgenDummyBlock.TYPE, WorldgenDummyBlock.Type.TOP_MATERIAL), false);
				} else if (depth > 0) {
					--depth;
					chunk.setBlockState(mut, ShatteredSky.Blocks.WORLDGEN_DUMMY.getDefaultState().with(WorldgenDummyBlock.TYPE, WorldgenDummyBlock.Type.SUBSTRATE), false);
				}
			}
		}

		this.generateShatteredBottom(height, mut, mut2, relativeX, relativeZ, chunk, defaultBlock, random);
	}

	protected void generateShatteredBottom(int height, BlockPos.Mutable mut, BlockPos.Mutable mut2, int relativeX, int relativeZ, Chunk chunk, BlockState defaultBlock, Random random) {
		for (int y = 0; y < height; ++y) {
			mut.set(relativeX, y, relativeZ);
			mut2.set(relativeX, y + 1, relativeZ);

			BlockState state = chunk.getBlockState(mut);
			BlockState nextState = chunk.getBlockState(mut2);

			if (state.isAir() && nextState == defaultBlock) {
				for (int i = 0; i < random.nextInt(random.nextInt(3) + 2); ++i) {
					mut.move(Direction.DOWN, random.nextInt(7));

					chunk.setBlockState(mut, ShatteredSky.Blocks.SKY_STONE.getDefaultState().with(SkyStoneBlock.SHATTERED, true), false);
				}

				return;
			}
		}
	}
}
