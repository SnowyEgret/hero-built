package ds.plato.item.spell.transform;

import net.minecraft.util.BlockPos;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellFillChecker extends AbstractSpellTransform {

	public SpellFillChecker() {
		super();
	}

	@Override
	public void invoke(IWorld world, final IPlayer player) {
		transformSelections(world, player, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				int i = 0;
				BlockPos pos = s.getPos();
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();
				if (((x & 1) == 0 && (z & 1) == 0) || ((x & 1) == 1 && (z & 1) == 1)) {
					i = ((y & 1) == 0) ? 0 : 1;
				} else {
					i = ((y & 1) == 0) ? 1 : 0;
				}
				//s = new Selection(pos, player.getHotbar()[i].state);
				s = new Selection(pos, player.getHotbar().getBlock(i));
				return s;
			}
		});
	}	
}
