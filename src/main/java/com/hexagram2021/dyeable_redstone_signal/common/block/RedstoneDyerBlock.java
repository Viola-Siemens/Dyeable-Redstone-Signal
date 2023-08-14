package com.hexagram2021.dyeable_redstone_signal.common.block;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.RedstoneDyerBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RedstoneDyerBlock extends BaseEntityBlock {

	public RedstoneDyerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override @Nullable
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new RedstoneDyerBlockEntity(blockPos, blockState);
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(blockState.getMenuProvider(level, blockPos));
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createRedstoneDyerTicker(level, type, DRSBlockEntities.REDSTONE_DYER.get());
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createRedstoneDyerTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends RedstoneDyerBlockEntity> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(type, blockEntityType, RedstoneDyerBlockEntity::serverTick);
	}
}
