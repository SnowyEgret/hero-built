package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface() {
		super(Select.ALL);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IPlayer player) {
		EnumFacing side = player.getPickManager().firstPick().side;
		boolean ignoreSide = false;
		if(player.getModifiers().isPressed(Modifier.SHIFT)) {
			positions = Select.ALL;
			ignoreSide = true;
		} else {
			positions = Select.planeForSide(side);
		}
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(player);
	}
	
}
