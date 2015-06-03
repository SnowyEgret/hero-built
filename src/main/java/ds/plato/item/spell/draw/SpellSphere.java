package ds.plato.item.spell.draw;

import net.minecraft.init.Items;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.Sphere;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellSphere extends AbstractSpellDraw {

	public SpellSphere(IUndo undo, ISelect select, IPick pick) {
		super(2, undo, select, pick);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Pick[] picks = pickManager.getPicks();
		boolean isHemisphere = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		IDrawable d = new Sphere(picks[0].point3d(), picks[1].point3d(), isHemisphere);
		draw(d, world, player.getHotbar()[0].state);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " A ", "A A", " A ", 'A', Items.bone };
	}
}
