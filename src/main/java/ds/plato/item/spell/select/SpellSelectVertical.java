package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.player.IPlayer;

public class SpellSelectVertical extends AbstractSpellSelect {

	public SpellSelectVertical() {
		super(Select.eastWest);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IPlayer player) {
		positions = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? Select.northSouth : Select.eastWest;
		super.invoke(player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
	
}
