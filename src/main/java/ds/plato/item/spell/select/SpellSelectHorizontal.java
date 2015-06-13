package ds.plato.item.spell.select;


public class SpellSelectHorizontal extends AbstractSpellSelect {

	public SpellSelectHorizontal() {
		super(Select.horizontal);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}
}
