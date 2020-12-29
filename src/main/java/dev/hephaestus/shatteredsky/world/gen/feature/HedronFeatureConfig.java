package dev.hephaestus.shatteredsky.world.gen.feature;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.FeatureConfig;

public class HedronFeatureConfig implements FeatureConfig {
    public static final Codec<HedronFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        // A group is just a group of fields, represented by `{ ... }` in json.
        // Each field is taken as a separate argument, and a group can have any number of fields.
        return instance.group(
                // Each of these fields has a name, that's all `fieldOf` is.
                PrimitiveCodec.STRING.fieldOf("hedron_block")
                        // xmap takes the result value of the field and lets you transform it in some way.
                        // Here, we take in the string, turn it into an identifier, and return the corresponding block.
                        .xmap(string -> Registry.BLOCK.get(new Identifier(string)),
                              block -> Registry.BLOCK.getId(block).toString()
                        ).forGetter(HedronFeatureConfig::getHedronBlock),
                Range.CODEC.fieldOf("size").forGetter(HedronFeatureConfig::getSize),
                Range.CODEC.fieldOf("y").forGetter(HedronFeatureConfig::getY)
        // Finally, apply takes in our `RecordCodecBuilder` and the constructor for our object.
        // The constructor's parameters must match the types of the fields exactly.
        ).apply(instance, HedronFeatureConfig::new);
    });

    private final Block hedronBlock;
    private final Range size;
    private final Range y;

    public HedronFeatureConfig(Block hedronBlock, Range size, Range y) {
        this.hedronBlock = hedronBlock;
        this.size = size;
        this.y = y;
    }

    public Block getHedronBlock() {
        return this.hedronBlock;
    }

    private Range getSize() {
        return this.size;
    }

    public int getMinSize() {
        return this.size.min;
    }

    public int getMaxSize() {
        return this.size.max;
    }

    private Range getY() {
        return this.y;
    }

    public int getMinY() {
        return this.y.min;
    }

    public int getMaxY() {
        return this.y.max;
    }

    public static class Range {
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
}
