package ds.plato.item.staff;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ds.plato.api.IPick;
import ds.plato.item.spell.Spell;

public abstract class StaffPreset extends Staff {

	private List<Spell> spells;

	protected StaffPreset(IPick pickManager, List<Spell> spells) {
		super(pickManager);
		this.spells = spells;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		setTag(stack);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		ItemStack stack = new ItemStack(this);
		setTag(stack);
		list.add(stack);
	}

	private void setTag(ItemStack stack) {
		stack.setTagCompound(new NBTTagCompound());
		int i = 0;
		for (Spell s : spells) {
			if (i < 9) {
				String n = s.getClass().getSimpleName();
				stack.getTagCompound().setString(String.valueOf(i), n);
				i++;
			} else {
				System.out.println("[StaffPreset.setTag] No room on staff for spell " + s);
			}
		}
		stack.getTagCompound().setInteger("o=", 0);
	}
	
	
}
