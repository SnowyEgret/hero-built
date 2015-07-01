package org.snowyegret.mojo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.event.EventHandlerClient;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.SpellInfo;
import org.snowyegret.mojo.item.spell.transform.SpellFillRandom;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.player.IPlayer;

public class Overlay {

	private static final int WHITE = 0xffffff;
	private static final int RED = 0xffaaaa;
	private static final int GREEN = 0xaaffaa;
	private static final int BLUE = 0xaaaaff;

	private Vec3i displacement;
	private double distance = -1;

	public Overlay() {
	}

	public void setDisplacement(Vec3i displacement) {
		this.displacement = displacement;
	}

	// Called by ForgeEventHandler.onRenderGameOverlayEvent on the client side
	public void drawSpell(Spell spell, IPlayer player) {

		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		int rowHeight = r.FONT_HEIGHT + 5;

		SpellInfo info = spell.getInfo();
		r.drawStringWithShadow(info.getName().toUpperCase(), x, y, WHITE);
		r.drawStringWithShadow(info.getDescription(), x, y += rowHeight, WHITE);
		r.drawStringWithShadow(info.getPicks(), x, y += rowHeight, GREEN);
		r.drawStringWithShadow(info.getModifiers(), x, y += rowHeight, BLUE);

		// Display the dimensions of the impending volume if player is picking or shift selecting
		// if (spell.isPicking() || (!spell.isPicking() && modifiers.isPressed(Modifier.SHIFT))) {
		boolean isFinishedPicking = EventHandlerClient.pickInfo.isFinishedPicking();
		// if (pickManager.isPicking() || (!pickManager.isPicking() && modifiers.isPressed(Modifier.SHIFT))) {
		if (!isFinishedPicking || (isFinishedPicking && Modifier.SHIFT.isPressed())) {
			if (displacement != null) {
				int dx = displacement.getX();
				int dy = displacement.getY();
				int dz = displacement.getZ();
				// Add 1 to get distance instead of displacement
				// FIXME height is broken after port to 1.8
				r.drawStringWithShadow(((dx >= 0) ? "East" : "West") + ": " + (Math.abs(dx) + 1) + "  "
						+ ((dz >= 0) ? "North" : "South") + ": " + (Math.abs(dz) + 1), x, y += rowHeight, RED);
				r.drawStringWithShadow(((dy >= 0) ? "Down" : "Up") + ": " + (Math.abs(dy) + 1), x, y += rowHeight, RED);
			}
		}

		r.drawStringWithShadow("Selection size: " + EventHandlerClient.selectionInfo.getSize(), x, y += rowHeight, RED);

		// TODO SpellFillRandom should set message
		if (spell instanceof SpellFillRandom) {
			r.drawStringWithShadow(player.getHotbar().toString(), x, y += rowHeight, BLUE);
		}

		if (MouseHandler.isOrbiting) {
			r.drawStringWithShadow(player.getDirection().toString().toLowerCase(), x, y += rowHeight, BLUE);
		}

		if (distance > 0) {
			String s = String.format("Distance: %.1f", distance);
			r.drawStringWithShadow(s, x, y += rowHeight, GREEN);
		}
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	// Called by ForgeEventHandler.onRenderGameOverlayEvent on the client side
	public void drawStaff(Staff staff, IPlayer player) {
		ItemStack stack = player.getHeldItemStack();
		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		String staffName = staff.getItemStackDisplayName(stack);
		r.drawStringWithShadow(staffName + " has no spells", x, y, WHITE);
	}

}
