package com.hexagram2021.dyeable_redstone_signal.common.register;

import com.google.common.collect.Lists;
import com.hexagram2021.dyeable_redstone_signal.common.item.RedstoneAmmeter;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;
import static com.hexagram2021.dyeable_redstone_signal.common.util.RegistryHelper.getRegistryName;

public class DRSItems {
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MODID);

	public static final ItemEntry<RedstoneAmmeter> REDSTONE_AMMETER = ItemEntry.register(
			"redstone_ammeter", () -> new RedstoneAmmeter(new Item.Properties().stacksTo(1))
	);

	private DRSItems() { }

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}

	public static class ItemEntry<T extends Item> implements Supplier<T>, ItemLike {
		public static final List<ItemEntry<? extends Item>> ALL_ITEMS = Lists.newArrayList();

		private final DeferredHolder<Item, T> regObject;

		private static ItemEntry<Item> simple(String name) {
			return simple(name, $ -> { }, $ -> { });
		}

		private static ItemEntry<Item> simple(String name, Consumer<Item.Properties> makeProps, Consumer<Item> processItem) {
			return register(name, () -> Util.make(new Item(Util.make(new Item.Properties(), makeProps)), processItem));
		}

		static <T extends Item> ItemEntry<T> register(String name, Supplier<? extends T> make) {
			return new ItemEntry<>(REGISTER.register(name, make));
		}

		@SuppressWarnings("SameParameterValue")
		static <T extends Item> ItemEntry<T> register(String name, Supplier<? extends T> make, boolean compat) {
			return new ItemEntry<>(REGISTER.register(name, make));
		}

		private static <T extends Item> ItemEntry<T> of(T existing) {
			return new ItemEntry<>(DeferredHolder.create(Registries.ITEM, getRegistryName(existing)));
		}

		private ItemEntry(DeferredHolder<Item, T> regObject) {
			this.regObject = regObject;
			ALL_ITEMS.add(this);
		}

		@Override
		public T get() {
			return this.regObject.get();
		}

		@Override
		public Item asItem() {
			return this.regObject.get();
		}

		public ResourceLocation getId() {
			return this.regObject.getId();
		}
	}
}
