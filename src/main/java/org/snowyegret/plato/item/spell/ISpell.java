package org.snowyegret.plato.item.spell;

import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.player.IPlayer;

public interface ISpell {

	public abstract void invoke(IPlayer player);

	public abstract String getMessage();

	public abstract int getNumPicks();

	public abstract void reset(IPlayer player);

	public abstract SpellInfo getInfo();

}