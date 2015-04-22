package ds.plato.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ds.plato.core.Player.Direction;
import ds.plato.core.HotbarDistribution;
import ds.plato.core.HotbarSlot;
import ds.plato.item.staff.OldStaff;
import ds.plato.item.staff.Staff;

public interface IPlayer {

	public abstract IWorld getWorld();

	public abstract HotbarSlot[] getHotbarSlots();

	public abstract Direction getDirection();

	public abstract HotbarDistribution getHotbarDistribution();

	public abstract ItemStack getHeldItemStack();

	public abstract Item getHeldItem();

	public abstract ISpell getSpell();

	public abstract Staff getStaff();

}