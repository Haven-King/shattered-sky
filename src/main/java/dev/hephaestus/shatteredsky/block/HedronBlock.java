package dev.hephaestus.shatteredsky.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Lazy;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HedronBlock extends Block {
	public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);

	private static final Direction[] VERTICAL_DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN};

	private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[] {
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
	};

	public HedronBlock(Block block) {
		this(FabricBlockSettings.copyOf(block).nonOpaque());
	}

	public HedronBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState()
				.with(SHAPE, Shape.POINT)
				.with(Properties.BOTTOM, true));


	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.fullCube();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SHAPE, Properties.BOTTOM);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		PlayerEntity player = ctx.getPlayer();

		if (direction.getAxis() == Direction.Axis.Y && ctx.getPlayerLookDirection() == direction.getOpposite() && player != null && (player.getPitch(0) > 80 || player.getPitch(0) < -80)) {
			return this.getDefaultState()
					.with(SHAPE, player.isSneaking()
						? Shape.POINT_TALL
						: Shape.POINT
					)
					.with(Properties.BOTTOM, direction != Direction.DOWN);
		}

		BlockState state = this.getDefaultState().with(Properties.BOTTOM,
				direction != Direction.DOWN
						&& (direction == Direction.UP || ctx.getHitPos().y - (double)ctx.getBlockPos().getY() <= 0.5D)
		);

		if (ctx.getPlayer() != null && !ctx.getPlayer().isSneaking()) {
			switch (ctx.getPlayerFacing()) {
				case NORTH:
					state = state.with(SHAPE, Shape.NORTH);
					break;
				case SOUTH:
					state = state.with(SHAPE, Shape.SOUTH);
					break;
				case WEST:
					state = state.with(SHAPE, Shape.WEST);
					break;
				case EAST:
					state = state.with(SHAPE, Shape.EAST);
					break;
			}
		}

		return getState(
				state,
				ctx.getWorld(),
				ctx.getBlockPos()
		);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (newState.getBlock() instanceof HedronBlock) {
			return getState(state, world, pos);
		} else {
			return state;
		}
	}

	public BlockState getState(BlockState state, WorldAccess world, BlockPos pos) {
		return getHeight(getShape(state, world, pos), world, pos);
	}

	public BlockState getHeight(BlockState state, WorldAccess world, BlockPos pos) {
		for (Direction dir : VERTICAL_DIRECTIONS) {
			BlockState neighbor = world.getBlockState(pos.offset(dir));

			if (neighbor.getBlock() instanceof HedronBlock && neighbor.get(Properties.BOTTOM) == state.get(Properties.BOTTOM)) {
				Shape shape = state.get(SHAPE);
				Shape neighborShape = neighbor.get(SHAPE);

				boolean matches = true;

				for (Direction direction : HORIZONTAL_DIRECTIONS) {
					if (shape.faces(direction) != neighborShape.faces(direction)) {
						matches = false;
						break;
					}
				}

				if (matches) {
					return state.with(SHAPE, shape.getTallVariant(dir == Direction.UP));
				}
			}
		}

		return state;
	}

	public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos) {
		final BlockState north = world.getBlockState(pos.north());
		final BlockState east = world.getBlockState(pos.east());
		final BlockState south = world.getBlockState(pos.south());
		final BlockState west = world.getBlockState(pos.west());

		final Block northBlock = north.getBlock();
		final Block eastBlock = east.getBlock();
		final Block southBlock = south.getBlock();
		final Block westBlock = west.getBlock();

		final Shape northShape = northBlock instanceof HedronBlock ? north.get(SHAPE) : null;
		final Shape eastShape = eastBlock instanceof HedronBlock ? east.get(SHAPE) : null;
		final Shape southShape = southBlock instanceof HedronBlock ? south.get(SHAPE) : null;
		final Shape westShape = westBlock instanceof HedronBlock ? west.get(SHAPE) : null;

		boolean bottom = state.get(Properties.BOTTOM);

		if (!(northBlock instanceof HedronBlock) && !(eastBlock instanceof HedronBlock)
				&& !(southBlock instanceof HedronBlock) && !(westBlock instanceof HedronBlock)) {
			return state;
		}

		if (eastBlock instanceof HedronBlock && westBlock instanceof HedronBlock
			&& east.get(Properties.BOTTOM) == bottom && west.get(Properties.BOTTOM) == bottom) {

			if ((eastShape.faces(Direction.NORTH) || eastShape.faces(Direction.UP))
					&& (westShape.faces(Direction.NORTH) || westShape.faces(Direction.UP))) {
				return state.with(SHAPE, Shape.NORTH);
			} else if (eastShape.faces(Direction.SOUTH) && westShape.faces(Direction.SOUTH)) {
				return state.with(SHAPE, Shape.SOUTH);
			}
		}

		if (northBlock instanceof HedronBlock && southBlock instanceof HedronBlock
				&& north.get(Properties.BOTTOM) == bottom && south.get(Properties.BOTTOM) == bottom) {

			if (northShape.faces(Direction.EAST) && southShape.faces(Direction.EAST)) {
				return state.with(SHAPE, Shape.EAST);
			} else if (northShape.faces(Direction.WEST) && southShape.faces(Direction.WEST)) {
				return state.with(SHAPE, Shape.WEST);
			}
		}

		if (northBlock instanceof HedronBlock && eastBlock instanceof HedronBlock) {
			return state.with(SHAPE, Shape.NORTH_EAST);
		}

		if (southBlock instanceof HedronBlock && eastBlock instanceof HedronBlock) {
			return state.with(SHAPE, Shape.SOUTH_EAST);
		}

		if (southBlock instanceof HedronBlock && westBlock instanceof HedronBlock) {
			return state.with(SHAPE, Shape.SOUTH_WEST);
		}

		if (northBlock instanceof HedronBlock && westBlock instanceof HedronBlock) {
			return state.with(SHAPE, Shape.NORTH_WEST);
		}

		return state;
	}

	public enum Shape implements StringIdentifiable {
		NORTH(true, Direction.NORTH),
		EAST(true, Direction.EAST),
		SOUTH(true, Direction.SOUTH),
		WEST(true, Direction.WEST),
		NORTH_EAST(true, Direction.NORTH, Direction.EAST),
		SOUTH_EAST(true, Direction.SOUTH, Direction.EAST),
		SOUTH_WEST(true, Direction.SOUTH, Direction.WEST),
		NORTH_WEST(true, Direction.NORTH, Direction.WEST),
		POINT(true, Direction.UP),
		
		NORTH_TIP(false, Direction.NORTH),
		EAST_TIP(false, Direction.EAST),
		SOUTH_TIP(false, Direction.SOUTH),
		WEST_TIP(false, Direction.WEST),
		NORTH_EAST_TIP(false, Direction.NORTH, Direction.EAST),
		SOUTH_EAST_TIP(false, Direction.SOUTH, Direction.EAST),
		SOUTH_WEST_TIP(false, Direction.SOUTH, Direction.WEST),
		NORTH_WEST_TIP(false, Direction.NORTH, Direction.WEST),
		NORTH_BASE(false, Direction.NORTH),
		EAST_BASE(false, Direction.EAST),
		SOUTH_BASE(false, Direction.SOUTH),
		WEST_BASE(false, Direction.WEST),
		NORTH_EAST_BASE(false, Direction.NORTH, Direction.EAST),
		SOUTH_EAST_BASE(false, Direction.SOUTH, Direction.EAST),
		SOUTH_WEST_BASE(false, Direction.SOUTH, Direction.WEST),
		NORTH_WEST_BASE(false, Direction.NORTH, Direction.WEST),
		POINT_TALL(false, Direction.UP);

		private final Lazy<String> name = new Lazy<>(() -> this.name().toLowerCase(Locale.ROOT));
		private final Set<Direction> facingDirections = new HashSet<>();
		public final boolean isShort;

		Shape(boolean isShort, Direction... facingDirections) {
			this.facingDirections.addAll(Arrays.asList(facingDirections));
			this.isShort = isShort;
		}

		public boolean faces(Direction direction) {
			return facingDirections.contains(direction);
		}

		@Override
		public String asString() {
			return name.get();
		}

		public Shape getTallVariant(boolean bottom) {
			if (isShort) {
				switch (this) {
					case NORTH: 		return bottom ? NORTH_BASE : NORTH_TIP;
					case EAST: 		return bottom ? EAST_BASE : EAST_TIP;
					case SOUTH:		return bottom ? SOUTH_BASE : SOUTH_TIP;
					case WEST:		return bottom ? WEST_BASE : WEST_TIP;
					case NORTH_EAST:	return bottom ? NORTH_EAST_BASE : NORTH_EAST_TIP;
					case SOUTH_EAST:	return bottom ? SOUTH_EAST_BASE : SOUTH_EAST_TIP;
					case SOUTH_WEST:	return bottom ? SOUTH_WEST_BASE : SOUTH_WEST_TIP;
					case NORTH_WEST:	return bottom ? NORTH_WEST_BASE : NORTH_WEST_TIP;
					case POINT: 		return POINT_TALL;
				}
			}

			return this;
		}
	}
}
