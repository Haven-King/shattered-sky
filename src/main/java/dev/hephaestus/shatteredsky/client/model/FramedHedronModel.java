package dev.hephaestus.shatteredsky.client.model;

import dev.hephaestus.shatteredsky.client.model.mesh.FramedHedronMeshBuilder;
import dev.hephaestus.shatteredsky.client.model.mesh.HedronMeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FramedHedronModel extends HedronModel {
    private final SpriteIdentifier frameId;

    private Sprite frame;

    public FramedHedronModel(SpriteIdentifier texture, SpriteIdentifier frame) {
        this(texture, texture, texture, frame);
    }

    public FramedHedronModel(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base, SpriteIdentifier frame) {
        super(face, inner, base);

        spriteCollection.add(frame);

        frameId = frame;
    }

    @Override
    protected HedronMeshBuilder getMeshBuilder() {
        return new FramedHedronMeshBuilder(face, inner, base, frame);
    }

    @Override
    public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();

        if (renderer != null) {
            frame = textureGetter.apply(frameId);

            super.bake(loader, textureGetter, rotationContainer, modelId);
        }

        return this;
    }

}
