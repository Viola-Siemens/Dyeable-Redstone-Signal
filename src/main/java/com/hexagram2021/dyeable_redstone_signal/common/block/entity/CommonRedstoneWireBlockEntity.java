package com.hexagram2021.dyeable_redstone_signal.common.block.entity;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class CommonRedstoneWireBlockEntity extends BlockEntity {
	private int[] coloredEnergies = Util.make(new int[16], (nums) -> Arrays.fill(nums, 0));

	static final ImmutableMap.Builder<String, Integer> COLOR_INDEX_BUILDER = ImmutableMap.<String, Integer>builder()
			.put("black", 0)
			.put("blue", 1)
			.put("brown", 2)
			.put("cyan", 3)
			.put("gray", 4)
			.put("green", 5)
			.put("light_blue", 6)
			.put("light_gray", 7)
			.put("lime", 8)
			.put("magenta", 9)
			.put("orange", 10)
			.put("pink", 11)
			.put("purple", 12)
			.put("red", 13)
			.put("white", 14)
			.put("yellow", 15);
	public static final Map<String, Integer> COLOR_INDEX = COLOR_INDEX_BUILDER.build();

	public static final String[] COLORS = {
			"black", "blue", "brown", "cyan",
			"gray", "green", "light_blue", "light_gray",
			"lime", "magenta", "orange", "pink",
			"purple", "red", "white", "yellow"
	};

	public CommonRedstoneWireBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(DRSBlockEntities.COMMON_REDSTONE_WIRE.get(), blockPos, blockState);
	}

	public int getColoredEnergy(int index) {
		return this.coloredEnergies[index];
	}

	public int[] getColoredEnergies() {
		return this.coloredEnergies;
	}

	public void setColoredEnergy(int index, int value) {
		this.coloredEnergies[index] = value;
	}

	public void setColoredEnergies(int[] values) {
		System.arraycopy(values, 0, this.coloredEnergies, 0, COLORS.length);
	}

	public int getMaxEnergy() {
		return Arrays.stream(this.coloredEnergies).max().orElse(0);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.saveAdditional(nbt, provider);

		nbt.putIntArray("Energies", this.coloredEnergies);
	}

	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.loadAdditional(nbt, provider);

		this.coloredEnergies = nbt.getIntArray("Energies");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveWithoutMetadata(provider);
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
}
