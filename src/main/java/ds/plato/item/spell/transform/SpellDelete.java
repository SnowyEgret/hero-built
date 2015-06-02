package ds.plato.item.spell.transform;

import net.minecraft.init.Blocks;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellDelete extends AbstractSpellTransform {

	public SpellDelete(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slots) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selectionManager's selection list.
				//TODO pass state to constructor
				Selection sel = new Selection(s.getPos(), Blocks.air);
				sel.setState(Blocks.air.getDefaultState());
				return sel;
			}
		});
	}
}
