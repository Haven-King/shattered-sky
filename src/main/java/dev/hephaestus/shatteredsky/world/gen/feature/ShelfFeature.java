package dev.hephaestus.shatteredsky.world.gen.feature;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.block.WorldgenDummyBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ShelfFeature extends Feature<ShelfFeatureConfig> {
    public ShelfFeature() {
        super(ShelfFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos origin, ShelfFeatureConfig config) {
        List<BlockPos> positions = new ArrayList<>();

        BlockPos.iterate(origin, origin.add(15, 255, 15)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            if (state.isOf(ShatteredSky.Blocks.WORLDGEN_DUMMY)) {
                positions.add(pos.toImmutable());
                this.setBlockState(world, pos, state.get(WorldgenDummyBlock.TYPE).getState());
            }
        });

        if (positions.isEmpty()) return false;

        BlockPos.Mutable mut = new BlockPos.Mutable();

        int radius;
        double d = (config.getRadius().max - config.getRadius().min) / (double) positions.size();

        for (int i = 0; i < positions.size(); ++i) {
            BlockPos pos = positions.get(i);
            radius = (int) (config.getRadius().min + d * i);

            for (int dX = -radius; dX < radius + 1; ++dX) {
                for (int dZ = -radius; dZ < radius + 1; ++dZ) {
                    mut.set(pos);
                    mut.move(dX, 0, dZ);

                    if (mut.isWithinDistance(pos, radius) && world.getBlockState(mut).isAir()) {
                        this.setBlockState(world, mut, config.getBlockState());
                    }
                }
            }
        }

        return true;
    }
}
