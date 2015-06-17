package ds.plato.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
import ds.plato.item.spell.Action;
import ds.plato.item.spell.Modifier;
import ds.plato.network.KeyMessage;

public class KeyHandler {

	private static Set<Integer> KEYS_TO_TRACK = new HashSet<Integer>();

	static {
		for (Modifier m : Modifier.values()) {
			KEYS_TO_TRACK.add(m.keyCode);
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
			System.out.println("keyCode=" + keyCode);
			// Pass the cursor position for Pasting
			BlockPos cursorPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
			Plato.network.sendToServer(new KeyMessage(keyCode, Keyboard.getEventKeyState(), cursorPos));
			
		}
	}
}
