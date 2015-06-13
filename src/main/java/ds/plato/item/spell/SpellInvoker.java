package ds.plato.item.spell;

import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.world.IWorld;

public class SpellInvoker {

	private ISpell spell;
	private IWorld world;
	private IPlayer player;
	private IPick pickManager;

	public SpellInvoker(IPick pickManager, ISpell spell, IWorld world, IPlayer player) {
		this.spell = spell;
		this.world = world;
		this.player = player;
		this.pickManager = pickManager;
	}

	public void invoke() {
		if (spell == null) {
			System.out.println("Spell is null");
			return;
		}
		pickManager.repick(world);
		//TODO How to pass modifiers?
		spell.invoke(world, player);		
	}

	public ISpell getSpell() {
		return spell;
	}

}
