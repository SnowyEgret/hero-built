package ds.plato.item.staff;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ds.plato.Plato;
import ds.plato.item.spell.Spell;

public class TagStaff {

	private NBTTagCompound tag;
	private int size = Staff.maxNumSpells;
	private final String INDEX = "index";

	public TagStaff(ItemStack stack) {
		tag = stack.getTagCompound();
		if (tag == null) {
			System.out.println("Tag null - created a new one");
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
	}

	public Spell getSpell() {
		return getSpell(getIndex());
	}
	
	public Spell getSpell(int i) {
		if (i < 0 || i > size - 1) {
			throw new IllegalArgumentException("Index not in tag range: " + i);
		}
		Spell spell = null;
		String name = tag.getString(String.valueOf(i));
		if (name != null && !name.equals("")) {
			spell = (Spell) GameRegistry.findItem(Plato.ID, name);
			if (spell == null) {
				throw new RuntimeException("Game registry could not find item. name=" + name);
			}
		}
		return spell;
	}

	public ItemStack getItemStack(int i) {
		Spell s = getSpell(i);
		if (s != null) {
			return new ItemStack(s);
		}
		return null;
	}


	public void setSpell(int i, Spell s) {
		if (i < 0 || i > size - 1) {
			throw new IllegalArgumentException("Index not in tag range: " + i);
		}
		String n = s.getClass().getSimpleName();
		tag.setString(String.valueOf(i), n);
	}
	
	public void setItemStack(int i, ItemStack stack) {
		//System.out.println("i=" + i);
		//System.out.println("stack=" + stack);
		if (i < 0 || i > size - 1) {
			throw new IllegalArgumentException("Index not in tag range: " + i);
		}
		
		if (stack == null) {
			tag.removeTag(String.valueOf(i));
		} else {
			Spell s = (Spell) stack.getItem();
			setSpell(i, s);
			//String n = stack.getItem().getClass().getSimpleName();
			//tag.setString(String.valueOf(i), n);
		}
		//System.out.println("tag=@" + System.identityHashCode(tag) + tag);
		// new Throwable().printStackTrace();
	}

	public int getIndex() {
		return tag.getInteger(INDEX);
	}

	public void setIndex(int i) {
		tag.setInteger(INDEX, i);
	}

	public void incrementIndex(int increment) {
		int i = tag.getInteger(INDEX);
		i = i + increment;
		tag.setInteger(INDEX, i);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TagStaff [tag=");
		builder.append(tag);
		builder.append("]");
		return builder.toString();
	}

	public NBTTagCompound getTag() {
		return tag;
	}
}
