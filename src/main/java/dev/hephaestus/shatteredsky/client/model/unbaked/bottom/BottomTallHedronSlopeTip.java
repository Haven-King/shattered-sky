package dev.hephaestus.shatteredsky.client.model.unbaked.bottom;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import dev.hephaestus.shatteredsky.client.model.FancyHedronModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;

public class BottomTallHedronSlopeTip extends FancyHedronModel {
    public BottomTallHedronSlopeTip(SpriteIdentifier texture) {
        super(texture);
    }

    public BottomTallHedronSlopeTip(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        super(face, inner, base);
    }

    @Override
    protected void build(DecoratedEmitter emitter) {
        final Sprite face = this.getSprite(this.face);
        final Sprite base = this.getSprite(this.base);
        final Sprite inner = this.getSprite(this.inner);

        emitter.quad(base, MutableQuadView.BAKE_LOCK_UV | MutableQuadView.BAKE_ROTATE_NONE,
                0, 1, 0, 0, 0,
                0, 1, 1, 0, 1,
                1, 1, 1, 1, 1,
                1, 1, 0, 1, 0
        );

        emitter.quad(base, MutableQuadView.BAKE_LOCK_UV | MutableQuadView.BAKE_ROTATE_NONE,
                0, 0, 0, 0, 1,
                1, 0, 0, 1, 1,
                1, 0, 0.5F, 1, 0.5F,
                0, 0, 0.5F, 0, 0.5F
        );

        emitter.quad(face,
                0, 0, 0.5F, 0, 1,
                1, 0, 0.5F, 1, 1,
                1, 1, 1, 1, 0,
                0, 1, 1, 0, 0
        );

        emitter.quad(inner,
                1, 0, 0, 1, 1,
                1, 1, 0, 1, 0,
                1, 1, 1, 0, 0,
                1, 0, 0.5F, 0.5F, 1
        );

        emitter.quad(inner,
                0, 0, 0, 0, 1,
                0, 0, 0.5F, 0.5F, 1,
                0, 1, 1, 1, 0,
                0, 1, 0, 0, 0
        );

        emitter.quad(inner,
                0, 0, 0, 1, 1,
                0, 1, 0, 1, 0,
                1, 1, 0, 0, 0,
                1, 0, 0, 0, 1
        );
    }
}
