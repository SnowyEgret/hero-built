package org.snowyegret.mojo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.event.EventHandlerClient;
import org.snowyegret.mojo.event.MouseHandler;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.SpellInfo;
import org.snowyegret.mojo.item.spell.transform.SpellFillRandom;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.player.Player;

public class Overlay {

	private static final int WHITE = 0xffffff;
	private static final int RED = 0xffaaaa;
	private static final int GREEN = 0xaaffaa;
	private static final int BLUE = 0xaaaaff;

	private Vec3i displacement;
	private Double distance = null;
	private String message = null;

	public Overlay() {
	}

	public void setDisplacement(Vec3i displacement) {
		this.displacement = displacement;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// Called by ForgeEventHandler.onRenderGameOverlayEvent on the client side
	public void drawSpell(Spell spell, Player player) {

		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		int rh = r.FONT_HEIGHT + 5;

		SpellInfo info = spell.getInfo();
		r.drawStringWithShadow(info.getName().toUpperCase(), x, y, WHITE);
		String description = info.getDescription();
		if (!description.isEmpty()) {
			r.drawStringWithShadow(description, x, y += rh, WHITE);
		}
		r.drawStringWithShadow(info.getPicks(), x, y += rh, GREEN);
		r.drawStringWithShadow(info.getModifiers(), x, y += rh, BLUE);

		// Display the dimensions of the impending volume if player is picking or shift selecting
		boolean isPicking = EventHandlerClient.pickInfo.isPicking();
		if (isPicking || (!isPicking && Modifier.SHIFT.isPressed())) {
			if (displacement != null) {
				int dx = displacement.getX();
				int dy = displacement.getY();
				int dz = displacement.getZ();
				if (dx != 0) {
					r.drawStringWithShadow(((dx > 0) ? "East" : "West") + ": " + (Math.abs(dx)), x, y += rh, RED);
				}
				if (dz != 0) {
					r.drawStringWithShadow(((dz > 0) ? "North" : "South") + ": " + (Math.abs(dz)), x, y += rh, RED);
				}
				if (dy != 0) {
					r.drawStringWithShadow(((dy > 0) ? "Down" : "Up") + ": " + (Math.abs(dy)), x, y += rh, RED);
				}
			}
		}

		if (EventHandlerClient.selectionInfo.getSize() != 0) {
			r.drawStringWithShadow("Selections: " + EventHandlerClient.selectionInfo.getSize(), x, y += rh, RED);
		}

		// TODO SpellFillRandom should set message
		if (spell instanceof SpellFillRandom) {
			r.drawStringWithShadow(player.getHotbar().toString(), x, y += rh, BLUE);
		}

		if (MouseHandler.isOrbiting) {
			r.drawStringWithShadow(player.getDirection().toString().toLowerCase(), x, y += rh, BLUE);
		}

		if (distance != null) {
			String s = String.format("Distance: %.1f", distance);
			r.drawStringWithShadow(s, x, y += rh, GREEN);
		}

		if (message != null) {
			r.drawStringWithShadow(message, x, y += rh, RED);
		}
	}

	// Called by ForgeEventHandler.onRenderGameOverlayEvent on the client side
	public void drawStaff(Staff staff, Player player) {
		ItemStack stack = player.getHeldItemStack();
		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		String staffName = staff.getItemStackDisplayName(stack);
		r.drawStringWithShadow(staffName + " has no spells", x, y, WHITE);
	}

}
