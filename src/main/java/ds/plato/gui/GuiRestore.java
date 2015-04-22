package ds.plato.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class GuiRestore extends GuiDialog {

	private GuiSavesList list;

	public GuiRestore(EntityPlayer player) {
		super(player, "Cancel");
	}

	@Override
	public void initGui() {
		super.initGui();
		List<String> filenames = new ArrayList<>();
		File folder = new File("saves");
		for (File file : folder.listFiles()) {
			if (file.isFile()) {
				String filename = file.getName();
				if (filename.endsWith(".json")) {
					filenames.add(filename.substring(0, filename.length() - 5));
				}
			}
		}
		int spacing = 25;
		int header = 20;
		int footer = h;
		String[] saves = new String[filenames.size()];
		list = new GuiSavesList(mc, w, h+30, header, footer, spacing, player, filenames.toArray(saves));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(null);
			break;
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		list.func_148179_a(par1, par2, par3);
	}

	@Override
	protected void mouseMovedOrUp(int p1, int p2, int p3) {
		super.mouseMovedOrUp(p1, p2, p3);
		list.func_148181_b(p1, p2, p3);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		list.drawScreen(par1, par2, par3);
	}
}
