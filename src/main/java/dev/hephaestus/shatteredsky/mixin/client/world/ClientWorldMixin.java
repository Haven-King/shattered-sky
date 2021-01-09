package dev.hephaestus.shatteredsky.mixin.client.world;

import dev.hephaestus.shatteredsky.ShatteredSky;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.level.ColorResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "method_23778(Lit/unimi/dsi/fastutil/objects/Object2ObjectArrayMap;)V", at = @At("TAIL"))
    private static void addSkyColorProvider(Object2ObjectArrayMap<ColorResolver, BiomeColorCache> map, CallbackInfo ci) {
        map.put(ShatteredSky.World.SKY_COLOR, new BiomeColorCache());
    }

    BlockPos pos;

    @Inject(method = "method_23777", at = @At("HEAD"))
    private void capturePosition(BlockPos blockPos, float f, CallbackInfoReturnable<Vec3d> cir) {
        pos = blockPos;
    }

    @Redirect(method = "method_23777", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getSkyColor()I"))
    private int getSmoothSkyColor(Biome biome) {
        if (MinecraftClient.getInstance().world != null && pos != null) {
            double r = 0, g = 0, b = 0;

            int radius = 10;

            BiomeAccess biomeAccess = MinecraftClient.getInstance().world.getBiomeAccess();
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, 0, -radius), pos.add(radius, 0, radius))) {
                int here = biomeAccess.getBiome(blockPos).getSkyColor();
                r += (here >> 16) & 0xFF;
                g += (here >> 8) & 0xFF;
                b += here & 0xFF;
            }

            int total = (radius * 2 + 1) * (radius * 2 + 1);
            r /= total;
            g /= total;
            b /= total;

            return 0xFF000000
                    | (int) r << 16
                    | (int) g << 8
                    | (int) b;
        }

        return biome.getSkyColor();
    }
}
