package dev.hephaestus.shatteredsky;

import dev.hephaestus.shatteredsky.util.RegistryUtil;
import dev.hephaestus.shatteredsky.world.gen.chunk.NoiseChunkGenerator;
import dev.hephaestus.shatteredsky.world.gen.feature.DummyBlockConsumer;
import dev.hephaestus.shatteredsky.world.gen.feature.HedronFeature;
import dev.hephaestus.shatteredsky.world.gen.feature.RandomFacingOreFeature;
import dev.hephaestus.shatteredsky.world.gen.feature.ShelfFeature;
import dev.hephaestus.shatteredsky.world.gen.surfacebuilder.PoolsSurfaceBuilder;
import dev.hephaestus.shatteredsky.world.gen.surfacebuilder.SkySurfaceBuilder;
import grondag.frex.api.event.RenderRegionBakeListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.level.ColorResolver;

public class WorldGen {
    @Environment(EnvType.CLIENT)
    public static final ColorResolver SKY_COLOR = (biome, x, z) -> biome.getSkyColor();

    public static void init() {
        Registry.register(Registry.CHUNK_GENERATOR, ShatteredSky.id("islands"), NoiseChunkGenerator.CODEC);

        Registry.register(Registry.SURFACE_BUILDER, ShatteredSky.id("default"), new SkySurfaceBuilder());
        Registry.register(Registry.SURFACE_BUILDER, ShatteredSky.id("pools"), new PoolsSurfaceBuilder());

        register("solid_hedron", new HedronFeature());
        register("capped_hedron", new HedronFeature());
        register("facing_ore", new RandomFacingOreFeature(OreFeatureConfig.CODEC));
        register("shelf", new ShelfFeature());
        register("dummy", new DummyBlockConsumer());
    }

    public static void initClient() {
        RenderRegionBakeListener.register(context -> {
            int min = Math.min(context.origin().getY(), context.origin().getY() + context.ySize() - 1);
            int max = Math.max(context.origin().getY(), context.origin().getY() + context.ySize() - 1);
            return ((min <= 10 && max >= 10) || (min <= 70) && max >= 70)
                    && RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE);
        }, (context, renderer) -> {
            int min = Math.min(context.origin().getY(), context.origin().getY() + context.ySize() - 1);
            int max = Math.max(context.origin().getY(), context.origin().getY() + context.ySize() - 1);

            // Without the `if` statement above, I will occasionally crash with an
            // `ArrayIndexOutOfBoundsException`. This is presumably because when 10 is not between the min and
            // max, we use a y value of 70. Theoretically, if this bake method only fires when the condition has
            // been satisfied, this isn't a problem, but clearly that's not the case.
            BlockPos pos = new BlockPos(
                    context.origin().getX(),
                    (min <= 10 && max >= 10) ? 10 : 70,
                    context.origin().getZ());

            BlockState state = ShatteredSky.Blocks.ATMOSPHERE_BLOCK.getDefaultState();

            // This isn't a huge deal, but with iterate being fully inclusive, this `size - 1` thing is a bit of
            // a pain.
            BlockPos.iterate(pos, pos.add(context.xSize() - 1, 0, context.zSize() - 1)).forEach(blockPos -> {
                renderer.bake(blockPos, state);
            });
        });
    }

    private static <C extends FeatureConfig, F extends Feature<C>> F register(final String id, final F feature) {
        return Registry.register(Registry.FEATURE, ShatteredSky.id(id), feature);
    }
}
