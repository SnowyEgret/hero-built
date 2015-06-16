package ds.plato.gui;

import java.awt.Font;

import net.minecraft.client.gui.GuiButton;
import say.swing.JFontChooser;
import ds.plato.item.spell.other.SpellText;
import ds.plato.player.IPlayer;

public class GuiSpellText extends GuiTextInputDialog {

	public GuiSpellText(IPlayer player) {
		super(player, "Font");
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 2:
			SpellText s = (SpellText) player.getHeldItem();
			JFontChooser chooser = new JFontChooser();
			Font font = s.getFont();
			System.out.println("font=" + font);
			if (font != null) {
				chooser.setSelectedFont(s.getFont());
			}
			chooser.showDialog(null);
			font = chooser.getSelectedFont();
			s.setFont(font);
			return;
		}
		super.actionPerformed(button);
	}
}
