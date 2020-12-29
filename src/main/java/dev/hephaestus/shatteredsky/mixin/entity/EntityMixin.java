package dev.hephaestus.shatteredsky.mixin.entity;

import dev.hephaestus.shatteredsky.ShatteredSky;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "spawnSprintingParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getRenderType()Lnet/minecraft/block/BlockRenderType;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void dontSpawnSprintingParticles(CallbackInfo ci, int i, int j, int k, BlockPos pos, BlockState state) {
		if (state.isOf(ShatteredSky.Blocks.ATMOSPHERE_BLOCK)) {
			ci.cancel();
		}
	}
}
