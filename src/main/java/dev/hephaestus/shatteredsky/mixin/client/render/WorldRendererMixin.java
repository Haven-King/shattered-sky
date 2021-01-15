package dev.hephaestus.shatteredsky.mixin.client.render;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "renderSky", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private double dontRenderVoid(Vec3d vec3d) {
        return RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE) ? Double.MAX_VALUE : vec3d.y;
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight()D"))
    private double dontRenderVoid(ClientWorld.Properties properties) {
        return RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE) ? 0 : properties.getSkyDarknessHeight();
    }
}
