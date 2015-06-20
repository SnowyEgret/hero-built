package org.snowyegret.plato.player;

import java.util.List;

import org.snowyegret.plato.item.spell.ISpell;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.item.staff.Staff;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.player.Player.Direction;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

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

	public abstract Clipboard getClipboard();

	public abstract List<BlockPos> getBounds();

	// Thought I saw something like this somewhere in an interface
	// public static IPlayer getPlayer(EntityPlayer player) {
	// this.player = player;
	// }
}