package ds.plato.item.spell.other;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Shell;
import ds.plato.select.Selection;
import ds.plato.undo.SetBlock;
import ds.plato.undo.Transaction;

public class SpellHoleFill extends Spell {

	public SpellHoleFill(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.water_bucket };
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		Transaction t = undoManager.newTransaction();
		for (Selection s : selectionManager.getSelections()) {
			Shell.Type type = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? Shell.Type.HORIZONTAL : Shell.Type.BELLOW;
			Shell shell = new Shell(type, s.point3i(), world);
			for (Point3i p : shell) {
				Block b = world.getBlock(p.x, p.y, p.z);
				if (b == Blocks.air || b == Blocks.water) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						HotbarSlot e = slotEntries[0];
						t.add(new SetBlock(world, selectionManager, p.x, p.y, p.z, e.block, e.metadata).set());
					} else {
						t.add(new SetBlock(world, selectionManager, p.x, p.y, p.z, s.block, s.metadata).set());
					}
				}
			}
			selectionManager.deselect(s);
		}
		t.commit();
	}
}
