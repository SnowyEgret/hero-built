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

public class Spell extends ItemBase {

	// TODO Move this property to the overlay.
	protected String message;
	protected SpellInfo info;
	private int numPicks;

	// For base spell model
	public Spell() {
	}

	// No longer abstact because we a instantiating it so that we can use its model as a base model
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
		System.out.println("pickManager=" + pickManager);
		pickManager.pick(pos, side);
		// This is done in method pick
		//MoJo.network.sendTo(new PickMessage(pickManager), (EntityPlayerMP) playerIn);
		if (pickManager.isFinishedPicking()) {
			invoke(player);
		}
		return true;

	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	public void invoke(IPlayer player) {
	}

	public String getMessage() {
		return message;
	}

	public SpellInfo getInfo() {
		return info;
	}

	public int getNumPicks() {
		return numPicks;
	}

	public void reset(IPlayer player) {
		PickManager pickManager = player.getPickManager();
		pickManager.clearPicks();
		pickManager.setNumPicks(numPicks);
		// TODO set this on the overlay
		message = null;
	}

	// Object -------------------------------------------------------

	// For Staff.addSpell(). Only one spell of each type on a staff
	@Override
	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
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
