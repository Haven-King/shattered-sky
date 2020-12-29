package dev.hephaestus.shatteredsky.mixin.block;

import dev.hephaestus.shatteredsky.ShatteredSky;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpreadableBlock.class)
public class SpreadableBlockMixin {
	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void surviveUnderAtmosphere(BlockState state, WorldView worldView, BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockPos upPos, BlockState up) {
		if (up.isOf(ShatteredSky.Blocks.ATMOSPHERE)) {
			cir.setReturnValue(true);
		}
	}
}
