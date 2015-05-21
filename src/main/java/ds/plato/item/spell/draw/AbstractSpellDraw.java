package ds.plato.item.spell.draw;

import javax.vecmath.Point3i;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import ds.geom.IDrawable;
import ds.geom.VoxelSet;
import ds.geom.solid.Solid;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.undo.Transaction;
import ds.plato.util.StringUtils;
import ds.plato.world.IWorld;

public abstract class AbstractSpellDraw extends Spell {

	public AbstractSpellDraw(int numPicks, IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(numPicks, undoManager, selectionManager, pickManager);
		//TODO
		info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

//	protected void draw(IDrawable drawable, IWorld world, Block block) {
//		draw(drawable, world, block, false, false);
//	}

	//protected void draw(IDrawable drawable, IWorld world, Block block, boolean isHollow, boolean onSurface) {
	protected void draw(IDrawable drawable, IWorld world, Block block) {
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		boolean isHollow = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Transaction t = undoManager.newTransaction();
		VoxelSet voxels = drawable.voxelize();
		if (drawable instanceof Solid && isHollow) {
			voxels = voxels.shell();
		}
		for (Point3i p : voxels) {
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			if(onSurface) {
				pos = pos.up();
			}
			t.add(new UndoableSetBlock(world, selectionManager, pos, block).set());
		}
		t.commit();
		
		//Try playing a sound
		String sound = "plato:"+StringUtils.toCamelCase(getClass());
		world.getWorld().playSoundAtEntity(Minecraft.getMinecraft().thePlayer,sound , 1f, 1f);
	}

}
