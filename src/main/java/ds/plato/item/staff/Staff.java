package ds.plato.item.staff;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
import ds.plato.item.ItemBase;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;

public abstract class Staff extends ItemBase implements IStaff {

	static int maxNumSpells = 9;
	private IPick pickManager;

	protected Staff(IPick pickManager) {
		this.pickManager = pickManager;
	}

	//Item--------------------------------------------------------

	// Adds information to rollover in creative tab
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		if (isEmpty(stack)) {
			rollOver.add(EnumChatFormatting.RED + "No spells on staff");
		} else {
			rollOver.add(EnumChatFormatting.GREEN + " " + numSpells(stack) + " spells on staff");
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float sx, float sy, float sz) {

		//To compare item stacks on both sides
		System.out.println("tag=" + stack.getTagCompound());

		if (world.isRemote) {
			return true;
		}

		// Open staff gui if space is down
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			player.openGui(Plato.instance, 3, world, 0, 0, 0);
			return true;
		}
		// Get the current spell and use it
		if (!isEmpty(stack)) {
			Spell s = getSpell(stack);
			s.onItemUse(stack, player, world, pos, side, sx, sy, sz);
			return true;
		}
		return false;
	}

	//ItemBase----------------------------------------------------------------------
	
	//Passes call on to current spell
	@Override
	public void onMouseClickLeft(ItemStack stack, BlockPos pos, EnumFacing sideHit) {
		if (!isEmpty(stack)) {
			getSpell(stack).onMouseClickLeft(stack, pos, sideHit);
		} else {
			System.out.println("Cannot select with an empty staff.");
		}
	}

	// IStaff ----------------------------------------------------------------------

	@Override
	public Spell getSpell(ItemStack stack) {
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

	@Override
	public Spell nextSpell(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		Spell s = null;
		for (int i = 0; i < maxNumSpells; i++) {
			if (t.getIndex() == maxNumSpells - 1) {
				t.setIndex(0);
			} else {
				t.incrementIndex(1);
			}
			s = t.getSpell();
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		
		// KeyHandler runs on client side and calls next spell when key tab is pressed
//		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
//			System.out.println("Sending NextSpellMessage to server.");
//			Plato.network.sendToServer(new NextSpellMessage());
//		}
		return s;
	}

	@Override
	public ISpell prevSpell(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		ISpell s = null;
		for (int i = 0; i < maxNumSpells; i++) {
			if (t.getIndex() == 0) {
				t.setIndex(maxNumSpells - 1);
			} else {
				t.incrementIndex(-1);
			}
			s = t.getSpell();
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		
		// KeyHandler runs on client side and calls previous spell when key ctrl-tab is pressed
//		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
//			System.out.println("Sending PrevSpellMessage to server.");
//			Plato.network.sendToServer(new PrevSpellMessage());
//		}
		return s;
	}

	@Override
	public int numSpells(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		int numSpells = 0;
		for (int i = 0; i < maxNumSpells; i++) {
			ISpell s = t.getSpell(i);
			if (s != null)
				numSpells++;
		}
		return numSpells;
	}

	@Override
	public boolean isEmpty(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		for (int i = 0; i < maxNumSpells; i++) {
			ISpell s = t.getSpell(i);
			if (s != null) {
				return false;
			}
		}
		return true;
	}
}
