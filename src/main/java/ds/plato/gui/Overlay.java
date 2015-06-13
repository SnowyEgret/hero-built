package ds.plato.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import org.lwjgl.input.Keyboard;

import ds.plato.event.MouseHandler;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.SpellInfo;
import ds.plato.item.spell.transform.SpellFillRandom;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;

public class Overlay {

	private Vec3i displacement;
	private final int white = 0xffffff;
	private final int red = 0xffaaaa;
	private final int green = 0xaaffaa;
	private final int blue = 0xaaaaff;

	public Overlay() {
	}

	public void setDisplacement(Vec3i displacement) {
		this.displacement = displacement;
	}

	public void drawSpell(ISpell spell) {

		IPlayer player = Player.instance();
		IPick pickManager = player.getPickManager();
		ISelect selectionManager = player.getSelectionManager();
		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		int rowHeight = r.FONT_HEIGHT + 5;

		SpellInfo info = spell.getInfo();
		r.drawStringWithShadow(info.getName().toUpperCase(), x, y, white);
		r.drawStringWithShadow(info.getDescription(), x, y += rowHeight, white);
		r.drawStringWithShadow(info.getPicks(), x, y += rowHeight, green);
		r.drawStringWithShadow(info.getModifiers(), x, y += rowHeight, blue);

		// Display the dimensions of the impending volume if player is picking or shift selecting
		// TODO Test that pickManager is same as one on server side.
		// if (spell.isPicking() || (!spell.isPicking() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
		if (pickManager.isPicking() || (!pickManager.isPicking() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
			if (displacement != null) {
				int dx = displacement.getX();
				int dy = displacement.getY();
				int dz = displacement.getZ();
				// Add 1 to get distance instead of displacement
				// FIXME height is broken after port to 1.8
				r.drawStringWithShadow(((dx >= 0) ? "East" : "West") + ": " + (Math.abs(dx) + 1) + "  "
						+ ((dz >= 0) ? "North" : "South") + ": " + (Math.abs(dz) + 1), x, y += rowHeight, red);
				r.drawStringWithShadow(((dy >= 0) ? "Down" : "Up") + ": " + (Math.abs(dy) + 1), x, y += rowHeight, red);
			}
		}

		// TODO Test that pickManager is same as one on server side.
		r.drawStringWithShadow("Selection size: " + selectionManager.size(), x, y += rowHeight, red);

		// TODO SpellFillRandom should set message
		if (spell instanceof SpellFillRandom) {
			r.drawStringWithShadow(player.getHotbar().getDistribution().toString(), x, y += rowHeight, blue);
		}

		if (MouseHandler.isOrbiting) {
			r.drawStringWithShadow(player.getDirection().toString().toLowerCase(), x, y += rowHeight, blue);
		}

		String message = spell.getMessage();
		if (message != null) {
			r.drawStringWithShadow(message, x, y += rowHeight, green);
		}
	}

	public void drawStaff(Staff staff, ItemStack stack) {
		int x = 10;
		int y = x;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		String staffName = staff.getItemStackDisplayName(stack);
		r.drawStringWithShadow(staffName + " has no spells", x, y, white);
	}

}
