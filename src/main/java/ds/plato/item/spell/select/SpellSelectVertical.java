package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Modifier;
import ds.plato.player.HotbarSlot;

public class SpellSelectVertical extends AbstractSpellSelect {

	public SpellSelectVertical(IUndo undo, ISelect select, IPick pick) {
		super(Select.eastWest, undo, select, pick);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		positions = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? Select.northSouth : Select.eastWest;
		super.invoke(world, slotEntries);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
}
