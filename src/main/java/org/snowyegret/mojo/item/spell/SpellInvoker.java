package org.snowyegret.mojo.item.spell;

import org.snowyegret.mojo.player.IPlayer;

@Deprecated
public class SpellInvoker {

	private ISpell spell;
	private IPlayer player;

	public SpellInvoker(IPlayer player, ISpell spell) {
		this.player = player;
		this.spell = spell;
	}

	public void invoke() {
		if (spell == null) {
			System.out.println("Spell is null");
			return;
		}
		player.getPickManager().repick();
		//TODO How to pass modifiers?
		spell.invoke(player);		
	}

	public ISpell getSpell() {
		return spell;
	}

}
