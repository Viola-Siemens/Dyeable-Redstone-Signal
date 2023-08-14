package com.hexagram2021.dyeable_redstone_signal.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.ticks.TickPriority;

public class CommonRedstoneConverter extends DiodeBlock {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	public CommonRedstoneConverter(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(POWERED, Boolean.FALSE)
				.setValue(POWER, 0));
	}

	@Override
	protected int getDelay(BlockState blockState) {
		return 2;
	}

	@Override
	protected int getOutputSignal(BlockGetter level, BlockPos blockPos, BlockState blockState) {
		BlockState converterBlockState = level.getBlockState(blockPos);
		return converterBlockState.is(this) ? converterBlockState.getValue(POWER) : 0;
	}

	private int calculateOutputSignal(Level level, BlockPos blockPos, BlockState blockState) {
		return this.getInputSignal(level, blockPos, blockState);
	}

	@Override
	protected void checkTickOnNeighbor(Level level, BlockPos blockPos, BlockState blockState) {
		if (!level.getBlockTicks().willTickThisTick(blockPos, this)) {
			int i = this.calculateOutputSignal(level, blockPos, blockState);
			int j = this.getOutputSignal(level, blockPos, blockState);
			if (i != j || blockState.getValue(POWERED) != this.shouldTurnOn(level, blockPos, blockState)) {
				TickPriority tickpriority = this.shouldPrioritize(level, blockPos, blockState) ? TickPriority.HIGH : TickPriority.NORMAL;
				level.scheduleTick(blockPos, this, 2, tickpriority);
			}
		}
	}

	private void refreshOutputState(Level level, BlockPos blockPos, BlockState blockState) {
		int i = this.calculateOutputSignal(level, blockPos, blockState);
		int j = this.getOutputSignal(level, blockPos, blockState);

		if (j != i) {
			boolean shouldTurnOn = this.shouldTurnOn(level, blockPos, blockState);
			boolean powered = blockState.getValue(POWERED);
			if (powered && !shouldTurnOn) {
				level.setBlock(blockPos, blockState.setValue(POWER, i).setValue(POWERED, Boolean.FALSE), 2);
			} else if (!powered && shouldTurnOn) {
				level.setBlock(blockPos, blockState.setValue(POWER, i).setValue(POWERED, Boolean.TRUE), 2);
			}

			this.updateNeighborsInFront(level, blockPos, blockState);
		}

	}

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
		this.refreshOutputState(level, blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWER, POWERED);
	}

	@Override @SuppressWarnings("deprecation")
	public void onNeighborChange(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, BlockPos neighbor) {
		if (pos.getY() == neighbor.getY() && world instanceof Level && !world.isClientSide()) {
			state.neighborChanged((Level)world, pos, world.getBlockState(neighbor).getBlock(), neighbor, false);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(POWER, 0);
	}
}
