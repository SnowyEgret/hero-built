package org.snowyegret.mojo.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.snowyegret.mojo.player.Player;

public class GuiDialog extends GuiScreen {

	protected int w = 260;
	protected int h = 100;
	private int buttonWidth = 60;
	protected int buttonHeight = 20;
	protected int padding = 10;
	protected Player player;
	protected List<String> buttonNames = new ArrayList<>();
	private int dx = 0;
	private int buttonCount = 0;

	public GuiDialog(Player player, String... buttonNames) {
		this.player = player;
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
		drawEntityOnScreen(x, y, 30, x - mouseX, y - mouseY, player.getPlayer());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	// Copied from GuiInventory
	/**
	 * Draws the entity to the screen. Args: xPos, yPos, scale, mouseX, mouseY, entityLiving
	 */
	public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) p_147046_0_, (float) p_147046_1_, 50.0F);
		GlStateManager.scale((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = p_147046_5_.renderYawOffset;
		float f3 = p_147046_5_.rotationYaw;
		float f4 = p_147046_5_.rotationPitch;
		float f5 = p_147046_5_.prevRotationYawHead;
		float f6 = p_147046_5_.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		p_147046_5_.renderYawOffset = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 20.0F;
		p_147046_5_.rotationYaw = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 40.0F;
		p_147046_5_.rotationPitch = -((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F;
		p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
		p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		rendermanager.setRenderShadow(true);
		p_147046_5_.renderYawOffset = f2;
		p_147046_5_.rotationYaw = f3;
		p_147046_5_.rotationPitch = f4;
		p_147046_5_.prevRotationYawHead = f5;
		p_147046_5_.rotationYawHead = f6;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	// TODO How to make
	private void drawPlayer() {
		GL11.glPushMatrix();
		GL11.glTranslatef(topLeftX(), topLeftY(), 0);
		float scale = 30F;
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180, 0, 0, 1);
		RenderHelper.enableStandardItemLighting();
		float x = 0, y = 0, z = 0, yaw = 0, partialTicks = 0;
		yaw = Mouse.getEventDX();
		System.out.println("yaw=" + yaw);
		// y = Mouse.getEventY();
		Minecraft.getMinecraft().getRenderManager()
				.renderEntityWithPosYaw(player.getPlayer(), x, y, z, yaw, partialTicks);
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();
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
