package dev.hephaestus.shatteredsky.client.model.mesh;

import dev.hephaestus.shatteredsky.block.HedronBlock;
import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.minecraft.client.texture.Sprite;

public class HedronMeshBuilder {
    private final Sprite face, inner, base;

    public HedronMeshBuilder(Sprite face, Sprite inner, Sprite base) {
        this.face = face;
        this.inner = inner;
        this.base = base;
    }

    public Inner createTop() {
        return new Top();
    }

    public Inner createBottom() {
        return new Bottom();
    }

    public abstract static class Inner implements HedronMeshBuilder.Short, HedronMeshBuilder.Tall {
        public Mesh getMesh(MeshBuilder builder, DecoratedEmitter emitter, HedronBlock.Shape shape) {
            switch (shape) {
                case NORTH:               return north(builder, emitter);
                case EAST:                return east(builder, emitter);
                case SOUTH:               return south(builder, emitter);
                case WEST:                return west(builder, emitter);
                case NORTH_EAST:          return northEast(builder, emitter);
                case SOUTH_EAST:          return southEast(builder, emitter);
                case SOUTH_WEST:          return southWest(builder, emitter);
                case NORTH_WEST:          return northWest(builder, emitter);
                case POINT:               return point(builder, emitter);
                case NORTH_TIP:            return northTip(builder, emitter);
                case EAST_TIP:             return eastTip(builder, emitter);
                case SOUTH_TIP:            return southTip(builder, emitter);
                case WEST_TIP:             return westTip(builder, emitter);
                case NORTH_EAST_TIP:       return northEastTip(builder, emitter);
                case SOUTH_EAST_TIP:       return southEastTip(builder, emitter);
                case SOUTH_WEST_TIP:       return southWestTip(builder, emitter);
                case NORTH_WEST_TIP:       return northWestTip(builder, emitter);
                case NORTH_BASE:         return northBase(builder, emitter);
                case EAST_BASE:          return eastBase(builder, emitter);
                case SOUTH_BASE:         return southBase(builder, emitter);
                case WEST_BASE:          return westBase(builder, emitter);
                case NORTH_EAST_BASE:    return northEastBase(builder, emitter);
                case SOUTH_EAST_BASE:    return southEastBase(builder, emitter);
                case SOUTH_WEST_BASE:    return southWestBase(builder, emitter);
                case NORTH_WEST_BASE:    return northWestBase(builder, emitter);
                case POINT_TALL:                return pointTall(builder, emitter);
            }

            throw new IllegalStateException();
        }
    }

    protected class Top extends Inner {
        protected void base(DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 1, 0, 0,
                    0, 0, 0, 0, 1,
                    1, 0, 0, 1, 1,
                    1, 0, 1, 1, 0
            );
        }

        @Override
        public Mesh point(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    0, 0, 0, 1, 1,
                    0.5F, 0.5F, 0.5F, 0.5F, 0.5F,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0.5F, 0.5F, 0.5F, 0.5F, 0.5F
            );

            emitter.triangle(face,
                    1, 0, 0, 1, 1,
                    0.5F, 0.5F, 0.5F, 0.5F, 0.5F,
                    1, 0, 1, 0, 1
            );

