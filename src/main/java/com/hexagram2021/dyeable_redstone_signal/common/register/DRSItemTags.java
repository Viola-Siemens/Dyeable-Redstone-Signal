package com.hexagram2021.dyeable_redstone_signal.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSItemTags {
	public static final TagKey<Item> DYED_REDSTONES = create("dyed_redstones");

	@SuppressWarnings("SameParameterValue")
	private static TagKey<Item> create(String name) {
		return TagKey.create(Registries.ITEM, new ResourceLocation(MODID, name));
	}
}
