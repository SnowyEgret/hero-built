package org.snowyegret.plato.item.spell.transform;

import org.snowyegret.plato.select.Selection;

public interface ITransform<S> {
	public Iterable<Selection> transform(Selection selection);
}
