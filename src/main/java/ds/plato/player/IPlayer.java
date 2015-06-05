package ds.plato.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import ds.plato.item.spell.ISpell;
import ds.plato.item.staff.Staff;
import ds.plato.player.Player.Direction;
import ds.plato.world.IWorld;

public interface IPlayer {
	
	public abstract EntityPlayer getPlayer();

	public abstract IWorld getWorld();

	public abstract HotbarSlot[] getHotbar();

	public abstract Direction getDirection();

	public abstract HotbarDistribution getHotbarDistribution();

	public abstract ItemStack getHeldItemStack();

	public abstract Item getHeldItem();

	public abstract ISpell getSpell();

	public abstract Staff getStaff();

	public abstract void orbitAround(Vec3 center, int dx, int dy);

	public abstract BlockPos getPosition();

	public abstract boolean isFlying();

	public abstract void openGui(int id, IWorld world);

	public abstract void moveTo(BlockPos pos);

	// Thought I saw something like this somewhere
	// public static IPlayer getPlayer(EntityPlayer player) {
	// this.player = player;
	// }
}