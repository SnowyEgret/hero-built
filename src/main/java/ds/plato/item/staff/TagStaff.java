package ds.plato.item.staff;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ds.plato.Plato;
import ds.plato.item.spell.Spell;

public class TagStaff {

	private NBTTagCompound tag;
	private int size = Staff.MAX_NUM_SPELLS;
	private final String INDEX = "index";

	public TagStaff(ItemStack stack) {
		tag = stack.getTagCompound();
		//System.out.println("tag=" + tag);
		//new Throwable().printStackTrace();
		if (tag == null) {
			System.out.println("Tag null - created a new one");
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
			setIndex(0);
		}
	}

	public Spell getSpell() {
		return getSpell(getIndex());
	}

	public Spell getSpell(int i) {
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

	public ItemStack getStack(int i) {
		Spell s = getSpell(i);
		if (s != null) {
			return new ItemStack(s);
		}
		return null;
	}

	public void setSpell(int i, Spell spell) {
		String n = spell.getClass().getSimpleName();
		tag.setString(String.valueOf(i), n);
	}

	public void setStack(int i, ItemStack stack) {
		if (stack == null) {
			tag.removeTag(String.valueOf(i));
		} else {
			Spell s = (Spell) stack.getItem();
			setSpell(i, s);
		}
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
