package dev.hephaestus.shatteredsky.block;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class HedronType {
    public final Identifier id;
    public final SpriteIdentifier face, inner, base;

    public HedronType(Identifier id, SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        this.id = id;
        this.face = face;
        this.inner = inner;
        this.base = base;
    }
}
