package com.hexagram2021.dyeable_redstone_signal.common.block;

import java.util.Arrays;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneRepeaterBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class CommonRedstoneRepeater extends DiodeBlock implements EntityBlock {
	public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
	public static final IntegerProperty DELAY = BlockStateProperties.DELAY;

	public CommonRedstoneRepeater(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(DELAY, 1)
				.setValue(LOCKED, Boolean.FALSE)
				.setValue(POWERED, Boolean.FALSE)
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand hand, BlockHitResult hit) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			level.setBlock(blockPos, blockState.cycle(DELAY), 3);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	@Override
	protected void checkTickOnNeighbor(Level level, BlockPos blockPos, BlockState blockState) {
		if (!this.isLocked(level, blockPos, blockState)) {
			boolean flag = blockState.getValue(POWERED);

			boolean update = false;
			if(level.getBlockEntity(blockPos) instanceof CommonRedstoneRepeaterBlockEntity commonRedstoneRepeater) {
				Direction direction = blockState.getValue(FACING);
				BlockPos input = blockPos.relative(direction);
				BlockState inputBlockState = level.getBlockState(input);
				BlockEntity inputBlockEntity = level.getBlockEntity(input);
				if(inputBlockEntity instanceof CommonRedstoneWireBlockEntity commonRedstoneWire) {
					for(int i = 0; i < CommonRedstoneWireBlockEntity.COLORS.length; ++i) {
						if(commonRedstoneRepeater.getColoredEnergy(i) != commonRedstoneWire.getColoredEnergy(i)) {
							update = true;
							break;
						}
					}
				} else if(inputBlockEntity instanceof CommonRedstoneRepeaterBlockEntity inputRepeater) {
					for(int i = 0; i < CommonRedstoneWireBlockEntity.COLORS.length; ++i) {
						if(commonRedstoneRepeater.getColoredEnergy(i) != inputRepeater.getColoredEnergy(i)) {
							update = true;
							break;
						}
					}
				} else if(inputBlockState.getBlock() instanceof ColorfulRedstoneWireBlock colorfulRedstoneWire) {
					int power = inputBlockState.getValue(ColorfulRedstoneWireBlock.POWER);
					for(int i = 0; i < CommonRedstoneWireBlockEntity.COLORS.length; ++i) {
						if(colorfulRedstoneWire.getColorIndex() == i) {
							if(commonRedstoneRepeater.getColoredEnergy(i) != power) {
								update = true;
								break;
							}
						} else if(commonRedstoneRepeater.getColoredEnergy(i) != 0) {
							update = true;
							break;
						}
					}
				} else {
					int power = level.getSignal(input, direction);
					for(int i = 0; i < CommonRedstoneWireBlockEntity.COLORS.length; ++i) {
						if(commonRedstoneRepeater.getColoredEnergy(i) != power) {
							update = true;
							break;
						}
					}
				}
			}

			if (update && !level.getBlockTicks().willTickThisTick(blockPos, this)) {
				TickPriority tickpriority = TickPriority.HIGH;
				if (this.shouldPrioritize(level, blockPos, blockState)) {
					tickpriority = TickPriority.EXTREMELY_HIGH;
				} else if (flag) {
					tickpriority = TickPriority.VERY_HIGH;
				}

				level.scheduleTick(blockPos, this, this.getDelay(blockState), tickpriority);
			}

		}
	}

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
		if (!this.isLocked(level, blockPos, blockState)) {
			boolean flag = blockState.getValue(POWERED);
			boolean flag1 = this.shouldTurnOn(level, blockPos, blockState);
			if (flag && !flag1) {
				if(level.getBlockEntity(blockPos) instanceof CommonRedstoneRepeaterBlockEntity commonRedstoneRepeater) {
					commonRedstoneRepeater.setColoredEnergies(Util.make(new int[16], (nums) -> Arrays.fill(nums, 0)));
				}
				level.setBlock(blockPos, blockState.setValue(POWERED, Boolean.FALSE), 2);
			} else {
				if(level.getBlockEntity(blockPos) instanceof CommonRedstoneRepeaterBlockEntity commonRedstoneRepeater) {
					Direction direction = blockState.getValue(FACING);
					BlockPos input = blockPos.relative(direction);
					BlockState inputBlockState = level.getBlockState(input);
					BlockEntity inputBlockEntity = level.getBlockEntity(input);
					if(inputBlockEntity instanceof CommonRedstoneWireBlockEntity commonRedstoneWire) {
						commonRedstoneRepeater.setColoredEnergies(commonRedstoneWire.getColoredEnergies());
					} else if(inputBlockEntity instanceof CommonRedstoneRepeaterBlockEntity inputRepeater) {
						commonRedstoneRepeater.setColoredEnergies(inputRepeater.getColoredEnergies());
					} else if(inputBlockState.getBlock() instanceof ColorfulRedstoneWireBlock colorfulRedstoneWire) {
						int[] energies = Util.make(new int[16], (nums) -> Arrays.fill(nums, 0));
						energies[colorfulRedstoneWire.getColorIndex()] = inputBlockState.getValue(ColorfulRedstoneWireBlock.POWER);
						commonRedstoneRepeater.setColoredEnergies(energies);
					} else {
						int[] energies = Util.make(new int[16], (nums) -> Arrays.fill(nums, level.getSignal(input, direction)));
						commonRedstoneRepeater.setColoredEnergies(energies);
					}
				}
				level.setBlock(blockPos, blockState.setValue(POWERED, Boolean.TRUE), 2);
				if (!flag1) {
					level.scheduleTick(blockPos, this, this.getDelay(blockState), TickPriority.VERY_HIGH);
				}
			}
		}
	}

	@Override
	protected int getDelay(BlockState blockState) {
		return blockState.getValue(DELAY) * 2;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = super.getStateForPlacement(context);
		return blockstate.setValue(LOCKED, this.isLocked(context.getLevel(), context.getClickedPos(), blockstate));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighbor, LevelAccessor level,
								  BlockPos blockPos, BlockPos neighborBlockPos) {
		return !level.isClientSide() && direction.getAxis() != blockState.getValue(FACING).getAxis() ?
				blockState.setValue(LOCKED, this.isLocked(level, blockPos, blockState)) :
				super.updateShape(blockState, direction, neighbor, level, blockPos, neighborBlockPos);
	}

	@Override
	public boolean isLocked(LevelReader level, BlockPos blockPos, BlockState blockState) {
		return this.getAlternateSignal(level, blockPos, blockState) > 0;
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
		if (blockState.getValue(POWERED)) {
			Direction direction = blockState.getValue(FACING);
			double d0 = (double)blockPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double)blockPos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double)blockPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			float f = -5.0F;
			if (random.nextBoolean()) {
				f = (float)(blockState.getValue(DELAY) * 2 - 1);
			}

			f /= 16.0F;
			double d3 = f * direction.getStepX();
			double d4 = f * direction.getStepZ();
			level.addParticle(DustParticleOptions.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, DELAY, LOCKED, POWERED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onNeighborChange(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, BlockPos neighbor) {
		if (pos.getY() == neighbor.getY() && world instanceof Level && !world.isClientSide()) {
			state.neighborChanged((Level)world, pos, world.getBlockState(neighbor).getBlock(), neighbor, false);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new CommonRedstoneRepeaterBlockEntity(blockPos, blockState);
	}
}
