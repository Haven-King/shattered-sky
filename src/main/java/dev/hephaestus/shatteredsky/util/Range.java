package dev.hephaestus.shatteredsky.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hephaestus.shatteredsky.world.gen.feature.HedronFeatureConfig;

public class Range {
    public static final Codec<Range> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PrimitiveCodec.INT.fieldOf("min").forGetter(range -> range.min),
            PrimitiveCodec.INT.fieldOf("max").forGetter(range -> range.max)
    ).apply(instance, Range::new));

    public final int min;
    public final int max;

    private Range(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
