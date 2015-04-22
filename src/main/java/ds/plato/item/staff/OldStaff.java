package ds.plato.item.staff;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import ds.plato.api.IPick;
import ds.plato.api.ISpell;
import ds.plato.item.spell.ISelector;
import ds.plato.item.spell.Spell;

@Deprecated
public abstract class OldStaff extends Item implements ISelector {

	protected Spell[] spells = new Spell[9];
	protected int ordinal = 0;
	private IPick pickManager;
	private String name = "Staff";

	// private boolean spellsInitialized = false;
	// To reduce overhead of onUpdate
	// private boolean isDirty = false;

	protected OldStaff(IPick pickManager) {
		this.pickManager = pickManager;
	}

	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}

	public boolean hasRecipe() {
		return getRecipe() != null;
	}

	@Override
	public void select(ItemStack stack, int x, int y, int z, int side) {
		if (!isEmpty()) {
			getSpell().onMouseClickLeft(stack, x, y, z, side);
		}
	}

	// @Override
	// public void onMouseClickRight(ItemStack stack, int x, int y, int z, int side) {
	// // For now, jump while right clicking to open gui again.
	// if (isEmpty() || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	// Player.getPlayer().openGui(3);
	// } else {
	// getSpell().onMouseClickRight(stack, x, y, z, side);
	// }
	// }

	// @Override
	// public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
	// int par5, int par6, int par7, float par8, float par9, float par10) {
	// if (isEmpty() || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	// Player.client().openGui(3);
	// } else {
	// return getSpell().onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9,
	// par10);
	// }
	// return false;
	// }

	public Spell getSpell() {
		if (isEmpty()) {
			return null;
		} else {
			Spell s = spells[ordinal];
			if (s == null) {
				s = nextSpell();
			}
			return s;
		}
	}

	public Spell nextSpell() {
		Spell s = null;
		for (int i = 0; i < spells.length; i++) {
			if (ordinal == spells.length - 1) {
				ordinal = 0;
			} else {
				ordinal++;
			}
			s = spells[ordinal];
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		return s;
	}

	public ISpell previousSpell() {
		ISpell s = null;
		for (int i = 0; i < spells.length; i++) {
			if (ordinal == 0) {
				ordinal = spells.length - 1;
			} else {
				ordinal--;
			}
			s = spells[ordinal];
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		return s;
	}

	// Called in preInit to initialize staffs
	public void addSpell(Spell spell) {
		for (int i = 0; i < spells.length; i++) {
			ISpell s = spells[i];
			if (s == null) {
				spells[i] = spell;
				return;
			}
		}
		throw new RuntimeException("No room for spell on staff. spell=" + spell + ", staff=" + this);
	}

	public int numSpells() {
		int numSpells = 0;
		for (ISpell s : spells) {
			if (s != null)
				numSpells++;
		}
		return numSpells;
	}

	public boolean isEmpty() {
		for (ISpell s : spells) {
			if (s != null) {
				return false;
			}
		}
		return true;
	}

	// // Sets tag when staff is crafted
	// @Override
	// public void onCreated(ItemStack stack, World w, EntityPlayer player) {
	// if (stack.stackTagCompound == null) {
	// stack.setTagCompound(new NBTTagCompound());
	// }
	// System.out.println("[Staff.onCreated] stack.stackTagCompound=" + stack.stackTagCompound);
	// // Read here http://forgetutorials.weebly.com/nbt-tags.html that tag must be initialized
	// // Not convinced he is right
	// // for (int i = 0; i < spells.length; i++) {
	// // stack.stackTagCompound.setString(String.valueOf(i), "");
	// // }
	// }

	// Adds information to rollover in creative tab
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		if (isEmpty()) {
			rollOver.add(EnumChatFormatting.RED + "No spells on staff");
		} else {
			rollOver.add(EnumChatFormatting.GREEN + " " + numSpells() + " spells on staff");
		}
	}

	// On first call, initializes spells array and ordinal from stack's tag.
	// Subsequently, on tick, syncs tag with spells array and ordinal
	// For subclasses of Staff which have fixed spells, only syncs ordinal.
	// @Override
	// public void onUpdate(ItemStack stack, World w, Entity entity, int par4, boolean par5) {
	// if (w.isRemote) {
	// return;
	// }
	// if (stack.getTagCompound() == null) {
	// stack.setTagCompound(new NBTTagCompound());
	// }
	// NBTTagCompound tag = stack.getTagCompound();
	//
	// // First time through, set spells array with tag info
	// if (!spellsInitialized) {
	// readFromNBT(tag);
	// } else {
	// if (isDirty) {
	// writeToNBT(tag);
	// }
	// }
	// //System.out.println("[Staff.onUpdate] stack.getTagCompound()=" + stack.getTagCompound());
	// }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append(" [ordinal=");
		builder.append(ordinal);
		builder.append(", spells=");
		builder.append(Arrays.toString(spells));
		builder.append("]");
		return builder.toString();
	}

	// ---------------------------------------------------------------------------------
	// Implementation of interface IInventory. Needed for GuiStaff and GuiStaffContainer

	// @Override
	// public int getSizeInventory() {
	// return spells.length;
	// }
	//
	// // Called every tick by GUI to draw screen.
	// // Called by decrStackSize
	// @Override
	// public ItemStack getStackInSlot(int i) {
	// if (spells[i] == null) {
	// return null;
	// } else {
	// return new ItemStack(spells[i]);
	// }
	// }
	//
	// @Override
	// public ItemStack decrStackSize(int i, int amount) {
	// System.out.println("\n[Staff.decrStackSize] i=" + i + ", amount=" + amount);
	// ItemStack stack = getStackInSlot(i);
	// // //if (stack != null) {
	// // Simplified because inventory stack limit is 1
	// // if (stack.stackSize <= amount) {
	// setInventorySlotContents(i, null);
	// // } else {
	// // stack = stack.splitStack(amount);
	// // if (stack.stackSize == 0) {
	// // setInventorySlotContents(i, null);
	// // }
	// // }
	// // } else {
	// // System.out.println("[Staff.decrStackSize] UNEXPEXTED! stack=" + stack);
	// // }
	// //System.out.println("[Staff.decrStackSize] stack=" + stack);
	// return stack;
	// }
	//
	// // When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	// // like when you close a workbench GUI.
	// // Not being called
	// @Override
	// public ItemStack getStackInSlotOnClosing(int i) {
	// System.out.println("[Staff.getStackInSlotOnClosing] i=" + i);
	// ItemStack stack = getStackInSlot(i);
	// if (stack != null) {
	// setInventorySlotContents(i, null);
	// }
	// return stack;
	// }
	//
	// @Override
	// public void setInventorySlotContents(int i, ItemStack stack) {
	// System.out.println("[Staff.setInventorySlotContents] i=" + i + ", stack=" + stack);
	// // new Exception().printStackTrace();
	// if (stack == null) {
	// // Fix for issue #85 Spells cannot be re-positioned on staff
	// spells[i] = null;
	// } else {
	// spells[i] = (Spell) stack.getItem();
	// }
	// }
	//
	// @Override
	// public String getInventoryName() {
	// return name;
	// }
	//
	// @Override
	// public boolean hasCustomInventoryName() {
	// return false;
	// }
	//
	// @Override
	// public int getInventoryStackLimit() {
	// // System.out.println("[Staff.getInventoryStackLimit]");
	// return 1;
	// }
	//
	// @Override
	// public void markDirty() {
	// this.isDirty = true;
	// }
	//
	// // Not being called
	// @Override
	// public boolean isUseableByPlayer(EntityPlayer player) {
	// System.out.println("[Staff.isUseableByPlayer] player=" + player);
	// return true;
	// }
	//
	// @Override
	// public void openInventory() {
	// }
	//
	// @Override
	// public void closeInventory() {
	// }
	//
	// // http://www.minecraftforge.net/forum/index.php?topic=14115.0
	// // Only called by hoppers, etc. Extend Slot and override isItemValid
	// @Override
	// public boolean isItemValidForSlot(int i, ItemStack stack) {
	// return true;
	// }
	//
	// // -------------------------------------------------------
	//
	// private void readFromNBT(NBTTagCompound tag) {
	// // Prepared staffs already have spells.
	// if (this instanceof StaffOak || this instanceof StaffBirch) {
	// System.out.println("[Staff.readFromNBT] Initializing spells. tag=" + tag);
	// int i = 0;
	// while (true) {
	// String spellClassName = tag.getString(String.valueOf(i));
	// if (spellClassName != null && !spellClassName.equals("")) {
	// System.out.println("[Staff.readFromNBT] Found string in tag: i=" + i + ", spellClassName="
	// + spellClassName);
	// Spell spell = (Spell) GameRegistry.findItem(Plato.ID, spellClassName);
	// if (spell == null) {
	// throw new RuntimeException("Game registry could not find item.  spellClassName="
	// + spellClassName);
	// }
	// System.out.println("[Staff.readFromNBT] Looked up spell in game registry. spell=" + spell);
	// spells[i] = spell;
	// i++;
	// } else {
	// break;
	// }
	// }
	// }
	// ordinal = tag.getInteger("ordinal");
	// spellsInitialized = true;
	// System.out.println("[Staff.readFromNBT] Staff initialized. " + this);
	// }
	//
	// private void writeToNBT(NBTTagCompound tag) {
	// // Prepared staffs can't be reordered (Only in creative mode - no recipe)
	// if (this instanceof StaffOak || this instanceof StaffBirch) {
	// int i = 0;
	// for (Spell s : spells) {
	// if (s == null) {
	// // If tags are not initialized in on created
	// tag.removeTag(String.valueOf(i));
	// } else {
	// String n = s.getClass().getSimpleName();
	// tag.setString(String.valueOf(i), n);
	// }
	// i++;
	// }
	// }
	// tag.setInteger("ordinal", ordinal);
	// System.out.println("[Staff.writeToNBT] tag=" + tag);
	// isDirty = false;
	// }
	//
}
