package ds.plato.item.spell.transform;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.IPlantable;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellFill extends AbstractSpellTransform {

	public SpellFill(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
	}

	@Override
	public void invoke(IWorld world, final IPlayer player) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selection list.
				// Use first (left-most) block in inventory
				return new Selection(s.getPos(), player.getHotbar()[0].state);
			}
		});
	}	
}
