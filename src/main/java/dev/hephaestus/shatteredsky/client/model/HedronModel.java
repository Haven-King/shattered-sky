package dev.hephaestus.shatteredsky.client.model;

import com.mojang.datafixers.util.Pair;
import dev.hephaestus.shatteredsky.block.HedronBlock;
import dev.hephaestus.shatteredsky.client.model.mesh.HedronMeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class HedronModel implements UnbakedModel, BakedModel, FabricBakedModel {
	protected final Collection<SpriteIdentifier> spriteCollection;

	protected final SpriteIdentifier faceId;
	protected final SpriteIdentifier innerId;
	protected final SpriteIdentifier baseId;

	protected Sprite face;
	protected Sprite inner;
	protected Sprite base;

	private final Map<HedronBlock.Shape, Mesh> topMeshes = new HashMap<>();
	private final Map<HedronBlock.Shape, Mesh> bottomMeshes = new HashMap<>();

	public HedronModel(SpriteIdentifier texture) {
		this(texture, texture, texture);
	}

	public HedronModel(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
		this.spriteCollection = new ArrayList<>();

		spriteCollection.add(face);
		spriteCollection.add(inner);
		spriteCollection.add(base);

		faceId = face;
		innerId = inner;
		baseId = base;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return spriteCollection;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		List<BakedQuad> quads = new ArrayList<>();

		Mesh mesh = topMeshes.get(state == null ? HedronBlock.Shape.POINT : state.get(HedronBlock.SHAPE));

		mesh.forEach(quadView -> quads.add(quadView.toBakedQuad(0, this.face, false)));

		return quads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
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
		return face;
	}

	@Override
	public ModelTransformation getTransformation() {
		return null;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return null;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {

		if (blockState.get(Properties.BOTTOM)) {
			renderContext.meshConsumer().accept(topMeshes.get(blockState.get(HedronBlock.SHAPE)));
		} else {
			renderContext.meshConsumer().accept(bottomMeshes.get(blockState.get(HedronBlock.SHAPE)));
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(topMeshes.get(HedronBlock.Shape.POINT));
	}

	protected HedronMeshBuilder getMeshBuilder() {
		return new HedronMeshBuilder(face, inner, base);
	}

	@Override
	public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();

		if (renderer != null) {
			MeshBuilder builder = renderer.meshBuilder();
			DecoratedEmitter emitter = new DecoratedEmitter(builder.getEmitter(), ModelRotation.X0_Y0);

			face = textureGetter.apply(faceId);
			inner = textureGetter.apply(innerId);
			base = textureGetter.apply(baseId);

			HedronMeshBuilder hedronMeshBuilder = this.getMeshBuilder();
			HedronMeshBuilder.Inner topMeshBuilder = hedronMeshBuilder.createTop();
			HedronMeshBuilder.Inner bottomMeshBuilder = hedronMeshBuilder.createBottom();

			for (HedronBlock.Shape shape : HedronBlock.Shape.values()) {
				topMeshes.put(shape, topMeshBuilder.getMesh(builder, emitter, shape));
				bottomMeshes.put(shape, bottomMeshBuilder.getMesh(builder, emitter, shape));
			}
		}

		return this;
	}
}
