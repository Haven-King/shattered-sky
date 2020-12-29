package dev.hephaestus.shatteredsky.client.model.mesh;

import dev.hephaestus.shatteredsky.client.model.DecoratedEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.minecraft.client.texture.Sprite;

public class FramedHedronMeshBuilder extends HedronMeshBuilder {
    private final Sprite frame;

    public FramedHedronMeshBuilder(Sprite face, Sprite inner, Sprite base, Sprite frame) {
        super(face, inner, base);
        this.frame = frame;
    }

    @Override
    public Inner createTop() {
        return new Top();
    }

    @Override
    public Inner createBottom() {
        return new Bottom();
    }

    public class Top extends HedronMeshBuilder.Top {
        @Override
        public Mesh point(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    -0.25F, 0, -0.25F, 1, 0,
                    0.25F, 0, -0.25F, 0.5F, 0,
                    0.25F, 0, 0.25F, 0.5F, 0.5F,
                    -0.25F, 0, 0.25F, 1, 0.5F
            );

            return super.point(builder, emitter);
        }

        @Override
        public Mesh northEast(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northEast(builder, emitter);
        }

        @Override
        public Mesh southEast(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southEast(builder, emitter);
        }

        @Override
        public Mesh southWest(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southWest(builder, emitter);
        }

        @Override
        public Mesh northWest(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northWest(builder, emitter);
        }

        @Override
        public Mesh pointTall(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    -0.25F, 0, -0.25F, 1, 0,
                    0.25F, 0, -0.25F, 0.5F, 0,
                    0.25F, 0, 0.25F, 0.5F, 0.5F,
                    -0.25F, 0, 0.25F, 1, 0.5F
            );

