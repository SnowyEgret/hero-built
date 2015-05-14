package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.player.HotbarSlot;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface(IUndo undo, ISelect select, IPick pick) {
		super(Select.all, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slots) {
		setConditions(new IsOnSurface());
		super.invoke(world, slots);
	}
}
