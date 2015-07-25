package org.snowyegret.mojo.item.spell.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.ITextSetable;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.message.client.OpenGuiMessage;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;

public class SpellSave extends Spell implements ITextSetable {

	private Pick pick;
	private static final String SIZE_KEY = "s";

	public SpellSave() {
		super(1);
	}

	@Override
	public void invoke(Player player) {

		if (player.getSelectionManager().size() != 0) {
			player.sendMessage(new OpenGuiMessage(GuiHandler.GUI_TEXT_INPUT_DIALOG));
			pick = player.getPickManager().firstPick();
		}
		player.getPickManager().clearPicks();
	}

	@Override
	public void setText(String text, Player player) {
		SelectionManager sm = player.getSelectionManager();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(SIZE_KEY , sm.size());
		int i = 0;
		tag.setTag(String.valueOf(i), pick.toNBT());
		i++;
		for (Selection s : sm.getSelections()) {
			tag.setTag(String.valueOf(i), s.toNBT());
			i++;
		}

		try {
			File file = File.createTempFile(text, ".save");
			file.deleteOnExit();
			CompressedStreamTools.writeCompressed(tag, new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player.getWorld().setState(pick.getPos(), MoJo.blockSaved.getDefaultState());
	}

}
