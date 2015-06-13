package ds.plato.item.spell.select;


public class SpellSelectDown extends AbstractSpellSelect {

	public SpellSelectDown() {
		super(Select.down);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
}
