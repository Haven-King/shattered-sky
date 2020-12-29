package dev.hephaestus.shatteredsky.block;

import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.util.RegistryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class PsuedoFluidBlock extends Block {
	public static VoxelShape CULLING_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 13, 16);

	protected PsuedoFluidBlock(AbstractBlock.Settings settings) {
		super(settings);

//		this.setDefaultState(this.getStateManager().getDefaultState()
//				.with(Properties.UP, false)
//				.with(Properties.DOWN, false)
//		);
	}

//	@Override
//	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
//		return CULLING_SHAPE;
//	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//		builder.add(
//				Properties.UP,
//				Properties.DOWN
//		);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return direction.getAxis().isHorizontal() || stateFrom.isOf(this);
	}

	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.emptyList();
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, state.getBlock(), this.getTickRate(world));
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, state.getBlock(), this.getTickRate(world));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return super.getRenderType(state);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (RegistryUtil.dimensionMatches(world, ShatteredSky.DIMENSION_TYPE) && pos.getY() < 70 && pos.getY() >= 10) {
			BlockPos.Mutable mut = new BlockPos.Mutable();

			for (Direction direction : Direction.values()) {
				mut.set(pos);
				mut.move(direction);

				if (mut.getY() > 10 && mut.getY() < 70 && world.getBlockState(mut).isAir()) {
					world.setBlockState(mut, state);
				}
			}
		} else {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	protected abstract int getTickRate(World world);
}
