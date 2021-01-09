package dev.hephaestus.shatteredsky.world.gen.surfacebuilder;

import dev.hephaestus.shatteredsky.ShatteredSky;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class PoolsSurfaceBuilder extends SkySurfaceBuilder {
	public PoolsSurfaceBuilder() {
		super();
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
		boolean water = noise > 0.35;

		for(int y = height; y >= 0; --y) {
			mut.set(relativeX, y, relativeZ);
			BlockState currentState = chunk.getBlockState(mut);

			if (currentState.isAir() || currentState.isOf(ShatteredSky.Blocks.ATMOSPHERE)) {
				depth = -1;
			} else if (currentState.isOf(defaultBlock.getBlock())) {
				if (depth == -1) {
					depth = maxDepth;

					if (water) {
						chunk.setBlockState(mut, Blocks.WATER.getDefaultState(), false);
						chunk.getFluidTickScheduler().schedule(mut, Fluids.WATER, 0);
					} else {
						chunk.setBlockState(mut, topMaterial, false);
					}
				} else if (depth > 0) {
					--depth;

					if (depth == maxDepth - 1) {
						chunk.setBlockState(mut, config.getUnderwaterMaterial(), false);
					} else {
						chunk.setBlockState(mut, underMaterial, false);
					}
				}
			}
		}

		this.generateShatteredBottom(height, mut, mut2, relativeX, relativeZ, chunk, defaultBlock, random);
	}
}
