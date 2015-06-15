package ds.plato.item.spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.item.ItemBase;
import ds.plato.network.PickMessage;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.world.IWorld;

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
		IWorld w = player.getWorld();
		IPick pickManager = player.getPickManager();
		pickManager.pick(w, pos, side);
		Plato.network.sendTo(new PickMessage(pickManager), (EntityPlayerMP) playerIn);
		if (pickManager.isFinishedPicking()) {
			invoke(w, Player.instance(playerIn));
		}
		return true;

	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	// ISpell --------------------------------------------

	@Override
	public abstract void invoke(IWorld world, IPlayer player);

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
	public void reset(IWorld world, IPick pickManager) {
		pickManager.clearPicks(world);
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
