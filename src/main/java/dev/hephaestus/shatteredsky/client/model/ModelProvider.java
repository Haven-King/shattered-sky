package dev.hephaestus.shatteredsky.client.model;

import dev.hephaestus.shatteredsky.block.HedronType;
import dev.hephaestus.shatteredsky.block.HedronTypes;
import dev.hephaestus.shatteredsky.client.model.unbaked.bottom.*;
import dev.hephaestus.shatteredsky.client.model.unbaked.top.*;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static dev.hephaestus.shatteredsky.ShatteredSky.id;

public class ModelProvider implements ModelResourceProvider {
	private final HashMap<Identifier, UnbakedModel> models = new HashMap<>();

	public ModelProvider() {
		register(id("block/atmosphere"), new AtmosphereModel());
//		register("block/stone_hedron", new HedronModel(
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone"))
//		));
//
//		register("block/skymetal_hedron", new FramedHedronModel(
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_inactive")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block"))
//		));
//
//		register("block/skymetal_hedron_corner", new HedronCorner(
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_active_silver")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone_bricks"))
//		));
//
//		register("block/skymetal_hedron_slope", new HedronSlope(
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_active_silver")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/hedron_metal_block")),
//				new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone_bricks"))
//		));

		HedronTypes.getTypes().forEachRemaining(this::register);
	}

	@Override
	public @Nullable UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
		return this.models.get(identifier);
	}

	public void register(Identifier id, UnbakedModel model) {
		this.models.put(id, model);
	}

	private void register(HedronType hedronType) {
		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_top"),
				new TopHedronCorner(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_top"),
				new TopHedronSlope(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_point_top"),
				new TopHedronPoint(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_tip_top"),
				new TopTallHedronCornerTip(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_base_top"),
				new TopTallHedronCornerBase(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_tip_top"),
				new TopTallHedronSlopeTip(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_base_top"),
				new TopTallHedronSlopeBase(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_point_tall_top"),
				new TopTallHedronPoint(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_bottom"),
				new BottomHedronCorner(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_bottom"),
				new BottomHedronSlope(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_point_bottom"),
				new BottomHedronPoint(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_tip_bottom"),
				new BottomTallHedronCornerTip(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_corner_base_bottom"),
				new BottomTallHedronCornerBase(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_tip_bottom"),
				new BottomTallHedronSlopeTip(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_slope_base_bottom"),
				new BottomTallHedronSlopeBase(hedronType.face, hedronType.inner, hedronType.base
		));

		register(new Identifier(hedronType.id.getNamespace(), "block/" + hedronType.id.getPath() + "_point_tall_bottom"),
				new BottomTallHedronPoint(hedronType.face, hedronType.inner, hedronType.base
		));
	}
}
