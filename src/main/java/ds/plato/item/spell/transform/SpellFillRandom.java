package ds.plato.item.spell.transform;

import net.minecraft.block.state.IBlockState;
import ds.plato.event.KeyHandler;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.SpellInvoker;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;

public class SpellFillRandom extends AbstractSpellTransform {

	public SpellFillRandom() {
		super();
	}

	@Override
	public void invoke(final IPlayer player) {
		transformSelections(player, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				IBlockState state = player.getHotbar().randomBlock();
				return new Selection(s.getPos(), state);
			}
		});
		// Right clicking is same
		player.setLastSpell(this);

	}
}
