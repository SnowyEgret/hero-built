package ds.plato.item.spell.other;

import java.io.IOException;

import javax.vecmath.Point3i;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.core.WorldWrapper;
import ds.plato.gui.ITextSetable;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.PersistentVoxelGroup;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.pick.Pick;

public class SpellSave extends Spell implements ITextSetable {

	private Point3i insertionPoint;

	public SpellSave(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		if (selectionManager.size() != 0) {
			Minecraft.getMinecraft().thePlayer.openGui(Plato.instance, 0, world.getWorld(), 0, 0, 0);
			Pick[] picks = pickManager.getPicks();
			insertionPoint = picks[0].point3i();
		}
		pickManager.clearPicks();
	}

	@Override
	public void setText(String text) {
		// TODO could create a new item in the players inventory. Could be named with an anvil like a sword. Could have
		// a texture generated from the player's view at the time of creation
		String json = null;
		try {
			// TODO
			json = new PersistentVoxelGroup(insertionPoint, selectionManager.getSelectionList()).write("saves/" + text
					+ ".json");
			System.out.println("[SpellSave.writeFile] json=" + json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pickManager.clearPicks();

		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		if (deleteOriginal) {
			SpellDelete s = new SpellDelete(undoManager, selectionManager, pickManager);
			pickManager.reset(s.getNumPicks());
			// Can't remember why this world is preferable, or maybe I just didn't set world and slotEntries in invoke
			//1.8
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			s.invoke(new WorldWrapper(player.worldObj), (HotbarSlot[]) null);
		}
		selectionManager.clearSelections();
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
