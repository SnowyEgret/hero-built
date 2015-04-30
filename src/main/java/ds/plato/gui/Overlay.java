package ds.plato.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3i;

import org.lwjgl.input.Keyboard;

import ds.plato.api.ISelect;
import ds.plato.api.ISpell;
import ds.plato.core.HotbarDistribution;
import ds.plato.core.Player;
import ds.plato.item.spell.SpellInfo;
import ds.plato.item.spell.transform.SpellFillRandom;
import ds.plato.item.staff.Staff;

public class Overlay {

	private ISelect selectionManager;
	//1.8 Seems Minecraft has a Vec3 class
	//private Vector3d displacement;
	private Vec3i displacement;
	private final int white = 0xffffff;
	private final int red = 0xffaaaa;
	private final int green = 0xaaffaa;
	private final int blue = 0xaaaaff;

	public Overlay(ISelect selectionManager) {
		this.selectionManager = selectionManager;
	}

	//public void setDisplacement(Vector3d displacement) {
	public void setDisplacement(Vec3i displacement) {
		this.displacement = displacement;
	}

	
	public void drawSpell(ISpell spell) {
		int x = 10;
		int y = x;
		//1.8
		//FontRenderer r = Minecraft.getMinecraft().fontRenderer;
		FontRenderer r = Minecraft.getMinecraft().fontRendererObj;
		int rowHeight = r.FONT_HEIGHT + 5;
		
		SpellInfo info = spell.getInfo();
		r.drawStringWithShadow(info.getName().toUpperCase() + " spell", x, y, white);
		r.drawStringWithShadow(info.getDescription(), x, y += rowHeight, white);
		r.drawStringWithShadow(info.getPicks(), x, y += rowHeight, green);
		r.drawStringWithShadow(info.getModifiers(), x, y += rowHeight, blue);


		if (spell.isPicking() || (!spell.isPicking() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
			if (displacement != null) {
				int dx = displacement.getX();
				int dy = displacement.getY();
				int dz = displacement.getZ();
				// Add 1 to get distance instead of displacement
				//FIXME height is broken after port to 1.8
				r.drawStringWithShadow(((dx >= 0) ? "East" : "West") + ": " + (Math.abs(dx)+1) + "  "
						+ ((dz >= 0) ? "North" : "South") + ": " + (Math.abs(dz)+1), x, y += rowHeight, red);
				r.drawStringWithShadow(((dy >= 0) ? "Down" : "Up") + ": " + (Math.abs(dy)+1), x, y += rowHeight, red);
			}
		}

		r.drawStringWithShadow("Selection size: " + selectionManager.size(), x, y += rowHeight, red);

		// TODO SpellFillRandom should set message
		if (spell instanceof SpellFillRandom) {
			HotbarDistribution d = Player.getPlayer().getHotbarDistribution();
			r.drawStringWithShadow(d.toString(), x, y += rowHeight, blue);
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
