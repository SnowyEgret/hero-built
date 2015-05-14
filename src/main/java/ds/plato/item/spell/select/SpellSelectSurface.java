package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

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
		EnumFacing side = pickManager.getPicks()[0].side;
		boolean ignoreSide = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			positions = Select.all;
			ignoreSide = true;
		} else {
			positions = Select.planeForSide(side);
		}
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(world, slots);
	}
}
