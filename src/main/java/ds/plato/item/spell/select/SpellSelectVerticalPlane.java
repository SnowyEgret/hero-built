package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;

public class SpellSelectVerticalPlane extends AbstractSpellSelect {

	public SpellSelectVerticalPlane(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.XY, undo, select, pick);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		shellType = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? Shell.Type.YZ : Shell.Type.XY;
		super.invoke(world, slotEntries);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
}
