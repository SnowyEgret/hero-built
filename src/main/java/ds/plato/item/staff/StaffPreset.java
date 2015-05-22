package ds.plato.item.staff;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;

public abstract class StaffPreset extends Staff {

	private List<Spell> spells;

	protected StaffPreset(IPick pickManager, List<Spell> spells) {
		super(pickManager);
		this.spells = spells;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		initTag(stack);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		System.out.println("Creating a new stack and tag.");
		ItemStack stack = new ItemStack(this);
		initTag(stack);
		list.add(stack);
	}
	
	//Private---------------------------------------------------------------

	private void initTag(ItemStack stack) {
		TagStaff tag = new TagStaff(stack);
		int i = 0;
		for (Spell s : spells) {
			if (i < maxNumSpells) {
				tag.setSpell(i, s);
				i++;
			} else {
				System.out.println("No room on staff for spell " + s);
			}
		}
		tag.setIndex(0);
		stack.setTagCompound(tag.getTag());

//		stack.setTagCompound(new NBTTagCompound());
//		int i = 0;
//		for (Spell s : spells) {
//			if (i < maxNumSpells) {
//				String n = s.getClass().getSimpleName();
//				stack.getTagCompound().setString(String.valueOf(i), n);
//				i++;
//			} else {
//				System.out.println("No room on staff for spell " + s);
//			}
//		}
//		stack.getTagCompound().setInteger("index", 0);
	}	
}
