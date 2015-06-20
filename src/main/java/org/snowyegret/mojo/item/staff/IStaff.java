package org.snowyegret.mojo.item.staff;

import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.pick.IPick;

import net.minecraft.item.ItemStack;

public interface IStaff {

	public ISpell getSpell(ItemStack stack, IPick pickManager);

	public ISpell nextSpell(ItemStack stack, IPick pickManager);

	public ISpell prevSpell(ItemStack stack, IPick pickManager);

	public int numSpells(ItemStack stack);

	public boolean isEmpty(ItemStack stack);

}
