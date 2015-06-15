package ds.plato.item.spell;

import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;

public interface ISpell {

	public abstract void invoke(IPlayer player);

	public abstract String getMessage();

	public abstract int getNumPicks();

	public abstract void reset(IPlayer player);

	public abstract SpellInfo getInfo();

}