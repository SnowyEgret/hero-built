package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.EnumFacing;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface(IUndo undo, ISelect select, IPick pick) {
		super(Select.all, undo, select, pick);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slots) {
		EnumFacing side = pickManager.getPicks()[0].side;
		boolean ignoreSide = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			positions = Select.all;
			ignoreSide = true;
		} else {
			positions = Select.planeForSide(side);
		}
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(world, slots);
	}
}
