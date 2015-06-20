package org.snowyegret.plato.item.spell.draw;

import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;
import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;

import ds.geom.PointSet;

public class SpellMeasure extends AbstractSpellDraw {

	public SpellMeasure() {
		super(2);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		message = String.format("Distance: %.1f", p0.distance(p1));
		PointSet points = new PointSet();
		if (modifiers.isPressed(Modifier.SHIFT)) {
			// TODO if point is on border between two blocks, draw both blocks
			points.addPoint(interpolate(p0, p1, .5d));
		}
		if (modifiers.isPressed(Modifier.SHIFT)) { // Alt
			double d = 1 / 3d;
			for (int i = 1; i < 3; i++) {
				double dd = i * d;
				points.addPoint(interpolate(p0, p1, i * d));
			}
		}
		if (modifiers.isPressed(Modifier.CTRL)) {
			points.addPoints(p0, p1);
		}
		if (!points.isEmpty()) {
			draw(points, player);
			//selectionManager.clearSelections(world);
		}
		//pickManager.clearPicks();
	}

	private Point3d interpolate(Point3d p0, Point3d p1, double d) {
		Point3d p = new Point3d();
		p.interpolate(p0, p1, d);
		return p;
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