            return super.pointTall(builder, emitter);
        }

        @Override
        public Mesh northEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            float dX = 0.5F;
            float dZ = -0.5F;

            emitter.quad(frame,
                    -0.25F + dX, 0, 0.75F + dZ, 0.5F, 1,
                    -0.25F + dX, 0, 1.25F + dZ, 1, 1,
                    0.25F + dX, 1, 0.75F + dZ, 1, 0,
                    0.25F + dX, 1, 0.25F + dZ, 0.5F, 0
            );

            emitter.quad(frame,
                    -0.25F + dX, 0, 1.25F + dZ, 0, 1,
                    0.25F + dX, 0, 1.25F + dZ, 0.5F, 1,
                    0.75F + dX, 1, 0.75F + dZ, 0.5F, 0,
                    0.25F + dX, 1, 0.75F + dZ, 0, 0
            );

            emitter.quad(frame,
                    0.25F + dX, 1, 0.25F + dZ, 0.25F, 0.25F,
                    0.25F + dX, 1, 0.75F + dZ, 0.25F, 0.75F,
                    0.75F + dX, 1, 0.75F + dZ, 0.75F, 0.75F,
                    0.75F + dX, 1, 0.25F + dZ, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    -0.25F + dX, 0, 1.25F + dZ, 0.25F, 0.25F,
                    -0.25F + dX, 0, 0.75F + dZ, 0.25F, 0.75F,
                    0.25F + dX, 0, 0.75F + dZ, 0.75F, 0.75F,
                    0.25F + dX, 0, 1.25F + dZ, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    0.25F + dX, 0, 1.25F + dZ, 0, 1,
                    0.25F + dX, 0, 0.75F + dZ, 0.5F, 1,
                    0.75F + dX, 1, 0.25F + dZ, 0.5F, 0,
                    0.75F + dX, 1, 0.75F + dZ, 0, 0
            );

            emitter.quad(frame,
                    -0.25F + dX, 0, 0.75F + dZ, 1, 1,
                    0.25F + dX, 1, 0.25F + dZ, 1, 0,
                    0.75F + dX, 1, 0.25F + dZ, 0.5F, 0,
                    0.25F + dX, 0, 0.75F + dZ, 0.5F, 1
            );

            return super.northEastTip(builder, emitter);
        }

        @Override
        public Mesh southEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            float d = 0.5F;

            emitter.quad(frame,
                    -0.25F + d, 0, -0.25F + d, 1, 1,
                    0.25F + d, 1, 0.25F + d, 1, 0,
                    0.75F + d, 1, 0.25F + d, 0.5F, 0,
                    0.25F + d, 0, -0.25F + d, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F + d, 0, -0.25F + d, 0, 1,
                    -0.25F + d, 0, 0.25F + d,0.5F, 1,
                    0.25F + d, 1, 0.75F + d, 0.5F, 0,
                    0.25F + d, 1, 0.25F + d, 0, 0
            );

            emitter.quad(frame,
                    0.25F + d, 1, 0.25F + d, 1, 1,
                    0.25F + d, 1, 0.75F + d, 1, 0.5F,
                    0.75F + d, 1, 0.75F + d, 0.5F, 0.5F,
                    0.75F + d, 1, 0.25F + d, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F + d, 0, -0.25F + d, 1, 0,
                    0.25F + d, 0, -0.25F + d, 0.5F, 0,
                    0.25F + d, 0, 0.25F + d, 0.5F, 0.5F,
                    -0.25F + d, 0, 0.25F + d, 1, 0.5F
            );

            emitter.quad(frame,
                    0.25F + d, 0, -0.25F + d, 1, 1,
                    0.75F + d, 1, 0.25F + d, 1, 0,
                    0.75F + d, 1, 0.75F + d, 0.5F, 0,
                    0.25F + d, 0, 0.75F + d, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F + d, 0, 0.25F + d, 0, 1,
                    0.25F + d, 0, 0.25F + d, 0.5F, 1,
                    0.75F + d, 1, 0.75F + d, 0.5F, 0,
                    0.25F + d, 1, 0.75F + d, 0, 0
            );

            return super.southEastTip(builder, emitter);
        }

        @Override
        public Mesh southWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            float dX = -0.5F;
            float dZ = 0.5F;

            emitter.quad(frame,
                    1.25F + dX, 0, -0.25F + dZ, 0, 1,
                    0.75F + dX, 0, -0.25F + dZ, 0.5F, 1,
                    0.25F + dX, 1, 0.25F + dZ, 0.5F, 0,
                    0.75F + dX, 1, 0.25F + dZ, 0, 0
            );

            emitter.quad(frame,
                    1.25F + dX, 0, -0.25F + dZ, 1, 1,
                    0.75F + dX, 1, 0.25F + dZ, 1, 0,
                    0.75F + dX, 1, 0.75F + dZ, 0.5F, 0,
                    1.25F + dX, 0, 0.25F + dZ, 0.5F, 1
            );

            emitter.quad(frame,
                    0.75F + dX, 1, 0.75F + dZ, 0.25F, 0.25F,
                    0.75F + dX, 1, 0.25F + dZ, 0.25F, 0.75F,
                    0.25F + dX, 1, 0.25F + dZ, 0.75F, 0.75F,
                    0.25F + dX, 1, 0.75F + dZ, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    1.25F + dX, 0, -0.25F + dZ, 0.25F, 0.25F,
                    1.25F + dX, 0, 0.25F + dZ, 0.25F, 0.75F,
                    0.75F + dX, 0, 0.25F + dZ, 0.75F, 0.75F,
                    0.75F + dX, 0, -0.25F + dZ, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    0.75F + dX, 0, -0.25F + dZ, 0, 1,
                    0.75F + dX, 0, 0.25F + dZ, 0.5F, 1,
                    0.25F + dX, 1, 0.75F + dZ, 0.5F, 0,
                    0.25F + dX, 1, 0.25F + dZ, 0, 0
            );

            emitter.quad(frame,
                    0.75F + dX, 1, 0.75F + dZ, 1, 0,
                    0.25F + dX, 1, 0.75F + dZ, 0.5F, 0,
                    0.75F + dX, 0, 0.25F + dZ, 0.5F, 1,
                    1.25F + dX, 0, 0.25F + dZ, 1, 1
            );

            return super.southWestTip(builder, emitter);
        }

        @Override
        public Mesh northWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            float d = -0.5F;
            emitter.quad(frame,
                    1.25F + d, 0, 1.25F + d, 1, 1,
                    0.75F + d, 1, 0.75F + d, 1, 0,
                    0.25F + d, 1, 0.75F + d, 0.5F, 0,
                    0.75F + d, 0, 1.25F + d, 0.5F, 1
            );

            emitter.quad(frame,
                    1.25F + d, 0, 1.25F + d, 0, 1,
                    1.25F + d, 0, 0.75F + d, 0.5F, 1,
                    0.75F + d, 1, 0.25F + d, 0.5F, 0,
                    0.75F + d, 1, 0.75F + d, 0, 0
            );

            emitter.quad(frame,
                    0.75F + d, 1, 0.75F + d, 0.75F, 0.75F,
                    0.75F + d, 1, 0.25F + d, 0.75F, 0.25F,
                    0.25F + d, 1, 0.25F + d, 0.25F, 0.25F,
                    0.25F + d, 1, 0.75F + d, 0.25F, 0.75F
            );

            emitter.quad(frame,
                    1.25F + d, 0, 1.25F + d, 1, 0,
                    0.75F + d, 0, 1.25F + d, 0.5F, 0,
                    0.75F + d, 0, 0.75F + d, 0.5F, 1,
                    1.25F + d, 0, 0.75F + d, 1, 1
            );

            emitter.quad(frame,
                    0.25F + d, 1, 0.25F + d, 0.5F, 0,
                    0.75F + d, 0, 0.75F + d, 0.5F, 1,
                    0.75F + d, 0, 1.25F + d, 1, 1,
                    0.25F + d, 1, 0.75F + d, 1, 0
            );

            emitter.quad(frame,
                    0.25F + d, 1, 0.25F + d, 0.5F, 0,
                    0.75F + d, 1, 0.25F + d, 0, 0,
                    1.25F + d, 0, 0.75F + d, 0, 1,
                    0.75F + d, 0, 0.75F + d, 0.5F, 1
            );

            return super.northWestTip(builder, emitter);
        }

        @Override
        public Mesh northEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    -0.25F, 0, 0.75F, 0.5F, 1,
                    -0.25F, 0, 1.25F, 1, 1,
                    0.25F, 1, 0.75F, 1, 0,
                    0.25F, 1, 0.25F, 0.5F, 0
            );

            emitter.quad(frame,
                    -0.25F, 0, 1.25F, 0, 1,
                    0.25F, 0, 1.25F, 0.5F, 1,
                    0.75F, 1, 0.75F, 0.5F, 0,
                    0.25F, 1, 0.75F, 0, 0
            );

            emitter.quad(frame,
                    0.25F, 1, 0.25F, 0.25F, 0.25F,
                    0.25F, 1, 0.75F, 0.25F, 0.75F,
                    0.75F, 1, 0.75F, 0.75F, 0.75F,
                    0.75F, 1, 0.25F, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    -0.25F, 0, 1.25F, 0.25F, 0.25F,
                    -0.25F, 0, 0.75F, 0.25F, 0.75F,
                    0.25F, 0, 0.75F, 0.75F, 0.75F,
                    0.25F, 0, 1.25F, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    0.25F, 0, 1.25F, 0, 1,
                    0.25F, 0, 0.75F, 0.5F, 1,
                    0.75F, 1, 0.25F, 0.5F, 0,
                    0.75F, 1, 0.75F, 0, 0
            );

            emitter.quad(frame,
                    -0.25F, 0, 0.75F, 1, 1,
                    0.25F, 1, 0.25F, 1, 0,
                    0.75F, 1, 0.25F, 0.5F, 0,
                    0.25F, 0, 0.75F, 0.5F, 1
            );

            return super.northEastBase(builder, emitter);
        }

        @Override
        public Mesh southEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    -0.25F, 0, -0.25F, 1, 1,
                    0.25F, 1, 0.25F, 1, 0,
                    0.75F, 1, 0.25F, 0.5F, 0,
                    0.25F, 0, -0.25F, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F, 0, -0.25F, 0, 1,
                    -0.25F, 0, 0.25F,0.5F, 1,
                    0.25F, 1, 0.75F, 0.5F, 0,
                    0.25F, 1, 0.25F, 0, 0
            );

            emitter.quad(frame,
                    0.25F, 1, 0.25F, 1, 1,
                    0.25F, 1, 0.75F, 1, 0.5F,
                    0.75F, 1, 0.75F, 0.5F, 0.5F,
                    0.75F, 1, 0.25F, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F, 0, -0.25F, 1, 0,
                    0.25F, 0, -0.25F, 0.5F, 0,
                    0.25F, 0, 0.25F, 0.5F, 0.5F,
                    -0.25F, 0, 0.25F, 1, 0.5F
            );

            emitter.quad(frame,
                    0.25F, 0, -0.25F, 1, 1,
                    0.75F, 1, 0.25F, 1, 0,
                    0.75F, 1, 0.75F, 0.5F, 0,
                    0.25F, 0, 0.75F, 0.5F, 1
            );

            emitter.quad(frame,
                    -0.25F, 0, 0.25F, 0, 1,
                    0.25F, 0, 0.25F, 0.5F, 1,
                    0.75F, 1, 0.75F, 0.5F, 0,
                    0.25F, 1, 0.75F, 0, 0
            );

            return super.southEastBase(builder, emitter);
        }

        @Override
        public Mesh southWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    1.25F, 0, -0.25F, 0, 1,
                    0.75F, 0, -0.25F, 0.5F, 1,
                    0.25F, 1, 0.25F, 0.5F, 0,
                    0.75F, 1, 0.25F, 0, 0
            );

            emitter.quad(frame,
                    1.25F, 0, -0.25F, 1, 1,
                    0.75F, 1, 0.25F, 1, 0,
                    0.75F, 1, 0.75F, 0.5F, 0,
                    1.25F, 0, 0.25F, 0.5F, 1
            );

            emitter.quad(frame,
                    0.75F, 1, 0.75F, 0.25F, 0.25F,
                    0.75F, 1, 0.25F, 0.25F, 0.75F,
                    0.25F, 1, 0.25F, 0.75F, 0.75F,
                    0.25F, 1, 0.75F, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    1.25F, 0, -0.25F, 0.25F, 0.25F,
                    1.25F, 0, 0.25F, 0.25F, 0.75F,
                    0.75F, 0, 0.25F, 0.75F, 0.75F,
                    0.75F, 0, -0.25F, 0.75F, 0.25F
            );

            emitter.quad(frame,
                    0.75F, 0, -0.25F, 0, 1,
                    0.75F, 0, 0.25F, 0.5F, 1,
                    0.25F, 1, 0.75F, 0.5F, 0,
                    0.25F, 1, 0.25F, 0, 0
            );

            emitter.quad(frame,
                    0.75F, 1, 0.75F, 1, 0,
                    0.25F, 1, 0.75F, 0.5F, 0,
                    0.75F, 0, 0.25F, 0.5F, 1,
                    1.25F, 0, 0.25F, 1, 1
            );

            return super.southWestBase(builder, emitter);
        }

        @Override
        public Mesh northWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    1.25F, 0, 1.25F, 1, 1,
                    0.75F, 1, 0.75F, 1, 0,
                    0.25F, 1, 0.75F, 0.5F, 0,
                    0.75F, 0, 1.25F, 0.5F, 1
            );

            emitter.quad(frame,
                    1.25F, 0, 1.25F, 0, 1,
                    1.25F, 0, 0.75F, 0.5F, 1,
                    0.75F, 1, 0.25F, 0.5F, 0,
                    0.75F, 1, 0.75F, 0, 0
            );

            emitter.quad(frame,
                    0.75F, 1, 0.75F, 0.75F, 0.75F,
                    0.75F, 1, 0.25F, 0.75F, 0.25F,
                    0.25F, 1, 0.25F, 0.25F, 0.25F,
                    0.25F, 1, 0.75F, 0.25F, 0.75F
            );

            emitter.quad(frame,
                    1.25F, 0, 1.25F, 1, 0,
                    0.75F, 0, 1.25F, 0.5F, 0,
                    0.75F, 0, 0.75F, 0.5F, 1,
                    1.25F, 0, 0.75F, 1, 1
            );

            emitter.quad(frame,
                    0.25F, 1, 0.25F, 0.5F, 0,
                    0.75F, 0, 0.75F, 0.5F, 1,
                    0.75F, 0, 1.25F, 1, 1,
                    0.25F, 1, 0.75F, 1, 0
            );

            emitter.quad(frame,
                    0.25F, 1, 0.25F, 0.5F, 0,
                    0.75F, 1, 0.25F, 0, 0,
                    1.25F, 0, 0.75F, 0, 1,
                    0.75F, 0, 0.75F, 0.5F, 1
            );

            return super.northWestBase(builder, emitter);
        }
    }

    public class Bottom extends HedronMeshBuilder.Bottom {
        @Override
        public Mesh point(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.point(builder, emitter);
        }

        @Override
        public Mesh northEast(MeshBuilder builder, DecoratedEmitter emitter) {


            return super.northEast(builder, emitter);
        }

        @Override
        public Mesh southEast(MeshBuilder builder, DecoratedEmitter emitter) {
            emitter.quad(frame,
                    0.25F, 1, 0.25F, 0.5F, 0.5F,
                    0.25F, 1, -0.25F, 0.5F, 1,
                    -0.25F, 1, -0.25F, 1, 1,
                    -0.25F, 1, 0.25F, 1, 0.5F
            );

            emitter.quad(frame,
                    -0.25F, 1, -0.25F, 0, 0,
                    0.75F, 0, 0.75F, 0, 1,
                    0.75F, 0, 1.25F, 0.5F, 1,
                    -0.25F, 1, 0.25F, 0.5F, 0
            );

            emitter.quad(frame,
                    0.75F, 0, 0.75F, 0.5F, 0.5F,
                    1.25F, 0, 0.75F, 1, 0.5F,
                    1.25F, 0, 1.25F, 1, 0,
                    0.75F, 0, 1.25F, 0.5F, 0
            );

            emitter.quad(frame,
                    0.75F, 0, 0.75F, 1, 1,
                    -0.25F, 1, -0.25F, 1, 0,
                    0.25F, 1, -0.25F, 0.5F, 0,
                    1.25F, 0, 0.75F, 0.5F, 1
            );

            emitter.quad(frame,
                0.25F, 1, -0.25F, 1, 0,
                    0.25F, 1, 0.25F, 0.5F, 0,
                    1.25F, 0, 1.25F, 0.5F, 1,
                    1.25F, 0, 0.75F, 1, 1
            );

            emitter.quad(frame,
                    -0.25F, 1, 0.25F, 0, 0,
                    0.75F, 0, 1.25F, 0, 1,
                    1.25F, 0, 1.25F, 0.5F, 1,
                    0.25F, 1, 0.25F, 0.5F, 0
            );

            return super.southEast(builder, emitter);
        }

        @Override
        public Mesh southWest(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southWest(builder, emitter);
        }

        @Override
        public Mesh northWest(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northWest(builder, emitter);
        }

        @Override
        public Mesh pointTall(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.pointTall(builder, emitter);
        }

        @Override
        public Mesh northEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northEastTip(builder, emitter);
        }

        @Override
        public Mesh southEastTip(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southEastTip(builder, emitter);
        }

        @Override
        public Mesh southWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southWestTip(builder, emitter);
        }

        @Override
        public Mesh northWestTip(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northWestTip(builder, emitter);
        }

        @Override
        public Mesh northEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northEastBase(builder, emitter);
        }

        @Override
        public Mesh southEastBase(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southEastBase(builder, emitter);
        }

        @Override
        public Mesh southWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.southWestBase(builder, emitter);
        }

        @Override
        public Mesh northWestBase(MeshBuilder builder, DecoratedEmitter emitter) {
            return super.northWestBase(builder, emitter);
        }
    }
}
