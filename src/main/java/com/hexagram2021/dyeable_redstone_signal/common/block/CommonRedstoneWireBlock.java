package com.hexagram2021.dyeable_redstone_signal.common.block;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CommonRedstoneWireBlock extends RedstoneWireBlock implements EntityBlock {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	private static final Vec3[] COLORS = Util.make(new Vec3[16], (vec3s) -> {
		for(int i = 0; i <= 15; ++i) {
			double f = (double)i / 15.0D;
			double f1 = f * 0.5D + (f > 0.0D ? 0.4D : 0.3D);
			double f2 = Mth.clamp(f * f * 0.7D - 0.35D, 0.0D, 1.0D);
			double f3 = Mth.clamp(f * f * 0.6D - 0.4D, 0.0D, 1.0D);
			vec3s[i] = new Vec3(f1, f2, f3);
		}
	});

	public CommonRedstoneWireBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState defaultBlockStateWithPower(BlockState blockState) {
		return this.defaultBlockState().setValue(POWER, blockState.getValue(POWER));
	}

	@Override
	public BlockState crossStateWithPower(BlockState blockState) {
		return super.crossStateWithPower(blockState).setValue(POWER, blockState.getValue(POWER));
	}

	@Override
	protected boolean canConnectWireWith(BlockState blockState) {
		return blockState.hasProperty(ColorfulRedstoneWireBlock.POWER);
	}

	@Override
	protected BlockState getDefaultState() {
		return super.getDefaultState().setValue(POWER, 0);
	}

	@Override
	protected void updatePowerStrength(Level level, BlockPos blockPos, BlockState blockState) {
		int[] signals = this.calculateTargetStrength(level, blockPos);
		boolean signal_changed = false;

		BlockEntity blockEntity = level.getBlockEntity(blockPos);

		if(blockEntity instanceof CommonRedstoneWireBlockEntity commonRedstoneWireBlockEntity) {
			for(int colorIndex = 0; colorIndex < CommonRedstoneWireBlockEntity.COLORS.length; ++colorIndex) {
				if (commonRedstoneWireBlockEntity.getColoredEnergy(colorIndex) != signals[colorIndex]) {
					signal_changed = true;
				}
			}
			if(signal_changed) {
				commonRedstoneWireBlockEntity.setColoredEnergies(signals);
				if (level.getBlockState(blockPos) == blockState) {
					level.setBlock(blockPos, blockState.setValue(POWER, commonRedstoneWireBlockEntity.getMaxEnergy()), 2);
				}

				level.updateNeighborsAt(blockPos, this);
				for(Direction direction : Direction.values()) {
					level.updateNeighborsAt(blockPos.relative(direction), this);
				}
			}
		}
	}

	private int[] calculateTargetStrength(Level level, BlockPos blockPos) {
		int i = getBestNeighborSignal(level, blockPos);
		int[] signals = new int[CommonRedstoneWireBlockEntity.COLORS.length];
		for(int colorIndex = 0; colorIndex < CommonRedstoneWireBlockEntity.COLORS.length; ++colorIndex) {
			int j = 0;
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				BlockPos blockpos = blockPos.relative(direction);
				BlockEntity blockEntity = level.getBlockEntity(blockpos);
				BlockState blockState = level.getBlockState(blockpos);
				j = Math.max(j, getWireSignal(blockEntity, blockState, colorIndex, direction));
				BlockPos blockpos1 = blockPos.above();
				if (blockState.isRedstoneConductor(level, blockpos) &&
						!level.getBlockState(blockpos1).isRedstoneConductor(level, blockpos1)) {
					j = Math.max(j, getWireSignal(level.getBlockEntity(blockpos.above()), level.getBlockState(blockpos.above()), colorIndex, direction));
				} else if (!blockState.isRedstoneConductor(level, blockpos)) {
					j = Math.max(j, getWireSignal(level.getBlockEntity(blockpos.below()), level.getBlockState(blockpos.below()), colorIndex, direction));
				}
			}
			signals[colorIndex] = Math.max(i, j - 1);
		}

		return signals;
	}

	private static int getBestNeighborSignal(Level level, BlockPos blockPos) {
		int i = 0;

		for(Direction direction : Direction.values()) {
			BlockPos curBlockPos = blockPos.relative(direction);
			BlockState blockState = level.getBlockState(curBlockPos);
			if(blockState.is(DRSBlocks.COMMON_REDSTONE_CONVERTER.get())) {
				int j = level.getSignal(curBlockPos, direction);
				if (j >= 15) {
					return 15;
				}

				if (j > i) {
					i = j;
				}
			}
		}

		return i;
	}

	public static int getColorForPower(int energy) {
		Vec3 vec3 = COLORS[energy];
		return Mth.color((float)vec3.x(), (float)vec3.y(), (float)vec3.z());
	}

	@Override
	public int getPower(BlockState blockState) {
		return blockState.getValue(POWER);
	}

	@Override
	protected BlockState setPowerFrom(BlockState blockState, BlockState source) {
		return blockState.setValue(POWER, source.getValue(POWER));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}

	@Override
	protected Vec3 getColor(int energy) {
		return COLORS[energy];
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new CommonRedstoneWireBlockEntity(blockPos, blockState);
	}
}
