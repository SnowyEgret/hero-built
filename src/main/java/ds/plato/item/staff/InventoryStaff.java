package ds.plato.item.staff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ds.plato.Plato;
import ds.plato.item.spell.Spell;

public class InventoryStaff implements IInventory {

	private TagWrapper tag;
	private int size = 9;
	IInventory inventory;
	ItemStack stack;
	private int slot;

	public InventoryStaff(IInventory inventoryContainingStaffItemStack, int slotInInventoryContainingStaffItemStack) {
		this.inventory = inventoryContainingStaffItemStack;
		this.slot = slotInInventoryContainingStaffItemStack;
		stack = inventoryContainingStaffItemStack.getStackInSlot(slotInInventoryContainingStaffItemStack);
		NBTTagCompound t = stack.getTagCompound();
		if (t == null) {
			t = new NBTTagCompound();
			stack.setTagCompound(t);
		}
		tag = new TagWrapper(t, size);
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return tag.getItemStack(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int amount) {
		ItemStack stack = tag.getItemStack(i);
		tag.setItemStack(i, null);
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (tag.getItemStack(i) != null) {
			ItemStack itemstack = tag.getItemStack(i);
			tag.setItemStack(i, null);
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		tag.setItemStack(i, stack);
		inventory.setInventorySlotContents(slot, this.stack);
	}

	@Override
	public int getSizeInventory() {
		return size;
	}

//1.8
//	@Override
//	public String getInventoryName() {
//		return null;
//	}

//1.8
//	@Override
//	public boolean hasCustomInventoryName() {
//		return false;
//	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	//1.8
//	@Override
//	public void openInventory() {
//	}

	//1.8
//	@Override
//	public void closeInventory() {
//	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		return true;
	}
	
	//---------------------------------- New methods in IInventory
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	private class TagWrapper {

		private NBTTagCompound tag;
		private int size;

		public TagWrapper(NBTTagCompound tag, int size) {
			this.tag = tag;
			this.size = size;
		}

		public ItemStack getItemStack(int i) {
			if (i < 0 || i > size - 1) {
				throw new IllegalArgumentException("Index not in tag range: " + i);
			}
			// System.out.println("[MyTagWrapper.getItemStack] tag=" + tag);
			String itemSimpleClassName = tag.getString(String.valueOf(i));
			if (itemSimpleClassName != null && !itemSimpleClassName.equals("")) {
				Spell spell = (Spell) GameRegistry.findItem(Plato.ID, itemSimpleClassName);
				if (spell == null) {
					throw new RuntimeException("Game registry could not find item.  itemSimpleClassName="
							+ itemSimpleClassName);
				}
				// System.out.println("[TagWrapper.getItemStack] Looked up spell in game registry. spell=" + spell);
				return new ItemStack(spell);
			}
			return null;
		}

		public void setItemStack(int i, ItemStack stack) {
			if (i < 0 || i > size - 1) {
				throw new IllegalArgumentException("Index not in tag range: " + i);
			}
			if (stack == null) {
				tag.removeTag(String.valueOf(i));
			} else {
				String n = stack.getItem().getClass().getSimpleName();
				// String n = StringUtils.toCamelCase(stack.getItem().getClass());
				tag.setString(String.valueOf(i), n);
			}
			System.out.println("[TagWrapper.setItemStack] i=" + i + ", tag=@" + System.identityHashCode(tag) + tag);
		}
	}


}