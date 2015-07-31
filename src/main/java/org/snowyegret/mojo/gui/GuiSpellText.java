package org.snowyegret.mojo.gui;

import java.awt.Font;

import net.minecraft.client.gui.GuiButton;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.message.server.PlayerSetFontMessage;
import org.snowyegret.mojo.player.Player;

import say.swing.JFontChooser;

public class GuiSpellText extends GuiTextInputDialog {

	private static final int FONT = 2;

	public GuiSpellText(Player player) {
		super(player, "Font");
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case FONT:
			// SpellText s = (SpellText) player.getHeldItem();
			JFontChooser chooser = new JFontChooser();
			// Font font = s.getDefaultFont();
			// System.out.println("font=" + font);
			// if (font != null) {
			// chooser.setSelectedFont(s.getDefaultFont());
			// }
			// We are on the client side. Message server to get player's current font
			// MoJo.network.sendToServer(new PlayerGetFontMessage());
			chooser.setSelectedFont(player.getFont());
			chooser.showDialog(null);
			Font font = chooser.getSelectedFont();
			// s.setFont(font);
			// player.setFont(font);
			// Message server to set font on player
			player.setFont(font);
			// Syncronize property on server side for SpellText
			MoJo.network.sendToServer(new PlayerSetFontMessage(font));
			return;
		}
		super.actionPerformed(button);
	}
}
