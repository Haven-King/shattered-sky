package dev.hephaestus.shatteredsky;

import dev.hephaestus.shatteredsky.block.*;
import dev.hephaestus.shatteredsky.client.model.ModelProvider;
import dev.hephaestus.shatteredsky.client.render.ShatteredSkyProperties;
import dev.hephaestus.shatteredsky.data.SkyFalls;
import dev.hephaestus.shatteredsky.fluid.AtmosphereFluid;
import dev.hephaestus.shatteredsky.fluid.FluidBlock;
import dev.hephaestus.shatteredsky.mixin.client.render.SkyPropertiesAccessor;
import dev.hephaestus.shatteredsky.util.ChunkDataDefinition;
import dev.hephaestus.shatteredsky.util.ChunkDataRegistry;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import dev.hephaestus.shatteredsky.util.SyncedChunkData;
import dev.hephaestus.shatteredsky.world.gen.chunk.NoiseChunkGenerator;
import dev.hephaestus.shatteredsky.world.gen.feature.DummyBlockConsumer;
import dev.hephaestus.shatteredsky.world.gen.feature.HedronFeature;
import dev.hephaestus.shatteredsky.world.gen.feature.RandomFacingOreFeature;
import dev.hephaestus.shatteredsky.world.gen.feature.ShelfFeature;
import dev.hephaestus.shatteredsky.world.gen.surfacebuilder.PoolsSurfaceBuilder;
import dev.hephaestus.shatteredsky.world.gen.surfacebuilder.SkySurfaceBuilder;
import grondag.frex.api.event.RenderRegionBakeListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.level.ColorResolver;

