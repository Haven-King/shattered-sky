package dev.hephaestus.shatteredsky.client.model.unbaked.bottom;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import dev.hephaestus.shatteredsky.client.model.FancyHedronModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;

public class BottomTallHedronPoint extends FancyHedronModel {
    public BottomTallHedronPoint(SpriteIdentifier texture) {
        super(texture);
    }

    public BottomTallHedronPoint(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        super(face, inner, base);
    }

    @Override
    protected void build(DecoratedEmitter emitter) {
        final Sprite face = this.getSprite(this.face);
        final Sprite base = this.getSprite(this.base);

        emitter.quad(base, MutableQuadView.BAKE_LOCK_UV | MutableQuadView.BAKE_ROTATE_NONE,
                0, 1, 0, 0, 0,
                0, 1, 1, 0, 1,
                1, 1, 1, 1, 1,
                1, 1, 0, 1, 0
        );

        emitter.triangle(face,
                0.5F, 0, 0.5F, 0.5F, 1,
                0, 1, 0, 1, 0,
                1, 1, 0, 0, 0
        );

        emitter.triangle(face,
                0.5F, 0, 0.5F, 0.5F, 1,
                0, 1, 1, 1, 0,
                0, 1, 0, 0, 0
        );

        emitter.triangle(face,
                0.5F, 0, 0.5F, 0.5F, 1,
                1, 1, 1, 1, 0,
                0, 1, 1, 0, 0
        );

        emitter.triangle(face,
                0.5F, 0, 0.5F, 0.5F, 1,
                1, 1, 0, 1, 0,
                1, 1, 1, 0, 0
        );
    }
}
