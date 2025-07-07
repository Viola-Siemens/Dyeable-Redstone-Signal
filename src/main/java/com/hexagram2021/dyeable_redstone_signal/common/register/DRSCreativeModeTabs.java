package com.hexagram2021.dyeable_redstone_signal.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

@SuppressWarnings("unused")
public class DRSCreativeModeTabs {
	private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_GROUP = REGISTER.register(
			"item_group", () -> CreativeModeTab.builder()
					.icon(() -> new ItemStack(DRSBlocks.CYAN_REDSTONE_WIRE.get()))
					.title(Component.translatable("itemGroup.dyeable_redstone_signal"))
					.displayItems((flags, output) -> DRSItems.ItemEntry.ALL_ITEMS.forEach(output::accept))
					.build()
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
