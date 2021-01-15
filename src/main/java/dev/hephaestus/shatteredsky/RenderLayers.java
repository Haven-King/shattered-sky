package dev.hephaestus.shatteredsky;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class RenderLayers extends RenderLayer {
    public static RenderLayer TRANSLUCENT = of("shatterdsky:translucent", VertexFormats.POSITION_COLOR_LIGHT, 7, 2097152, true, true, MultiPhaseParameters.builder().shadeModel(SMOOTH_SHADE_MODEL).lightmap(ENABLE_LIGHTMAP).transparency(TRANSLUCENT_TRANSPARENCY).target(CLOUDS_TARGET).build(true));

    private RenderLayers(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
