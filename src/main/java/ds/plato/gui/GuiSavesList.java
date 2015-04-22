package ds.plato.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.item.spell.other.SpellRestore;

public class GuiSavesList extends GuiListExtended {

	private List<Row> rows = new ArrayList<Row>();
	private EntityPlayer player;

	public GuiSavesList(Minecraft mc, int w, int h, int top, int bottom, int spacing, EntityPlayer player, String... filenames) {
		super(mc, w, h, top, bottom, spacing);
		this.player = player;
		for (String filename : filenames) {
			GuiButton b = new GuiButton(spacing, spacing, spacing, filename);
			rows.add(new Row(b));
		}
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return rows.get(index);
	}

	@Override
	protected int getSize() {
		return rows.size();
	}

	@SideOnly(Side.CLIENT)
	private class Row implements IGuiListEntry {

		private GuiButton button;
		private final Minecraft mc = Minecraft.getMinecraft();

		public Row(GuiButton button) {
			this.button = button;
		}

		@Override
		public void drawEntry(int p1, int p2, int y, int p4, int p5, Tessellator p6, int p7, int p8, boolean p9) {
			button.yPosition = y;
			button.drawButton(mc, p7, p8);
		}

		@Override
		public boolean mousePressed(int p1, int p2, int p3, int p4, int p5, int p6) {
			System.out.println(button.displayString);
			SpellRestore s = (SpellRestore) player.getHeldItem().getItem();
			s.restore(button.displayString);
			mc.displayGuiScreen(null);
			return button.mousePressed(mc, p2, p3);
		}

		@Override
		public void mouseReleased(int p1, int p2, int p3, int p4, int p5, int p6) {
			button.mouseReleased(p2, p3);
		}

	}
}
