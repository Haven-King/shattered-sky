package dev.hephaestus.shatteredsky.client.model;

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BakedMesh implements BakedModel, FabricBakedModel {
    private final Mesh mesh;
    private final Sprite sprite;

    protected boolean useAmbientOcclusion = false;
    protected boolean hasDepth = false;
    protected boolean isSideLit = false;

    public BakedMesh(Mesh mesh, Sprite sprite) {
        this.mesh = mesh;
        this.sprite = sprite;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.useAmbientOcclusion;
    }

    @Override
    public boolean hasDepth() {
        return this.hasDepth;
    }

    @Override
    public boolean isSideLit() {
        return this.isSideLit;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return this.sprite;
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
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext context) {
        if (mesh != null) {
            context.meshConsumer().accept(mesh);
        }
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext context) {
        if (mesh != null) {
            context.meshConsumer().accept(mesh);
        }
    }
}
