package dev.hephaestus.shatteredsky.client.model;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;

public abstract class FancyHedronModel extends FancyUnbakedModel {
    private final SpriteIdentifier sprite;

    protected final SpriteIdentifier face, inner, base;

    public FancyHedronModel(SpriteIdentifier texture) {
        this(texture, texture, texture);
    }

    public FancyHedronModel(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        super(new SpriteIdentifier[]{face, inner, base});
        this.sprite = face;

        this.face = face;
        this.inner = inner;
        this.base = base;
    }

    @Override
    protected SpriteIdentifier getSprite() {
        return this.sprite;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }
}
