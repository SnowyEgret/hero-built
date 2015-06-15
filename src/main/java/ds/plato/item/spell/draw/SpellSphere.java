package ds.plato.item.spell.draw;

import net.minecraft.init.Items;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.Sphere;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSphere extends AbstractSpellDraw {

	public SpellSphere() {
		super(2);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		boolean isHemisphere = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		IDrawable d = new Sphere(picks[0].point3d(), picks[1].point3d(), isHemisphere);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " A ", "A A", " A ", 'A', Items.bone };
	}
}
