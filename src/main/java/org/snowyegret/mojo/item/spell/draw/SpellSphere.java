package org.snowyegret.mojo.item.spell.draw;

import net.minecraft.init.Items;

import org.lwjgl.input.Keyboard;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.undo.IUndo;

import ds.geom.IDrawable;
import ds.geom.surface.Sphere;

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
		boolean isHemisphere = modifiers.isPressed(Modifier.SHIFT);
		IDrawable d = new Sphere(picks[0].point3d(), picks[1].point3d(), isHemisphere);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " A ", "A A", " A ", 'A', Items.bone };
	}
}
