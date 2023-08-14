package com.hexagram2021.dyeable_redstone_signal.common.world;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSItems;
import com.hexagram2021.dyeable_redstone_signal.common.util.DRSSounds;
import com.hexagram2021.dyeable_redstone_signal.mixin.HeroGiftsTaskAccess;
import com.hexagram2021.dyeable_redstone_signal.mixin.StructureTemplatePoolAccess;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;
import static com.hexagram2021.dyeable_redstone_signal.common.util.RegistryHelper.getRegistryName;

public class Villages {
	public static final ResourceLocation ENERGY_RESEARCHER = new ResourceLocation(MODID, "energy_researcher");

	public static void init() {
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_ENERGY_RESEARCHER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/energy_researcher_gift"));
	}

	public static void addAllStructuresToPool(RegistryAccess registryAccess) {
		addToPool(new ResourceLocation("village/plains/houses"), new ResourceLocation(MODID, "village/houses/plains_energy_researcher"), 5, registryAccess);
	}

	@SuppressWarnings("SameParameterValue")
	private static void addToPool(ResourceLocation poolName, ResourceLocation toAdd, int weight, RegistryAccess registryAccess) {
		Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
		StructureTemplatePoolAccess pool = (StructureTemplatePoolAccess) Objects.requireNonNull(registry.get(poolName), poolName.getPath());
		List<Pair<StructurePoolElement, Integer>> rawTemplates = pool.getRawTemplates() instanceof ArrayList ?
				pool.getRawTemplates() : new ArrayList<>(pool.getRawTemplates());

		SinglePoolElement addedElement = SinglePoolElement.single(toAdd.toString()).apply(StructureTemplatePool.Projection.RIGID);
		rawTemplates.add(Pair.of(addedElement, weight));
		pool.getTemplates().add(addedElement);

		pool.setRawTemplates(rawTemplates);
	}

	@SuppressWarnings("SameParameterValue")
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registers {
		public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister.create(ForgeRegistries.POI_TYPES, MODID);
		public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MODID);

		public static final RegistryObject<PoiType> POI_REDSTONE_DYER = POINTS_OF_INTEREST.register(
				"redstone_dyer", () -> createPOI(assembleStates(DRSBlocks.REDSTONE_DYER.get()))
		);
		public static final RegistryObject<VillagerProfession> PROF_ENERGY_RESEARCHER = PROFESSIONS.register(
				ENERGY_RESEARCHER.getPath(), () -> createProf(ENERGY_RESEARCHER, POI_REDSTONE_DYER::getKey, DRSSounds.VILLAGER_WORK_ENERGY_RESEARCHER)
		);

		private static Collection<BlockState> assembleStates(Block block) {
			return block.getStateDefinition().getPossibleStates();
		}

		private static PoiType createPOI(Collection<BlockState> block) {
			return new PoiType(ImmutableSet.copyOf(block), 1, 1);
		}

		private static VillagerProfession createProf(ResourceLocation name, Supplier<ResourceKey<PoiType>> poi, SoundEvent sound) {
			ResourceKey<PoiType> poiName = poi.get();
			return new VillagerProfession(
					name.toString(),
					(p) -> p.is(poiName),
					(p) -> p.is(poiName),
					ImmutableSet.of(),
					ImmutableSet.of(),
					sound
			);
		}

		public static void init(IEventBus bus) {
			Villages.Registers.POINTS_OF_INTEREST.register(bus);
			Villages.Registers.PROFESSIONS.register(bus);
		}
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class Events {
		@SubscribeEvent
		public static void registerTrades(VillagerTradesEvent event) {
			Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

			ResourceLocation currentVillagerProfession = getRegistryName(event.getType());
			if (ENERGY_RESEARCHER.equals(currentVillagerProfession)) {
				trades.get(1).add(new DRSTrades.EmeraldForItems(Items.REDSTONE, 4, 1, DRSTrades.COMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new DRSTrades.EmeraldForItems(Items.COBBLESTONE, 24, 1, DRSTrades.COMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new DRSTrades.EmeraldForItems(Items.QUARTZ, 8, 1, DRSTrades.COMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.BLACK_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.BLUE_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.BROWN_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.CYAN_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.GRAY_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.GREEN_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.LIGHT_BLUE_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.LIGHT_GRAY_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.LIME_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.MAGENTA_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.ORANGE_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.PINK_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.PURPLE_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.RED_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.WHITE_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.YELLOW_DYE), 1, 2, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.BLACK_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.BLUE_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.BROWN_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.CYAN_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.GRAY_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.GREEN_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.LIGHT_BLUE_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.LIGHT_GRAY_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.LIME_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.MAGENTA_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.ORANGE_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.PINK_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.PURPLE_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.RED_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.WHITE_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new DRSTrades.ItemsAndEmeraldsToItems(Items.REDSTONE, 4, 1, DRSBlocks.YELLOW_REDSTONE_WIRE.get().asItem(), 4, DRSTrades.DEFAULT_SUPPLY, DRSTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new DRSTrades.ItemsForEmeralds(new ItemStack(DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem()), 1, 2, DRSTrades.COMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new DRSTrades.EmeraldForItems(Items.BONE_MEAL, 32, 1, DRSTrades.COMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_4_BUY));
				trades.get(5).add(new DRSTrades.ItemsForEmeralds(new ItemStack(Items.WATER_BUCKET), 1, 1, DRSTrades.UNCOMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new DRSTrades.EnchantedItemForEmeralds(Items.DIAMOND_HOE, 4, 15, 10, true, DRSTrades.UNCOMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new DRSTrades.ItemsForEmeralds(new ItemStack(DRSItems.REDSTONE_AMMETER.get()), 3, 1, DRSTrades.UNCOMMON_ITEMS_SUPPLY, DRSTrades.XP_LEVEL_5_TRADE));
			}
		}
	}
}
