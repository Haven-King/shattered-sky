package dev.hephaestus.shatteredsky.client.model.unbaked.bottom;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import dev.hephaestus.shatteredsky.client.model.FancyHedronModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.math.Direction;

public class BottomHedronSlope extends FancyHedronModel {
    public BottomHedronSlope(SpriteIdentifier texture) {
        super(texture);
    }

    public BottomHedronSlope(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        super(face, inner, base);
    }

    @Override
    protected void build(DecoratedEmitter emitter) {
        final Sprite face = this.getSprite(this.face);
        final Sprite base = this.getSprite(this.base);
        final Sprite inner = this.getSprite(this.inner);

        emitter.setNominalFace(Direction.UP);
        emitter.quad(base, MutableQuadView.BAKE_LOCK_UV | MutableQuadView.BAKE_ROTATE_NONE,
                0, 1, 0, 0, 0,
                0, 1, 1, 0, 1,
                1, 1, 1, 1, 1,
                1, 1, 0, 1, 0
        );

        emitter.setNominalFace(Direction.SOUTH);
        emitter.quad(face,
                1, 1, 1, 1, 0,
                0, 1, 1, 0, 0,
                0, -0.0001F, 0, 0, 1,
                1, -0.0001F, 0, 1, 1
        );

        emitter.setNominalFace(Direction.WEST);
        emitter.triangle(inner,
                0, -0.0001F, 0, 0, 1,
                0, 1, 1, 1, 0,
                0, 1, 0, 0, 0
        );

        emitter.setNominalFace(Direction.EAST);
        emitter.triangle(inner,
                1, -0.0001F, 0, 1, 1,
                1, 1, 0, 1, 0,
                1, 1, 1, 0, 0
        );

        emitter.setNominalFace(Direction.NORTH);
        emitter.quad(inner,
                0, -0.0001F, 0, 1, 1,
                0, 1, 0, 1, 0,
                1, 1, 0, 0, 0,
                1, -0.0001F, 0, 0, 1
        );
    }
}
