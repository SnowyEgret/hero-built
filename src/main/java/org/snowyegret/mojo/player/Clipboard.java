package org.snowyegret.mojo.player;

import org.snowyegret.mojo.select.Selection;

import net.minecraft.util.BlockPos;

public class Clipboard {

	private Iterable<Selection> selections;
	private BlockPos origin;

	public void setSelections(Iterable<Selection> selections) {
		this.selections = selections;
	}

	public Iterable<Selection> getSelections() {
		return selections;
	}

	public void setOrigin(BlockPos origin) {
		this.origin = origin;	
	}

	public BlockPos getOrigin() {
		return origin;
	}

}
