package org.snowyegret.mojo.item.spell.other;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.ClientProxy;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockMaquetteTileEntity;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.gui.ITextInput;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.message.client.OpenGuiMessage;
import org.snowyegret.mojo.message.client.SpellMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class SpellMaquette extends Spell implements ITextInput {

	// TODO is this the right place for this?
	public static final String KEY_SIZE = "size";
	public static final String KEY_ORIGIN = "origin";
	public static final String EXTENTION = ".maquette";

	public SpellMaquette() {
		super(1);
	}

	@Override
	public void invoke(Player player) {
		if (player.getSelectionManager().size() != 0) {
			player.sendMessage(new OpenGuiMessage(GuiHandler.GUI_TEXT_INPUT_DIALOG));
		} else {
			player.sendMessage(new SpellMessage("item.spell_save.message.no_selections"));
		}
		player.clearPicks();
	}

	@Override
	public void setText(String text, Player player) {

		// Create tag
		SelectionManager sm = player.getSelectionManager();
		//TODO Should be block under cursor
		BlockPos origin = sm.firstSelection().getPos();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(KEY_SIZE, sm.size());
		tag.setLong(KEY_ORIGIN, origin.toLong());
		int i = 0;
		for (Selection s : sm.getSelections()) {
			tag.setTag(String.valueOf(i), s.toNBT());
			i++;
		}

		// Write tag to file
		Path path = null;
		try {
			// path = Files.createFile(Paths.get(ClientProxy.PATH_SAVES, text + origin.toLong() + EXTENTION));
			// TODO if file exists
			// player.sendMessage(new OpenGuiMessage(GuiHandler.FILE_OVERWRITE_DIALOG));
			path = Files.createFile(Paths.get(ClientProxy.PATH_SAVES.toString(), text + origin.toLong() + EXTENTION));
			CompressedStreamTools.writeCompressed(tag, new FileOutputStream(path.toFile()));
		} catch (IOException e) {
			System.out.println(e);
			player.clearSelections();
			player.clearPicks();
			return;
		}
		// System.out.println("path=" + path);

		// Delete original
		List<IUndoable> deletes = Lists.newArrayList();
		// FIXME If the selections are not deleted, BlockSaved will be placed at position of first selection and the
		// TileEntity will be PrevStateTileEntity instead of BlockSavedTileEntity
		// boolean deleteSelections = player.getModifiers().isPressed(Modifier.CTRL);
		boolean deleteSelections = true;
		if (deleteSelections) {
			final IBlockState air = Blocks.air.getDefaultState();
			for (Selection s : sm.getSelections()) {
				deletes.add(new UndoableSetBlock(s.getPos(), s.getState(), air));
			}
		}

		player.clearSelections();
		player.clearPicks();

		Transaction t = new Transaction();
		t.addAll(deletes);
		t.add(new UndoableSetBlock(origin, player.getWorld().getState(origin), MoJo.blockMaquette.getDefaultState()));
		t.dO(player);

		// Write path to tile entity
		IWorld w = player.getWorld();
		BlockMaquetteTileEntity te = (BlockMaquetteTileEntity) w.getTileEntity(origin);
		te.setPath(path.toString());
		te.setTag(tag);
		System.out.println("te=" + te);
	}

	@Override
	public void cancel(Player player) {
		player.clearSelections();
		player.clearPicks();
	}

}
