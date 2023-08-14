package com.hexagram2021.dyeable_redstone_signal.common.world;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class DRSTrades {
	public static final int DEFAULT_SUPPLY = 12;
	public static final int COMMON_ITEMS_SUPPLY = 16;
	public static final int UNCOMMON_ITEMS_SUPPLY = 3;
	public static final int ONLY_SUPPLY_ONCE = 1;

	public static final int XP_LEVEL_1_SELL = 1;
	public static final int XP_LEVEL_1_BUY = 2;
	public static final int XP_LEVEL_2_SELL = 5;
	public static final int XP_LEVEL_2_BUY = 10;
	public static final int XP_LEVEL_3_SELL = 10;
	public static final int XP_LEVEL_3_BUY = 20;
	public static final int XP_LEVEL_4_SELL = 15;
	public static final int XP_LEVEL_4_BUY = 30;
	public static final int XP_LEVEL_5_TRADE = 30;

	public static final float LOW_TIER_PRICE_MULTIPLIER = 0.05F;
	public static final float HIGH_TIER_PRICE_MULTIPLIER = 0.2F;

	static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int cost;
		private final int numberOfEmerald;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public EmeraldForItems(ItemLike item, int cost, int numberOfEmerald, int maxUses, int Xp) {
			this.item = item.asItem();
			this.cost = cost;
			this.numberOfEmerald = numberOfEmerald;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			ItemStack itemstack = new ItemStack(this.item, this.cost);
			return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD, numberOfEmerald), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public ItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int Xp) {
			this.itemStack = itemStack;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int baseLevel;
		private final int addLevel;
		private final boolean treasure;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public EnchantedItemForEmeralds(Item item, int baseEmeraldCost, int baseLevel, int addLevel, boolean treasure, int maxUses, int Xp) {
			this.itemStack = new ItemStack(item);
			this.baseEmeraldCost = baseEmeraldCost;
			this.baseLevel = baseLevel;
			this.addLevel = addLevel;
			this.treasure = treasure;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			int i = this.baseLevel + rand.nextInt(this.addLevel);
			ItemStack itemstack = EnchantmentHelper.enchantItem(rand, new ItemStack(this.itemStack.getItem()), i, treasure);
			int j = Math.min(this.baseEmeraldCost + i, 64);
			return new MerchantOffer(new ItemStack(Items.EMERALD, j), itemstack, this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack fromItem;
		private final int fromCount;
		private final int emeraldCost;
		private final ItemStack toItem;
		private final int toCount;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public ItemsAndEmeraldsToItems(ItemLike forItem, int fromCount, int emeraldCost, Item toItem, int toCount, int maxUses, int Xp) {
			this.fromItem = new ItemStack(forItem);
			this.fromCount = fromCount;
			this.emeraldCost = emeraldCost;
			this.toItem = new ItemStack(toItem);
			this.toCount = toCount;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}
}
