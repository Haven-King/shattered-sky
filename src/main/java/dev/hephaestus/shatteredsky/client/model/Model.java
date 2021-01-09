package dev.hephaestus.shatteredsky.client.model;

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public abstract class Model implements UnbakedModel, BakedModel, FabricBakedModel {
	private final SpriteIdentifier spriteId;
	protected Mesh mesh = null;
	protected boolean ambientOcclusion = false;
	protected boolean depth;
	protected Sprite sprite;

	protected Model(SpriteIdentifier sprite) {
		this.spriteId = sprite;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return null;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.ambientOcclusion;
	}

	@Override
	public boolean hasDepth() {
		return this.depth;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Override
	public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		this.sprite = textureGetter.apply(this.spriteId);

		return this;
	}
}