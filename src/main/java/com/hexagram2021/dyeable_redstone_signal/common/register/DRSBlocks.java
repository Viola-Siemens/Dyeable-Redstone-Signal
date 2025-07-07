package com.hexagram2021.dyeable_redstone_signal.common.register;

import com.hexagram2021.dyeable_redstone_signal.common.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSBlocks {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, MODID);

	private DRSBlocks() {}

	public static final BlockBehaviour.Properties REDSTONE_WIRE_PROPERTIES =
			BlockBehaviour.Properties.of().noCollission().instabreak().pushReaction(PushReaction.DESTROY);

	public static final BlockBehaviour.Properties CONVERTER_PROPERTIES =
			BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY);

	public static final BlockBehaviour.Properties REDSTONE_DYER_PROPERTIES =
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(2.0F);

	public static final DeferredHolder<Block, CommonRedstoneWireBlock> COMMON_REDSTONE_WIRE = REGISTER.register(
		"common_redstone_wire", () -> new CommonRedstoneWireBlock(REDSTONE_WIRE_PROPERTIES)
	);

	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> BLACK_REDSTONE_WIRE = REGISTER.register(
			"black_redstone_wire", () -> new ColorfulRedstoneWireBlock("black", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> BLUE_REDSTONE_WIRE = REGISTER.register(
			"blue_redstone_wire", () -> new ColorfulRedstoneWireBlock("blue", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> BROWN_REDSTONE_WIRE = REGISTER.register(
			"brown_redstone_wire", () -> new ColorfulRedstoneWireBlock("brown", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> CYAN_REDSTONE_WIRE = REGISTER.register(
			"cyan_redstone_wire", () -> new ColorfulRedstoneWireBlock("cyan", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> GRAY_REDSTONE_WIRE = REGISTER.register(
			"gray_redstone_wire", () -> new ColorfulRedstoneWireBlock("gray", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> GREEN_REDSTONE_WIRE = REGISTER.register(
			"green_redstone_wire", () -> new ColorfulRedstoneWireBlock("green", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> LIGHT_BLUE_REDSTONE_WIRE = REGISTER.register(
			"light_blue_redstone_wire", () -> new ColorfulRedstoneWireBlock("light_blue", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> LIGHT_GRAY_REDSTONE_WIRE = REGISTER.register(
			"light_gray_redstone_wire", () -> new ColorfulRedstoneWireBlock("light_gray", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> LIME_REDSTONE_WIRE = REGISTER.register(
			"lime_redstone_wire", () -> new ColorfulRedstoneWireBlock("lime", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> MAGENTA_REDSTONE_WIRE = REGISTER.register(
			"magenta_redstone_wire", () -> new ColorfulRedstoneWireBlock("magenta", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> ORANGE_REDSTONE_WIRE = REGISTER.register(
			"orange_redstone_wire", () -> new ColorfulRedstoneWireBlock("orange", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> PINK_REDSTONE_WIRE = REGISTER.register(
			"pink_redstone_wire", () -> new ColorfulRedstoneWireBlock("pink", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> PURPLE_REDSTONE_WIRE = REGISTER.register(
			"purple_redstone_wire", () -> new ColorfulRedstoneWireBlock("purple", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> RED_REDSTONE_WIRE = REGISTER.register(
			"red_redstone_wire", () -> new ColorfulRedstoneWireBlock("red", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> WHITE_REDSTONE_WIRE = REGISTER.register(
			"white_redstone_wire", () -> new ColorfulRedstoneWireBlock("white", REDSTONE_WIRE_PROPERTIES)
	);
	public static final DeferredHolder<Block, ColorfulRedstoneWireBlock> YELLOW_REDSTONE_WIRE = REGISTER.register(
			"yellow_redstone_wire", () -> new ColorfulRedstoneWireBlock("yellow", REDSTONE_WIRE_PROPERTIES)
	);

	public static RedstoneWireBlock getColorfulRedstoneWire(int index) {
		return switch (index) {
			case 0 -> BLACK_REDSTONE_WIRE.get();
			case 1 -> BLUE_REDSTONE_WIRE.get();
			case 2 -> BROWN_REDSTONE_WIRE.get();
			case 3 -> CYAN_REDSTONE_WIRE.get();
			case 4 -> GRAY_REDSTONE_WIRE.get();
			case 5 -> GREEN_REDSTONE_WIRE.get();
			case 6 -> LIGHT_BLUE_REDSTONE_WIRE.get();
			case 7 -> LIGHT_GRAY_REDSTONE_WIRE.get();
			case 8 -> LIME_REDSTONE_WIRE.get();
			case 9 -> MAGENTA_REDSTONE_WIRE.get();
			case 10 -> ORANGE_REDSTONE_WIRE.get();
			case 11 -> PINK_REDSTONE_WIRE.get();
			case 12 -> PURPLE_REDSTONE_WIRE.get();
			case 13 -> RED_REDSTONE_WIRE.get();
			case 14 -> WHITE_REDSTONE_WIRE.get();
			case 15 -> YELLOW_REDSTONE_WIRE.get();
			default -> COMMON_REDSTONE_WIRE.get();
		};
	}

	public static final DeferredHolder<Block, CommonRedstoneConverter> COMMON_REDSTONE_CONVERTER = REGISTER.register(
			"common_redstone_converter", () -> new CommonRedstoneConverter(CONVERTER_PROPERTIES)
	);

	public static final DeferredHolder<Block, CommonRedstoneRepeater> COMMON_REDSTONE_REPEATER = REGISTER.register(
			"common_redstone_repeater", () -> new CommonRedstoneRepeater(CONVERTER_PROPERTIES)
	);

	public static final DeferredHolder<Block, RedstoneDyerBlock> REDSTONE_DYER = REGISTER.register(
			"redstone_dyer", () -> new RedstoneDyerBlock(REDSTONE_DYER_PROPERTIES)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);

		registerWireItem(COMMON_REDSTONE_WIRE);

		registerWireItem(BLACK_REDSTONE_WIRE);
		registerWireItem(BLUE_REDSTONE_WIRE);
		registerWireItem(BROWN_REDSTONE_WIRE);
		registerWireItem(CYAN_REDSTONE_WIRE);
		registerWireItem(GRAY_REDSTONE_WIRE);
		registerWireItem(GREEN_REDSTONE_WIRE);
		registerWireItem(LIGHT_BLUE_REDSTONE_WIRE);
		registerWireItem(LIGHT_GRAY_REDSTONE_WIRE);
		registerWireItem(LIME_REDSTONE_WIRE);
		registerWireItem(MAGENTA_REDSTONE_WIRE);
		registerWireItem(ORANGE_REDSTONE_WIRE);
		registerWireItem(PINK_REDSTONE_WIRE);
		registerWireItem(PURPLE_REDSTONE_WIRE);
		registerWireItem(RED_REDSTONE_WIRE);
		registerWireItem(WHITE_REDSTONE_WIRE);
		registerWireItem(YELLOW_REDSTONE_WIRE);

		registerItem(COMMON_REDSTONE_CONVERTER);
		registerItem(COMMON_REDSTONE_REPEATER);
		registerItem(REDSTONE_DYER);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static DRSItems.ItemEntry<BlockItem> registerWireItem(DeferredHolder<Block, ? extends Block> block) {
		return DRSItems.ItemEntry.register(block.getId().getPath().replaceAll("_wire", ""), () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
	}

	@SuppressWarnings("UnusedReturnValue")
	private static DRSItems.ItemEntry<BlockItem> registerItem(DeferredHolder<Block, ? extends Block> block) {
		return DRSItems.ItemEntry.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
