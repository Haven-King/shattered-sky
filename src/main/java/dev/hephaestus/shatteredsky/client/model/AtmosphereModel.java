package dev.hephaestus.shatteredsky.client.model;

import com.mojang.datafixers.util.Pair;
import dev.hephaestus.shatteredsky.ShatteredSky;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtmosphereModel extends Model {
	private static final SpriteIdentifier SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, ShatteredSky.id("block/atmosphere"));;
	private static final Collection<SpriteIdentifier> SPRITE_COLLECTION = Collections.singleton(SPRITE);

	public AtmosphereModel() {
		this.sprites = new Sprite[1];
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return SPRITE_COLLECTION;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
		if (blockPos.getY() == 10) {
			renderContext.meshConsumer().accept(mesh);
		} else if (blockPos.getY() == 70) {
			RenderContext.QuadTransform transform = mv -> {
				Vector3f tmp = new Vector3f();

				for (int i = 0; i < 4; ++i) {
					mv.copyPos(i, tmp);
					tmp.add(0, 13/16F, 0);
					mv.pos(i, tmp);

					if (mv.hasNormal(i)) {
						mv.copyNormal(i, tmp);
						tmp.add(0, 0.5F, 0);
						mv.normal(i, tmp);

						mv.colorIndex(0);
					}
				}

				return true;
			};

			renderContext.pushTransform(transform);
			renderContext.meshConsumer().accept(mesh);
			renderContext.popTransform();
		}
	}

	@Override
	public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		sprites[0] = textureGetter.apply(SPRITE);

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		int color = -1;

		emitter.pos(0, 0, 0, 0).spriteColor(0, 0, color).sprite(0, 0, 0, 0);
		emitter.pos(1, 0, 0, 1).spriteColor(1, 0, color).sprite(1, 0, 0, 0);
		emitter.pos(2, 1, 0, 1).spriteColor(2, 0, color).sprite(2, 0, 0, 0);
		emitter.pos(3, 1, 0, 0).spriteColor(3, 0, color).sprite(3, 0, 0, 0);

		emitter.spriteBake(0, sprites[0], 0);
		emitter.emit();

		emitter.pos(3, 0, 0, 0).spriteColor(3, 0, color).sprite(3, 0, 0, 0);
		emitter.pos(2, 0, 0, 1).spriteColor(2, 0, color).sprite(2, 0, 0, 0);
		emitter.pos(1, 1, 0, 1).spriteColor(1, 0, color).sprite(1, 0, 0, 0);
		emitter.pos(0, 1, 0, 0).spriteColor(0, 0, color).sprite(0, 0, 0, 0);

		emitter.spriteBake(0, sprites[0], 0);
		emitter.emit();

		mesh = builder.build();

		return this;
	}
}
