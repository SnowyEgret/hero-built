package ds.plato.item.staff;

import net.minecraft.item.ItemStack;
import ds.plato.item.spell.ISpell;
import ds.plato.pick.IPick;

public interface IStaff {

	public ISpell getSpell(ItemStack stack, IPick pickManager);

	public ISpell nextSpell(ItemStack stack, IPick pickManager);

	public ISpell prevSpell(ItemStack stack, IPick pickManager);

	public int numSpells(ItemStack stack);

	public boolean isEmpty(ItemStack stack);

}
