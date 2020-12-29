package dev.hephaestus.shatteredsky.mixin.client.world;

import dev.hephaestus.shatteredsky.ShatteredSky;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.level.ColorResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "method_23778(Lit/unimi/dsi/fastutil/objects/Object2ObjectArrayMap;)V", at = @At("TAIL"))
    private static void addSkyColorProvider(Object2ObjectArrayMap<ColorResolver, BiomeColorCache> map, CallbackInfo ci) {
        map.put(ShatteredSky.World.SKY_COLOR, new BiomeColorCache());
    }
}
