package dev.hephaestus.shatteredsky.client.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public abstract class FancyUnbakedModel implements UnbakedModel {
    private final Map<SpriteIdentifier, Sprite> sprites = new HashMap<>();
    private final Collection<SpriteIdentifier> textureDependencies;
    private final SpriteIdentifier[] spriteIdentifiers;

    protected FancyUnbakedModel(SpriteIdentifier[] spriteIdentifiers) {
        this.spriteIdentifiers = spriteIdentifiers;
        this.textureDependencies = Arrays.asList(spriteIdentifiers);
    }

    protected abstract SpriteIdentifier getSprite();
    protected abstract void build(DecoratedEmitter emitter);

    private SpriteIdentifier[] getSpriteIdentifiers() {
        return this.spriteIdentifiers;
    }

    protected final Sprite getSprite(SpriteIdentifier id) {
        return sprites.get(id);
    }

    @Override
    public final Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return this.textureDependencies;
    }

    @Override
    public final @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();

        if (renderer != null) {
            MeshBuilder builder = renderer.meshBuilder();
            DecoratedEmitter emitter = new DecoratedEmitter(builder.getEmitter(), rotationContainer);

            this.sprites.clear();

            for (SpriteIdentifier id : this.getSpriteIdentifiers()) {
                this.sprites.putIfAbsent(id, textureGetter.apply(id));
            }

            this.build(emitter);

            return new BakedMesh(builder.build(), textureGetter.apply(this.getSprite()));
        }

        return null;
    }
}