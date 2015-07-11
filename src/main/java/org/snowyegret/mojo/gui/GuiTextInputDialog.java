package org.snowyegret.mojo.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.message.server.KeyMessage;
import org.snowyegret.mojo.message.server.SpellTextMessage;
import org.snowyegret.mojo.player.Player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextInputDialog extends GuiDialog {

	private GuiTextField textField;
	private String text;
	private final int margin = 20;

	public GuiTextInputDialog(Player player, String... buttons) {
		super(player, "Ok", "Cancel");
		if (buttons != null) {
			for (String b : buttons) {
				addButtonName(b);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			text = textField.getText();
			// TODO GuiTextInputDialog must message server with text on "Ok" #234
			// We are on the client side. Message server
			MoJo.network.sendToServer(new SpellTextMessage(text));
//			ITextSetable s = (ITextSetable) player.getHeldItem();
//			s.setText(text, player);
			break;
		case 1:
			break;
		}
		mc.displayGuiScreen(null);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		textField.textboxKeyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);
		textField.mouseClicked(par1, par2, par3);
	}

	@Override
	public void initGui() {
		super.initGui();
		// 1.8
		// textField = new GuiTextField(mc.fontRenderer, topLeftX() + margin, topLeftY() + margin, w - (2 * margin),
		// buttonHeight);
		textField = new GuiTextField(margin, mc.fontRendererObj, topLeftX() + margin, topLeftY() + margin, w
				- (2 * margin), buttonHeight);
	}

	@Override
	public void drawScreen(int x, int y, float par3) {
		super.drawScreen(x, y, par3);
		textField.drawTextBox();
		textField.setFocused(true);
	}
}
