package org.snowyegret.mojo.item.staff;

import net.minecraft.item.ItemStack;

import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.pick.PickManager;

public interface IStaff {

	public ISpell getSpell(ItemStack stack, PickManager pickManager);
	
	public ISpell nextSpell(ItemStack stack, PickManager pickManager);

	public ISpell prevSpell(ItemStack stack, PickManager pickManager);

	public int numSpells(ItemStack stack);

	public boolean isEmpty(ItemStack stack);

}
