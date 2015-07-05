package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;

import ds.geom.IDrawable;
import ds.geom.surface.Cone;

public class SpellCone extends AbstractSpellDraw {

	public SpellCone() {
		super(3);
	}

	@Override
	public void invoke(Player player) {
		Pick[] picks = player.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		Point3d p2 = picks[2].point3d();
		// If the third pick is in the plane of the first set the height as
		// the distance to the second pick. This distance can be read from the overlay.
		if (Math.abs(p0.y - p2.y) < .00001) {
			p2.y = p2.y + p1.distance(p2);
		}
		IDrawable d = new Cone(p0, p1, p2);
		draw(d, player, picks[0].side);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
	
}
