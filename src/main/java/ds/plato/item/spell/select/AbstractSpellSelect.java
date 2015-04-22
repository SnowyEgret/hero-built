package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.block.BlockSelected;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.Pick;
import ds.plato.select.Selection;

public abstract class AbstractSpellSelect extends Spell {

	protected Shell.Type shellType;
	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;

	public AbstractSpellSelect(Shell.Type type, IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
		this.shellType = type;
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot...slotEntries) {
		
		// Select the pick if there are no selections
		Pick p = pickManager.getPicks()[0];
		pickManager.clearPicks();
		if (selectionManager.size() == 0) {
			selectionManager.select(world, p.x, p.y, p.z);
		}
		
		// Shrink or grow selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			shrinkSelections(shellType, world);
		} else {
			// Is this really the first block? getSelections gets the values from a map.
			Block patternBlock = selectionManager.getSelections().iterator().next().block;
			growSelections(shellType, world, patternBlock);
		}
	}

	protected void growSelections(Shell.Type shellType, IWorld world, Block patternBlock) {
		List<Point3i> newGrownSelections = new ArrayList();
		for (Point3i center : selectionManager.getGrownSelections()) {
			Shell shell = new Shell(shellType, center, world);
			for (Point3i p : shell) {
				Block block = world.getBlock(p.x, p.y, p.z);
				if (!(block instanceof BlockAir) && !(block instanceof BlockSelected)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) { // Alt
						selectionManager.select(world, p.x, p.y, p.z);
						newGrownSelections.add(p);
					} else {
						if (block == patternBlock) {
							selectionManager.select(world, p.x, p.y, p.z);
							newGrownSelections.add(p);
						}
					}
				}
			}
		}
		selectionManager.setGrownSelections(newGrownSelections);
	}

	protected void shrinkSelections(Shell.Type shellType, IWorld world) {
		List<Selection> shrunkSelections = new ArrayList<>();
		for (Selection s : selectionManager.getSelections()) {
			Shell shell = new Shell(shellType, s.point3i(), world);
			for (Point3i p : shell) {
				Block b = world.getBlock(p.x, p.y, p.z);
				if (!(b instanceof BlockSelected)) {
					shrunkSelections.add(s);
					break;
				}

			}
		}
		for (Selection s : shrunkSelections) {
			selectionManager.deselect(s);
		}
		selectionManager.clearGrownSelections();
	}
}