            emitter.triangle(face,
                    1, 0, 1, 1, 1,
                    0.5F, 0.5F, 0.5F, 0.5F, 0.5F,
                    0, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh north(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 0, 1, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh east(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    1, 1, 0, 0, 0
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    1, 1, 1, 1, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            return builder.build();
        }

        @Override
        public Mesh south(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 1, 1,
                    0, 1, 1, 1, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh west(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    1, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    0, 1, 1, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            return builder.build();
        }

        @Override
        public Mesh northEast(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(face,
                    1, 1, 0, 1, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southEast(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    1, 1, 1, 1, 0,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1
            );

            emitter.triangle(face,
                    0, 0, 0, 1, 1,
                    1, 1, 1, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southWest(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    0, 0, 0, 1, 1,
                    0, 1, 1, 1, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(face,
                    0, 1, 1, 0, 0,
                    1, 0, 1, 0, 1,
                    1, 0, 0, 1, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    0, 1, 1, 0, 1,
                    0, 0, 1, 0, 0,
                    1, 0, 1, 1, 1
            );

            return builder.build();
        }

        @Override
        public Mesh northWest(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            emitter.triangle(face,
                    0, 1, 0, 1, 0,
                    1, 0, 1, 0, 1,
                    1, 0, 0, 1, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 0, 0, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh pointTall(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.triangle(face,
                    0, 0, 0, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0
            );

            emitter.triangle(face,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0
            );

            emitter.triangle(face,
                    1, 0, 0, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh northTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 0, 0, 1,
                    1, 0, 0, 1, 1,
                    1, 0, 0.5F, 1, 0.5F,
                    0, 0, 0.5F, 0, 0.5F
            );

            emitter.quad(face,
                    0, 0, 0.5F, 0, 1,
                    1, 0, 0.5F, 1, 1,
                    1, 1, 0, 1, 0,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 0.5F, 0.5F, 1,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 0, 0.5F, 0.5F, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh eastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0.5F, 0, 0, 0.5F, 1,
                    1, 0, 0, 1, 1,
                    1, 0, 1, 1, 0,
                    0.5F, 0, 1, 0.5F, 0
            );

            emitter.quad(face,
                    0.5F, 0, 0, 0, 1,
                    0.5F, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0.5F, 0, 1, 0.5F, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    0.5F, 0, 0, 0.5F, 1,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 0.5F, 0, 0.5F,
                    1, 0, 0.5F, 1, 0.5F,
                    1, 0, 1, 1, 0,
                    0, 0, 1, 0, 0
            );

            emitter.quad(face,
                    0, 0, 0.5F, 1, 1,
                    0, 1, 1, 1, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 0.5F, 0, 1
            );

            emitter.triangle(inner,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1,
                    1, 0, 0.5F, 0.5F, 1
            );

            emitter.triangle(inner,
                    0, 0, 0.5F, 0.5F, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.quad(inner,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            return builder.build();
        }

        @Override
        public Mesh westTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 0, 0, 1,
                    0.5F, 0, 0, 0.5F, 1,
                    0.5F, 0, 1, 0.5F, 0,
                    0, 0, 1, 0, 0
            );

            emitter.quad(face,
                    0.5F, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0, 1, 1, 0, 0,
                    0.5F, 0, 1, 0, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0.5F, 0, 0, 0.5F, 1
            );

            emitter.triangle(inner,
                    0, 0, 1, 0, 1,
                    0.5F, 0, 1, 0.5F, 1,
                    0, 1, 1, 0, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0.5F, 0, 0, 0.5F, 1,
                    1, 0, 0, 1, 1,
                    1, 0, 0.5F, 1, 0.5F,
                    0.5F, 0, 0.5F, 0.5F, 0.5F
            );

            emitter.triangle(face,
                    0.5F, 0, 0, 0, 1,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(face,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    1F, 0, 0.5F, 1, 1,
                    1, 1, 0, 1, 0
            );

            emitter.triangle(inner,
                    0.5F, 0, 0, 0.5F, 1,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 0, 0.5F, 0.5F, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0.5F, 0, 0.5F,  0.5F, 0.5F,
                    1, 0, 0.5F, 1F, 0.5F,
                    1, 0, 1, 1, 0,
                    0.5F, 0, 1, 0.5F, 0
            );

            emitter.triangle(face,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    1, 1, 1, 0, 0,
                    1, 0, 0.5F, 0, 1
            );

            emitter.triangle(face,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    0.5F, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    1, 0, 0.5F, 0.5F, 1,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.triangle(inner,
                    0.5F, 0, 1, 0.5F, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            return builder.build();
        }

        @Override
        public Mesh southWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 0.5F, 0, 0.5F,
                    0.5F, 0, 0.5F, 0.5F, 0.5F,
                    0.5F, 0, 1, 0.5F, 0,
                    0, 0, 1, 0, 0
            );

            emitter.triangle(face,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    0, 0, 0.5F, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.triangle(face,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    0, 1, 1, 0, 0,
                    0.5F, 0, 1, 0, 1
            );

            emitter.triangle(inner,
                    0, 0, 1, 0, 1,
                    0.5F, 0, 1, 0.5F, 1,
                    0, 1, 1, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 0, 0.5F, 0.5F, 1
            );

            return builder.build();
        }

        @Override
        public Mesh northWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 0, 0, 0, 1,
                    0.5F, 0, 0, 0.5F, 1,
                    0.5F, 0, 0.5F, 0.5F, 0.5F,
                    0, 0, 0.5F, 0, 0.5F
            );

            emitter.triangle(face,
                    0, 1, 0, 0, 0,
                    0, 0, 0.5F, 0, 1,
                    0.5F, 0, 0.5F, 0.5F, 1
            );

            emitter.triangle(face,
                    0, 1, 0, 1, 0,
                    0.5F, 0, 0.5F, 0.5F, 1,
                    0.5F, 0, 0, 1, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0.5F, 0, 0, 0.5F, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 0.5F, 0.5F, 1,
                    0, 1, 0, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    1, 0, 1, 1, 1,
                    1, 1, 0.5F, 1, 0,
                    0, 1, 0.5F, 0, 0,
                    0, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 0.5F, 0.5F, 0,
                    0, 1, 0, 0, 0
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 0.5F, 0.5F, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 1, 0, 0, 0,
                    0, 1, 0.5F, 0, 0.5F,
                    1, 1, 0.5F, 1, 0.5F,
                    1, 1, 0, 1, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh eastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0.5F, 1, 1, 1, 0,
                    0.5F, 1, 0, 0, 0
            );

            emitter.quad(inner,
                    0.5F, 1, 1, 0.5F, 0,
                    0,  0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0.5F, 1, 0, 0.5F, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    0.5F, 1, 0, 0.5F, 0,
                    0.5F, 1, 1, 0.5F, 1,
                    1, 1, 1, 1, 1,
                    1, 1, 0, 1, 0
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 1, 1,
                    0, 1, 0.5F, 1, 0,
                    1, 1, 0.5F, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    0, 1, 0.5F, 0, 0.5F,
                    0, 1, 1, 0, 1,
                    1, 1, 1, 1, 1,
                    1, 1, 0.5F, 1, 0.5F
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0.5F, 0.5F, 0
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0.5F, 0.5F, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1
            );

            return builder.build();
        }

        @Override
        public Mesh westBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    1, 0, 0, 1, 1,
                    0.5F, 1, 0, 1, 0,
                    0.5F, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 1, 0, 0, 0,
                    0, 1, 1, 0, 1,
                    0.5F, 1, 1, 0.5F, 1,
                    0.5F, 1, 0, 0.5F, 0
            );

            emitter.quad(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    0.5F, 1, 1, 0.5F, 0,
                    0, 1, 1, 0, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0.5F, 1, 0, 0.5F, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    0.5F, 1, 0F, 0, 0
            );

            emitter.quad(face,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 0.5F, 1, 0
            );

            emitter.quad(inner,
                    0.5F, 1, 0, 0.5F, 0,
                    0.5F, 1, 0.5F, 0.5F, 0.5F,
                    1, 1, 0.5F, 1, 0.5F,
                    1, 1, 0, 1, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0.5F, 1, 0, 0.5F, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    1, 0, 1, 0, 1,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 0.5F, 0.5F, 0
            );

            return builder.build();
        }

        @Override
        public Mesh southEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    1, 1, 0.5F, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(face,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0.5F, 1, 1, 1, 0,
                    0.5F, 1, 0.5F, 0.5F, 0
            );

            emitter.quad(inner,
                    0.5F, 1, 0.5F, 0.5F, 0.5F,
                    0.5F, 1, 1, 0.5F, 1,
                    1, 1, 1, 1, 1,
                    1, 1, 0.5F, 1, 0.5F
            );

            emitter.quad(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    0.5F, 1, 1, 0.5F, 0
            );

            emitter.quad(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0.5F, 0.5F, 0,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh southWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 0, 1, 1,
                    0, 1, 0.5F, 1, 0,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(face,
                    1, 0, 0, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    0.5F, 1, 1, 0, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 1, 0.5F, 0, 0.5F,
                    0, 1, 1, 0, 1,
                    0.5F, 1, 1, 0.5F, 1,
                    0.5F, 1, 0.5F, 0.5F, 0.5F
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0.5F, 0.5F, 0
            );

            emitter.quad(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    0.5F, 1, 1, 0.5F, 0,
                    0, 1, 1, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            base(emitter);

            emitter.quad(face,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    0, 1, 0.5F, 0, 0
            );

            emitter.quad(face,
                    1, 0, 0, 1, 1,
                    0.5F, 1, 0, 1, 0,
                    0.5F, 1, 0.5F, 0.5F, 0,
                    1, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 0.5F, 0.5F, 0,
                    0, 1, 0, 0, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    0.5F, 1, 0, 0.5F, 0,
                    1, 0, 0, 0, 1
            );

            return builder.build();
        }
    }

    protected class Bottom extends Inner {
        private void top(DecoratedEmitter emitter) {
            emitter.quad(base,
                    0, 1, 0, 0, 0,
                    0, 1, 1, 0, 1,
                    1, 1, 1, 1, 1,
                    1, 1, 0, 1, 0
            );
        }

        @Override
        public Mesh point(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh north(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.quad(face,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0,
                    0, 0, 0, 0, 1,
                    1, 0, 0, 1, 1
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0
            );

            emitter.quad(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            return builder.build();
        }

        @Override
        public Mesh east(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.quad(face,
                    0, 1, 0, 0, 0,
                    1, 0, 0, 0, 1,
                    1, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );


            emitter.triangle(inner,
                    0, 1, 1, 0, 0,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.quad(inner,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0
            );

            return builder.build();
        }

        @Override
        public Mesh south(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.quad(face,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 1, 0, 1,
                    0, 0, 1, 1, 1
            );

            emitter.triangle(inner,
                    1, 1, 1, 0, 0,
                    1, 0, 1, 0, 1,
                    1, 1, 0, 1, 0
            );

            emitter.triangle(inner,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.quad(inner,
                    0, 0, 1, 0, 1,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh west(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.quad(face,
                    0, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0,
                    0, 0, 1, 0, 1
            );

            emitter.quad(inner,
                    0, 0, 0, 0, 1,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 1, 0, 1,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northEast(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.triangle(face,
                    0, 1, 0, 0, 0,
                    1, 0, 0, 0, 1,
                    0, 1, 1, 1, 0
            );

            emitter.triangle(face,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0,
                    1, 0, 0, 1, 1
            );

            emitter.triangle(inner,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    1, 0, 0, 0, 1
            );

            emitter.triangle(inner,
                    1, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh southEast(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.triangle(face,
                    1, 0, 1, 0, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(face,
                    1, 0, 1, 1, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    1, 0, 1, 0, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0
            );

            emitter.triangle(inner,
                    1, 0, 1, 1, 1,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh southWest(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.triangle(face,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0,
                    0, 0, 1, 1, 1
            );

            emitter.triangle(face,
                    1, 1, 1, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 1, 0, 1, 0
            );

            emitter.triangle(inner,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 1, 1,
                    0, 1, 1, 1, 0
            );

            emitter.triangle(inner,
                    0, 1, 1, 0, 0,
                    0, 0, 1, 0, 1,
                    1, 1, 1, 1, 0
            );

            return builder.build();
        }

        @Override
        public Mesh northWest(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            emitter.triangle(face,
                    0, 0, 0, 0, 1,
                    1, 1, 1, 1, 0,
                    0, 1, 1, 0, 0
            );

            emitter.triangle(face,
                    0, 0, 0, 1, 1,
                    1, 1, 0, 1, 0,
                    1, 1, 1, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 1, 1,
                    0, 1, 0, 1, 0,
                    1, 1, 0, 0, 0
            );

            emitter.triangle(inner,
                    0, 0, 0, 0, 1,
                    0, 1, 1, 1, 0,
                    0, 1, 0, 0, 0
            );

            return builder.build();
        }

        @Override
        public Mesh pointTall(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh eastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh westTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh eastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh westBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh southWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }

        @Override
        public Mesh northWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            top(emitter);

            return builder.build();
        }
    }

    interface Short {
        Mesh point(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh north(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh east(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh south(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh west(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northEast(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southEast(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southWest(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northWest(MeshBuilder builder, DecoratedEmitter emitter);
    }

    interface Tall {
        Mesh pointTall(MeshBuilder builder, DecoratedEmitter emitter);

        Mesh northTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh eastTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh westTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northEastTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southEastTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southWestTip(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northWestTip(MeshBuilder builder, DecoratedEmitter emitter);

        Mesh northBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh eastBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh westBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northEastBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southEastBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh southWestBase(MeshBuilder builder, DecoratedEmitter emitter);
        Mesh northWestBase(MeshBuilder builder, DecoratedEmitter emitter);
    }
}
