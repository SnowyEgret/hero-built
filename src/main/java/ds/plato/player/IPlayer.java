package ds.plato.player;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.player.Player.Direction;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.world.IWorld;

public interface IPlayer {

	public abstract EntityPlayer getPlayer();

	public abstract IWorld getWorld();

	public abstract Hotbar getHotbar();

	public abstract Direction getDirection();

	public abstract ItemStack getHeldItemStack();

	public abstract Item getHeldItem();

	public abstract ISpell getSpell();

	public abstract Staff getStaff();

	public abstract void orbitAround(Vec3 center, int dx, int dy);

	public abstract BlockPos getPosition();

	public abstract boolean isFlying();

	public abstract void openGui(int id);

	public abstract void moveTo(BlockPos pos);

	public abstract Modifiers getModifiers();

	public abstract IUndo getUndoManager();

	public abstract ISelect getSelectionManager();

	public abstract IPick getPickManager();

	public abstract void setLastSpell(ISpell spell);

	public abstract void playSoundAtPlayer(String sound);

	// Thought I saw something like this somewhere in an interface
	// public static IPlayer getPlayer(EntityPlayer player) {
	// this.player = player;
	// }
}