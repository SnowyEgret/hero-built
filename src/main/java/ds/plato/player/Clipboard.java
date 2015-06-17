package ds.plato.player;

import net.minecraft.util.BlockPos;
import ds.plato.select.Selection;

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
