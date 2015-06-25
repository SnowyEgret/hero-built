package org.snowyegret.mojo.item.spell;

import org.snowyegret.mojo.player.IPlayer;

public interface ISpell {

	public void invoke(IPlayer player);

	public abstract String getMessage();

	public abstract int getNumPicks();

	public abstract void reset(IPlayer player);

	public abstract SpellInfo getInfo();

}