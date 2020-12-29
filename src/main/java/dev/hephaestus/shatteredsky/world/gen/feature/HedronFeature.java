package dev.hephaestus.shatteredsky.world.gen.feature;

import com.mojang.serialization.Codec;
import dev.hephaestus.shatteredsky.block.HedronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HedronFeature<T extends HedronFeatureConfig> extends Feature<T> {
    public HedronFeature(Codec<T> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, HedronFeatureConfig config) {
        ChunkRegion region = (ChunkRegion) world;

        final int radius = random.nextInt(config.getMaxSize() - config.getMinSize()) + config.getMinSize();
        final int wiggle = 48 - radius * 2 - 2;
        final BlockPos origin = new BlockPos(
                pos.getX() - Math.floorMod(pos.getX(), 16) + 8 + random.nextInt(wiggle) - wiggle / 2,
                Math.max(pos.getY(), config.getMinY()) + random.nextInt(config.getMaxY() - config.getMinY()),
                pos.getZ() - Math.floorMod(pos.getY(), 16) + 8 + random.nextInt(wiggle) - wiggle / 2);
        final BlockPos.Mutable mut = new BlockPos.Mutable();
        final Map<BlockPos, BlockState> states = new HashMap<>();

        int r = radius;

        for (int dY = 0; dY >= -radius; --dY, --r) {
            if (buildSection(region, config, origin, mut, states, r, dY)) return false;
        }

        r = radius;

        for (int dY = 1; dY <= radius * 2 + 1; ++dY) {
            if (buildSection(region, config, origin, mut, states, r, dY)) return false;

            if (dY % 2 == 0) {
                --r;
            }
        }

        for (Map.Entry<BlockPos, BlockState> entry : states.entrySet()) {
            this.setBlockState(world, entry.getKey(), entry.getValue());
        }

        return true;
    }

    private boolean buildSection(ChunkRegion region, HedronFeatureConfig config, BlockPos origin, BlockPos.Mutable mut, Map<BlockPos, BlockState> states, int r, int dY) {
        for (int dX = -r; dX <= r; ++dX) {
            for (int dZ = -r; dZ <= r; ++dZ) {
                mut.set(origin);
                mut.move(dX, dY, dZ);

                if (!region.getBlockState(mut).isAir() || !region.isChunkLoaded(mut.getX() >>4, mut.getZ() >> 4)) {
                    return true;
                }

                BlockState state = getState(config.getHedronBlock(), r, dX, dZ, dY);

                if (!state.isAir()) {
                    states.put(new BlockPos(mut), state);
                }
            }
        }
        return false;
    }

    private static BlockState getState(Block block, int r, int dX, int dZ, int dY) {
        HedronBlock.Shape shape;

        if (r == 0) {
            shape = HedronBlock.Shape.POINT;
        } else if (dX == dZ && Math.abs(dX) == r) {
            shape = dX > 0
                    ? HedronBlock.Shape.NORTH_WEST
                    : HedronBlock.Shape.SOUTH_EAST;
        } else if (dX == -dZ && Math.abs(dX) == r) {
            shape = dX > 0
                    ? HedronBlock.Shape.SOUTH_WEST
                    : HedronBlock.Shape.NORTH_EAST;
        } else if (dX == r) {
            shape = HedronBlock.Shape.WEST;
        } else if (dX == -r) {
            shape = HedronBlock.Shape.EAST;
        } else if (dZ == r) {
            shape = HedronBlock.Shape.NORTH;
        } else if (dZ == -r) {
            shape = HedronBlock.Shape.SOUTH;
        } else {
            return Blocks.AIR.getDefaultState();
        }

        if (dY > 0) {
            shape = shape.getTallVariant(dY % 2 == 1);
        }

        return block.getDefaultState().with(Properties.BOTTOM, dY > 0).with(HedronBlock.SHAPE, shape);
    }
}
