package ds.plato.item.staff;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
import ds.plato.item.ItemBase;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.util.StringUtils;

public abstract class Staff extends ItemBase implements IStaff {

	final int size = 9;
	IPick pickManager;

	// private final String modelPath = "models/" + StringUtils.toCamelCase(getClass());
	// private final ResourceLocation modelLocation = new ResourceLocation("plato", modelPath + ".obj");
	// private final ResourceLocation textureLocation = new ResourceLocation("plato", modelPath + ".png");

	protected Staff(IPick pickManager) {
		this.pickManager = pickManager;
	}

	// Passes call on to current spell
	@Override
	public void onMouseClickLeft(ItemStack stack, BlockPos pos, EnumFacing sideHit) {
		if (!isEmpty(stack)) {
			getSpell(stack).onMouseClickLeft(stack, pos, sideHit);
		} else {
			System.out.println("Cannot select with an empty staff.");
		}
	}

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

		//Let Spell.onItemUse do this.
		// if(world.isRemote) {
		// return true;
		// }
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

	// IStaff ----------------------------------------------------------------------

	@Override
	public Spell getSpell(ItemStack stack) {
		if (isEmpty(stack)) {
			return null;
		}
		// System.out.println("ordinal="+getOrdinal(stack));
		Spell s = getSpellAtIndex(stack, getIndex(stack));
		if (s == null) {
			s = nextSpell(stack);
		}
		return s;
	}

	@Override
	public Spell nextSpell(ItemStack stack) {
		Spell s = null;
		for (int i = 0; i < size; i++) {
			if (getIndex(stack) == size - 1) {
				setIndex(stack, 0);
			} else {
				incrementIndex(stack, 1);
			}
			s = getSpellAtIndex(stack, getIndex(stack));
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		return s;
	}

	@Override
	public ISpell previousSpell(ItemStack stack) {
		ISpell s = null;
		for (int i = 0; i < size; i++) {
			if (getIndex(stack) == 0) {
				setIndex(stack, size - 1);
			} else {
				incrementIndex(stack, -1);
			}
			s = getSpellAtIndex(stack, getIndex(stack));
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}
		return s;
	}

	@Override
	public int numSpells(ItemStack stack) {
		int numSpells = 0;
		for (int i = 0; i < size; i++) {
			ISpell s = getSpellAtIndex(stack, i);
			if (s != null)
				numSpells++;
		}
		return numSpells;
	}

	@Override
	public boolean isEmpty(ItemStack stack) {
		for (int i = 0; i < size; i++) {
			ISpell s = getSpellAtIndex(stack, i);
			if (s != null) {
				return false;
			}
		}
		return true;
	}

	// Private ---------------------------------------------------

	private Spell getSpellAtIndex(ItemStack stack, int i) {
		NBTTagCompound t = getTag(stack);
		String name = t.getString(String.valueOf(i));
		if (name != null && !name.equals("")) {
			Spell spell = (Spell) GameRegistry.findItem(Plato.ID, name);
			if (spell == null) {
				throw new RuntimeException("Game registry could not find item.  itemSimpleClassName=" + name);
			}
			return spell;
		}
		return null;
	}

	private int getIndex(ItemStack stack) {
		NBTTagCompound t = getTag(stack);
		int ordinal = t.getInteger("index");
		return ordinal;
	}

	private void setIndex(ItemStack stack, int i) {
		// if (i == 0) {
		// System.out.println();
		// new Throwable().printStackTrace();
		// }
		NBTTagCompound t = getTag(stack);
		t.setInteger("index", i);
	}

	private void incrementIndex(ItemStack stack, int increment) {
		NBTTagCompound t = getTag(stack);
		int i = t.getInteger("index");
		i = i + increment;
		t.setInteger("index", i);
	}

	private NBTTagCompound getTag(ItemStack stack) {
		NBTTagCompound t = stack.getTagCompound();
		if (t == null) {
			System.out.println("Tag null - created a new one");
			t = new NBTTagCompound();
			stack.setTagCompound(t);
		}
		return t;
	}
}
