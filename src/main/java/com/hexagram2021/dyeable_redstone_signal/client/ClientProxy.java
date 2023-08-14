package com.hexagram2021.dyeable_redstone_signal.client;

import com.hexagram2021.dyeable_redstone_signal.client.screens.RedstoneDyerScreen;
import com.hexagram2021.dyeable_redstone_signal.common.CommonProxy;
import com.hexagram2021.dyeable_redstone_signal.common.block.ColorfulRedstoneWireBlock;
import com.hexagram2021.dyeable_redstone_signal.common.block.CommonRedstoneWireBlock;
import com.hexagram2021.dyeable_redstone_signal.common.block.RedstoneWireBlock;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSContainerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {
	public static void modConstruction() {

	}

	@SuppressWarnings("removal")
	private static void setRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.COMMON_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.BLACK_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.BLUE_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.BROWN_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.CYAN_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.GRAY_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.GREEN_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.LIGHT_BLUE_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.LIGHT_GRAY_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.LIME_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.MAGENTA_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.ORANGE_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.PINK_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.PURPLE_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.RED_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.WHITE_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DRSBlocks.YELLOW_REDSTONE_WIRE.get(), RenderType.cutoutMipped());
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register(
				(blockState, level, blockPos, tint) ->
					CommonRedstoneWireBlock.getColorForPower(((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.COMMON_REDSTONE_WIRE.get()
		);
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(0, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.BLACK_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(1, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.BLUE_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(2, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.BROWN_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(3, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.CYAN_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(4, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.GRAY_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(5, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.GREEN_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(6, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.LIGHT_BLUE_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(7, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.LIGHT_GRAY_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(8, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.LIME_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(9, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.MAGENTA_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(10, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.ORANGE_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(11, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.PINK_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(12, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.PURPLE_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
						ColorfulRedstoneWireBlock.getColorForPower(13, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.RED_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(14, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.WHITE_REDSTONE_WIRE.get());
		event.register(
				(blockState, level, blockPos, tint) ->
					ColorfulRedstoneWireBlock.getColorForPower(15, ((RedstoneWireBlock)blockState.getBlock()).getPower(blockState)),
				DRSBlocks.YELLOW_REDSTONE_WIRE.get()
		);
	}

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event) {
		setRenderLayer();
		registerContainersAndScreens();
	}


	private static void registerContainersAndScreens() {
		MenuScreens.register(DRSContainerTypes.REDSTONE_DYER_MENU.get(), RedstoneDyerScreen::new);
	}
}
