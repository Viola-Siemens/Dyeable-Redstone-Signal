package com.hexagram2021.dyeable_redstone_signal.common.register;

import com.hexagram2021.dyeable_redstone_signal.common.crafting.RedstoneDyerMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSContainerTypes {
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

	public static final RegistryObject<MenuType<RedstoneDyerMenu>> REDSTONE_DYER_MENU = REGISTER.register(
			"redstone_dyer", () -> new MenuType<>(RedstoneDyerMenu::new, FeatureFlags.VANILLA_SET)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
