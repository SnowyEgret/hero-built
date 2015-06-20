package org.snowyegret.mojo.item.spell.transform;

import org.snowyegret.mojo.select.Selection;

public interface ITransform<S> {
	public Iterable<Selection> transform(Selection selection);
}
