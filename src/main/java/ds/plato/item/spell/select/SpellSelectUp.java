package ds.plato.item.spell.select;


public class SpellSelectUp extends AbstractSpellSelect {

	public SpellSelectUp() {
		super(Select.UP);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
