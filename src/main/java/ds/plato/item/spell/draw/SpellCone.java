package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.Cone;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellCone extends AbstractSpellDraw {

	public SpellCone(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(3, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Cone(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		draw(d, world, player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
