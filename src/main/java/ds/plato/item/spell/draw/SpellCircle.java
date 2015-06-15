package ds.plato.item.spell.draw;

import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.curve.CircleXZ;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellCircle extends AbstractSpellDraw {

	public SpellCircle() {
		super(2);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		if (onSurface) {
			p0.y += 1;
			p1.y += 1;
		}
		IDrawable d = new CircleXZ(p0, p1);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
