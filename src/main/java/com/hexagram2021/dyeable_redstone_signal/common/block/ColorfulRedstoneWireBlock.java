package com.hexagram2021.dyeable_redstone_signal.common.block;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class ColorfulRedstoneWireBlock extends RedstoneWireBlock {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	private static final Vec3[][] COLORS = Util.make(new Vec3[16][16], (vec3ss) -> {
		for(int i = 0; i <= 15; ++i) {
			double v = (15.0D - i) / 40.0D;
			vec3ss[0][i] = new Vec3(v, v, v);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			double g = 0.0D;
			double b = v * 0.45D + (v > 0.0D ? 0.45D : 0.35D);
			vec3ss[1][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.5D + 0.2D;
			double g = Math.pow(v, 1.5D) * 0.35D + 0.05D;
			double b = 0.0D;
			vec3ss[2][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = 0.0D;
			double g = v * 0.5D + 0.2D;
			double b = v * 0.5D + 0.2D;
			vec3ss[3][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (i + 1.0D) / 32.0D;
			vec3ss[4][i] = new Vec3(v, v, v);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = 0.0D;
			double g = v * 0.45D + (v > 0.0D ? 0.45D : 0.35D);
			double b = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			vec3ss[5][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = Mth.clamp(v * 0.7D - 0.2D, 0.0D, 1.0D);
			double g = v * v * 0.6D + 0.25D;
			double b = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			vec3ss[6][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (i + 4.0D) / 24.0D;
			vec3ss[7][i] = new Vec3(v, v, v);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = Math.pow(v, 1.5D) * 0.35D + 0.05D;
			double g = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			double b = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			vec3ss[8][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			double g = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			double b = Math.pow(v, 1.5D) * 0.35D + 0.05D;
			vec3ss[9][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.45D + (v > 0.0D ? 0.45D : 0.35D);
			double g = Math.pow(v, 1.5D) * 0.35D + 0.05D;
			double b = 0;
			vec3ss[10][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			double g = Mth.clamp(v * 0.7D - 0.2D, 0.0D, 1.0D);
			double b = v * v * 0.6D + 0.25D;
			vec3ss[11][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.5D + 0.2D;
			double g = 0.0D;
			double b = v * 0.5D + 0.2D;
			vec3ss[12][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.45D + (v > 0.0D ? 0.45D : 0.35D);
			double g = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			double b = 0.0D;
			vec3ss[13][i] = new Vec3(r, g, b);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (i + 9.0D) / 24.0D;
			vec3ss[14][i] = new Vec3(v, v, v);
		}
		for(int i = 0; i <= 15; ++i) {
			double v = (double)i / 15.0D;
			double r = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			double g = v * 0.65D + (v > 0.0D ? 0.35D : 0.25D);
			double b = Mth.clamp(v * v * 0.75D - 0.5D, 0.0D, 1.0D);
			vec3ss[15][i] = new Vec3(r, g, b);
		}
	});

	private final String COLOR_NAME;

	public ColorfulRedstoneWireBlock(String colorName, Properties properties) {
		super(properties);
		this.COLOR_NAME = colorName;
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
		return (blockState.is(this) && ((ColorfulRedstoneWireBlock)(blockState.getBlock())).getColorIndex() == this.getColorIndex()) ||
				blockState.is(DRSBlocks.COMMON_REDSTONE_WIRE.get());
	}

	@Override
	protected BlockState getDefaultState() {
		return super.getDefaultState().setValue(POWER, 0);
	}

	public int getColorIndex() {
		return CommonRedstoneWireBlockEntity.COLOR_INDEX.get(this.COLOR_NAME);
	}

	public String getColorName() {
		return this.COLOR_NAME;
	}

	@Override
	protected void updatePowerStrength(Level level, BlockPos blockPos, BlockState blockState) {
		int strength = this.calculateTargetStrength(level, blockPos);

		if (this.getPower(blockState) != strength) {
			if (level.getBlockState(blockPos) == blockState) {
				level.setBlock(blockPos, blockState.setValue(POWER, strength), 2);
			}

			level.updateNeighborsAt(blockPos, this);
			for(Direction direction : Direction.values()) {
				level.updateNeighborsAt(blockPos.relative(direction), this);
			}
		}
	}

	private int calculateTargetStrength(Level level, BlockPos blockPos) {
		int i = getBestNeighborSignal(level, blockPos);
		int j = 0;
		int color = this.getColorIndex();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos blockpos = blockPos.relative(direction);
			BlockEntity blockEntity = level.getBlockEntity(blockpos);
			BlockState blockState = level.getBlockState(blockpos);
			j = Math.max(j, getWireSignal(blockEntity, blockState, color, direction));
			BlockPos blockpos1 = blockPos.above();
			if (blockState.isRedstoneConductor(level, blockpos) &&
					!level.getBlockState(blockpos1).isRedstoneConductor(level, blockpos1)) {
				j = Math.max(j, getWireSignal(level.getBlockEntity(blockpos.above()), level.getBlockState(blockpos.above()), color, direction));
			} else if (!blockState.isRedstoneConductor(level, blockpos)) {
				j = Math.max(j, getWireSignal(level.getBlockEntity(blockpos.above()), level.getBlockState(blockpos.below()), color, direction));
			}
		}

		return Math.max(i, j - 1);
	}

	private static int getBestNeighborSignal(Level level, BlockPos blockPos) {
		int i = 0;

		for(Direction direction : Direction.values()) {
			BlockPos curBlockPos = blockPos.relative(direction);
			if(level.getBlockState(curBlockPos).is(DRSBlocks.COMMON_REDSTONE_CONVERTER.get())) {
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

	public static int getColorForPower(int colorIndex, int energy) {
		Vec3 vec3 = COLORS[colorIndex][energy];
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
		return COLORS[this.getColorIndex()][energy];
	}

	public int getColoredEnergy(BlockState blockState, int color) {
		return this.getColorIndex() == color ? blockState.getValue(ColorfulRedstoneWireBlock.POWER) : 0;
	}
}
