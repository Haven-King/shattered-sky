package dev.hephaestus.shatteredsky.mixin.client.render;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.client.render.AtmosphereRenderer;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Shadow private static float red;
    @Shadow private static float green;
    @Shadow private static float blue;

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private static double adjustVoidVector(Vec3d vec3d) {
        return RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE)
                ? Double.MAX_VALUE : vec3d.y;
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer;lastWaterFogColorUpdateTime:J", opcode = Opcodes.PUTSTATIC))
    private static void renderFog(Camera camera, float tickDelta, ClientWorld world, int i, float f, CallbackInfo ci) {
        if (RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE) && camera.getPos().y <= 70.85 && camera.getPos().y >= 10) {
//            float ah = Math.min(1.0F / red, Math.min(1.0F / green, 1.0F / blue));
//            float ag = 0F;
            red = AtmosphereRenderer.getRed(red);
            green = AtmosphereRenderer.getGreen(green);
            blue = AtmosphereRenderer.getBlue(blue);
        }
    }

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void updateFogDensity(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        if (RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE) && camera.getPos().y <= 70.85 && camera.getPos().y >= 10) {
            AtmosphereRenderer.applyFog(camera, fogType, viewDistance, thickFog);
        }
    }
}
