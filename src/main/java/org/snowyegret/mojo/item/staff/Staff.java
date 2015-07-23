package org.snowyegret.mojo.item.staff;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.item.ItemBase;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.Player;

public class Staff extends ItemBase {

	static final int MAX_NUM_SPELLS = 9;

	// Adds information to rollover in creative tab
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		if (isEmpty(stack)) {
			rollOver.add(EnumChatFormatting.RED + "No spells on staff");
		} else {
			rollOver.add(EnumChatFormatting.GREEN + " " + numSpells(stack) + " spells on staff");
		}
	}

	// http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	// TODO Check that this is the default
	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1; // return any value greater than zero
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float sx, float sy, float sz) {

		// To compare item stacks and their tags on both sides
		// System.out.println("tag=" + stack.getTagCompound());
		// Return if called from the client thread
		if (world.isRemote) {
			return true;
		}

		Player player = new Player(playerIn);
		Modifiers modifiers = player.getModifiers();

		// We are on the server side. Open staff gui if space bar is down
		if (modifiers.isPressed(Modifier.SPACE)) {
			player.openGui(GuiHandler.GUI_STAFF);
			// Fix for Picking with a spell on an oak staff opens the staff gui #187
			// The key release was lost in the wash.
			modifiers.setPressed(Modifier.SPACE, false);
			return true;
		}

		// Get the current spell on this staff and use it
		if (!isEmpty(stack)) {
			Spell s = getSpell(stack);
			s.onItemUse(stack, playerIn, world, pos, side, sx, sy, sz);
			return true;
		}

		return false;
	}

	public Spell getSpell(ItemStack stack) {
		// System.out.println("tag=" + stack.getTagCompound());
		// Throwable().printStackTrace();
		if (isEmpty(stack)) {
			return null;
		}
		TagStaff t = new TagStaff(stack);
		Spell s = t.getSpell();
		if (s == null) {
			s = nextSpell(stack);
		}
		return s;
	}

	public Spell nextSpell(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		Spell s = null;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			if (t.getIndex() == MAX_NUM_SPELLS - 1) {
				t.setIndex(0);
			} else {
				t.incrementIndex(1);
			}
			s = t.getSpell();
			if (s != null) {
				break;
			}
			// if (s == null) {
			// continue;
			// } else {
			// break;
			// }
		}
		return s;
	}

	public Spell prevSpell(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		Spell s = null;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			if (t.getIndex() == 0) {
				t.setIndex(MAX_NUM_SPELLS - 1);
			} else {
				t.incrementIndex(-1);
			}
			s = t.getSpell();
			if (s != null) {
				break;
			}
			// if (s == null) {
			// continue;
			// } else {
			// break;
			// }
		}
		return s;
	}

	public int numSpells(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		int numSpells = 0;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			Spell s = t.getSpell(i);
			if (s != null)
				numSpells++;
		}
		return numSpells;
	}

	public boolean isEmpty(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			Spell s = t.getSpell(i);
			if (s != null) {
				return false;
			}
		}
		return true;
	}

}
