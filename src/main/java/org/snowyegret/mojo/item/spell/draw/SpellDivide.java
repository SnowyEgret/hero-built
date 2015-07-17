package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import net.minecraft.entity.player.EntityPlayerMP;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.message.client.SelectionMessage;
import org.snowyegret.mojo.message.client.SpellMessage;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.geom.PointSet;

public class SpellDivide extends AbstractSpellDraw {

	public SpellDivide() {
		super(2);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(Player player) {
		
		Modifiers modifiers = player.getModifiers();
		boolean drawMarkers = modifiers.isPressed(Modifier.CTRL);
		boolean divideByTwo = modifiers.isPressed(Modifier.SHIFT);
		boolean divideByThree = modifiers.isPressed(Modifier.ALT);
		
		Pick[] picks = player.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		player.clearPicks();
		
		player.sendMessage(new SpellMessage("item.spell_divide.message", p0.distance(p1)));
		PointSet points = new PointSet();
		if (divideByTwo) {
			// TODO if point is on border between two blocks, draw both blocks
			points.addPoint(interpolate(p0, p1, .5d));
		}
		if (divideByThree) {
			double d = 1 / 3d; 
			for (int i = 1; i < 3; i++) {
				double dd = i * d;
				points.addPoint(interpolate(p0, p1, i * d));
			}
		}
		if (drawMarkers) {
			points.addPoints(p0, p1);
		}
		if (!points.isEmpty()) {
			draw(points, player, picks[0].side);
		}
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	private Point3d interpolate(Point3d p0, Point3d p1, double d) {
		Point3d p = new Point3d();
		p.interpolate(p0, p1, d);
		return p;
	}
	
}
