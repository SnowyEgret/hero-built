package ds.plato.item.spell.draw;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.VoxelSet;
import ds.geom.solid.Solid;
import ds.plato.Plato;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.util.StringUtils;
import ds.plato.world.IWorld;

public abstract class AbstractSpellDraw extends Spell {

	public AbstractSpellDraw(int numPicks, IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(numPicks, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

	protected void draw(IDrawable drawable, IWorld world, IPlayer player) {

		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
		
		Modifiers modifiers = player.getModifiers();
		boolean isHollow = modifiers.isPressed(Modifier.SHIFT);
		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);

		VoxelSet voxels = drawable.voxelize();

		if (drawable instanceof Solid && isHollow) {
			voxels = voxels.shell();
		}

		Jumper jumper = new Jumper(player);

		List<UndoableSetBlock> setBlocks = new ArrayList<>();
		List<BlockPos> reselects = new ArrayList<>();
		for (Point3i p : voxels) {
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			if (onSurface) {
				pos = pos.up();
			}
			jumper.setHeight(pos);
			IBlockState state = player.getHotbar().firstBlock();
			setBlocks.add(new UndoableSetBlock(world, selectionManager, pos, state));
			reselects.add(pos);
		}

		jumper.jump();

		// Set the blocks inside an undoManager transaction
		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : setBlocks) {
			t.add(u.set());
		}
		t.commit();

		// Select all transformed blocks
		if (Minecraft.getMinecraft().isSingleplayer() && !Plato.forceMessaging) {
			for (BlockPos pos : reselects) {
				// FIXME in MP select is rejecting these even though clearSelections
				// should have removed BlockSelected from world.
				// Same problem as when first corner of a region was left unselected
				selectionManager.select(world, pos);
			}
		} else {
			selectionManager.setReselects(reselects);
		}
		
		String sound = "plato:" + StringUtils.toCamelCase(getClass());
		// world.getWorld().playSoundAtEntity(player.getPlayer(), sound, 1f, 1f);
		// TODO
		// world.playSound(player, this, setBlocks.size());
		// new Sound(this, setBlocks.size()).play(world, player);
		// this.playSound(setBlocks.size(), world, player);
		// world.playSound(new Sound(this, setBlocks.size()), player);
	}

}
