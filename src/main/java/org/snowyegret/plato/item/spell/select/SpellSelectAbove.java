package org.snowyegret.plato.item.spell.select;


public class SpellSelectAbove extends AbstractSpellSelect {

	public SpellSelectAbove() {
		super(Select.ABOVE_INCLUSIVE);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
