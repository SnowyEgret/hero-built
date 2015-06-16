package ds.plato.item.spell.select;


public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow() {
		super(Select.BELOW_INCLUSIVE);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
