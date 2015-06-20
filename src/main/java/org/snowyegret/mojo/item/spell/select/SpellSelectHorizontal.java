package org.snowyegret.mojo.item.spell.select;


public class SpellSelectHorizontal extends AbstractSpellSelect {

	public SpellSelectHorizontal() {
		super(Select.HORIZONTAL);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}
}
