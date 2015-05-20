package ds.plato.api;

import javax.vecmath.Point3i;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import ds.plato.item.staff.Staff;
import ds.plato.player.HotbarDistribution;
import ds.plato.player.HotbarSlot;
import ds.plato.player.Player;
import ds.plato.player.Player.Direction;

public interface IPlayer {

	public abstract IWorld getWorld();

	public abstract HotbarSlot[] getHotbar();

	public abstract Direction getDirection();

	public abstract HotbarDistribution getHotbarDistribution();

	public abstract ItemStack getHeldItemStack();

	public abstract Item getHeldItem();

	public abstract ISpell getSpell();

	public abstract Staff getStaff();

	public abstract void incrementJumpHeight(BlockPos pos);

	public abstract void jump();

	public abstract void orbitAround(Point3i centroid);
}