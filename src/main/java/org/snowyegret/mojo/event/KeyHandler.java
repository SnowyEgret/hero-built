package org.snowyegret.mojo.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.item.spell.Action;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.message.server.KeyMessage;
import org.snowyegret.mojo.player.Player;

public class KeyHandler {

	private static Set<Integer> KEYS_TO_TRACK = new HashSet<Integer>();

	static {
		for (Modifier m : Modifier.values()) {
			KEYS_TO_TRACK.add(m.keyCode);
			if (m.keyCodeRight != -1) {
				KEYS_TO_TRACK.add(m.keyCodeRight);
			}
		}
		for (Action a : Action.values()) {
			KEYS_TO_TRACK.add(a.keyCode);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		if (KEYS_TO_TRACK.contains(Keyboard.getEventKey())) {
			int keyCode = Keyboard.getEventKey();
			// TODO Find a way to track shift right. We are not getting events for right shift, ctrl, alt.
			// int keyCodeRight = Keyboard.getNumKeyboardEvents();
			//System.out.println("keyCode=" + keyCode);
			//System.out.println("state=" + Keyboard.getEventKeyState());
			BlockPos cursorPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
			MoJo.network.sendToServer(new KeyMessage(keyCode, Keyboard.getEventKeyState(), cursorPos));
		}
	}
}
