package com.hexagram2021.dyeable_redstone_signal.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

public interface RegistryHelper {
	static ResourceLocation getRegistryName(Item item) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
	}
	static ResourceLocation getRegistryName(Block block) {
		return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
	}
	static ResourceLocation getRegistryName(VillagerProfession profession) {
		return Objects.requireNonNull(BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
	}
}
