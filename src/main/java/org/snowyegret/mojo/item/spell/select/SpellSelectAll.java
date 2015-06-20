package org.snowyegret.mojo.item.spell.select;


public class SpellSelectAll extends AbstractSpellSelect {

	public SpellSelectAll() {
		super(Select.ALL);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
