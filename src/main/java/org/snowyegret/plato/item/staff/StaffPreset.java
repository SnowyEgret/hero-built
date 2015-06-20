package org.snowyegret.plato.item.staff;

import java.util.List;

import org.snowyegret.plato.item.spell.Spell;
import org.snowyegret.plato.pick.IPick;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class StaffPreset extends Staff {

	private List<Spell> spells;

	protected StaffPreset(List<Spell> spells) {
		this.spells = spells;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		initTag(stack);
	}

	//http://www.minecraftforge.net/forum/index.php/topic,23385.msg118671.html
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List subItems) {
		System.out.println("Creating a new stack and tag.");
		ItemStack stack = new ItemStack(this);
		initTag(stack);
		subItems.add(stack);
	}
	
	//Private---------------------------------------------------------------

	private void initTag(ItemStack stack) {
		TagStaff tag = new TagStaff(stack);
		int i = 0;
		for (Spell s : spells) {
			if (i < MAX_NUM_SPELLS) {
				tag.setSpell(i, s);
				i++;
			} else {
				System.out.println("No room on staff for spell " + s);
			}
		}
		tag.setIndex(0);
		stack.setTagCompound(tag.getTag());
	}	
}
