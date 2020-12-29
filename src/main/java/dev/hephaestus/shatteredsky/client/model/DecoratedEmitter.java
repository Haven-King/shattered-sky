package dev.hephaestus.shatteredsky.client.model;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecoratedEmitter implements QuadEmitter {
    private final QuadEmitter quadEmitter;
    private final ModelBakeSettings bakeSettings;

    private Direction nominalFace = null;
    private Vector3f normal = null;


    public DecoratedEmitter(QuadEmitter quadEmitter, ModelBakeSettings bakeSettings) {
        this.quadEmitter = quadEmitter;
        this.bakeSettings = bakeSettings;
    }

    private void vertex(int index, float x, float y, float z, int color, float u, float v) {
        Vector3f pos = new Vector3f(x, y, z);

        pos.add(-0.5F, -0.5F, -0.5F);
        pos.rotate(this.bakeSettings.getRotation().getRotation2());
        pos.add(0.5F, 0.5F, 0.5F);

        quadEmitter.pos(index, pos).spriteColor(index, 0, color).sprite(index, 0, u, v);

        if (normal != null) {
            Vector3f normal = this.normal.copy();
            normal.rotate(this.bakeSettings.getRotation().getRotation2());

            quadEmitter.normal(index, normal);
        }
    }

    public void triangle(Sprite sprite, int bakeFlags, Point point1, Point point2, Point point3) {
        quad(sprite, bakeFlags, point1, point2, point3, point3);
    }

    public void triangle(Sprite sprite, int bakeFlags,
                         float x1, float y1, float z1, float u1, float v1,
                         float x2, float y2, float z2, float u2, float v2,
                         float x3, float y3, float z3, float u3, float v3) {
        triangle(sprite, bakeFlags,
                x1, y1, z1, -1, u1, v1,
                x2, y2, z2, -1, u2, v2,
                x3, y3, z3, -1, u3, v3
        );
    }

    public void triangle(Sprite sprite,
                         float x1, float y1, float z1, float u1, float v1,
                         float x2, float y2, float z2, float u2, float v2,
                         float x3, float y3, float z3, float u3, float v3) {
        triangle(sprite, 0,
                x1, y1, z1, -1, u1, v1,
                x2, y2, z2, -1, u2, v2,
                x3, y3, z3, -1, u3, v3
        );
    }

    public void triangle(Sprite sprite, int color, int bakeFlags,
                         float x1, float y1, float z1, float u1, float v1,
                         float x2, float y2, float z2, float u2, float v2,
                         float x3, float y3, float z3, float u3, float v3) {
        triangle(sprite, bakeFlags,
                x1, y1, z1, color, u1, v1,
                x2, y2, z2, color, u2, v2,
                x3, y3, z3, color, u3, v3
        );
    }

    public void triangle(Sprite sprite, int bakeFlags,
                         float x1, float y1, float z1, int color1, float u1, float v1,
                         float x2, float y2, float z2, int color2, float u2, float v2,
                         float x3, float y3, float z3, int color3, float u3, float v3)
    {
        quad(sprite, bakeFlags,
                x1, y1, z1, color1, u1, v1,
                x2, y2, z2, color2, u2, v2,
                x3, y3, z3, color3, u3, v3,
                x3, y3, z3, color3, u3, v3
        );
    }

    public void quad(Sprite sprite, int bakeFlags, Point point1, Point point2, Point point3, Point point4) {
        quad(sprite, bakeFlags,
                point1.x, point1.y, point1.z, point1.color, point1.u, point1.v,
                point2.x, point2.y, point2.z, point2.color, point2.u, point2.v,
                point3.x, point3.y, point3.z, point3.color, point3.u, point3.v,
                point4.x, point4.y, point4.z, point4.color, point4.u, point4.v
        );
    }

    public void quad(Sprite sprite, int bakeFlags,
                     float x1, float y1, float z1, float u1, float v1,
                     float x2, float y2, float z2, float u2, float v2,
                     float x3, float y3, float z3, float u3, float v3,
                     float x4, float y4, float z4, float u4, float v4) {
        quad(sprite, bakeFlags,
                x1, y1, z1, -1, u1, v1,
                x2, y2, z2, -1, u2, v2,
                x3, y3, z3, -1, u3, v3,
                x4, y4, z4, -1, u4, v4
        );
    }

    public void quad(Sprite sprite,
                     float x1, float y1, float z1, float u1, float v1,
                     float x2, float y2, float z2, float u2, float v2,
                     float x3, float y3, float z3, float u3, float v3,
                     float x4, float y4, float z4, float u4, float v4) {
        quad(sprite, 0,
                x1, y1, z1, -1, u1, v1,
                x2, y2, z2, -1, u2, v2,
                x3, y3, z3, -1, u3, v3,
                x4, y4, z4, -1, u4, v4
        );
    }

    public void quad(Sprite sprite, int bakeFlags,
                      float x1, float y1, float z1, int color1, float u1, float v1,
                      float x2, float y2, float z2, int color2, float u2, float v2,
                      float x3, float y3, float z3, int color3, float u3, float v3,
                      float x4, float y4, float z4, int color4, float u4, float v4)
    {
        final float w = sprite.getWidth();
        final float h = sprite.getHeight();

        vertex(0, x1, y1, z1, color1, u1 * w, v1 * h);
        vertex(1, x2, y2, z2, color2, u2 * w, v2 * h);
        vertex(2, x3, y3, z3, color3, u3 * w, v3 * h);
        vertex(3, x4, y4, z4, color4, u4 * w, v4 * h);

        quadEmitter.nominalFace(this.nominalFace == null ? quadEmitter.lightFace() : this.nominalFace);
        quadEmitter.spriteBake(0, sprite, bakeFlags);
        quadEmitter.emit();
        this.nominalFace = null;
        this.normal = null;
    }

    public void setNominalFace(Direction direction) {
        this.nominalFace = direction;
    }

    public void setNormal(float x, float y, float z) {
        if (this.normal == null) {
            this.normal = new Vector3f();
        }

        this.normal.set(x, y, z);
    }

    @Override
    public QuadEmitter material(RenderMaterial renderMaterial) {
        return this.quadEmitter.material(renderMaterial);
    }

    @Override
    public QuadEmitter cullFace(Direction direction) {
        return this.quadEmitter.cullFace(direction);
    }

    @Override
    public QuadEmitter nominalFace(Direction direction) {
        return this.quadEmitter.nominalFace(direction);
    }

    @Override
    public QuadEmitter colorIndex(int i) {
        return this.quadEmitter.colorIndex(i);
    }

    @Override
    public QuadEmitter fromVanilla(int[] ints, int i, boolean b) {
        return this.quadEmitter.fromVanilla(ints, i, b);
    }

    @Override
    public MutableQuadView fromVanilla(BakedQuad bakedQuad, RenderMaterial renderMaterial, Direction direction) {
        return this.quadEmitter.fromVanilla(bakedQuad, renderMaterial, direction);
    }

    @Override
    public QuadEmitter tag(int i) {
        return this.quadEmitter.tag(i);
    }

    @Override
    public QuadEmitter pos(int i, float x, float y, float z) {
        return this.quadEmitter.pos(i, x, y, z);
    }

    @Override
    public MutableQuadView normal(int i, float v, float v1, float v2) {
        return this.quadEmitter.normal(i, v, v1, v2);
    }

    @Override
    public QuadEmitter lightmap(int i, int i1) {
        return this.quadEmitter.lightmap(i, i1);
    }

    @Override
    public QuadEmitter spriteColor(int i, int i1, int i2) {
        return this.quadEmitter.spriteColor(i, i1, i2);
    }

    @Override
    public QuadEmitter sprite(int i, int i1, float v, float v1) {
        return this.quadEmitter.sprite(i, i1, v, v1);
    }

    @Override
    public QuadEmitter spriteBake(int i, Sprite sprite, int i1) {
        return this.quadEmitter.spriteBake(i, sprite, i1);
    }

    @Override
    public QuadEmitter emit() {
        return this.quadEmitter.emit();
    }

    @Override
    public void toVanilla(int i, int[] ints, int i1, boolean b) {
        this.quadEmitter.toVanilla(i, ints, i1, b);
    }

    @Override
    public void copyTo(MutableQuadView mutableQuadView) {
        this.quadEmitter.copyTo(mutableQuadView);
    }

    @Override
    public RenderMaterial material() {
        return this.quadEmitter.material();
    }

    @Override
    public int colorIndex() {
        return this.quadEmitter.colorIndex();
    }

    @Override
    public @NotNull Direction lightFace() {
        return this.quadEmitter.lightFace();
    }

    @Override
    public @Nullable Direction cullFace() {
        return this.quadEmitter.cullFace();
    }

    @Override
    public Direction nominalFace() {
        return this.quadEmitter.nominalFace();
    }

    @Override
    public Vector3f faceNormal() {
        return this.quadEmitter.faceNormal();
    }

    @Override
    public int tag() {
        return this.quadEmitter.tag();
    }

    @Override
    public Vector3f copyPos(int i, @Nullable Vector3f vector3f) {
        return this.quadEmitter.copyPos(i, vector3f);
    }

    @Override
    public float posByIndex(int i, int i1) {
        return this.quadEmitter.posByIndex(i, i1);
    }

    @Override
    public float x(int i) {
        return this.quadEmitter.x(i);
    }

    @Override
    public float y(int i) {
        return this.quadEmitter.y(i);
    }

    @Override
    public float z(int i) {
        return this.quadEmitter.z(i);
    }

    @Override
    public boolean hasNormal(int i) {
        return this.quadEmitter.hasNormal(i);
    }

    @Override
    public @Nullable Vector3f copyNormal(int i, @Nullable Vector3f vector3f) {
        return this.quadEmitter.copyNormal(i, vector3f);
    }

    @Override
    public float normalX(int i) {
        return this.quadEmitter.normalX(i);
    }

    @Override
    public float normalY(int i) {
        return this.quadEmitter.normalY(i);
    }

    @Override
    public float normalZ(int i) {
        return this.quadEmitter.normalZ(i);
    }

    @Override
    public int lightmap(int i) {
        return this.quadEmitter.lightmap(i);
    }

    @Override
    public int spriteColor(int i, int i1) {
        return this.quadEmitter.spriteColor(i, i1);
    }

    @Override
    public float spriteU(int i, int i1) {
        return 0;
    }

    @Override
    public float spriteV(int i, int i1) {
        return 0;
    }

    public static class Point {
        public final float x, y, z, u, v;
        public final int color;

        public Point(float x, float y, float z, int u, int v) {
            this(x, y, z, -1, u, v);
        }

        public Point(float x, float y, float z, int color, float u, float v) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
            this.u = u;
            this.v = v;
        }
    }
}
