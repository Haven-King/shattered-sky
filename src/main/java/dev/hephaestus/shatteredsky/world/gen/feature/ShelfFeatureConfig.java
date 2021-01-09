package dev.hephaestus.shatteredsky.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hephaestus.shatteredsky.util.Range;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ShelfFeatureConfig implements FeatureConfig {
    public static final Codec<ShelfFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                BlockState.CODEC.fieldOf("block_state").forGetter(ShelfFeatureConfig::getBlockState),
                Range.CODEC.fieldOf("radius").forGetter(ShelfFeatureConfig::getRadius)
        ).apply(instance, ShelfFeatureConfig::new)
    );

    private final BlockState blockState;
    private final Range radius;

    public ShelfFeatureConfig(BlockState blockState, Range radius) {
        this.blockState = blockState;
        this.radius = radius;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public Range getRadius() {
        return radius;
    }
}
