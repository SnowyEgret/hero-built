package ds.plato.item.spell;

import ds.plato.player.IPlayer;
import ds.plato.world.IWorld;

public interface ISpell {

	public abstract void invoke(IWorld world, IPlayer player);

	public abstract String getMessage();

	public abstract int getNumPicks();

	public abstract boolean isPicking();

	public abstract void reset();

	public abstract SpellInfo getInfo();

}