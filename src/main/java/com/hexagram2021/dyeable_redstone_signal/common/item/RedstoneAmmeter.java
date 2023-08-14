package com.hexagram2021.dyeable_redstone_signal.common.item;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneRepeaterBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneAmmeter extends Item {
	@SuppressWarnings("PointlessArithmeticExpression")
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Player player = context.getPlayer();
		if(!(player instanceof ServerPlayer) || !player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		BlockState blockState = level.getBlockState(blockPos);
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if(blockEntity instanceof CommonRedstoneWireBlockEntity commonRedstoneWireBlockEntity) {
			player.sendSystemMessage(Component.translatable(
					"info.energy.header", blockState.getBlock().getName(), blockPos.getX(), blockPos.getY(), blockPos.getZ()
			));
			for(int i = 0; i < 4; ++i) {
				player.sendSystemMessage(Component.translatable(
						"info.energy.line" + i,
						commonRedstoneWireBlockEntity.getColoredEnergy(i * 4 + 0),
						commonRedstoneWireBlockEntity.getColoredEnergy(i * 4 + 1),
						commonRedstoneWireBlockEntity.getColoredEnergy(i * 4 + 2),
						commonRedstoneWireBlockEntity.getColoredEnergy(i * 4 + 3)
				));
			}
			return InteractionResult.SUCCESS;
		}
		if(blockEntity instanceof CommonRedstoneRepeaterBlockEntity commonRedstoneRepeaterBlockEntity) {
			player.sendSystemMessage(Component.translatable(
					"info.energy.header", blockState.getBlock().getName(), blockPos.getX(), blockPos.getY(), blockPos.getZ()
			));
			for(int i = 0; i < 4; ++i) {
				player.sendSystemMessage(Component.translatable(
						"info.energy.line" + i,
						commonRedstoneRepeaterBlockEntity.getColoredEnergy(i * 4 + 0),
						commonRedstoneRepeaterBlockEntity.getColoredEnergy(i * 4 + 1),
						commonRedstoneRepeaterBlockEntity.getColoredEnergy(i * 4 + 2),
						commonRedstoneRepeaterBlockEntity.getColoredEnergy(i * 4 + 3)
				));
			}
			return InteractionResult.SUCCESS;
		}
		if(blockState.hasProperty(BlockStateProperties.POWER)) {
			player.sendSystemMessage(Component.translatable(
					"info.energy.content",
					blockState.getBlock().getName(),
					blockPos.getX(), blockPos.getY(), blockPos.getZ(),
					blockState.getValue(BlockStateProperties.POWER)
			));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public RedstoneAmmeter(Properties properties) {
		super(properties);
	}
}
