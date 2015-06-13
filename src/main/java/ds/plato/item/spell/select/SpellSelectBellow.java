package ds.plato.item.spell.select;


public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow() {
		super(Select.belowInclusive);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