public class ShatteredSky implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "shatteredsky";

	public static final RegistryKey<DimensionType> DIMENSION_TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id("dimension_type"));
	public static final RegistryKey<net.minecraft.world.World> WORLD = RegistryKey.of(Registry.DIMENSION, id("dimension"));
	public static final Identifier SKY_FALLS_ID = id("chunk_data", "sky_falls");
	public static final ChunkDataDefinition<SkyFalls> SKY_FALLS = ChunkDataRegistry.register(SKY_FALLS_ID, SkyFalls::new);
	@Override
	public void onInitialize() {
		Blocks.init();
		Items.init();
		Fluids.init();
		World.init();
		Networking.init();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> new ModelProvider());
		SkyPropertiesAccessor.getIdMap().put(id("sky"), new ShatteredSkyProperties());
		Blocks.initClient();
		Fluids.initClient();
		Networking.initClient();
		World.initClient();
	}

	public static Identifier id(String... path) {
		return new Identifier(MOD_ID, String.join(".", path));
	}

	public static class Materials {
		public static final Material ATMOSPHERE = new FabricMaterialBuilder(MaterialColor.LIGHT_BLUE).lightPassesThrough().allowsMovement().lightPassesThrough().notSolid().replaceable().liquid().build();
	}

	public static class Blocks {
		public static final FluidBlock ATMOSPHERE = new FluidBlock(Fluids.ATMOSPHERE, FabricBlockSettings.of(Materials.ATMOSPHERE).nonOpaque().solidBlock((a, b, c) -> false).suffocates((a, b, c) -> false).blockVision((a, b, c) -> false).noCollision().strength(100.0F).dropsNothing());
		public static final Block ATMOSPHERE_BLOCK = new AtmosphereBlock(FabricBlockSettings.of(Materials.ATMOSPHERE).nonOpaque().solidBlock((a, b, c) -> false).suffocates((a, b, c) -> false).blockVision((a, b, c) -> false).noCollision().strength(100.0F).dropsNothing());
		public static final Block STONE_HEDRON = new HedronBlock(net.minecraft.block.Blocks.STONE);
		public static final Block SKYMETAL_BLOCK = new Block(FabricBlockSettings.of(Material.METAL));
		public static final Block SKYMETAL_HEDRON = new HedronBlock(FabricBlockSettings.of(Material.METAL).luminance(15).emissiveLighting((state, world, pos) -> true));
		public static final Block CRYSTAL_ORE = new Block(FabricBlockSettings.of(Material.STONE));
		public static final Block SKY_DIRT = new Block(FabricBlockSettings.of(Material.SOIL));
		public static final Block SKY_STONE = new SkyStoneBlock(FabricBlockSettings.of(Material.STONE));
		public static final Block WORLDGEN_DUMMY = new WorldgenDummyBlock(FabricBlockSettings.of(Material.STONE));
		public static final Block MUSHROOM_BLOCK = new Block(FabricBlockSettings.of(Material.PLANT));

        static {
			register("atmosphere", ATMOSPHERE_BLOCK);
			register("stone_hedron", STONE_HEDRON);
			register("skymetal_hedron", SKYMETAL_HEDRON);
			register("skymetal_block", SKYMETAL_BLOCK);
			register("crystal_ore", CRYSTAL_ORE);
			register("sky_dirt", SKY_DIRT);
			register("sky_stone", SKY_STONE);
			register("worldgen_dummy", WORLDGEN_DUMMY);
			register("mushroom_block", MUSHROOM_BLOCK);

			HedronTypes.register(id("stone_hedron"),
					new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone"))
			);

			HedronTypes.register(id("skymetal_hedron"),
					new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_active_silver")),
					new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block")),
					new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone_bricks"))
			);
		}

		private static <T extends Block> T register(final String id, final T block) {
			return Registry.register(Registry.BLOCK, id(id), block);
		}

		public static void init() {
		}

		@Environment(EnvType.CLIENT)
		public static void initClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(ATMOSPHERE_BLOCK, RenderLayer.getTranslucent());
			ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
			{
				int color = ((BlockColorProvider) state.getBlock()).getColor(state, world, pos, tintIndex);

				if (pos != null /* && Config.atmosphereSmoothingRadius > 0 */) {
					int radius = 10; // TODO Smoothing config.
					int r = 0, g = 0, b = 0;

					for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, 0, -radius), pos.add(radius, 0, radius))) {
						int here = ((BlockColorProvider) state.getBlock()).getColor(state, world, blockPos, tintIndex);
						r += (here >> 16) & 0xFF;
						g += (here >> 8) & 0xFF;
						b += here & 0xFF;
					}

					int total = (radius * 2 + 1) * (radius * 2 + 1);
					r /= total;
					g /= total;
					b /= total;

					color = (color & 0xFF000000)
							| r << 16
							| g << 8
							| b;
				}

				return color;
			}, ATMOSPHERE_BLOCK);
		}
	}

	public static class Items {
		public static final BlockItem STONE_HEDRON = new BlockItem(Blocks.STONE_HEDRON, new Item.Settings());
		public static final BlockItem SKYMETAL_HEDRON = new BlockItem(Blocks.SKYMETAL_HEDRON, new Item.Settings());
		public static final BlockItem SKYMETAL_BLOCK = new BlockItem(Blocks.SKYMETAL_BLOCK, new Item.Settings());
		public static final BlockItem CRYSTAL_ORE = new BlockItem(Blocks.CRYSTAL_ORE, new Item.Settings());
		public static final BlockItem SKY_DIRT = new BlockItem(Blocks.SKY_DIRT, new Item.Settings());
		public static final BlockItem SKY_STONE = new BlockItem(Blocks.SKY_STONE, new Item.Settings());
		public static final BlockItem MUSHROOM_BLOCK = new BlockItem(Blocks.MUSHROOM_BLOCK, new Item.Settings());

		static {
			register("stone_hedron", STONE_HEDRON);
			register("skymetal_hedron", SKYMETAL_HEDRON);
			register("skymetal_block", SKYMETAL_BLOCK);
			register("crystal_ore", CRYSTAL_ORE);
			register("sky_dirt", SKY_DIRT);
			register("sky_stone", SKY_STONE);
			register("mushroom_block", MUSHROOM_BLOCK);
		}

		public static void init() {

		}

		private static <T extends Item> T register(final String id, final T item) {
			return Registry.register(Registry.ITEM, id(id), item);
		}
	}

	public static class Fluids {
		public static final FlowableFluid ATMOSPHERE = new AtmosphereFluid();
		public static final SpriteIdentifier ATMOSPHERE_STILL = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/atmosphere"));
		public static final SpriteIdentifier ATMOSPHERE_FLOWING = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/atmosphere_flowing"));

		static {
			register("atmosphere", ATMOSPHERE);
		}

		public static void init() {
		}

		@Environment(EnvType.CLIENT)
		public static void initClient() {
//			ModelLoaderAccessor.getDefaultTextures().add(ATMOSPHERE_STILL);
//			ModelLoaderAccessor.getDefaultTextures().add(ATMOSPHERE_FLOWING);
//			FluidRenderHandlerRegistry.INSTANCE.register(ATMOSPHERE, new AtmosphereFluid.Renderer());
//			RenderLayersAccessor.getFluids().put(ATMOSPHERE, RenderLayer.getTranslucent());
		}

		private static <T extends Fluid> T register(final String id, final T fluid) {
			return Registry.register(Registry.FLUID, id(id), fluid);
		}
	}

	public static class World {
		@Environment(EnvType.CLIENT)
		public static final ColorResolver SKY_COLOR = (biome, x, z) -> biome.getSkyColor();

		public static void init() {
			Registry.register(Registry.CHUNK_GENERATOR, id("islands"), NoiseChunkGenerator.CODEC);

			Registry.register(Registry.SURFACE_BUILDER, id("default"), new SkySurfaceBuilder());
			Registry.register(Registry.SURFACE_BUILDER, id("pools"), new PoolsSurfaceBuilder());

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
			return Registry.register(Registry.FEATURE, id(id), feature);
		}
	}

	public static class RenderLayers extends RenderLayer {
		public static RenderLayer TRANSLUCENT = of("shatterdsky:translucent", VertexFormats.POSITION_COLOR_LIGHT, 7, 2097152, true, true, RenderLayer.MultiPhaseParameters.builder().shadeModel(SMOOTH_SHADE_MODEL).lightmap(ENABLE_LIGHTMAP).transparency(TRANSLUCENT_TRANSPARENCY).target(CLOUDS_TARGET).build(true));

		private RenderLayers(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}
	}

	public static class Networking {
		public static final Identifier SYNC_CHUNK_DATA = id("chunk_data", "sync");

		public static void init() {

		}

		@Environment(EnvType.CLIENT)
		public static void initClient() {
			ClientSidePacketRegistry.INSTANCE.register(SYNC_CHUNK_DATA, SyncedChunkData::receive);
		}
	}
}
