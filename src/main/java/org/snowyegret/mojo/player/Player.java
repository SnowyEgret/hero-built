package org.snowyegret.mojo.player;

import java.awt.Font;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.geom.EnumPlane;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.message.client.SpellMessage;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.TransactionManager;
import org.snowyegret.mojo.world.IWorld;
import org.snowyegret.mojo.world.WorldWrapper;

import com.google.common.collect.Lists;

public class Player {

	private EntityPlayer player;
	private PlayerProperties props;
	private String blockSavedPath;

//	public enum Direction {
//		NORTH,
//		SOUTH,
//		EAST,
//		WEST;
//	}

	public Player(EntityPlayer player) {
		this.player = player;
		props = (PlayerProperties) player.getExtendedProperties(PlayerProperties.NAME);
		// if (props == null) {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>props=" + props);
		// new Throwable().printStackTrace();
		// }
	}

	public Player() {
		this(Minecraft.getMinecraft().thePlayer);
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public IWorld getWorld() {
		return new WorldWrapper(player.worldObj);
	}

	public Hotbar getHotbar() {
		return new Hotbar(player.inventory);
	}

//	public Direction getDirection() {
//		int yaw = (int) (player.rotationYawHead);
//		yaw += (yaw >= 0) ? 45 : -45;
//		yaw /= 90;
//		int modulus = yaw % 4;
//		Direction direction = null;
//		switch (modulus) {
//		case 0:
//			direction = Direction.SOUTH;
//			break;
//		case 1:
//			direction = Direction.WEST;
//			break;
//		case -1:
//			direction = Direction.EAST;
//			break;
//		case 2:
//			direction = Direction.NORTH;
//			break;
//		case -2:
//			direction = Direction.NORTH;
//			break;
//		case 3:
//			direction = Direction.EAST;
//			break;
//		case -3:
//			direction = Direction.WEST;
//			break;
//		default:
//			throw new RuntimeException("Unexpected modulus. Got " + modulus);
//		}
//		return direction;
//	}

	public EnumPlane getVerticalPlane() {
		//switch (getDirection()) {
		switch (getHorizonatalFacing()) {
		case EAST:
			return EnumPlane.VERTICAL_XY_EAST_WEST;
		case WEST:
			return EnumPlane.VERTICAL_XY_EAST_WEST;
		case NORTH:
			return EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
		case SOUTH:
			return EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
		default:
			return null;
		}
	}

	public ItemStack getHeldItemStack() {
		// Not sure what the difference is
		// return player.getCurrentEquippedItem();
		return player.getHeldItem();
	}

	public Item getHeldItem() {
		return getHeldItemStack().getItem();
	}

	public Spell getSpell() {
		Spell spell = null;
		ItemStack stack = player.getHeldItem();
		if (stack != null) {
			Item item = stack.getItem();
			if (item instanceof Spell) {
				spell = (Spell) item;
			} else if (item instanceof Staff) {
				spell = ((Staff) item).getSpell(stack);
			}
		}
		return spell;
	}

	public Staff getStaff() {
		Staff staff = null;
		ItemStack is = player.getHeldItem();
		if (is != null) {
			Item item = is.getItem();
			if (item instanceof Staff) {
				staff = (Staff) item;
			}
		}
		return staff;
	}

	public void orbitAround(Vec3 pos, int dx, int dy) {
		pos = pos.add(new Vec3(.5, .5, .5));
		Vec3 v = player.getPositionVector();
		v = v.subtract(pos);
		v = v.rotateYaw((float) (-dx * Math.PI / 180));
		// v = v.rotatePitch((float) (-dy * Math.PI / 180));
		double yaw = 180 / Math.PI * Math.atan2(v.zCoord, v.xCoord) + 90;
		// double pitch = 180 / Math.PI * Math.atan2(v.yCoord, Math.sqrt(v.xCoord*v.xCoord+v.zCoord*v.zCoord));
		v = v.add(pos);
		player.setLocationAndAngles(v.xCoord, v.yCoord, v.zCoord, (float) yaw, player.rotationPitch);
	}

	public BlockPos getPosition() {
		return player.getPosition();
	}

	public boolean isFlying() {
		return player.capabilities.isFlying;
	}

	public void openGui(int id) {
		player.openGui(MoJo.instance, id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
	}

	public void moveTo(BlockPos pos) {
		player.moveEntity(pos.getX(), pos.getY(), pos.getZ());
	}

	public Modifiers getModifiers() {
		return props.getModifiers();
	}

	public TransactionManager getTransactionManager() {
		// TODO This is null for some reason
		props = (PlayerProperties) player.getExtendedProperties(PlayerProperties.NAME);
		return props.getUndoManager();
	}

	public SelectionManager getSelectionManager() {
		return props.getSelectionManager();
	}

	public PickManager getPickManager() {
		return props.getPickManager();
	}

	public void setLastSpell(Spell spell) {
		props.setLastSpell(spell);
	}

	public Spell getLastSpell() {
		return props.getLastSpell();
	}

	public void setLastInvokedSpell(Spell spell) {
		props.setLastInvokedSpell(spell);
	}

	public Spell getLastInvokedSpell() {
		return props.getLastInvokedSpell();
	}

	public void setFont(Font font) {
		props.setFont(font);
	}

	public Font getFont() {
		return props.getFont();
	}

	public void setBlockSavedPath(String blockSavedPath) {
		props.setBlockSavedPath(blockSavedPath);
	}

	public String getBlockSavedPath() {
		return props.getBlockSavedPath();
	}

	public Clipboard getClipboard() {
		return props.getClipboard();
	}

	public void playSoundAtPlayer(String sound) {
		getWorld().getWorld().playSoundAtEntity(player, sound, 1f, 1f);
	}

	public List<BlockPos> getBounds() {
		List<BlockPos> bounds = Lists.newArrayList();
		bounds.add(player.getPosition());
		bounds.add(player.getPosition().up());
		return bounds;
	}

	public void doTransaction(List<IUndoable> setBlocks) {
		getTransactionManager().doTransaction(setBlocks);
	}

	public void clearSelections() {
		getSelectionManager().clearSelections();
	}

	public void clearPicks() {
		getPickManager().clearPicks();
	}

	public Iterable<Selection> getSelections() {
		return getSelectionManager().getSelections();
	}

	public Pick[] getPicks() {
		return getPickManager().getPicks();
	}

	public void resetPicks(Spell spell) {
		PickManager pickManager = getPickManager();
		pickManager.clearPicks();
		pickManager.setNumPicks(spell.getNumPicks());
		sendMessage(new SpellMessage());
	}

	public void sendMessage(IMessage message) {
		MoJo.network.sendTo(message, (EntityPlayerMP) player);
	}

	public EnumFacing getHorizonatalFacing() {
		return player.getHorizontalFacing();
	}

}
