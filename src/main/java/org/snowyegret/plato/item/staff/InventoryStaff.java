package org.snowyegret.plato.item.staff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryStaff implements IInventory {

	private TagStaff tag;
	private ItemStack parentStack;
	private int parentSlot;
	private IInventory parentInventory;

	//We could just pass the stack here, but we need these references for setInventorySlotContents
	public InventoryStaff(IInventory parentInventory, int parentSlot) {
	//public InventoryStaff(ItemStack stack) {
		//this.stack = stack;
		this.parentInventory = parentInventory;
		this.parentSlot = parentSlot;
		parentStack = parentInventory.getStackInSlot(parentSlot);
		tag = new TagStaff(parentStack);
	}

	// IInventory--------------------------------------------------------

	@Override
	public ItemStack getStackInSlot(int slot) {
		return tag.getStack(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = tag.getStack(slot);
		tag.setStack(slot, null);
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		// Seems like this is not being called
		return tag.getStack(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		tag.setStack(slot, stack);
		//Fix for issue #42: Staff loses spells when used after adding spells
		parentStack.setTagCompound(tag.getTag());
		parentInventory.setInventorySlotContents(parentSlot, parentStack);
	}

	@Override
	public int getSizeInventory() {
		return Staff.MAX_NUM_SPELLS;
	}

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

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	// ---------------------------------- New 1.8 methods in IInventory

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// Seems this is not being called
		System.out.println();
//		for (int i = 0; i < parentInventory.getSizeInventory(); ++i) {
//			setInventorySlotContents(i, null);
//		}
	}
}