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

	public SpellFill() {
		super();
	}

	@Override
	public void invoke(IWorld world, final IPlayer player) {
		transformSelections(world, player, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selection list.
				// Use first (left-most) block in inventory
				return new Selection(s.getPos(), player.getHotbar().firstBlock());
			}
		});
	}

	//Added for call from MouseHandler or SpellFillMessageHandler
	public void invoke(IWorld world, final IPlayer player, final IBlockState state) {
		transformSelections(world, player, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selection list.
				// Use first (left-most) block in inventory
				return new Selection(s.getPos(), state);
			}
		});
	}	
}
