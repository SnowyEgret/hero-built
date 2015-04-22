package ds.plato.gui;

import java.awt.Font;

import say.swing.JFontChooser;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import ds.plato.item.spell.other.SpellText;

public class GuiSpellText extends GuiTextInputDialog {

	public GuiSpellText(EntityPlayer player) {
		super(player, "Font");
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 2:
			SpellText s = (SpellText) player.getHeldItem().getItem();
			JFontChooser chooser = new JFontChooser();
			Font font = s.getFont();
			System.out.println("[GuiSpellText.actionPerformed] font=" + font);
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
