package ds.plato.item.spell.transform;

import ds.plato.select.Selection;

public interface ITransform<S> {
	public Iterable<Selection> transform(Selection selection);
}
