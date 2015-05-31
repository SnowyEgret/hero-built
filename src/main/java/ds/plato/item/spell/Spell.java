package ds.plato.item.spell;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import ds.plato.item.ItemBase;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;
import ds.plato.world.WorldWrapper;

public abstract class Spell extends ItemBase implements ISpell {

	protected IUndo undoManager;
	protected ISelect selectionManager;
	protected IPick pickManager;
	protected String message;
	private int numPicks;
	protected SpellInfo info;

	protected int jumpHeight = 0;
	protected String CTRL = "ctrl,";
	protected String ALT = "alt,";
	protected String SHIFT = "shift,";
	protected String X = "X,";
	protected String Y = "Y,";
	protected String Z = "Z,";

	public Spell(int numPicks, IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		this.numPicks = numPicks;
		this.undoManager = undoManager;
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
		info = new SpellInfo(this);
	}

	protected void incrementJumpHeight(BlockPos pos, IPlayer player) {
		BlockPos p = player.getPosition();
		int dx = pos.getX() - p.getX();
		int dy = pos.getY() - p.getY();
		int dz = pos.getZ() - p.getZ();
		System.out.println(dx);
		System.out.println(dy);
		if (dx == 0 && dz == 0 && dy > 0) {
			if (dy > jumpHeight) {
				jumpHeight = dy;
			}
		}
	}

	@Override
	public void onMouseClickLeft(ItemStack stack, BlockPos pos, EnumFacing sideHit) {

		IPlayer player = Player.getPlayer();
		IWorld w = player.getWorld();

		// Shift replaces the current selections with a region.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			Block b = selectionManager.firstSelection().getBlock();
			selectionManager.clearSelections(w);
			// TODO modifier for block type
			for (Object o : BlockPos.getAllInBox(lastPos, pos)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
					if (w.getBlock((BlockPos) o) == b) {
						selectionManager.select(w, (BlockPos) o);
					}
				} else {
					selectionManager.select(w, (BlockPos) o);
				}
			}
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			System.out.println("pos=" + pos);
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(w, pos);
			} else {
				selectionManager.select(w, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections(w);
		selectionManager.select(w, pos);
	}

	// FIXME Has no effect
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (world.isRemote) {
			return true;
		}
		IWorld w = new WorldWrapper(world);
		pickManager.pick(w, pos, side);
		if (pickManager.isFinishedPicking()) {
			 //invoke(w, Player.getPlayer().getHotbar());
			invoke(w, new Player(playerIn).getHotbar());
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	// ISpell --------------------------------------------

	@Override
	public abstract void invoke(IWorld world, final HotbarSlot... slots);

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
	public boolean isPicking() {
		return pickManager.isPicking();
	}

	@Override
	public void reset() {
		pickManager.clearPicks();
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
}
