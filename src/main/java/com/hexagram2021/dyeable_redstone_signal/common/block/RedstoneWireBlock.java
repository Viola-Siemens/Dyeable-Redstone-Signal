package com.hexagram2021.dyeable_redstone_signal.common.block;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneRepeaterBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class RedstoneWireBlock extends Block {
	public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
	public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
	public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, NORTH,
			Direction.EAST, EAST,
			Direction.SOUTH, SOUTH,
			Direction.WEST, WEST
	));
	protected static final int H = 1;
	protected static final int W = 3;
	protected static final int E = 13;
	protected static final int N = 3;
	protected static final int S = 13;
	private static final VoxelShape SHAPE_DOT = Block.box(W, 0, N, E, H, S);
	private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(W, 0, 0, E, H, S),
			Direction.SOUTH, Block.box(W, 0, N, E, H, 16),
			Direction.EAST, Block.box(W, 0, N, 16, H, S),
			Direction.WEST, Block.box(0, 0, N, E, H, S)
	));
	private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(W, 0, 0, E, 16, 1)),
			Direction.SOUTH, Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(W, 0, 15, E, 16, 16)),
			Direction.EAST, Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15, 0, N, 16, 16, S)),
			Direction.WEST, Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0, 0, N, 1, 16, S))
	));
	private static final Map<ShapeType, VoxelShape> SHAPES_CACHE = Maps.newHashMap();
	private static final float PARTICLE_DENSITY = 0.2F;
	private final BlockState crossState;

	@SuppressWarnings("FieldMayBeFinal")
	private boolean shouldSignal = true;

	static class ShapeType {
		private final RedstoneSide NORTH;
		private final RedstoneSide EAST;
		private final RedstoneSide SOUTH;
		private final RedstoneSide WEST;

		public ShapeType(RedstoneSide north, RedstoneSide east, RedstoneSide south, RedstoneSide west) {
			this.NORTH = north;
			this.EAST = east;
			this.SOUTH = south;
			this.WEST = west;
		}

		public ShapeType(BlockState blockState) {
			this.NORTH = blockState.getValue(RedstoneWireBlock.NORTH);
			this.EAST = blockState.getValue(RedstoneWireBlock.EAST);
			this.SOUTH = blockState.getValue(RedstoneWireBlock.SOUTH);
			this.WEST = blockState.getValue(RedstoneWireBlock.WEST);
		}

		@Override
		public boolean equals(Object object) {
			if(object instanceof ShapeType shapeType) {
				return this.NORTH == shapeType.NORTH &&
						this.EAST == shapeType.EAST &&
						this.SOUTH == shapeType.SOUTH &&
						this.WEST == shapeType.WEST;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return NORTH.ordinal() * 64 + EAST.ordinal() * 16 + SOUTH.ordinal() * 4 + WEST.ordinal();
		}


		@Nullable
		public RedstoneSide getDirectionRedstoneSide(Direction direction) {
			return switch (direction) {
				case NORTH -> this.NORTH;
				case EAST -> this.EAST;
				case SOUTH -> this.SOUTH;
				case WEST -> this.WEST;
				default -> null;
			};
		}
	}

	private static VoxelShape calculateShape(ShapeType shapeType) {
		VoxelShape voxelshape = SHAPE_DOT;

		for(Direction direction : Direction.Plane.HORIZONTAL) {
			RedstoneSide redstoneside = shapeType.getDirectionRedstoneSide(direction);
			if (redstoneside == RedstoneSide.SIDE) {
				voxelshape = Shapes.or(voxelshape, SHAPES_FLOOR.get(direction));
			} else if (redstoneside == RedstoneSide.UP) {
				voxelshape = Shapes.or(voxelshape, SHAPES_UP.get(direction));
			}
		}

		return voxelshape;
	}

	static {
		for(RedstoneSide north: RedstoneSide.values()) {
			for(RedstoneSide east: RedstoneSide.values()) {
				for(RedstoneSide south: RedstoneSide.values()) {
					for(RedstoneSide west: RedstoneSide.values()) {
						ShapeType shapeType = new ShapeType(north, east, south, west);
						SHAPES_CACHE.put(shapeType, calculateShape(shapeType));
					}
				}
			}
		}
	}

	protected BlockState getDefaultState() {
		return this.stateDefinition.any()
				.setValue(NORTH, RedstoneSide.NONE)
				.setValue(EAST, RedstoneSide.NONE)
				.setValue(SOUTH, RedstoneSide.NONE)
				.setValue(WEST, RedstoneSide.NONE);
	}

	public RedstoneWireBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getDefaultState());
		this.crossState = this.defaultBlockState()
				.setValue(NORTH, RedstoneSide.SIDE)
				.setValue(EAST, RedstoneSide.SIDE)
				.setValue(SOUTH, RedstoneSide.SIDE)
				.setValue(WEST, RedstoneSide.SIDE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return SHAPES_CACHE.get(new ShapeType(blockState));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.getConnectionState(context.getLevel(), this.crossState, context.getClickedPos());
	}

	public BlockState defaultBlockStateWithPower(BlockState blockState) {
		return this.defaultBlockState();
	}

	public BlockState crossStateWithPower(BlockState blockState) {
		return this.crossState;
	}

	private BlockState getConnectionState(BlockGetter level, BlockState blockState, BlockPos blockPos) {
		boolean flag = isDot(blockState);
		blockState = this.getMissingConnections(level, this.defaultBlockStateWithPower(blockState), blockPos);
		if (flag && isDot(blockState)) {
			return blockState;
		}
		boolean north_connected = blockState.getValue(NORTH).isConnected();
		boolean south_connected = blockState.getValue(SOUTH).isConnected();
		boolean east_connected = blockState.getValue(EAST).isConnected();
		boolean west_connected = blockState.getValue(WEST).isConnected();
		boolean z_unconnected = !north_connected && !south_connected;
		boolean x_unconnected = !east_connected && !west_connected;
		if (!west_connected && z_unconnected) {
			blockState = blockState.setValue(WEST, RedstoneSide.SIDE);
		}

		if (!east_connected && z_unconnected) {
			blockState = blockState.setValue(EAST, RedstoneSide.SIDE);
		}

		if (!north_connected && x_unconnected) {
			blockState = blockState.setValue(NORTH, RedstoneSide.SIDE);
		}

		if (!south_connected && x_unconnected) {
			blockState = blockState.setValue(SOUTH, RedstoneSide.SIDE);
		}

		return blockState;
	}

	private BlockState getMissingConnections(BlockGetter level, BlockState blockState, BlockPos blockPos) {
		boolean flag = !level.getBlockState(blockPos.above()).isRedstoneConductor(level, blockPos);

		for(Direction direction : Direction.Plane.HORIZONTAL) {
			if (!blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected()) {
				RedstoneSide redstoneside = this.getConnectingSide(level, blockPos, direction, flag);
				blockState = blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside);
			}
		}

		return blockState;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighbor, LevelAccessor level,
								  BlockPos blockPos, BlockPos neighborBlockPos) {
		if (direction == Direction.DOWN) {
			return blockState;
		}
		if (direction == Direction.UP) {
			return this.getConnectionState(level, blockState, blockPos);
		}
		RedstoneSide redstoneside = this.getConnectingSide(level, blockPos, direction);
		return redstoneside.isConnected() == blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() &&
				!isCross(blockState) ? blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside) :
					this.getConnectionState(
							level,
							this.crossStateWithPower(blockState).setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside),
							blockPos
					);
	}

	private static boolean isCross(BlockState blockState) {
		return blockState.getValue(NORTH).isConnected() &&
				blockState.getValue(SOUTH).isConnected() &&
				blockState.getValue(EAST).isConnected() &&
				blockState.getValue(WEST).isConnected();
	}

	private static boolean isDot(BlockState blockState) {
		return !blockState.getValue(NORTH).isConnected() &&
				!blockState.getValue(SOUTH).isConnected() &&
				!blockState.getValue(EAST).isConnected() &&
				!blockState.getValue(WEST).isConnected();
	}

	protected abstract boolean canConnectWireWith(BlockState blockState);

	@SuppressWarnings("deprecation")
	@Override
	public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor level, BlockPos blockPos, int update, int limit) {
		BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

		for(Direction direction : Direction.Plane.HORIZONTAL) {
			RedstoneSide redstoneside = blockState.getValue(PROPERTY_BY_DIRECTION.get(direction));
			BlockState curBlockState = level.getBlockState(mutableblockpos.setWithOffset(blockPos, direction));
			if (redstoneside != RedstoneSide.NONE && !canConnectWireWith(curBlockState)) {
				mutableblockpos.move(Direction.DOWN);
				BlockState blockstate = level.getBlockState(mutableblockpos);
				if (!blockstate.is(Blocks.OBSERVER)) {
					BlockPos blockpos = mutableblockpos.relative(direction.getOpposite());
					BlockState blockstate1 = blockstate.updateShape(
							direction.getOpposite(), level.getBlockState(blockpos), level, mutableblockpos, blockpos
					);
					updateOrDestroy(blockstate, blockstate1, level, mutableblockpos, update, limit);
				}

				mutableblockpos.setWithOffset(blockPos, direction).move(Direction.UP);
				BlockState blockstate3 = level.getBlockState(mutableblockpos);
				if (!blockstate3.is(Blocks.OBSERVER)) {
					BlockPos blockpos1 = mutableblockpos.relative(direction.getOpposite());
					BlockState blockstate2 = blockstate3.updateShape(
							direction.getOpposite(), level.getBlockState(blockpos1), level, mutableblockpos, blockpos1
					);
					updateOrDestroy(blockstate3, blockstate2, level, mutableblockpos, update, limit);
				}
			}
		}

	}

	private RedstoneSide getConnectingSide(BlockGetter level, BlockPos blockPos, Direction direction) {
		return this.getConnectingSide(
				level, blockPos, direction,
				!level.getBlockState(blockPos.above()).isRedstoneConductor(level, blockPos)
		);
	}

	private RedstoneSide getConnectingSide(BlockGetter level, BlockPos blockPos, Direction direction, boolean goUp) {
		BlockPos blockpos = blockPos.relative(direction);
		BlockState blockstate = level.getBlockState(blockpos);
		if (goUp) {
			boolean flag = this.canSurviveOn(level, blockpos, blockstate);
			if (flag && this.shouldConnectTo(level.getBlockState(blockpos.above()), null)) {
				if (blockstate.isFaceSturdy(level, blockpos, direction.getOpposite())) {
					return RedstoneSide.UP;
				}

				return RedstoneSide.SIDE;
			}
		}

		if (this.shouldConnectTo(blockstate, direction)) {
			return RedstoneSide.SIDE;
		}
		if (blockstate.isRedstoneConductor(level, blockpos)) {
			return RedstoneSide.NONE;
		}
		BlockPos blockPosBelow = blockpos.below();
		return this.shouldConnectTo(level.getBlockState(blockPosBelow), null) ?RedstoneSide.SIDE : RedstoneSide.NONE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
		BlockPos below = blockPos.below();
		BlockState belowBlockState = level.getBlockState(below);
		return this.canSurviveOn(level, below, belowBlockState);
	}

	private boolean canSurviveOn(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) || blockState.is(Blocks.HOPPER);
	}

	protected abstract void updatePowerStrength(Level level, BlockPos blockPos, BlockState blockState);

	private void checkCornerChangeAt(Level level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		if (canConnectWireWith(blockState)) {
			level.updateNeighborsAt(blockPos, this);

			for(Direction direction : Direction.values()) {
				level.updateNeighborsAt(blockPos.relative(direction), this);
			}

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState neighbor, boolean update) {
		if (!neighbor.is(blockState.getBlock()) && !neighbor.is(DRSBlocks.COMMON_REDSTONE_WIRE.get()) && !level.isClientSide) {
			this.updatePowerStrength(level, blockPos, blockState);

			for(Direction direction : Direction.Plane.VERTICAL) {
				level.updateNeighborsAt(blockPos.relative(direction), this);
			}

			this.updateNeighborsOfNeighboringWires(level, blockPos);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState neighbor, boolean update) {
		if (!update && !blockState.is(neighbor.getBlock()) && !neighbor.is(DRSBlocks.COMMON_REDSTONE_WIRE.get())) {
			super.onRemove(blockState, level, blockPos, neighbor, false);
			if (!level.isClientSide) {
				for(Direction direction : Direction.values()) {
					level.updateNeighborsAt(blockPos.relative(direction), this);
				}

				this.updatePowerStrength(level, blockPos, blockState);
				this.updateNeighborsOfNeighboringWires(level, blockPos);
			}
		}
	}

	private void updateNeighborsOfNeighboringWires(Level level, BlockPos blockPos) {
		for(Direction direction : Direction.Plane.HORIZONTAL) {
			this.checkCornerChangeAt(level, blockPos.relative(direction));
		}

		for(Direction direction1 : Direction.Plane.HORIZONTAL) {
			BlockPos blockpos = blockPos.relative(direction1);
			if (level.getBlockState(blockpos).isRedstoneConductor(level, blockpos)) {
				this.checkCornerChangeAt(level, blockpos.above());
			} else {
				this.checkCornerChangeAt(level, blockpos.below());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighborBlockPos, boolean update) {
		if (!level.isClientSide) {
			if (blockState.canSurvive(level, blockPos)) {
				this.updatePowerStrength(level, blockPos, blockState);
			} else {
				dropResources(blockState, level, blockPos);
				level.removeBlock(blockPos, false);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getDirectSignal(BlockState blockState, BlockGetter level, BlockPos blockPos, Direction direction) {
		return !this.shouldSignal ? 0 : blockState.getSignal(level, blockPos, direction);
	}

	public abstract int getPower(BlockState blockState);

	protected BlockState setPowerFrom(BlockState blockState, BlockState source) {
		return blockState;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter level, BlockPos blockPos, Direction direction) {
		if (this.shouldSignal && direction != Direction.DOWN) {
			int i = this.getPower(blockState);
			if (i == 0) {
				return 0;
			}
			return direction != Direction.UP &&
					!this.getConnectionState(level, blockState, blockPos)
							.getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite())).isConnected() ? 0 : i;
		}
		return 0;
	}

	protected boolean shouldConnectTo(BlockState other, @Nullable Direction direction) {
		if (other.is(DRSBlocks.COMMON_REDSTONE_WIRE.get())) {
			return true;
		}
		if(this instanceof CommonRedstoneWireBlock && other.getBlock() instanceof ColorfulRedstoneWireBlock) {
			return true;
		}
		if(this instanceof ColorfulRedstoneWireBlock colorfulRedstone1 &&
				other.getBlock() instanceof ColorfulRedstoneWireBlock colorfulRedstone2) {
			return colorfulRedstone1.getColorIndex() == colorfulRedstone2.getColorIndex();
		}
		if (other.is(Blocks.REPEATER) || other.is(DRSBlocks.COMMON_REDSTONE_REPEATER.get())) {
			Direction facing = other.getValue(RepeaterBlock.FACING);
			return facing == direction || facing.getOpposite() == direction;
		}
		if (other.is(Blocks.OBSERVER)) {
			return direction == other.getValue(ObserverBlock.FACING);
		}
		return other.isSignalSource() && direction != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState blockState) {
		return this.shouldSignal;
	}

	private void spawnParticlesAlongLine(Level level, RandomSource random, BlockPos blockPos, Vec3 vec3, Direction direction1, Direction direction2, float begin, float end) {
		float f = end - begin;
		if (!(random.nextFloat() >= PARTICLE_DENSITY * f)) {
			float f1 = 0.4375F;
			float f2 = begin + f * random.nextFloat();
			double d0 = 0.5D + (double)(f1 * (float)direction1.getStepX()) + (double)(f2 * (float)direction2.getStepX());
			double d1 = 0.5D + (double)(f1 * (float)direction1.getStepY()) + (double)(f2 * (float)direction2.getStepY());
			double d2 = 0.5D + (double)(f1 * (float)direction1.getStepZ()) + (double)(f2 * (float)direction2.getStepZ());
			level.addParticle(
					new DustParticleOptions(vec3.toVector3f(), 1.0F),
					(double)blockPos.getX() + d0, (double)blockPos.getY() + d1, (double)blockPos.getZ() + d2, 0.0D, 0.0D, 0.0D
			);
		}
	}

	protected abstract Vec3 getColor(int energy);

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
		int i = this.getPower(blockState);
		Vec3 color = this.getColor(i);
		if (i != 0) {
			for(Direction direction : Direction.Plane.HORIZONTAL) {
				RedstoneSide redstoneside = blockState.getValue(PROPERTY_BY_DIRECTION.get(direction));
				switch(redstoneside) {
					case UP:
						this.spawnParticlesAlongLine(level, random, blockPos, color, direction, Direction.UP, -0.5F, 0.5F);
					case SIDE:
						this.spawnParticlesAlongLine(level, random, blockPos, color, Direction.DOWN, direction, 0.0F, 0.5F);
						break;
					case NONE:
					default:
						this.spawnParticlesAlongLine(level, random, blockPos, color, Direction.DOWN, direction, 0.0F, 0.3F);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 -> blockState
					.setValue(NORTH, blockState.getValue(SOUTH))
					.setValue(EAST, blockState.getValue(WEST))
					.setValue(SOUTH, blockState.getValue(NORTH))
					.setValue(WEST, blockState.getValue(EAST));
			case COUNTERCLOCKWISE_90 -> blockState
					.setValue(NORTH, blockState.getValue(EAST))
					.setValue(EAST, blockState.getValue(SOUTH))
					.setValue(SOUTH, blockState.getValue(WEST))
					.setValue(WEST, blockState.getValue(NORTH));
			case CLOCKWISE_90 -> blockState
					.setValue(NORTH, blockState.getValue(WEST))
					.setValue(EAST, blockState.getValue(NORTH))
					.setValue(SOUTH, blockState.getValue(EAST))
					.setValue(WEST, blockState.getValue(SOUTH));
			default -> blockState;
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
			case FRONT_BACK -> blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
			default -> super.mirror(blockState, mirror);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand interactionHand, BlockHitResult result) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		}
		if (isCross(blockState) || isDot(blockState)) {
			BlockState nextState = isCross(blockState) ? this.defaultBlockState() : this.crossState;
			nextState = this.setPowerFrom(nextState, blockState);
			nextState = this.getConnectionState(level, nextState, blockPos);
			if (nextState != blockState) {
				level.setBlock(blockPos, nextState, 3);
				this.updatesOnShapeChange(level, blockPos, blockState, nextState);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	private void updatesOnShapeChange(Level level, BlockPos blockPos, BlockState blockState, BlockState neighbor) {
		for(Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos relative = blockPos.relative(direction);
			if (blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != neighbor.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() &&
					level.getBlockState(relative).isRedstoneConductor(level, relative)) {
				level.updateNeighborsAtExceptFromFacing(relative, neighbor.getBlock(), direction.getOpposite());
			}
		}
	}

	protected static int getWireSignal(@Nullable BlockEntity blockEntity, BlockState blockState, int colorIndex, Direction direction) {
		if(blockEntity instanceof CommonRedstoneWireBlockEntity wireBlockEntity) {
			return wireBlockEntity.getColoredEnergy(colorIndex);
		}
		if(blockEntity instanceof CommonRedstoneRepeaterBlockEntity repeaterBlockEntity) {
			if(direction == blockState.getValue(CommonRedstoneRepeater.FACING)) {
				return repeaterBlockEntity.getColoredEnergy(colorIndex) == 0 ? 0 : 16;
			}
			return 0;
		}
		return blockState.is(DRSBlocks.getColorfulRedstoneWire(colorIndex)) ?
				((ColorfulRedstoneWireBlock)(blockState.getBlock())).getColoredEnergy(blockState, colorIndex) : 0;
	}
}
