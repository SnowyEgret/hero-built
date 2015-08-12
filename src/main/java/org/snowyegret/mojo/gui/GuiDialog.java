package org.snowyegret.mojo.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

import org.snowyegret.mojo.player.Player;

public class GuiDialog extends GuiScreen {

	protected int w = 260;
	protected int h = 100;
	private int buttonWidth = 60;
	protected int buttonHeight = 20;
	protected int padding = 10;
	protected List<String> buttonNames = new ArrayList<>();
	private int dx = 0;
	private int buttonCount = 0;

	public GuiDialog(String... buttonNames) {
		for (String b : buttonNames) {
			this.buttonNames.add(b);
		}
	}

	@Override
	public void initGui() {
		buttonList.clear();
		dx = 0;
		buttonCount = 0;
		for (String b : buttonNames) {
			addButton(b);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		int x = topLeftX();
		int y = topLeftY();
		GuiInventory.drawEntityOnScreen(x, y, 30, x - mouseX, y - mouseY, mc.thePlayer);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected int topLeftX() {
		return (width - w) / 2;
	}

	protected int topLeftY() {
		return (height - h) / 2;
	}

	protected void addButtonName(String button) {
		this.buttonNames.add(button);
	}

	private void addButton(String buttonName) {
		buttonList.add(new GuiButton(buttonCount, topLeftX() + (w / 2) - (widthOfButtons() / 2) + dx, topLeftY() + h
				- padding - buttonHeight, buttonWidth, buttonHeight, buttonName));
		buttonCount++;
		dx += buttonWidth + padding;
	}

	private int widthOfButtons() {
		return buttonNames.size() * buttonWidth + (buttonNames.size() - 1) * padding;
	}
}
