package dev.hephaestus.shatteredsky.world.gen.feature;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.block.WorldgenDummyBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class DummyBlockConsumer extends Feature<DefaultFeatureConfig> {
    public DummyBlockConsumer() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos origin, DefaultFeatureConfig config) {
        Collection<BlockPos> positions = new ArrayList<>();

        BlockPos.iterate(origin, origin.add(15, 255, 15)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            if (state.isOf(ShatteredSky.Blocks.WORLDGEN_DUMMY)) {
                positions.add(pos.toImmutable());
                this.setBlockState(world, pos, state.get(WorldgenDummyBlock.TYPE).getState());
            }
        });

        return !positions.isEmpty();
    }
}
