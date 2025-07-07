package com.hexagram2021.dyeable_redstone_signal.common.register;

import com.hexagram2021.dyeable_redstone_signal.common.crafting.RedstoneDyerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSContainerTypes {
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<RedstoneDyerMenu>> REDSTONE_DYER_MENU = REGISTER.register(
			"redstone_dyer", () -> new MenuType<>(RedstoneDyerMenu::new, FeatureFlags.VANILLA_SET)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
