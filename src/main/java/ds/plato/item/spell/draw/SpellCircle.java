package ds.plato.item.spell.draw;

import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.geom.IDrawable;
import ds.geom.curve.CircleXZ;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;
import ds.plato.player.Player;

public class SpellCircle extends AbstractSpellDraw {

	public SpellCircle(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		Pick[] picks = pickManager.getPicks();
		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		if (onSurface) {
			p0.y += 1;
			p1.y += 1;
		}
		IDrawable d = new CircleXZ(p0, p1);
		draw(d, world, slotEntries[0].block);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
