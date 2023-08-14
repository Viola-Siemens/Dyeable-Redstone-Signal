package com.hexagram2021.dyeable_redstone_signal.common.crafting;

import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSContainerTypes;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import static com.hexagram2021.dyeable_redstone_signal.common.block.entity.RedstoneDyerBlockEntity.*;

public class RedstoneDyerMenu extends AbstractContainerMenu {
	private static final int INV_SLOT_START = 3;
	private static final int INV_SLOT_END = 30;
	private static final int USE_ROW_SLOT_START = 30;
	private static final int USE_ROW_SLOT_END = 39;
	public static final int SLOT_COUNT = 3;
	public static final int DATA_COUNT = 3;

	private final Container redstoneDyer;
	private final ContainerData redstoneDyerData;
	final Slot inputSlot;
	final Slot dyeSlot;

	public RedstoneDyerMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public RedstoneDyerMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(DRSContainerTypes.REDSTONE_DYER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.redstoneDyer = container;
		this.redstoneDyerData = data;

		this.inputSlot = this.addSlot(new Slot(container, SLOT_INPUT, 120, 15) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.REDSTONE) || itemStack.is(DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem()) || itemStack.is(DRSItemTags.DYED_REDSTONES);
			}

			@Override
			public int getMaxStackSize() {
				return 64;
			}
		});
		this.dyeSlot = this.addSlot(new Slot(container, DYE_INPUT, 31, 15) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Tags.Items.DYES);
			}

			@Override
			public int getMaxStackSize() {
				return 64;
			}
		});
		this.addSlot(new Slot(container, SLOT_RESULT, 120, 49) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return false;
			}

			@Override
			public int getMaxStackSize() {
				return 64;
			}
		});
		this.addDataSlots(data);

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.redstoneDyer.stillValid(player);
	}

	@Override
	public MenuType<?> getType() {
		return DRSContainerTypes.REDSTONE_DYER_MENU.get();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == SLOT_RESULT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index == SLOT_INPUT || index == DYE_INPUT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
				if(this.inputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, SLOT_INPUT, DYE_INPUT, false)) {
						return ItemStack.EMPTY;
					}
				} else if(this.dyeSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, DYE_INPUT, SLOT_RESULT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END) {
				if(this.inputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, SLOT_INPUT, DYE_INPUT, false)) {
						return ItemStack.EMPTY;
					}
				} else if(this.dyeSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, DYE_INPUT, SLOT_RESULT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}

			slot.setChanged();
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
			this.broadcastChanges();
		}

		return itemstack;
	}

	@Override
	public boolean clickMenuButton(Player player, int index) {
		if(index >= 0 && index <= 1) {
			this.redstoneDyerData.set(DATA_TODO, index + 1);
			this.broadcastChanges();
			return true;
		}
		return false;
	}

	public int getFluidLevel() {
		return this.redstoneDyerData.get(DATA_FLUID);
	}

	public int getFluidType() {
		return this.redstoneDyerData.get(DATA_DYE_TYPE);
	}

	public int tickToDo() {
		return this.redstoneDyerData.get(DATA_TODO);
	}
}
