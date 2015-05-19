package ds.plato.item.spell.draw;

import net.minecraft.init.Items;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.Sphere;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;

public class SpellSphere extends AbstractSpellDraw {

	public SpellSphere(IUndo undo, ISelect select, IPick pick) {
		super(2, undo, select, pick);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot... slots) {
		Pick[] picks = pickManager.getPicks();
		boolean isHemisphere = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		IDrawable d = new Sphere(picks[0].point3d(), picks[1].point3d(), isHemisphere);
		draw(d, world, slots[0].block);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " A ", "A A", " A ", 'A', Items.bone };
	}
}
