package org.snowyegret.mojo.item.spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.ItemBase;
import org.snowyegret.mojo.network.PickMessage;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Player;

public abstract class Spell extends ItemBase implements ISpell {

	protected String message;
	protected SpellInfo info;
	protected final String CTRL = "ctrl,";
	protected final String ALT = "alt,";
	protected final String SHIFT = "shift,";
	protected final String X = "X,";
	protected final String Y = "Y,";
	protected final String Z = "Z,";
	private int numPicks;

	public Spell(int numPicks) {
		super();
		this.numPicks = numPicks;
		info = new SpellInfo(this);
	}

	// FIXME Has no effect
	// @Override
	// public EnumAction getItemUseAction(ItemStack stack) {
	// return EnumAction.NONE;
	// }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (world.isRemote) {
			return true;
		}
		IPlayer player = Player.instance(playerIn);
		PickManager pickManager = player.getPickManager();
		pickManager.pick(player, pos, side);
		MoJo.network.sendTo(new PickMessage(pickManager), (EntityPlayerMP) playerIn);
		if (pickManager.isFinishedPicking()) {
			invoke(Player.instance(playerIn));
		}
		return true;

	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	// ISpell --------------------------------------------

	@Override
	public abstract void invoke(IPlayer player);

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public SpellInfo getInfo() {
		return info;
	}

	@Override
	public int getNumPicks() {
		return numPicks;
	}

	@Override
	public void reset(IPlayer player) {
		PickManager pickManager = player.getPickManager();
		pickManager.clearPicks(player);
		pickManager.reset(numPicks);
		message = null;
	}

	// Object -------------------------------------------------------

	// For Staff.addSpell(). Only one spell of each type on a staff
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() == obj.getClass())
			return true;
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		return builder.toString();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
