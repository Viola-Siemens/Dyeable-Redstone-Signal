package com.hexagram2021.dyeable_redstone_signal.client.screens;

import com.hexagram2021.dyeable_redstone_signal.common.block.entity.CommonRedstoneWireBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.block.entity.RedstoneDyerBlockEntity;
import com.hexagram2021.dyeable_redstone_signal.common.crafting.RedstoneDyerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

@OnlyIn(Dist.CLIENT)
public class RedstoneDyerScreen extends AbstractContainerScreen<RedstoneDyerMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/redstone_dyer.png");

	private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
	private static final int RECIPES_IMAGE_SIZE_HEIGHT = 16;
	private static final int RECIPES_X = 32;
	private static final int RECIPES_Y = 51;

	public RedstoneDyerScreen(RedstoneDyerMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
		--this.titleLabelY;
	}

	@Override
	public void render(PoseStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BG_LOCATION);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int recipeX = this.leftPos + RECIPES_X;
		int recipeY = this.topPos + RECIPES_Y;
		this.renderButtons(transform, x, y, recipeX, recipeY);
		int fluidLevel = this.menu.getFluidLevel();
		if(fluidLevel > 0) {
			int k = Mth.clamp(fluidLevel, 1, 50);
			fill(transform, i + 19, j + 69 - k, i + 27, j + 69, RedstoneDyerBlockEntity.DYE_COLORS[this.menu.getFluidType()].getTextColor() | 0xff000000);
		}
	}

	@Override
	protected void renderTooltip(PoseStack transform, int x, int y) {
		super.renderTooltip(transform, x, y);

		int recipeX = this.leftPos + RECIPES_X;
		int recipeY = this.topPos + RECIPES_Y;

		for(int i = 0; i < 2; ++i) {
			int curX = recipeX + i * RECIPES_IMAGE_SIZE_WIDTH;
			int curY = recipeY + 2;
			if (x >= curX && x < curX + RECIPES_IMAGE_SIZE_WIDTH && y >= curY && y < curY + RECIPES_IMAGE_SIZE_WIDTH) {
				this.renderTooltip(transform, Component.translatable("tooltip.dyeable_redstone_signal.redstone_dyer" + i), x, y);
			}
		}
		if(this.menu.getFluidLevel() > 0 && x >= this.leftPos + 19 && x < this.leftPos + 27 && y >= this.topPos + 19 && y < this.topPos + 69) {
			this.renderTooltip(
					transform, Component.translatable(
						"tooltip.dyeable_redstone_signal.dye_slot",
						Component.translatable("item.minecraft." + CommonRedstoneWireBlockEntity.COLORS[this.menu.getFluidType()] + "_dye"),
						this.menu.getFluidLevel()
					),
					x, y
			);
		}
	}

	private void renderButtons(PoseStack transform, int x, int y, int recipeX, int recipeY) {
		for(int i = 0; i < 2; ++i) {
			int curX = recipeX + i * (RECIPES_IMAGE_SIZE_WIDTH + 1);
			int curY = recipeY + 2;
			int h = this.imageHeight;
			if(this.menu.tickToDo() == i + 1) {
				h += RECIPES_IMAGE_SIZE_HEIGHT;
			} else if (x >= curX && y >= curY && x < curX + RECIPES_IMAGE_SIZE_WIDTH && y < curY + RECIPES_IMAGE_SIZE_HEIGHT) {
				h += RECIPES_IMAGE_SIZE_HEIGHT * 2;
			}

			this.blit(transform, curX, curY - 1, i * RECIPES_IMAGE_SIZE_WIDTH, h, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int key) {
		final SimpleSoundInstance uiSound = SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F);

		int recipeX = this.leftPos + RECIPES_X;
		int recipeY = this.topPos + RECIPES_Y;

		for(int i = 0; i < 2; ++i) {
			double d0 = x - (double)(recipeX + i * RECIPES_IMAGE_SIZE_WIDTH);
			double d1 = y - (double)(recipeY);
			if (d0 >= 0.0D && d1 >= 0.0D && d0 < RECIPES_IMAGE_SIZE_WIDTH && d1 < RECIPES_IMAGE_SIZE_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, i)) {
				Minecraft.getInstance().getSoundManager().play(uiSound);
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
				return true;
			}
		}

		return super.mouseClicked(x, y, key);
	}
}
