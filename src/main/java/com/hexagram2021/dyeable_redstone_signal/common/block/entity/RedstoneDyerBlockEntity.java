package com.hexagram2021.dyeable_redstone_signal.common.block.entity;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.dyeable_redstone_signal.common.block.ColorfulRedstoneWireBlock;
import com.hexagram2021.dyeable_redstone_signal.common.crafting.RedstoneDyerMenu;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlockEntities;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSBlocks;
import com.hexagram2021.dyeable_redstone_signal.common.register.DRSItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class RedstoneDyerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
	public static final int SLOT_INPUT = 0;
	public static final int DYE_INPUT = 1;
	public static final int SLOT_RESULT = 2;

	public static final int DATA_FLUID = 0;
	public static final int DATA_DYE_TYPE = 1;
	public static final int DATA_TODO = 2;

	public static final int FLUID_PER_DYE = 5;
	public static final int MAX_FLUID_LEVEL = 50;

	private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT, DYE_INPUT};
	private static final int[] SLOTS_FOR_SIDES = new int[]{DYE_INPUT, SLOT_INPUT};
	private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT};

	int fluid;
	int dye;
	int todo;

	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case DATA_FLUID -> RedstoneDyerBlockEntity.this.fluid;
				case DATA_DYE_TYPE -> RedstoneDyerBlockEntity.this.dye;
				case DATA_TODO -> RedstoneDyerBlockEntity.this.todo;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_FLUID -> RedstoneDyerBlockEntity.this.fluid = value;
				case DATA_DYE_TYPE -> RedstoneDyerBlockEntity.this.dye = value;
				case DATA_TODO -> RedstoneDyerBlockEntity.this.todo = value;
			}

		}

		public int getCount() {
			return RedstoneDyerMenu.DATA_COUNT;
		}
	};

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.redstone_dyer");
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public RedstoneDyerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(DRSBlockEntities.REDSTONE_DYER.get(), blockPos, blockState);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.fluid = nbt.getInt("Fluid");
		this.dye = nbt.getInt("DyeColor");
		this.todo = nbt.getInt("ToDo");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("Fluid", this.fluid);
		nbt.putInt("DyeColor", this.dye);
		nbt.putInt("ToDo", this.todo);
		ContainerHelper.saveAllItems(nbt, this.items);
	}

	/*
		0: black
		1: blue
		2: brown
		3: cyan
		4: gray
		5: green
		6: light_blue
		7: light_gray
		8: lime
		9: magenta
		10: orange
		11: pink
		12: purple
		13: red
		14: white
		15: yellow;
	 */

	public static final byte[][] COLOR_MIX = {
	//			0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15
	/*  0 */  { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  4, -1 },
	/*  1 */  {-1,  1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1, 12,  6, -1 },
	/*  2 */  {-1, -1,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	/*  3 */  {-1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	/*  4 */  {-1, -1, -1, -1,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1,  7, -1 },
	/*  5 */  {-1,  3, -1, -1, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1,  8, -1 },
	/*  6 */  {-1, -1, -1, -1, -1, -1,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	/*  7 */  {-1, -1, -1, -1, -1, -1, -1,  7, -1, -1, -1, -1, -1, -1, -1, -1 },
	/*  8 */  {-1, -1, -1, -1, -1, -1, -1, -1,  8, -1, -1, -1, -1, -1, -1, -1 },
	/*  9 */  {-1, -1, -1, -1, -1, -1, -1, -1, -1,  9, -1, -1, -1, -1, -1, -1 },
	/* 10 */  {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1, -1 },
	/* 11 */  {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11,  9, -1, -1, -1 },
	/* 12 */  {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  9, 12, -1, -1, -1 },
	/* 13 */  {-1, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 13, 11, 10 },
	/* 14 */  { 4,  6, -1, -1,  7,  8, -1, -1, -1, -1, -1, -1, -1, 11, 14, -1 },
	/* 15 */  {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, 15 },
	};

	public static final Map<DyeColor, Integer> DYE_INDEX = ImmutableMap.<DyeColor, Integer>builder()
			.put(DyeColor.BLACK, 0)
			.put(DyeColor.BLUE, 1)
			.put(DyeColor.BROWN, 2)
			.put(DyeColor.CYAN, 3)
			.put(DyeColor.GRAY, 4)
			.put(DyeColor.GREEN, 5)
			.put(DyeColor.LIGHT_BLUE, 6)
			.put(DyeColor.LIGHT_GRAY, 7)
			.put(DyeColor.LIME, 8)
			.put(DyeColor.MAGENTA, 9)
			.put(DyeColor.ORANGE, 10)
			.put(DyeColor.PINK, 11)
			.put(DyeColor.PURPLE, 12)
			.put(DyeColor.RED, 13)
			.put(DyeColor.WHITE, 14)
			.put(DyeColor.YELLOW, 15)
			.build();
	public static final DyeColor[] DYE_COLORS = {
			DyeColor.BLACK, DyeColor.BLUE, DyeColor.BROWN, DyeColor.CYAN,
			DyeColor.GRAY, DyeColor.GREEN, DyeColor.LIGHT_BLUE, DyeColor.LIGHT_GRAY,
			DyeColor.LIME, DyeColor.MAGENTA, DyeColor.ORANGE, DyeColor.PINK,
			DyeColor.PURPLE, DyeColor.RED, DyeColor.WHITE, DyeColor.YELLOW
	};

	public static void tryMixDye(RedstoneDyerBlockEntity blockEntity, int fuelIndex, ItemStack fuel) {
		if(blockEntity.fluid % FLUID_PER_DYE == 0 && blockEntity.fluid * 2 <= MAX_FLUID_LEVEL) {
			int count = blockEntity.fluid / FLUID_PER_DYE;
			if(count <= fuel.getCount()) {
				int mixIndex = COLOR_MIX[blockEntity.dye][fuelIndex];
				if(mixIndex < 0) {
					return;
				}
				fuel.shrink(count);
				blockEntity.dye = mixIndex;
				blockEntity.fluid += FLUID_PER_DYE * count;
			}
		}
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, RedstoneDyerBlockEntity blockEntity) {
		ItemStack fuel = blockEntity.items.get(DYE_INPUT);
		if(!fuel.isEmpty()) {
			DyeColor current = DyeColor.getColor(fuel);
			if(DYE_INDEX.containsKey(current)) {
				int fuelIndex = DYE_INDEX.get(current);
				if(blockEntity.fluid <= 0) {
					fuel.shrink(1);
					blockEntity.dye = fuelIndex;
					blockEntity.fluid += FLUID_PER_DYE;
				} else if (fuelIndex == blockEntity.dye) {
					if(blockEntity.fluid + FLUID_PER_DYE <= MAX_FLUID_LEVEL) {
						fuel.shrink(1);
						blockEntity.fluid += FLUID_PER_DYE;
					}
				} else {	//Mixing Dye Color
					tryMixDye(blockEntity, fuelIndex, fuel);
				}
			}
		}

		ItemStack ingredient = blockEntity.items.get(SLOT_INPUT);
		ItemStack result = blockEntity.items.get(SLOT_RESULT);
		if(!ingredient.isEmpty() && blockEntity.fluid > 0) {
			if(ingredient.is(DRSItemTags.DYED_REDSTONES) && ingredient.getItem() instanceof BlockItem blockItem &&
					blockItem.getBlock() instanceof ColorfulRedstoneWireBlock wireBlock) {
				int mixIndex = COLOR_MIX[blockEntity.dye][wireBlock.getColorIndex()];
				if(mixIndex >= 0) {
					Item redstone = DRSBlocks.getColorfulRedstoneWire(mixIndex).asItem();

					if(result.isEmpty()) {
						ingredient.shrink(1);
						blockEntity.fluid -= 1;
						blockEntity.items.set(SLOT_RESULT, new ItemStack(redstone));
					} else if(result.is(redstone)) {
						ingredient.shrink(1);
						blockEntity.fluid -= 1;
						result.grow(1);
					}
				}
			} else if(ingredient.is(DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem()) || ingredient.is(Items.REDSTONE)) {
				Item redstone = DRSBlocks.getColorfulRedstoneWire(blockEntity.dye).asItem();

				if(result.isEmpty()) {
					ingredient.shrink(1);
					blockEntity.fluid -= 1;
					blockEntity.items.set(SLOT_RESULT, new ItemStack(redstone));
				} else if(result.is(redstone)) {
					ingredient.shrink(1);
					blockEntity.fluid -= 1;
					result.grow(1);
				}
			}
		}

		switch (blockEntity.todo) {
			case 1 -> {
				if (blockEntity.fluid <= 0) {
					blockEntity.fluid = 0;
					blockEntity.todo = 0;
				}
				blockEntity.fluid -= 1;
				if (blockEntity.fluid % FLUID_PER_DYE == 0) {
					blockEntity.todo = 0;
				}
			}
			case 2 -> {
				if (blockEntity.fluid <= 0) {
					blockEntity.fluid = 0;
					blockEntity.todo = 0;
				}
				blockEntity.fluid -= 1;
				if (blockEntity.fluid == 0) {
					blockEntity.todo = 0;
				}
			}
		}
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if(direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		if(direction == Direction.UP) {
			return SLOTS_FOR_UP;
		}
		return SLOTS_FOR_SIDES;
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void fillStackedContents(StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.items, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.items, index);
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		return switch (index) {
			case SLOT_INPUT -> itemStack.is(Items.REDSTONE) || itemStack.is(DRSBlocks.COMMON_REDSTONE_WIRE.get().asItem()) || itemStack.is(DRSItemTags.DYED_REDSTONES);
			case DYE_INPUT -> itemStack.is(Tags.Items.DYES);
			default -> false;
		};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
		return true;
	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
			if (facing == Direction.UP) {
				return handlers[0].cast();
			} else if (facing == Direction.DOWN) {
				return handlers[1].cast();
			} else {
				return handlers[2].cast();
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (LazyOptional<? extends IItemHandler> handler : handlers) {
			handler.invalidate();
		}
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new RedstoneDyerMenu(id, inventory, this, this.dataAccess);
	}
}
