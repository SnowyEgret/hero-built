package org.snowyegret.mojo.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import org.lwjgl.opengl.GL11;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.message.server.TextInputMessage;
import org.snowyegret.mojo.player.Player;

public class GuiTextInputDialog extends GuiDialog {

	private static final int OK = 0;
	private static final int CANCEL = 1;
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
		case OK:
			text = textField.getText();
			// TODO GuiTextInputDialog must message server with text on "Ok" #234
			// We are on the client side. Message server
			MoJo.network.sendToServer(new TextInputMessage(text));
			break;
		case CANCEL:
			MoJo.network.sendToServer(new TextInputMessage(TextInputMessage.CANCEL));
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
