package com.hexagram2021.dyeable_redstone_signal.common.block.entity;

import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

@SuppressWarnings("unused")
public class CommonRedstoneRepeaterBlockEntity extends BlockEntity {

	public CommonRedstoneRepeaterBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(DRSBlockEntities.COMMON_REDSTONE_REPEATER.get(), blockPos, blockState);
	}

	public int getColoredEnergy(int index) {
		return this.colored_energies[index];
	}

	public int[] getColoredEnergies() {
		return this.colored_energies;
	}

	public void setColoredEnergy(int index, int value) {
		this.colored_energies[index] = value;
	}

	public void setColoredEnergies(int[] values) {
		System.arraycopy(values, 0, this.colored_energies, 0, CommonRedstoneWireBlockEntity.COLORS.length);
	}

	public int getMaxEnergy() {
		return Arrays.stream(this.colored_energies).max().orElse(0);
	}
	private int[] colored_energies = Util.make(new int[16], (nums) -> Arrays.fill(nums, 0));

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		nbt.putIntArray("Energies", this.colored_energies);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);

		this.colored_energies = nbt.getIntArray("Energies");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
}
