package dev.hephaestus.shatteredsky.client.model.unbaked.top;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import dev.hephaestus.shatteredsky.client.model.FancyHedronModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;

public class TopHedronSlope extends FancyHedronModel {
    public TopHedronSlope(SpriteIdentifier texture) {
        super(texture);
    }

    public TopHedronSlope(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        super(face, inner, base);
    }

    @Override
    protected void build(DecoratedEmitter emitter) {
        final Sprite face = this.getSprite(this.face);
        final Sprite base = this.getSprite(this.base);
        final Sprite inner = this.getSprite(this.inner);

        emitter.quad(base, MutableQuadView.BAKE_LOCK_UV | MutableQuadView.BAKE_ROTATE_NONE,
                0, 0, 1, 0, 0,
                0, 0, 0, 0, 1,
                1, 0, 0, 1, 1,
                1, 0, 1, 1, 0
        );

        emitter.quad(face,
                0, 1.0001F, 0, 0, 0,
                0, 0, 1, 0, 1,
                1, 0, 1, 1, 1,
                1, 1.0001F, 0, 1, 0
        );

        emitter.quad(inner,
                0, 0, 0, 1, 1,
                0, 1.0001F, 0, 1, 0,
                1, 1.0001F, 0, 0, 0,
                1, 0, 0, 0, 1
        );

        emitter.triangle(inner,
                0, 0, 0, 0, 1,
                0, 0, 1, 1, 1,
                0, 1.0001F, 0, 0, 0
        );

        emitter.triangle(inner,
                1, 0, 0, 1, 1,
                1, 1.0001F, 0, 1, 0,
                1, 0, 1, 0, 1
        );
    }
}
