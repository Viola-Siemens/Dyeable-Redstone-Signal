package com.hexagram2021.dyeable_redstone_signal.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneRepeaterBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.RedstoneDyerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

	public static final RegistryObject<BlockEntityType<CommonRedstoneWireBlockEntity>> COMMON_REDSTONE_WIRE = REGISTER.register(
			"common_redstone_wire", () -> new BlockEntityType<>(
					CommonRedstoneWireBlockEntity::new, ImmutableSet.of(DRSBlocks.COMMON_REDSTONE_WIRE.get()), null
			)
	);
	public static final RegistryObject<BlockEntityType<CommonRedstoneRepeaterBlockEntity>> COMMON_REDSTONE_REPEATER = REGISTER.register(
			"common_redstone_repeater", () -> new BlockEntityType<>(
					CommonRedstoneRepeaterBlockEntity::new, ImmutableSet.of(DRSBlocks.COMMON_REDSTONE_REPEATER.get()), null
			)
	);
	public static final RegistryObject<BlockEntityType<RedstoneDyerBlockEntity>> REDSTONE_DYER = REGISTER.register(
			"redstone_dyer", () -> new BlockEntityType<>(
					RedstoneDyerBlockEntity::new, ImmutableSet.of(DRSBlocks.REDSTONE_DYER.get()), null
			)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
