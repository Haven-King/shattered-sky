package dev.hephaestus.shatteredsky.fluid;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_5423;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class AtmosphereFluid extends FlowableFluid {
	@Override
	public Fluid getFlowing() {
		return ShatteredSky.Fluids.ATMOSPHERE;
	}

	@Override
	public Fluid getStill() {
		return ShatteredSky.Fluids.ATMOSPHERE;
	}

	@Override
	protected boolean isInfinite() {
		return true;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	protected int getFlowSpeed(WorldView world) {
		return 10;
	}

	protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
		super.appendProperties(builder);
		builder.add(LEVEL);
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 0;
	}

	@Override
	public Item getBucketItem() {
		return Items.AIR;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return true;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 5;
	}

	@Override
	protected float getBlastResistance() {
		return 100F;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return ShatteredSky.Blocks.ATMOSPHERE.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
	}

	@Override
	public boolean isStill(FluidState state) {
		return true;
	}

	@Override
	public int getLevel(FluidState state) {
		return 8;
	}

	@Override
	public VoxelShape getShape(FluidState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	protected boolean canFlow(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo, BlockState flowToBlockState, FluidState fluidState, Fluid fluid) {
		boolean f = false;

		if (world instanceof class_5423) {
			if (RegistryUtil.dimensionMatches((class_5423) world, ShatteredSky.DIMENSION_TYPE)) {
				f = flowTo.getY() <= 70 && flowTo.getY() > 10; // This is the "sea level" of our dimension.
			}
		}

		return f && flowToBlockState.isAir() && super.canFlow(world, fluidPos, fluidBlockState, flowDirection, flowTo, flowToBlockState, fluidState, fluid);
	}

	public static class Renderer implements FluidRenderHandler {
		private static final Lazy<Sprite[]> SPRITES = new Lazy<>(() ->
			new Sprite[] {
				ShatteredSky.Fluids.ATMOSPHERE_STILL.getSprite(),
				ShatteredSky.Fluids.ATMOSPHERE_FLOWING.getSprite(),
			}
		);

		@Override
		public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
			return 0x007FAAFF;
		}

		@Override
		public Sprite[] getFluidSprites(@Nullable BlockRenderView blockRenderView, @Nullable BlockPos blockPos, FluidState fluidState) {
			return SPRITES.get();
		}
	}
}
