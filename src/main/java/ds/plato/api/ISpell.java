package ds.plato.api;

import ds.plato.item.spell.SpellInfo;
import ds.plato.player.HotbarSlot;

public interface ISpell {

	//Takes one or more slots in the players hotbar. Some spells (SpellFill, SpellFillRandom, SpellFillChecker) use
	//more than one block and also the position of the block in the hotbar
	public abstract void invoke(IWorld world, HotbarSlot... slots);

	public abstract String getMessage();

	public abstract int getNumPicks();

	public abstract boolean isPicking();

	public abstract void reset();

	public abstract SpellInfo getInfo();

}