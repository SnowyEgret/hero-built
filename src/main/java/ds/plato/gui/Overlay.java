package ds.plato.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;
import ds.plato.Plato;
import ds.plato.event.MouseHandler;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.SpellInfo;
import ds.plato.item.spell.transform.SpellFillRandom;
import ds.plato.item.staff.Staff;
import ds.plato.player.IPlayer;

public class Overlay {

	private static final int WHITE = 0xffffff;
	private static final int RED = 0xffaaaa;
	private static final int GREEN = 0xaaffaa;
	private static final int BLUE = 0xaaaaff;

	private Vec3i displacement;

	public Overlay() {
	}

	public void setDisplacement(Vec3i displacement) {
		this.displacement = displacement;
	}

	// Called by ForgeEventHandler.onRenderGameOverlayEvent on the client side
	public void drawSpell(ISpell spell, IPlayer player) {

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
		boolean isFinishedPicking = Plato.pickInfo.isFinishedPicking();
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

		// r.drawStringWithShadow("Selection size: " + selectionManager.size(), x, y += rowHeight, RED);
		r.drawStringWithShadow("Selection size: " + Plato.selectionInfo.getSize(), x, y += rowHeight, RED);

		// TODO SpellFillRandom should set message
		if (spell instanceof SpellFillRandom) {
			r.drawStringWithShadow(player.getHotbar().toString(), x, y += rowHeight, BLUE);
		}

		if (MouseHandler.isOrbiting) {
			r.drawStringWithShadow(player.getDirection().toString().toLowerCase(), x, y += rowHeight, BLUE);
		}

		String message = spell.getMessage();
		if (message != null) {
			r.drawStringWithShadow(message, x, y += rowHeight, GREEN);
		}
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
