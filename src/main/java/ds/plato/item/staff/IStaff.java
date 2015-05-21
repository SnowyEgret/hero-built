package ds.plato.item.staff;

import ds.plato.item.spell.ISpell;
import net.minecraft.item.ItemStack;

public interface IStaff {

	public ISpell getSpell(ItemStack stack);

	public ISpell nextSpell(ItemStack stack);

	public ISpell previousSpell(ItemStack stack);

	public int numSpells(ItemStack stack);

	public boolean isEmpty(ItemStack stack);

}
