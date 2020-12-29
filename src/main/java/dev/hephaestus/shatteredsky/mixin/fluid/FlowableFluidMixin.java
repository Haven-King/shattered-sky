package dev.hephaestus.shatteredsky.mixin.fluid;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.data.SkyFalls;
import dev.hephaestus.shatteredsky.util.ChunkDataRegistry;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {

	@Shadow protected abstract boolean isInfinite();

	@Inject(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void flowInVoid(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci) {
		if (world instanceof ServerWorld) {
			SkyFalls skyFalls = ChunkDataRegistry.get(ShatteredSky.SKY_FALLS_ID, world.getChunk(pos));
			skyFalls.tick(pos, fluidState.getFluid());
			world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
		}
	}

	private WorldAccess lastWorld = null;

	@Inject(method = "getUpdatedState", at = @At("HEAD"))
	private void captureWorld(WorldView world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> cir) {
		lastWorld = (WorldAccess) world;
	}

	@Redirect(method = "getUpdatedState", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;isInfinite()Z"))
	private boolean shouldFlow(FlowableFluid flowableFluid) {
		return this.isInfinite() && (!RegistryUtil.dimensionMatches(lastWorld, ShatteredSky.DIMENSION_TYPE));
	}
}
