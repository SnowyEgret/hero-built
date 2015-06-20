package org.snowyegret.plato.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.snowyegret.plato.player.Player;

//Started with GuiChest
//http://www.minecraftforge.net/wiki/Containers_and_GUIs
public class GuiStaff extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
	private int rowHeight = 35;

	public GuiStaff(GuiStaffContainer container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
		ItemStack stack = Player.instance().getHeldItemStack();
		if (stack == null) {
			return;
		}
		String staffName = stack.getDisplayName();
		fontRendererObj.drawString(staffName, 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, rowHeight + 2, 4210752);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p1, int p2, int p3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y,             0, 0,   xSize, rowHeight);
		drawTexturedModalRect(x, y + rowHeight, 0, 126, xSize, 96);
	}
}
