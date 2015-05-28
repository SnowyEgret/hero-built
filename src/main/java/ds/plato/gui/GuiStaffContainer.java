package ds.plato.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

//http://www.minecraftforge.net/wiki/Containers_and_GUIs
public class GuiStaffContainer extends Container {

	private IInventory inventoryStaff;

	public GuiStaffContainer(InventoryPlayer inventoryPlayer, IInventory inventoryStaff) {
		this.inventoryStaff = inventoryStaff;

		for (int i = 0; i < inventoryStaff.getSizeInventory(); i++) {
			//Overrides isItemValid to permit only spells in these slots
			addSlotToContainer(new GuiStaffSlot(inventoryStaff, i, 8 + i * 18, 18));
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 49 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 107));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		boolean isUsable = inventoryStaff.isUseableByPlayer(player);
		return isUsable;
	}

	//Called when a player shift clicks on a slot
	// @Override
	// public ItemStack transferStackInSlot(EntityPlayer player, int i) {
	// System.out.println("[GuiStaffContainer.transferStackInSlot] i=" + i);
	// ItemStack stack = null;
	// Slot slot = (Slot) inventorySlots.get(i);
	//
	// // null checks and checks if the item can be stacked (maxStackSize > 1)
	// if (slot != null && slot.getHasStack()) {
	// stack = slot.getStack().copy();
	//
	// // merges the item into player inventory since its in the tileEntity
	// if (i < 9) {
	// if (!this.mergeItemStack(stack, 0, 35, true)) {
	// return null;
	// }
	// }
	// // places it into the tileEntity is possible since its in the player inventory
	// else if (!this.mergeItemStack(stack, 0, 9, false)) {
	// return null;
	// }
	//
	// if (stack.stackSize == 0) {
	// slot.putStack(null);
	// } else {
	// slot.onSlotChanged();
	// }
	//
	// if (stack.stackSize == stack.stackSize) {
	// return null;
	// }
	// slot.onPickupFromSlot(player, stack);
	// }
	// return stack;
	// }
}
