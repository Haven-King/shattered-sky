package dev.hephaestus.shatteredsky.client.model.unbaked.bottom;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import dev.hephaestus.shatteredsky.client.model.FancyHedronModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.math.Direction;

public class BottomHedronCorner extends FancyHedronModel {
    public BottomHedronCorner(SpriteIdentifier texture) {
        super(texture);
    }

    public BottomHedronCorner(SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
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

        emitter.setNominalFace(Direction.EAST);
        emitter.triangle(face,
                0, -0.0001F, 0, 0, 1,
                1, 1, 1, 1, 0,
                0, 1, 1, 0, 0
        );

        emitter.setNominalFace(Direction.SOUTH);
        emitter.triangle(face,
                0, -0.0001F, 0, 1, 1,
                1, 1, 0, 1, 0,
                1, 1, 1, 0, 0
        );

        emitter.setNominalFace(Direction.WEST);
        emitter.triangle(inner,
                0, -0.0001F, 0, 1, 1,
                0, 1, 0, 1, 0,
                1, 1, 0, 0, 0
        );

        emitter.setNominalFace(Direction.NORTH);
        emitter.triangle(inner,
                0, -0.0001F, 0, 0, 1,
                0, 1, 1, 1, 0,
                0, 1, 0, 0, 0
        );
    }
}