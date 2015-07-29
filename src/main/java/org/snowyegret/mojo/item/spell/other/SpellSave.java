package org.snowyegret.mojo.item.spell.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockSavedTileEntity;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.ITextSetable;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.transform.SpellDelete;
import org.snowyegret.mojo.message.client.OpenGuiMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

public class SpellSave extends Spell implements ITextSetable {

	private BlockPos origin;
	// TODO is this the right place for this?
	public static final String KEY_SIZE = "size";
	public static final String KEY_ORIGIN = "origin";

	public SpellSave() {
		super(1);
	}

	@Override
	public void invoke(Player player) {

		SelectionManager sm = player.getSelectionManager();
		if (sm.size() != 0) {
			player.sendMessage(new OpenGuiMessage(GuiHandler.GUI_TEXT_INPUT_DIALOG));
			origin = sm.firstSelection().getPos();
		}
		player.getPickManager().clearPicks();
	}

	@Override
	public void setText(String text, Player player) {
		SelectionManager sm = player.getSelectionManager();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(KEY_SIZE, sm.size());
		tag.setLong(KEY_ORIGIN, origin.toLong());
		int i = 0;
		for (Selection s : sm.getSelections()) {
			tag.setTag(String.valueOf(i), s.toNBT());
			i++;
		}

		File file = null;
		try {
			file = File.createTempFile(text, ".save");
			//file = Files.createFile(Paths.get(text+".save"), null);
			file.deleteOnExit();
			CompressedStreamTools.writeCompressed(tag, new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO modifier for delete
		new SpellDelete().invoke(player);
		sm.clearSelections();

		// TODO should there be a player.doTransaction(IUndoable)?
		// Same for KeyMessageHandler case PASTE:
		// TODO deletes should be in same transaction
		// String path = file.getPath();

		Transaction t = new Transaction();
		t.add(new UndoableSetBlock(origin, player.getWorld().getState(origin), MoJo.blockSaved.getDefaultState()));
		t.dO(player);

		IWorld w = player.getWorld();
		BlockSavedTileEntity te = (BlockSavedTileEntity) w.getTileEntity(origin);
		String path = file.getPath();
		System.out.println("path=" + path);
		te.setPath(path);

	}

}
