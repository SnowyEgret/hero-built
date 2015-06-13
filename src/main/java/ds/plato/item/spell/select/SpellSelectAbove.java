package ds.plato.item.spell.select;


public class SpellSelectAbove extends AbstractSpellSelect {

	public SpellSelectAbove() {
		super(Select.aboveInclusive);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
