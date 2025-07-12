package com.hexagram2021.dyeable_redstone_signal.common;

import com.hexagram2021.dyeable_redstone_signal.common.block.RedstoneWireBlock;
import com.hexagram2021.dyeable_redstone_signal.common.register.*;
import com.hexagram2021.dyeable_redstone_signal.common.util.DRSSounds;
import com.hexagram2021.dyeable_redstone_signal.common.world.Villages;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Consumer;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DRSContent {
	public static void modConstruction(IEventBus bus, Consumer<Runnable> runLater) {
		DRSBlocks.init(bus);
		DRSItems.init(bus);
		DRSBlockEntities.init(bus);
		DRSCreativeModeTabs.init(bus);
		DRSContainerTypes.init(bus);
		Villages.Registers.init(bus);
	}

	public static void init() {
		Villages.init();

		CauldronInteraction DYED_REDSTONE = (blockState, level, blockPos, player, hand, itemStack) -> {
			Item item = itemStack.getItem();
			if(item instanceof BlockItem blockItem && blockItem.getBlock() instanceof RedstoneWireBlock) {
				if (!level.isClientSide) {
					if (!player.getAbilities().instabuild) {
						itemStack.shrink(1);
					}
					ItemStack redstone;
					if (item == DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem()) {
						redstone = new ItemStack(Items.REDSTONE, 1);
					} else {
						redstone = new ItemStack(DRSBlocks.COMMON_REDSTONE_WIRE.get(), 1);
					}
					if (itemStack.isEmpty()) {
						player.setItemInHand(hand, redstone);
					} else if (player.getInventory().add(redstone)) {
						player.inventoryMenu.sendAllDataToRemote();
					} else {
						player.drop(redstone, false);
					}
					LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.PASS;
		};
		Map<Item, CauldronInteraction> waterInteraction = CauldronInteraction.WATER.map();
		waterInteraction.put(DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.BLACK_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.BLUE_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.BROWN_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.CYAN_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.GRAY_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.GREEN_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.LIGHT_BLUE_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.LIGHT_GRAY_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.LIME_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.MAGENTA_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.ORANGE_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.PINK_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.PURPLE_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.RED_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.WHITE_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
		waterInteraction.put(DRSBlocks.YELLOW_REDSTONE_WIRE.get().asItem(), DYED_REDSTONE);
	}

	@SubscribeEvent
	public static void onRegister(RegisterEvent event) {
		DRSSounds.init(event);
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				DRSBlockEntities.REDSTONE_DYER.get(),
				(container, side) -> side == null ? new InvWrapper(container) : new SidedInvWrapper(container, side)
		);
	}
}
